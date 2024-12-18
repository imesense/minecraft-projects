package org.imesense.dynamicspawncontrol;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.api.IDebug;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.util.Random;
import net.minecraft.block.BlockTallGrass;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventSandBox implements IDebug
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventSandBox()
    {
		CodeGeneric.printInitClassToLog(this.getClass());

        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    //@SubscribeEvent
    //public static void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        // Задание плотности тумана
   //     event.setDensity(0.05f); // Чем больше значение, тем плотнее туман
    //    GlStateManager.setFog(GlStateManager.FogMode.EXP);
    //    event.setCanceled(true); // Обязательно отменяем стандартное поведение, чтобы применился наш туман
   // }

    //@SubscribeEvent
   // public static void onFogColors(EntityViewRenderEvent.FogColors event) {
        // Установка красного цвета для тумана
   //     event.setRed(0.7f);   // Красный оттенок
   //     event.setGreen(0.2f); // Зеленый оттенок
   //     event.setBlue(0.2f);  // Синий оттенок
   // }

    //@SubscribeEvent
    //public void onGetGrassColor(BiomeEvent.GetGrassColor event) {
    //    // Проверяем, что это болото (Swampland)
    //    if (event.getBiome() == Biomes.SWAMPLAND) {
    //        // Устанавливаем зеленоватый оттенок для травы
    //        event.setNewColor(0x013220);  // Зеленоватый цвет (RGB)
    //    }
    //}
//
    //@SubscribeEvent
    //public void onGetFoliageColor(BiomeEvent.GetFoliageColor event) {
    //    // Проверяем, что это болото (Swampland)
    //    if (event.getBiome() == Biomes.SWAMPLAND) {
    //        // Устанавливаем зеленоватый оттенок для листвы
    //        event.setNewColor(0x013220);  // Зеленоватый цвет (RGB)
    //    }
    //}
//
    //@SubscribeEvent
    //public void onGetWaterColor(BiomeEvent.GetWaterColor event) {
    //    // Проверяем, что это болото (Swampland)
    //    if (event.getBiome() == Biomes.SWAMPLAND) {
    //        // Устанавливаем зеленоватый оттенок для воды
    //        event.setNewColor(0x013220);  // Темно-зеленоватый цвет воды (RGB)
    //    }
    //}
}
