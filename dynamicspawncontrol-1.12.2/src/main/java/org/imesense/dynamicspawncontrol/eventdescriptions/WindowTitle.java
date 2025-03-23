package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.config.mainwindow.MainWindowTitleConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.lwjgl.opengl.Display;

import javax.annotation.Nonnull;
import java.util.Objects;

@InitLog
public final class WindowTitle
{
    private static volatile WindowTitle _INSTANCE;

    public static WindowTitle getInstance()
    {
        return CodeGeneric.getInstance(WindowTitle.class);
    }

    public WindowTitle()
    {
        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    private final String TITLE =
            MainWindowTitleConfig.getInstance(MainWindowTitleConfig.class).getWindowTitle();

    public String setTextWindowTitle(@Nonnull String formatString)
    {
        @Nonnull final String MC_VERSION = Loader.instance().getMinecraftModContainer().getVersion();
        @Nonnull final String MOD_COUNT = Integer.toString(Loader.instance().getModList().size());

        formatString = formatString.replaceAll("%mcver%", MC_VERSION);
        formatString = formatString.replaceAll("%modcount%", MOD_COUNT);

        return formatString;
    }

    public void replace()
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
