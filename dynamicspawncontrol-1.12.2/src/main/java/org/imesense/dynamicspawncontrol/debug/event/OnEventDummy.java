package org.imesense.dynamicspawncontrol.debug.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.debug.IDebug;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.lwjgl.opengl.GL11;
import scala.util.Random;

import java.util.Objects;

/**
 *
 */
@Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventDummy implements IDebug
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventDummy()
    {
		CodeGenericUtil.printInitClassToLog(this.getClass());

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
}
