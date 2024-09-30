package org.imesense.dynamicspawncontrol.technical.eventprocessor.primitive;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.windowtitle.DataWindowTitle;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.lwjgl.opengl.Display;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 *
 */
@Mod.EventBusSubscriber
public final class OnWindowTitle
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnWindowTitle()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(OnWindowTitle.class);
    }

    /**
     *
     */
    @Nonnull
    private static final String TITLE = DataWindowTitle.windowTitle.instance.getWindowTitle();

    /**
     *
     * @param event
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public synchronized void onUpdateClientTick_0(TickEvent.ClientTickEvent event)
    {
        replace();
    }

    /**
     *
     * @param formatString
     * @return
     */
    public static String setTextWindowTitle(@Nonnull String formatString)
    {
        @Nonnull final String mcVersion = Loader.instance().getMinecraftModContainer().getVersion();
        @Nonnull final String modCount = Integer.toString(Loader.instance().getModList().size());

        formatString = formatString.replaceAll("%mcver%", mcVersion);
        formatString = formatString.replaceAll("%modcount%", modCount);

        return formatString;
    }

    /**
     *
     */
    public static void replace()
    {
        if (FMLCommonHandler.instance().getSide().isClient())
        {
            if (!Objects.equals(TITLE, ""))
            {
                Display.setTitle(setTextWindowTitle(TITLE));
            }
        }
    }
}
