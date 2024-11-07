package org.imesense.dynamicspawncontrol.event;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.config.data.WindowTitleData;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.lwjgl.opengl.Display;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 *
 */
@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class OnEventWindowTitle
{
    /**
     *
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public OnEventWindowTitle()
    {
		CodeGeneric.printInitClassToLog(this.getClass());
		
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;
    }

    /**
     *
     */
    @Nonnull
    private static final String TITLE = WindowTitleData.ConfigDataWindowTitle.Instance.getWindowTitle();

    /**
     *
     * @param clientTickEvent
     */
    @SubscribeEvent
    public void onUpdateClientTick_0(TickEvent.ClientTickEvent clientTickEvent)
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
        @Nonnull final String MC_VERSION = Loader.instance().getMinecraftModContainer().getVersion();
        @Nonnull final String MOD_COUNT = Integer.toString(Loader.instance().getModList().size());

        formatString = formatString.replaceAll("%mcver%", MC_VERSION);
        formatString = formatString.replaceAll("%modcount%", MOD_COUNT);

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
