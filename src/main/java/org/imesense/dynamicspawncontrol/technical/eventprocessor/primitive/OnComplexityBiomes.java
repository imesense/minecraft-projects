package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import static net.minecraft.client.gui.Gui.*;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnComplexityBiomes
{
     //* TODO: Реализовать 'высоту' сложности, например 5 черепков от 5 до 20 высота в шахте и так далее

    /**
     *
     */
    private String biomeText = "";

    /**
     *
     */
    private long biomeEntryTime = 0;

    /**
     *
     */
    private Biome currentBiome = null;

    /**
     *
     */
    private Biome confirmedBiome = null;

    /**
     *
     */
    private long lastBiomeChangeTime = 0;

    /**
     *
     */
    private final long BIOMES_CHANGE_MIN_TIME = 3000;

    /**
     *
     */
    private final Minecraft mc = Minecraft.getMinecraft();

    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnComplexityBiomes()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onPlayerTick(LivingEvent.LivingUpdateEvent event)
    {
        if (event.getEntity() instanceof EntityPlayerMP)
        {
            EntityPlayerMP player = (EntityPlayerMP) event.getEntity();
            Biome biome = player.world.getBiome(player.getPosition());

            if (biome != currentBiome)
            {
                currentBiome = biome;
                biomeEntryTime = System.currentTimeMillis();
            }

            long currentTime = System.currentTimeMillis();

            if (currentBiome != confirmedBiome && currentTime - biomeEntryTime >= this.BIOMES_CHANGE_MIN_TIME)
            {
                confirmedBiome = currentBiome;
                lastBiomeChangeTime = currentTime;
                biomeText = confirmedBiome.getBiomeName();
            }
        }
    }

    /**
     *
     * @param event
     */
    @SubscribeEvent
    public synchronized void onRenderOverlay(RenderGameOverlayEvent.Text event)
    {
        long currentTime = System.currentTimeMillis();

        if (confirmedBiome != null && currentTime - lastBiomeChangeTime < 5000)
        {
            int boxWidth = 120;
            int boxHeight = 50;

            int screenWidth = mc.displayWidth / mc.gameSettings.guiScale;

            int xPos = (screenWidth - boxWidth) / 2;
            int yPos = 20;

            int backgroundColor = 0x80000000;

            drawRect(xPos, yPos, xPos + boxWidth, yPos + boxHeight, backgroundColor);

            int textWidth = mc.fontRenderer.getStringWidth(biomeText);
            int textXPos = xPos + (boxWidth - textWidth) / 2;
            int textYPos = yPos + 10;

            mc.fontRenderer.drawString(biomeText, textXPos, textYPos, 0xFFFFFF);

            int[] skullCounts =
            {
                getRedSkullCountForBiome(confirmedBiome),
                getOrangeSkullCountForBiome(confirmedBiome),
                getRedSkullCountForBiomePart(confirmedBiome),
                getOrangeSkullCountForBiomePart(confirmedBiome)
            };

            ResourceLocation[] skullTextures =
            {
                new ResourceLocation("dynamicspawncontrol",
                        "textures/gui/red_skull.png"),

                new ResourceLocation("dynamicspawncontrol",
                        "textures/gui/orange_skull.png"),

                new ResourceLocation("dynamicspawncontrol",
                        "textures/gui/red_skull_part.png"),

                new ResourceLocation("dynamicspawncontrol",
                        "textures/gui/orange_skull_part.png")
            };

            int totalSkulls = skullCounts[0] + skullCounts[1] + skullCounts[2] + skullCounts[3];
            int skullWidth = 16;
            int skullSpacing = 2;
            int totalSkullWidth = (skullWidth * totalSkulls) + (skullSpacing * (totalSkulls - 1));

            int skullXPos = xPos + (boxWidth - totalSkullWidth) / 2;
            int skullYPos = textYPos + 20;

            for (int i = 0; i < skullCounts.length; i++)
            {
                for (int j = 0; j < skullCounts[i]; j++)
                {
                    mc.getTextureManager().bindTexture(skullTextures[i]);
                    drawModalRectWithCustomSizedTexture(skullXPos, skullYPos, 0, 0, skullWidth, skullWidth, skullWidth, skullWidth);
                    skullXPos += skullWidth + skullSpacing;
                }
            }
        }
    }

    /**
     *
     * @param biome
     * @return
     */
    private int getRedSkullCountForBiome(Biome biome)
    {
        switch (biome.getBiomeName())
        {
            case "Extreme Hills":
                return 5;
            case "Desert":
                return 3;
            case "Swampland":
                return 4;
            case "Ice Flats":
                return 5;
            case "Forest":
                return 2;
            case "Jungle":
                return 4;
            case "Beach":
                return 1;
            case "Taiga":
                return 3;
            case "Mesa":
                return 4;
            default:
                return 0;
        }
    }

    /**
     *
     * @param biome
     * @return
     */
    private int getRedSkullCountForBiomePart(Biome biome)
    {
        switch (biome.getBiomeName())
        {
            default:
                return 0;
        }
    }

    /**
     *
     * @param biome
     * @return
     */
    private int getOrangeSkullCountForBiome(Biome biome)
    {
        switch (biome.getBiomeName())
        {
            case "Plains":
                return 1;
            case "Savanna":
                return 1;
            case "Savanna Plateau":
                return 3;
            case "Desert":
                return 2;
            case "Swampland":
                return 1;
            case "Ice Flats":
                return 2;
            case "Forest":
                return 3;
            case "Jungle":
                return 1;
            case "Beach":
                return 2;
            case "Taiga":
                return 1;
            case "Mesa":
                return 1;
            default:
                return 0;
        }
    }

    /**
     *
     * @param biome
     * @return
     */
    private int getOrangeSkullCountForBiomePart(Biome biome)
    {
        switch (biome.getBiomeName())
        {
            default:
                return 0;
        }
    }
}
