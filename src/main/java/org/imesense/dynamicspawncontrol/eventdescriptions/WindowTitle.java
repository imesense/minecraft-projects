package org.imesense.dynamicspawncontrol.eventdescriptions;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.config.data.WindowTitleData;
import org.lwjgl.opengl.Display;

import javax.annotation.Nonnull;
import java.util.Objects;

public final class WindowTitle
{
    private static volatile WindowTitle _INSTANCE;

    public static WindowTitle getInstance()
    {
        return CodeGeneric.getInstance(WindowTitle.class);
    }

    public WindowTitle()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
    }

    @Nonnull
    private final String TITLE = WindowTitleData.ConfigDataWindowTitle.Instance.getWindowTitle();

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
