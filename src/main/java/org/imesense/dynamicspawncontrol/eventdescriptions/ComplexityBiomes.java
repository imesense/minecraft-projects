package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.config.biomescategories.BiomeCategoriesConfig;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

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
    private static final long BIOME_CONFIRMATION_DELAY = 3000;
    private static final long BIOME_DISPLAY_DURATION = 5000;

    private int[] targetDepthSkulls = new int[4];
    private final int[] currentDisplaySkulls = new int[4];

    private long skullChangeStartTime = 0;
    private static final long SKULL_CHANGE_DURATION = 1000;

    private static final ResourceLocation[] SKULL_TEXTURES =
    {
        new ResourceLocation("dynamicspawncontrol", "textures/gui/red_skull.png"),
        new ResourceLocation("dynamicspawncontrol", "textures/gui/orange_skull.png"),
        new ResourceLocation("dynamicspawncontrol", "textures/gui/red_skull_part.png"),
        new ResourceLocation("dynamicspawncontrol", "textures/gui/orange_skull_part.png")
    };

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

    public void handleBiomesChange(EntityPlayerMP entityPlayerMP)
    {
        if (isUnderground(entityPlayerMP))
        {
            handleUnderground(entityPlayerMP.posY);
        }
        else
        {
            handleSurface(entityPlayerMP);
        }
    }

    private boolean isUnderground(EntityPlayerMP player)
    {
        return player.posY <= 55 && !player.world.canSeeSky(
                new BlockPos(player.posX, player.posY + player.getEyeHeight(), player.posZ));
    }

    private void handleUnderground(double playerY)
    {
        int currentDepthLevel = getDepthLevel(playerY);

        if (currentDepthLevel != lastDepthLevel)
        {
            lastDepthLevel = currentDepthLevel;
            lastDepthChangeTime = System.currentTimeMillis();
            targetDepthSkulls = DepthCalculator.getSkullCountsForDepth(playerY);
            skullChangeStartTime = System.currentTimeMillis();
        }

        updateSkullAnimation();

        currentBiome = null;
        confirmedBiome = null;
        biomesText = "";
    }

    private void handleSurface(EntityPlayerMP player)
    {
        Biome biome = player.world.getBiome(player.getPosition());

        if (biome != currentBiome)
        {
            currentBiome = biome;
            biomesEntryTime = System.currentTimeMillis();
        }

        long currentTime = System.currentTimeMillis();

        if (currentBiome != confirmedBiome &&
                currentTime - biomesEntryTime >= BIOME_CONFIRMATION_DELAY)
        {
            confirmedBiome = currentBiome;
            lastBiomesChangeTime = currentTime;

            String biomesName = confirmedBiome.getBiomeName();
            biomesText = BiomeCategoriesConfig.getCategoryForBiome(biomesName).getName();
        }

        lastDepthLevel = -1;
    }

    private void updateSkullAnimation()
    {
        long currentTime = System.currentTimeMillis();
        float progress = Math.min(1.0f,
                (currentTime - skullChangeStartTime) / (float)SKULL_CHANGE_DURATION);

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
        EntityPlayer entityPlayer = UniqueField.CLIENT.player;

        if (entityPlayer == null)
        {
            return;
        }

        long currentTime = System.currentTimeMillis();
        boolean isUnderground = isClientPlayerUnderground(entityPlayer);

        if (isUnderground)
        {
            renderUndergroundOverlay(currentTime);
        }
        else
        {
            renderSurfaceOverlay(currentTime);
        }
    }

    private boolean isClientPlayerUnderground(EntityPlayer player)
    {
        return player.posY <= 55 && !player.world.canSeeSky(
                new BlockPos(player.posX, player.posY + player.getEyeHeight(), player.posZ));
    }

    private void renderUndergroundOverlay(long currentTime)
    {
        if (currentTime - lastDepthChangeTime < DISPLAY_DURATION)
        {
            renderOverlay("Глубинная зона", currentDisplaySkulls);
        }
    }

    private void renderSurfaceOverlay(long currentTime)
    {
        boolean showBiome = confirmedBiome != null &&
                currentTime - lastBiomesChangeTime < BIOME_DISPLAY_DURATION;

        if (showBiome)
        {
            String biomesName = confirmedBiome.getBiomeName();
            int[] skullCounts = BiomeCategoriesConfig.getCategoryForBiome(biomesName).getSkulls();
            renderOverlay(biomesText, skullCounts);
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

        drawRect(xPos, yPos, xPos + boxWidth, yPos + boxHeight, 0x80000000);

        int textWidth = UniqueField.CLIENT.fontRenderer.getStringWidth(text);
        int textXPos = xPos + (boxWidth - textWidth) / 2;
        int textYPos = yPos + 5;
        UniqueField.CLIENT.fontRenderer.drawString(text, textXPos, textYPos, 0xFFFFFF);

        renderSkulls(xPos, yPos, boxWidth, boxHeight, skullCounts);
    }

    private void renderSkulls(int xPos, int yPos, int boxWidth, int boxHeight, int[] skullCounts)
    {
        int totalSkulls = skullCounts[0] + skullCounts[1] + skullCounts[2] + skullCounts[3];
        int skullWidth = 12, skullHeight = 12, skullSpacing = 2;
        int totalSkullWidth = (skullWidth * totalSkulls) + (skullSpacing * (totalSkulls - 1));

        int skullXPos = xPos + (boxWidth - totalSkullWidth) / 2;
        int skullYPos = yPos + boxHeight - skullHeight - 5;

        for (int i = 0; i < skullCounts.length; i++)
        {
            for (int j = 0; j < skullCounts[i]; j++)
            {
                UniqueField.CLIENT.getTextureManager().bindTexture(SKULL_TEXTURES[i]);

                drawModalRectWithCustomSizedTexture(skullXPos, skullYPos,
                        0, 0, skullWidth, skullHeight, skullWidth, skullHeight);

                skullXPos += skullWidth + skullSpacing;
            }
        }
    }

    private static final class DepthCalculator
    {
        private static int[] getSkullCountsForDepth(double playerY)
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
}
