package org.imesense.dynamicspawncontrol;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import scala.util.Random;

import java.util.Arrays;
import java.util.List;

public class DivineRPGSummoner extends CommandBase
{
    private static final List<String> MOB_LIST = Arrays.asList(
            "divinerpg:crab",
            "divinerpg:cyclops",
            "divinerpg:ehu",
            "divinerpg:brown_grizzle",
            "divinerpg:white_grizzle",
            "divinerpg:husk",
            "divinerpg:jack_o_man",
            "divinerpg:king_crab",
            "divinerpg:kobblin",
            "divinerpg:liopleurodon",
            "divinerpg:livestock_merchant",
            "divinerpg:pumpkin_spider",
            "divinerpg:rainbour",
            "divinerpg:saguaro_worm",
            "divinerpg:shark",
            "divinerpg:smelter",
            "divinerpg:snapper",
            "divinerpg:stone_golem",
            "divinerpg:whale",

            "divinerpg:ender_triplets",
            "divinerpg:fractite",
            "divinerpg:workshop_merchant",
            "divinerpg:workshop_tinkerer",
            "divinerpg:bunny",
            "divinerpg:weak_cori",
            "divinerpg:mage",
            "divinerpg:mystic",
            "divinerpg:sorcerer",
            "divinerpg:captain_merik",
            "divinerpg:datticon",
            "divinerpg:fyracryx",
            "divinerpg:kazari",
            "divinerpg:leorna",
            "divinerpg:paratiku",
            "divinerpg:golem_of_rejuvenation",
            "divinerpg:seimer",
            "divinerpg:lord_vatticus",
            "divinerpg:war_general",
            "divinerpg:wraith",
            "divinerpg:zelus",
            "divinerpg:crypt_keeper",
            "divinerpg:dissiment",
            "divinerpg:dreamwrecker",
            "divinerpg:mysterious_man_layer_1",
            "divinerpg:mysterious_man_layer_2",
            "divinerpg:mysterious_man_layer_3",
            "divinerpg:the_hunger",
            "divinerpg:temple_guardian",
            "divinerpg:twins",
            "divinerpg:zone",
            "divinerpg:zoragon",
            "divinerpg:experienced_cori",
            "divinerpg:the_watcher",
            "divinerpg:twilight_demon"
    );

    @Override
    public String getName() {
        return "summondsc";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/summondsc - spawns all DivineRPG mobs from blacklist";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2; // только для OP
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (!(sender instanceof EntityPlayer))
            return;

        EntityPlayer player = (EntityPlayer) sender;
        World world = player.world;
        BlockPos basePos = player.getPosition();

        int offset = 0;

        Random random = new Random();

        for (String id : MOB_LIST)
        {
            ResourceLocation rl = new ResourceLocation(id);
            Entity entity = EntityList.createEntityByIDFromName(rl, world);

            if (entity != null)
            {
                double radius = 3.0;

                double angle = random.nextDouble() * Math.PI * 2;
                double distance = random.nextDouble() * radius;

                double x = basePos.getX() + Math.cos(angle) * distance;
                double z = basePos.getZ() + Math.sin(angle) * distance;
                double y = world.getHeight(new BlockPos(x, basePos.getY(), z)).getY();

                entity.setPosition(x, y, z);
                world.spawnEntity(entity);
            }
            else
            {
                sender.sendMessage(new TextComponentString("Failed to spawn: " + id));
            }
        }
    }
}
