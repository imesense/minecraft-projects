package org.imesense.dynamicspawncontrol.technical.initializer;

import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.config.blockgenerator.CfgBlockWorldGenerator;
import org.imesense.dynamicspawncontrol.technical.config.cachedameworld.CfgCacheWorldGame;
import org.imesense.dynamicspawncontrol.technical.config.gamedebugger.CfgGameDebugger;
import org.imesense.dynamicspawncontrol.technical.config.gameworldtime.CfgPluginWorldTime;
import org.imesense.dynamicspawncontrol.technical.config.logfile.CfgLogFile;
import org.imesense.dynamicspawncontrol.technical.config.player.CfgPlayer;
import org.imesense.dynamicspawncontrol.technical.config.rendernight.CfgRenderNight;
import org.imesense.dynamicspawncontrol.technical.config.skeletondropitem.CfgSkeletonDropItem;
import org.imesense.dynamicspawncontrol.technical.config.spiderattackweb.CfgSpiderAttackWeb;
import org.imesense.dynamicspawncontrol.technical.config.windowtitle.CfgWindowTitle;
import org.imesense.dynamicspawncontrol.technical.config.zombiedropitem.CfgZombieDropItem;
import org.imesense.dynamicspawncontrol.technical.customlibrary.inlineannotations.DCSSingleConfig;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import java.lang.reflect.Constructor;

/**
 *
 */
public final class RegisterCfgClasses
{
    /**
     *
     */
    private static final Class<?>[] CONFIG_CLASSES =
    {
        CfgCacheWorldGame.class,
        CfgGameDebugger.class,
        CfgLogFile.class,
        CfgPlayer.class,
        CfgRenderNight.class,
        CfgWindowTitle.class,
        CfgBlockWorldGenerator.class,
        CfgPluginWorldTime.class,
        CfgZombieDropItem.class,
        CfgSkeletonDropItem.class,
        CfgSpiderAttackWeb.class
    };

    /**
     *
     */
    public RegisterCfgClasses()
    {
        CodeGenericUtils.printInitClassToLog(this.getClass());
    }

    /**
     *
     */
    public static void initializeConfigs()
    {
        for (Class<?> configClass : CONFIG_CLASSES)
        {
            initializeConfig(configClass);
        }
    }

    /**
     *
     * @param configClass
     * @param <T>
     */
    private static <T> void initializeConfig(Class<T> configClass)
    {
        try
        {
            if (configClass.isAnnotationPresent(DCSSingleConfig.class))
            {
                DCSSingleConfig configAnnotation = configClass.getAnnotation(DCSSingleConfig.class);
                String configFileName = configAnnotation.fileName();

                Constructor<T> constructor = configClass.getConstructor(String.class);
                T configInstance = constructor.newInstance(configFileName);

                Log.writeDataToLogFile(0, "Initialized config: " + configFileName);
                Log.writeDataToLogFile(0, "configClass: " + configClass + " " + configInstance);
            }
            else
            {
                Log.writeDataToLogFile(2, "No ConfigClass annotation found in: " + configClass.getName());
            }
        }
        catch (NoSuchMethodException e)
        {
            Log.writeDataToLogFile(2, "Constructor with String parameter not found in class: " + configClass.getName() + " - " + e.getMessage());
        }
        catch (Exception e)
        {
            Log.writeDataToLogFile(2, "Exception in class: " + configClass.getName() + " - " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
