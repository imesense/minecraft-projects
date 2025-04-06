package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.HashMap;
import java.util.Map;

import static net.minecraft.client.gui.Gui.*;

@InitLog
public final class ComplexityBiomes
{
    private String biomesText = "";
    private long biomesEntryTime = 0;
    private Biome currentBiome = null;
    private Biome confirmedBiome = null;
    private long lastBiomesChangeTime = 0;
    private long lastDepthChangeTime = 0;
    private int lastDepthLevel = -1;
    private static final int[] DEPTH_THRESHOLDS = { 55, 48, 38, 28, 18, 10 };
    private static final long DISPLAY_DURATION = 5000;

    private int[] targetDepthSkulls = new int[4];
    private int[] currentDisplaySkulls = new int[4];
    private long skullChangeStartTime = 0;
    private static final long SKULL_CHANGE_DURATION = 1000;

    private static final Map<String, int[]> BIOME_SKULL_MAP = new HashMap<>();

    static
    {
        // Plains biomes
        addBiomeSkulls("Plains", 0, 1, 0, 1);
        addBiomeSkulls("Sunflower Plains", 1, 1, 0, 1);

        // Forest biomes
        addBiomeSkulls("Forest", 2, 1, 0, 0);
        addBiomeSkulls("ForestHills", 3, 2, 0, 0);
        addBiomeSkulls("Flower Forest", 0, 1, 0, 1);
        addBiomeSkulls("Birch Forest", 1, 1, 0, 0);
        addBiomeSkulls("Birch Forest Hills", 2, 0, 0, 0);
        addBiomeSkulls("Birch Forest M", 2, 0, 0, 0);
        addBiomeSkulls("Roofed Forest", 3, 0, 0, 0);
        addBiomeSkulls("Roofed Forest M", 4, 0, 0, 0);

        // Taiga biomes
        addBiomeSkulls("Taiga", 2, 0, 0, 0);
        addBiomeSkulls("TaigaHills", 2, 2, 0, 0);
        addBiomeSkulls("Taiga M", 2, 0, 0, 1);
        addBiomeSkulls("Cold Taiga", 4, 1, 0, 0);
        addBiomeSkulls("Cold Taiga Hills", 5, 0, 0, 0);
        addBiomeSkulls("Mega Taiga", 2, 1, 0, 0);
        addBiomeSkulls("Mega Taiga Hills", 3, 2, 0, 0);
        addBiomeSkulls("Redwood Taiga Hills M", 6, 0, 0, 0);
        addBiomeSkulls("Mega Spruce Taiga", 5, 2, 0, 0);

        // Jungle biomes
        addBiomeSkulls("Jungle", 7, 0, 0, 0);
        addBiomeSkulls("JungleHills", 7, 0, 0, 0);
        addBiomeSkulls("Jungle M", 7, 0, 0, 0);
        addBiomeSkulls("JungleEdge", 3, 2, 0, 0);

        // Mountain biomes
        addBiomeSkulls("Extreme Hills", 4, 0, 1, 0);
        addBiomeSkulls("Extreme Hills+", 6, 0, 0, 0);
        addBiomeSkulls("Extreme Hills M", 5, 0, 0, 0);
        addBiomeSkulls("Extreme Hills+ M", 6, 0, 0, 0);

        // Desert biomes
        addBiomeSkulls("Desert", 4, 0, 0, 0);
        addBiomeSkulls("Desert M", 3, 0, 0, 0);
        addBiomeSkulls("DesertHills", 5, 0, 1, 0);

        // Ice biomes
        addBiomeSkulls("Ice Plains", 3, 0, 1, 0);
        addBiomeSkulls("Ice Mountains", 4, 2, 0, 0);

        // Swamp biomes
        addBiomeSkulls("Swampland", 4, 2, 0, 0);

        // Savanna biomes
        addBiomeSkulls("Savanna", 0, 3, 0, 0);
        addBiomeSkulls("Savanna M", 0, 4, 0, 0);
        addBiomeSkulls("Savanna Plateau", 2, 1, 0, 0);
        addBiomeSkulls("Savanna Plateau M", 6, 1, 0, 0);

        // Mesa biomes
        addBiomeSkulls("Mesa", 5, 1, 0, 0);
        addBiomeSkulls("Mesa Plateau F", 6, 1, 0, 0);

        // Ocean biomes
        addBiomeSkulls("Ocean", 4, 2, 0, 0);
        addBiomeSkulls("Deep Ocean", 7, 0, 0, 0);

        // Other biomes
        addBiomeSkulls("River", 0, 1, 0, 0);
        addBiomeSkulls("Stone Beach", 0, 0, 0, 1);
    }

    private static void addBiomeSkulls(String biomeName, int red, int orange, int redPart, int orangePart)
    {
        BIOME_SKULL_MAP.put(biomeName, new int[] { red, orange, redPart, orangePart });
    }

    private static volatile ComplexityBiomes _INSTANCE;

    public static ComplexityBiomes getInstance()
    {
        return CodeGeneric.getInstance(ComplexityBiomes.class);
    }

    public ComplexityBiomes()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public void handleBiomesChange(EntityPlayerMP player)
    {
        boolean isUnderground = player.posY <= 55 &&
                !player.world.canSeeSky(new BlockPos(player.posX, player.posY + player.getEyeHeight(), player.posZ));

        if (isUnderground)
        {
            int currentDepthLevel = getDepthLevel(player.posY);

            if (currentDepthLevel != lastDepthLevel)
            {
                lastDepthLevel = currentDepthLevel;
                lastDepthChangeTime = System.currentTimeMillis();
                targetDepthSkulls = getSkullCountsForDepth(player.posY);
                skullChangeStartTime = System.currentTimeMillis();
            }

            updateSkullAnimation();

            currentBiome = null;
            confirmedBiome = null;
            biomesText = "";
        }
        else
        {
            Biome biome = player.world.getBiome(player.getPosition());

            if (biome != currentBiome)
            {
                currentBiome = biome;
                biomesEntryTime = System.currentTimeMillis();
            }

            long currentTime = System.currentTimeMillis();

            if (currentBiome != confirmedBiome && currentTime - biomesEntryTime >= 3000)
            {
                confirmedBiome = currentBiome;
                lastBiomesChangeTime = currentTime;
                biomesText = confirmedBiome.getBiomeName();
            }

            lastDepthLevel = -1;
        }
    }

    private void updateSkullAnimation()
    {
        long currentTime = System.currentTimeMillis();
        float progress = Math.min(1.0f, (currentTime - skullChangeStartTime) / (float)SKULL_CHANGE_DURATION);

        for (int i = 0; i < 4; i++)
        {
            if (progress >= 1.0f)
            {
                currentDisplaySkulls[i] = targetDepthSkulls[i];
            }
            else
            {
                float current = currentDisplaySkulls[i];
                float target = targetDepthSkulls[i];

                currentDisplaySkulls[i] = Math.round(current + (target - current) * progress);
            }
        }
    }

    private int getDepthLevel(double y)
    {
        for (int i = 0; i < DEPTH_THRESHOLDS.length; i++)
        {
            if (y > DEPTH_THRESHOLDS[i])
            {
                return i;
            }
        }

        return DEPTH_THRESHOLDS.length;
    }

    public void renderBiomesOverlay()
    {
        EntityPlayer player = UniqueField.CLIENT.player;

        if (player == null)
        {
            return;
        }

        long currentTime = System.currentTimeMillis();

        boolean isUnderground = player.posY <= 55 &&
                !player.world.canSeeSky(new BlockPos(player.posX, player.posY + player.getEyeHeight(), player.posZ));

        if (isUnderground)
        {
            if (currentTime - lastDepthChangeTime < DISPLAY_DURATION)
            {
                renderOverlay("Deep Area", currentDisplaySkulls);
            }
        }
        else
        {
            boolean showBiome = confirmedBiome != null && currentTime - lastBiomesChangeTime < 5000;

            if (showBiome)
            {
                int[] skullCounts = BIOME_SKULL_MAP.getOrDefault(confirmedBiome.getBiomeName(), new int[]{0, 0, 0, 0});
                renderOverlay(biomesText, skullCounts);
            }
        }
    }

    private void renderOverlay(String text, int[] skullCounts)
    {
        int boxWidth = 140;
        int boxHeight = 40;

        ScaledResolution scaledResolution = new ScaledResolution(UniqueField.CLIENT);
        int screenWidth = scaledResolution.getScaledWidth();

        int xPos = (screenWidth - boxWidth) / 2;
        int yPos = 35;

        int backgroundColor = 0x80000000;
        drawRect(xPos, yPos, xPos + boxWidth, yPos + boxHeight, backgroundColor);

        int textWidth = UniqueField.CLIENT.fontRenderer.getStringWidth(text);
        int textXPos = xPos + (boxWidth - textWidth) / 2;
        int textYPos = yPos + 5;

        UniqueField.CLIENT.fontRenderer.drawString(text, textXPos, textYPos, 0xFFFFFF);

        ResourceLocation[] skullTextures =
        {
            new ResourceLocation("dynamicspawncontrol", "textures/gui/red_skull.png"),
            new ResourceLocation("dynamicspawncontrol", "textures/gui/orange_skull.png"),
            new ResourceLocation("dynamicspawncontrol", "textures/gui/red_skull_part.png"),
            new ResourceLocation("dynamicspawncontrol", "textures/gui/orange_skull_part.png")
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

    private int[] getSkullCountsForDepth(double playerY)
    {
        int redSkulls = 0;
        int orangeSkulls = 0;
        int redPart = 0;
        int orangePart = 0;

        if (playerY <= 55 && playerY > 48)
        {
            redSkulls = 1;
        }
        else if (playerY <= 48 && playerY > 38)
        {
            redSkulls = 2;
        }
        else if (playerY <= 38 && playerY > 28)
        {
            redSkulls = 3;
        }
        else if (playerY <= 28 && playerY > 18)
        {
            redSkulls = 4;
        }
        else if (playerY <= 18 && playerY > 10)
        {
            redSkulls = 5;
        }
        else if (playerY <= 10)
        {
            redSkulls = 5;
            orangeSkulls = 2;
        }

        return new int[] {redSkulls, orangeSkulls, redPart, orangePart};
    }
}
