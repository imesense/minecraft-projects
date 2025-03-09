package org.imesense.dynamicspawncontrol.event;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.config.data.WindowTitleData;
import org.lwjgl.opengl.Display;

import javax.annotation.Nonnull;
import java.util.Objects;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class WindowTitle
{
    public WindowTitle()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
    }

    @Nonnull
    private static final String TITLE = WindowTitleData.ConfigDataWindowTitle.Instance.getWindowTitle();

    @SubscribeEvent
    public void onUpdateClientTick_0(TickEvent.ClientTickEvent clientTickEvent)
    {
        replace();
    }

    public static String setTextWindowTitle(@Nonnull String formatString)
    {
        @Nonnull final String MC_VERSION = Loader.instance().getMinecraftModContainer().getVersion();
        @Nonnull final String MOD_COUNT = Integer.toString(Loader.instance().getModList().size());

        formatString = formatString.replaceAll("%mcver%", MC_VERSION);
        formatString = formatString.replaceAll("%modcount%", MOD_COUNT);

        return formatString;
    }

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
