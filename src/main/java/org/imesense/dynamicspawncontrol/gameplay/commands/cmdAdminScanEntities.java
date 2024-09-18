package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.technical.customlibrary.AuxFunctions;
import org.imesense.dynamicspawncontrol.technical.customlibrary.CmdCalledType;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;
import org.imesense.dynamicspawncontrol.technical.customlibrary.TextEnumColors;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public final class cmdAdminScanEntities extends CommandBase
{
    /**
     *
     * @param nameClass
     */
    public cmdAdminScanEntities(final String nameClass)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], nameClass);
    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_scan_entities";
    }

    /**
     *
     * @param sender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender)
    {
        return "/dsc_scan_entities";
    }

    /**
     *
     * @param server
     * @param sender
     * @param args
     */
    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String... args)
    {
        World world = sender.getEntityWorld();

        AtomicInteger iterator = new AtomicInteger();
        List<Entity> entities = world.loadedEntityList;

        Log.writeDataToLogFile(Log.TypeLog[0], "------------ START SCAN ENTITY LIST ------------");

        for (Entity entity : entities)
        {
            if (entity != null)
            {
                Log.writeDataToLogFile(Log.TypeLog[0], "-----------------------------------------------------------");

                @Nonnull String entityType;
                Log.writeDataToLogFile(Log.TypeLog[0], "Iteration: " + iterator.getAndIncrement());
                Log.writeDataToLogFile(Log.TypeLog[0], entity.toString());
                Log.writeDataToLogFile(Log.TypeLog[0], "Entity ID: " + entity.getEntityId());

                ResourceLocation entityKey = EntityList.getKey(entity);

                if (entityKey != null && entityKey.getResourceDomain().equals("minecraft"))
                {
                    entityType = "minecraft:" + entityKey.getResourcePath();
                }
                else
                {
                    entityType = entityKey != null ? entityKey.toString() : "Unknown";
                }

                Log.writeDataToLogFile(Log.TypeLog[0], "Entity: " + entityType);

                Log.writeDataToLogFile(Log.TypeLog[0], "-----------------------------------------------------------");
            }
        }

        sender.sendMessage(new TextComponentString(
                AuxFunctions.UNICODE.SECTION +
                        TextEnumColors.GREEN.getCode() +
                        CmdCalledType.CMD.getDescription() +
                        AuxFunctions.UNICODE.WHITE_SPACE +
                        "The scan is completed"));

        Log.writeDataToLogFile(Log.TypeLog[0], "------------ END SCAN ENTITY LIST ------------");
    }
}
