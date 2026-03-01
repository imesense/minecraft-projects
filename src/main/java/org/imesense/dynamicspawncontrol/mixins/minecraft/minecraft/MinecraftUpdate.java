package org.imesense.dynamicspawncontrol.mixins.minecraft.minecraft;

import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.PixelFormat;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Minecraft.class)
public abstract class MinecraftUpdate
{
    @Shadow private int displayWidth;
    @Shadow private int displayHeight;

    @Shadow private boolean fullscreen;
    @Shadow @Final private static Logger LOGGER;

    @Shadow protected abstract void updateDisplayMode() throws LWJGLException;

    @Overwrite
    private void createDisplay() throws LWJGLException
    {
        Display.setResizable(true);
        Display.setTitle("Minecraft: 1.12.2-14.23.5.2864 + Dynamic Spawn Control");

        try
        {
            Display.create((new PixelFormat()).withDepthBits(24));
        }
        catch (LWJGLException lwjglexception)
        {
            LOGGER.error("Couldn't set pixel format", lwjglexception);

            try
            {
                Thread.sleep(1000L);
            }
            catch (InterruptedException ignored) { }

            if (this.fullscreen)
            {
                this.updateDisplayMode();
            }

            Display.create();
        }
    }
}