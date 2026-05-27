package org.imesense.dynamicspawncontrol.bloodmoonmanager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

public class CommandBloodmoon extends CommandBase {
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos) {
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            suggestions.add("force");
            suggestions.add("stop");
            suggestions.add("entitynames");
        }
        return getListOfStringsMatchingLastWord(args, suggestions);
    }

    public int getRequiredPermissionLevel() {
        return 2;
    }

    public String getName() {
        return DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID;
    }

    public String getUsage(ICommandSender sender) {
        return "/bloodmoon <force|stop|entitynames>";
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: net.minecraft.command.CommandException */
    /* JADX INFO: Thrown type has an unknown type hierarchy: net.minecraft.command.WrongUsageException */
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException, WrongUsageException {
        if (args.length == 0) {
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }
        String subCommand = args[0];
        if (BloodmoonHandler.INSTANCE == null) {
            throw new CommandException(I18n.format("text.bloodmoon.commandError", new Object[0]), new Object[0]);
        }
        if (subCommand.equals("force")) {
            BloodmoonHandler.INSTANCE.force();
            sender.sendMessage(new TextComponentTranslation("text.bloodmoon.force", new Object[0]));
            return;
        }
        if (subCommand.equals("stop")) {
            BloodmoonHandler.INSTANCE.stop();
            sender.sendMessage(new TextComponentTranslation("text.bloodmoon.stop", new Object[0]));
            return;
        }
        if (subCommand.equals("entitynames")) {
            Entity senderEntity = sender.getCommandSenderEntity();
            Set<String> names = new HashSet<>();
            List<Entity> monsterNearby = senderEntity.world.getEntitiesInAABBexcluding(senderEntity, senderEntity.getEntityBoundingBox().grow(10.0d, 10.0d, 10.0d), EntitySelectors.NOT_SPECTATING);
            for (Entity e : monsterNearby) {
                if (e instanceof IMob) {
                    String entityName = BloodMoonSpawnValidator.getEntityName(e.getClass());
                    if (entityName != null) {
                        names.add(entityName);
                    }
                }
            }
            sender.sendMessage(new TextComponentTranslation("text.bloodmoon.entity", new Object[0]));
            for (String s : names) {
                sender.sendMessage(new TextComponentString(" - " + s));
            }
            return;
        }
        throw new WrongUsageException(getUsage(sender), new Object[0]);
    }
}
