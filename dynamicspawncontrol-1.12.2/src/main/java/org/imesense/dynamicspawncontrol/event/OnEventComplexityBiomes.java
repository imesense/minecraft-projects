package org.imesense.dynamicspawncontrol.event;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import static net.minecraft.client.gui.Gui.*;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventComplexityBiomes
{
     //* TODO: Реализовать 'высоту' сложности, например 5 черепков от 5 до 20 высота в шахте и так далее

    /**
     *
     */
    private String biomesText = "";

    /**
     *
     */
    private long biomesEntryTime = 0;

    /**
     *
     */
    private final byte NULL = 0;

    /**
     *
     */
    private final byte MIN = 1;

    /**
     *
     */
    private final byte MAX = 7;

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
    private long lastBiomesChangeTime = 0;

    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventComplexityBiomes()
    {
        CodeGeneric.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     * @param livingUpdateEvent
     */
    @SubscribeEvent
    public void onPlayerTick_0(LivingEvent.LivingUpdateEvent livingUpdateEvent)
    {
        if (livingUpdateEvent.getEntity() instanceof EntityPlayerMP)
        {
            EntityPlayerMP entityPlayerMP = (EntityPlayerMP) livingUpdateEvent.getEntity();
            Biome biome = entityPlayerMP.world.getBiome(entityPlayerMP.getPosition());

            if (biome != currentBiome)
            {
                currentBiome = biome;
                biomesEntryTime = System.currentTimeMillis();
            }

            long currentTime = System.currentTimeMillis();

            long BIOMES_CHANGE_MIN_TIME = 3000;

            if (currentBiome != confirmedBiome && currentTime - biomesEntryTime >= BIOMES_CHANGE_MIN_TIME)
            {
                confirmedBiome = currentBiome;
                lastBiomesChangeTime = currentTime;
                biomesText = confirmedBiome.getBiomeName();
            }
        }
    }

    /**
     *
     * @param text
     */
    @SubscribeEvent
    public void onRenderOverlay_1(RenderGameOverlayEvent.Text text)
    {
        long currentTime = System.currentTimeMillis();

        if (confirmedBiome != null && currentTime - lastBiomesChangeTime < 5000)
        {
            int boxWidth = 140;
            int boxHeight = 40;

            ScaledResolution scaledResolution = new ScaledResolution(UniqueField.CLIENT);
            int screenWidth = scaledResolution.getScaledWidth();

            int xPos = (screenWidth - boxWidth) / 2;
            int yPos = 35;

            int backgroundColor = 0x80000000;

            drawRect(xPos, yPos, xPos + boxWidth, yPos + boxHeight, backgroundColor);

            int textWidth = UniqueField.CLIENT.fontRenderer.getStringWidth(biomesText);
            int textXPos = xPos + (boxWidth - textWidth) / 2;
            int textYPos = yPos + 5;

            UniqueField.CLIENT.fontRenderer.drawString(biomesText, textXPos, textYPos, 0xFFFFFF);

            int[] skullCounts =
            {
                getRedSkullCountForBiomes(confirmedBiome),
                getOrangeSkullCountForBiomes(confirmedBiome),
                getRedSkullCountForBiomesPart(confirmedBiome),
                getOrangeSkullCountForBiomesPart(confirmedBiome)
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
            int skullWidth = 12, skullHeight = 12, skullSpacing = 2;
            int totalSkullWidth = (skullWidth * totalSkulls) + (skullSpacing * (totalSkulls - 1));

            int skullXPos = xPos + (boxWidth - totalSkullWidth) / 2;
            int skullYPos = yPos + boxHeight - skullHeight - 5;

            for (int i = 0; i < skullCounts.length; i++)
            {
                for (int j = 0; j < skullCounts[i]; j++)
                {
                    UniqueField.CLIENT.getTextureManager().bindTexture(skullTextures[i]);

                    drawModalRectWithCustomSizedTexture(skullXPos, skullYPos,
                            0, 0, skullWidth, skullHeight, skullWidth, skullHeight);

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
    private int getRedSkullCountForBiomes(Biome biome)
    {
        switch (biome.getBiomeName())
        {
            case "Sunflower Plains":
            case "Birch Forest":
                return this.MIN;
            case "Taiga":
            case "TaigaHills":
            case "Forest":
            case "Mega Taiga":
            case "Taiga M":
            case "Savanna Plateau":
            case "Birch Forest Hills":
                return 2;
            case "Roofed Forest":
            case "Mega Taiga Hills":
            case "ForestHills":
            case "Ice Plains":
                return 3;
            case "Swampland":
            case "Extreme Hills":
            case "Desert":
            case "Cold Taiga":
            case "Ice Mountains":
                return 4;
            case "Mega Spruce Taiga":
            case "DesertHills":
            case "Extreme Hills M":
            case "Cold Taiga Hills":
            case "Mesa":
                return 5;
            case "Extreme Hills+":
            case "Savanna Plateau M":
            case "Extreme Hills+ M":
            case "Mesa Plateau F":
                return 6;
            case "Jungle":
            case "JungleHills":
            case "Jungle M":
                return this.MAX;
            default:
                return this.NULL;
        }
    }

    /**
     *
     * @param biome
     * @return
     */
    private int getRedSkullCountForBiomesPart(Biome biome)
    {
        switch (biome.getBiomeName())
        {
            case "Extreme Hills":
            case "DesertHills":
            case "Ice Plains":
                return this.MIN;
            default:
                return this.NULL;
        }
    }

    /**
     *
     * @param biome
     * @return
     */
    private int getOrangeSkullCountForBiomes(Biome biome)
    {
        switch (biome.getBiomeName())
        {
            case "Plains":
            case "Forest":
            case "Mega Taiga":
            case "Savanna Plateau":
            case "Savanna Plateau M":
            case "Sunflower Plains":
            case "Birch Forest":
            case "Cold Taiga":
            case "Flower Forest":
            case "Mesa":
            case "Mesa Plateau F":
                return this.MIN;
            case "TaigaHills":
            case "Swampland":
            case "Mega Taiga Hills":
            case "Mega Spruce Taiga":
            case "ForestHills":
            case "Ice Mountains":
                return 2;
            case "Savanna":
                return 3;
            case "Savanna M":
                return 4;
            default:
                return this.NULL;
        }
    }

    /**
     *
     * @param biome
     * @return
     */
    private int getOrangeSkullCountForBiomesPart(Biome biome)
    {
        switch (biome.getBiomeName())
        {
            case "Plains":
            case "Taiga M":
            case "Sunflower Plains":
            case "Flower Forest":
                return this.MIN;
            default:
                return this.NULL;
        }
    }
}
