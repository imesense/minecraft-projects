package org.imesense.dynamicspawncontrol.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import javax.annotation.Nonnull;

/**
 *
 */
@InitLog
public final class CmdAdminDumpEntity extends CommandBase
{
    /**
     *
     */
    public CmdAdminDumpEntity()
    {

    }

    /**
     *
     * @return
     */
    @Nonnull
    @Override
    public String getName()
    {
        return "dsc_dump_entity";
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
        return "/dsc_dump_entity";
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
        if (iCommandSender instanceof EntityPlayerMP)
        {
            RayTraceResult rayTraceResult = UniqueField.CLIENT.objectMouseOver;

            if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.ENTITY)
            {
                Entity entityHit = rayTraceResult.entityHit;
                StringBuilder entityInfo = new StringBuilder("Entity Info: ");

                ResourceLocation entityResourceLocation = EntityList.getKey(entityHit);
                String entityFullName = entityResourceLocation != null ? entityResourceLocation.toString() : "Unknown";

                entityInfo.append("Full Name: ").append(entityFullName).append("\n");
                entityInfo.append("Name: ").append(entityHit.getName()).append(", ");
                entityInfo.append("ID: ").append(entityHit.getEntityId()).append(", ");
                entityInfo.append("Class: ").append(entityHit.getClass().getSimpleName()).append("\n");

                if (entityHit instanceof EntityLivingBase)
                {
                    EntityLivingBase livingEntity = (EntityLivingBase) entityHit;

                    entityInfo.append("Armor: ");

                    for (ItemStack armorPiece : livingEntity.getArmorInventoryList())
                    {
                        if (!armorPiece.isEmpty())
                        {
                            entityInfo.append(armorPiece.getDisplayName()).append(" ");
                        }
                    }
                    entityInfo.append("\n");

                    ItemStack heldItem = livingEntity.getHeldItemMainhand();
                    entityInfo.append("Held Item: ")
                            .append(heldItem.isEmpty() ? "None" : heldItem.getDisplayName())
                            .append("\n");

                    entityInfo.append("Active Effects: ");

                    if (livingEntity.getActivePotionEffects().isEmpty())
                    {
                        entityInfo.append("None\n");
                    }
                    else
                    {
                        for (PotionEffect effect : livingEntity.getActivePotionEffects())
                        {
                            entityInfo.append(effect.getEffectName()).append(" (")
                                    .append(effect.getAmplifier()).append("), ");
                        }
                        entityInfo.append("\n");
                    }
                }
                else
                {
                    entityInfo.append("This entity is not living (no armor or effects).\n");
                }

                iCommandSender.sendMessage(new TextComponentString(entityInfo.toString()));
                Log.write(0, entityInfo.toString());
            }
            else
            {
                iCommandSender.sendMessage(new TextComponentString(TextFormatting.RED + "The entity is not selected!"));
            }
        }
        else
        {
            iCommandSender.sendMessage(new TextComponentString(TextFormatting.RED + "This command can only be used by players!"));
        }
    }
}