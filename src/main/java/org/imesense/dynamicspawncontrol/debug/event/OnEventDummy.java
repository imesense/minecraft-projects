package org.imesense.dynamicspawncontrol.debug.event;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;
import org.imesense.dynamicspawncontrol.debug.IDebug;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
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
}
