package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.text.ChatColorUtil;
import org.imesense.dynamicspawncontrol.core.text.CmdCallType;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@InitLog
public final class CmdAdminScanEntities extends CommandBase
{
    public CmdAdminScanEntities()
    {

    }

    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_scan_entities";
    }

    /**
     *
     * @param iCommandSender
     * @return
     */
    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender iCommandSender)
    {
        return "/dsc_scan_entities";
    }

    /**
     *
     * @param minecraftServer
     * @param iCommandSender
     * @param args
     */
    @Override
    public void execute(@Nonnull MinecraftServer minecraftServer, @Nonnull ICommandSender iCommandSender, @Nonnull String... args)
    {
        World world = iCommandSender.getEntityWorld();

        AtomicInteger atomicInteger = new AtomicInteger();
        List<Entity> entityList = world.loadedEntityList;

        Log.write(0, "------------ START SCAN ENTITY LIST ------------");

        for (Entity entity : entityList)
        {
            if (entity != null)
            {
                Log.write(0, "-----------------------------------------------------------");

                @Nonnull String entityType;

                Log.write(0, "Iteration: " + atomicInteger.getAndIncrement());
                Log.write(0, entity.toString());
                Log.write(0, "Entity ID: " + entity.getEntityId());

                ResourceLocation resourceLocation = EntityList.getKey(entity);

                if (resourceLocation != null &&
                        resourceLocation.getResourceDomain().equals("minecraft"))
                {
                    entityType = "minecraft:" + resourceLocation.getResourcePath();
                }
                else
                {
                    entityType = resourceLocation != null ?
                            resourceLocation.toString() : "Unknown";
                }

                Log.write(0, "Entity: " + entityType);

                Log.write(0, "-----------------------------------------------------------");
            }
        }

        iCommandSender.sendMessage(new TextComponentString(
                ChatColorUtil.color(CmdCallType.COMMAND + " The scan is completed",
                        TextFormatting.GREEN)
        ));


        Log.write(0, "------------ END SCAN ENTITY LIST ------------");
    }
}
