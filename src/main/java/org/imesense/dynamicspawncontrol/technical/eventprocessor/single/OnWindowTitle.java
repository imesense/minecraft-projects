package org.imesense.dynamicspawncontrol.technical.eventprocessor.single;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.technical.configs.ConfigWindowTitle;
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
     * @param nameClass
     */
    public OnWindowTitle(final String nameClass)
    {

    }

    /**
     *
     */
    @Nonnull
    private static final String TITLE = ConfigWindowTitle.WindowTitle;

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
