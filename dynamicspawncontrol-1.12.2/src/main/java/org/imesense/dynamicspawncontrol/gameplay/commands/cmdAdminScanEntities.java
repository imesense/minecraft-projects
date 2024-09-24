package org.imesense.dynamicspawncontrol.gameplay.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.customlibrary.*;

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
     */
    private static boolean instanceExists = false;

    /**
     *
     */
    public cmdAdminScanEntities()
    {
        if (instanceExists)
        {
            Log.writeDataToLogFile(2, String.format("An instance of [%s] already exists!", this.getClass().getSimpleName()));
            throw new RuntimeException();
        }

        instanceExists = true;

        CodeGenericUtils.printInitClassToLog(cmdAdminScanEntities.class);
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

        Log.writeDataToLogFile(0, "------------ START SCAN ENTITY LIST ------------");

        for (Entity entity : entities)
        {
            if (entity != null)
            {
                Log.writeDataToLogFile(0, "-----------------------------------------------------------");

                @Nonnull String entityType;
                Log.writeDataToLogFile(0, "Iteration: " + iterator.getAndIncrement());
                Log.writeDataToLogFile(0, entity.toString());
                Log.writeDataToLogFile(0, "Entity ID: " + entity.getEntityId());

                ResourceLocation entityKey = EntityList.getKey(entity);

                if (entityKey != null && entityKey.getResourceDomain().equals("minecraft"))
                {
                    entityType = "minecraft:" + entityKey.getResourcePath();
                }
                else
                {
                    entityType = entityKey != null ? entityKey.toString() : "Unknown";
                }

                Log.writeDataToLogFile(0, "Entity: " + entityType);

                Log.writeDataToLogFile(0, "-----------------------------------------------------------");
            }
        }

        sender.sendMessage(new TextComponentString(
                   EnumUnicodeCharacters.SECTION.getCharacter() +
                        EnumTextColors.GREEN.getCode() +
                        EnumCmdCalledType.CMD.getDescription() +
                        EnumUnicodeCharacters.WHITE_SPACE.getCharacter() +
                        "The scan is completed"));

        Log.writeDataToLogFile(0, "------------ END SCAN ENTITY LIST ------------");
    }
}
