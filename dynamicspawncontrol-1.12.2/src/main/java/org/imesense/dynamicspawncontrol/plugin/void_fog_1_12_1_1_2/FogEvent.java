package org.imesense.dynamicspawncontrol.plugin.void_fog_1_12_1_1_2;

import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.opengl.GLContext;

public class FogEvent
{
    int dimensionIdVoid = 255;

    @SubscribeEvent
    public void particles(TickEvent.ClientTickEvent e) {
        if (/*!ConfigHandler.enabled ||*/ Minecraft.getMinecraft().isGamePaused()) {
            return;
        }
        EntityPlayerSP entityPlayerSP = Minecraft.getMinecraft().player;
        WorldClient worldClient = Minecraft.getMinecraft().world;
        if (entityPlayerSP != null && worldClient != null) {
            int x = MathHelper.floor(((EntityPlayer) entityPlayerSP).posX);
            int y = MathHelper.floor(((EntityPlayer) entityPlayerSP).posY);
            int z = MathHelper.floor(((EntityPlayer) entityPlayerSP).posZ);
            Random random = new Random();
            for (int l = 0; l < 1000; l++) {
                int i1 = (x + ((World) worldClient).rand.nextInt(16)) - ((World) worldClient).rand.nextInt(16);
                int j1 = (y + ((World) worldClient).rand.nextInt(16)) - ((World) worldClient).rand.nextInt(16);
                int k1 = (z + ((World) worldClient).rand.nextInt(16)) - ((World) worldClient).rand.nextInt(16);
                IBlockState block = worldClient.getBlockState(new BlockPos(i1, j1, k1));
                if (block.getMaterial() == Material.AIR) {
                    if (((World) worldClient).rand.nextInt(8) > ((/*C0000VoidFog.voidcraft &&*/ ((World) worldClient).provider.getDimension() == /*tamaized.voidcraft.common.handlers.ConfigHandler.*/dimensionIdVoid) ? 0 : j1) && ((worldClient.getWorldInfo().getTerrainType() != WorldType.FLAT && !((World) worldClient).provider.isNether()) || (/*C0000VoidFog.voidcraft &&*/ ((World) worldClient).provider.getDimension() == /*tamaized.voidcraft.common.handlers.ConfigHandler.*/dimensionIdVoid))) {
                        worldClient.spawnParticle(EnumParticleTypes.SUSPENDED_DEPTH, i1 + ((World) worldClient).rand.nextFloat(), j1 + ((World) worldClient).rand.nextFloat(), k1 + ((World) worldClient).rand.nextFloat(), 0.0d, 0.0d, 0.0d, new int[0]);
                    }
                } else {
                    block.getBlock().randomDisplayTick(block, worldClient, new BlockPos(i1, j1, k1), random);
                }
            }
        }
    }

    @SubscribeEvent
    public void render(EntityViewRenderEvent.RenderFogEvent e) {
        //if (!ConfigHandler.enabled) {
        //    return;
       // }
        EntityPlayer entity = (EntityPlayer) e.getEntity();
        WorldClient worldclient = Minecraft.getMinecraft().world;
        boolean flag = false;
        if (entity instanceof EntityPlayer) {
            flag = entity.capabilities.isCreativeMode;
        }
        float f1 = e.getFarPlaneDistance();
        if ((worldclient.getWorldInfo().getTerrainType() != WorldType.FLAT && !worldclient.provider.isNether() && !flag) || (/*C0000VoidFog.voidcraft &&*/ worldclient.provider.getDimension() == /*tamaized.voidcraft.common.handlers.ConfigHandler.*/dimensionIdVoid)) {
            double d0 = (((entity.getBrightnessForRender() & 15728640) >> 20) / 16.0d) + (((/*C0000VoidFog.voidcraft &&*/ worldclient.provider.getDimension() == /*tamaized.voidcraft.common.handlers.ConfigHandler.*/dimensionIdVoid) ? 15.0d : ((Entity) entity).posY + 4.0d) / 32.0d);
            if (d0 < 1.0d) {
                if (d0 < 0.0d) {
                    d0 = 0.0d;
                }
                float f2 = 100.0f * ((float) (d0 * d0));
                if (f2 < 5.0f) {
                    f2 = 5.0f;
                }
                if (f1 > f2) {
                    f1 = f2;
                }
            }
            GlStateManager.setFog(GlStateManager.FogMode.LINEAR);
            if (e.getFogMode() < 0) {
                GlStateManager.setFogStart(0.0f);
                GlStateManager.setFogEnd(f1);
            } else {
                GlStateManager.setFogStart(f1 * 0.75f);
                GlStateManager.setFogEnd(f1);
            }
            if (GLContext.getCapabilities().GL_NV_fog_distance) {
                GlStateManager.glFogi(34138, 34139);
            }
            if (worldclient.provider.doesXZShowFog((int) ((Entity) entity).posX, (int) ((Entity) entity).posZ)) {
                GlStateManager.setFogStart(f1 * 0.05f);
                GlStateManager.setFogEnd(Math.min(f1, 192.0f) * 0.5f);
            }
        }
    }

    @SubscribeEvent
    public void color(EntityViewRenderEvent.FogColors e) {
        /*if (!ConfigHandler.enabled) {
            return;
        }*/
        Entity entity = e.getEntity();
        WorldClient worldclient = Minecraft.getMinecraft().world;
        double d0 = entity.lastTickPosY + ((entity.posY - entity.lastTickPosY) * e.getRenderPartialTicks() * worldclient.provider.getVoidFogYFactor());
        if (d0 < 1.0d) {
            if (d0 < 0.0d) {
                d0 = 0.0d;
            }
            double d02 = d0 * d0;
            if (/*C0000VoidFog.voidcraft &&*/ worldclient.provider.getDimension() == /*tamaized.voidcraft.common.handlers.ConfigHandler.*/dimensionIdVoid) {
                d02 = 0.0d;
            }
            e.setRed((float) (e.getRed() * d02));
            e.setGreen((float) (e.getGreen() * d02));
            e.setBlue((float) (e.getBlue() * d02));
        }
    }
}
