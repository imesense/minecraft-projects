package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.core.collection.CmdCallTypeCollection;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumTextColor;
import org.imesense.dynamicspawncontrol.technical.customlibrary.enumeration.EnumUnicodeCharacter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 */
public final class CmdAdminScanEntities extends CommandBase
{
    /**
     *
     */
    public CmdAdminScanEntities()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
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

        Log.writeDataToLogFile(0, "------------ START SCAN ENTITY LIST ------------");

        for (Entity entity : entityList)
        {
            if (entity != null)
            {
                Log.writeDataToLogFile(0, "-----------------------------------------------------------");

                @Nonnull String entityType;

                Log.writeDataToLogFile(0, "Iteration: " + atomicInteger.getAndIncrement());
                Log.writeDataToLogFile(0, entity.toString());
                Log.writeDataToLogFile(0, "Entity ID: " + entity.getEntityId());

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

                Log.writeDataToLogFile(0, "Entity: " + entityType);

                Log.writeDataToLogFile(0, "-----------------------------------------------------------");
            }
        }

        iCommandSender.sendMessage(new TextComponentString(
                   EnumUnicodeCharacter.SECTION.getCharacter() +
                        EnumTextColor.GREEN.getCode() +
                        CmdCallTypeCollection.instance.getDescription(1) +
                        EnumUnicodeCharacter.WHITE_SPACE.getCharacter() +
                        "The scan is completed"));

        Log.writeDataToLogFile(0, "------------ END SCAN ENTITY LIST ------------");
    }
}
