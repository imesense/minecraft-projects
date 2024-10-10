package org.imesense.dynamicspawncontrol.technical.customlibrary;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.technical.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.technical.eventprocessor.signal.SignalDataGetter;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.imesense.dynamicspawncontrol.technical.customlibrary.MultipleKeyWords.CommonKeyWorlds.*;

/**
 *
 * @param <T>
 */
public abstract class ListActionsConsumer<T extends SignalDataGetter>
{
    /**
     *
     */
    protected final List<Consumer<T>> ACTIONS = new ArrayList<>();

    /**
     *
     */
    public ListActionsConsumer()
    {

    }

    /**
     *
     * @param map
     */
    protected void addActions(AttributeMap<?> map)
    {
        if (map.has(ACTION_MESSAGE))
        {
            this.addDoMessageAction(map);
        }

        if (map.has(ACTION_ANGRY))
        {
            this.addAngryAction(map);
        }

        if (map.has(ACTION_HELD_ITEM))
        {
            this.addHeldItem(map);
        }

        if (map.has(ACTION_ARMOR_BOOTS))
        {
            this.addArmorItem(map, ACTION_ARMOR_BOOTS, EntityEquipmentSlot.FEET);
        }

        if (map.has(ACTION_ARMOR_LEGS))
        {
            this.addArmorItem(map, ACTION_ARMOR_LEGS, EntityEquipmentSlot.LEGS);
        }

        if (map.has(ACTION_ARMOR_HELMET))
        {
            this.addArmorItem(map, ACTION_ARMOR_HELMET, EntityEquipmentSlot.HEAD);
        }

        if (map.has(ACTION_ARMOR_CHEST))
        {
            this.addArmorItem(map, ACTION_ARMOR_CHEST, EntityEquipmentSlot.CHEST);
        }

        if (map.has(ACTION_SET_NBT))
        {
            this.addMobNBT(map);
        }

        if (map.has(ACTION_HEALTH_MULTIPLY) && map.has(ACTION_HEALTH_ADD))
        {
            this.addHealthAction(map);
        }

        if (map.has(ACTION_SPEED_MULTIPLY) && map.has(ACTION_SPEED_ADD))
        {
            this.addSpeedAction(map);
        }

        if (map.has(ACTION_DAMAGE_MULTIPLY) && map.has(ACTION_DAMAGE_ADD))
        {
            this.addDamageAction(map);
        }

        if (map.has(ACTION_CUSTOM_NAME))
        {
            this.addCustomName(map);
        }

        if (map.has(ACTION_POTION))
        {
            this.addPotionsAction(map);
        }

        if (map.has(ACTION_GIVE))
        {
            this.addGiveAction(map);
        }

        if (map.has(ACTION_DROP))
        {
            this.addDropAction(map);
        }

        if (map.has(ACTION_COMMAND))
        {
            this.addCommandAction(map);
        }

        if (map.has(ACTION_FIRE))
        {
            this.addFireAction(map);
        }

        if (map.has(ACTION_EXPLOSION))
        {
            this.addExplosionAction(map);
        }

        if (map.has(ACTION_CLEAR))
        {
            this.addClearAction(map);
        }

        if (map.has(ACTION_DAMAGE))
        {
            this.addDoDamageAction(map);
        }

        if (map.has(ACTION_SET_BLOCK))
        {
            this.addSetBlockAction(map);
        }

        if (map.has(ACTION_SET_HELD_ITEM))
        {
            this.addSetHeldItemAction(map);
        }

        if (map.has(ACTION_SET_HELD_AMOUNT))
        {
            this.addSetHeldAmountAction(map);
        }
    }

    /**
     *
     * @param map
     */
    private void addDoMessageAction(AttributeMap<?> map)
    {
        Object message = map.get(ACTION_MESSAGE);

        this.ACTIONS.add(event ->
        {
            EntityPlayerMP player = event.getPlayer();

            if (player == null)
            {
                player = (EntityPlayerMP) event.getWorld().getClosestPlayerToEntity(event.getEntityLiving(), 100);
            }

            if (player != null)
            {
                player.sendStatusMessage(new TextComponentString((String)message), false);
            }
        });
    }

    /**
     *
     * @param map
     */
    public void addAngryAction(AttributeMap<?> map)
    {
        Object actionAngry = map.get(ACTION_ANGRY);

        if ((Boolean)actionAngry)
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving instanceof EntityPigZombie)
                {
                    EntityPigZombie pigZombie = (EntityPigZombie) entityLiving;
                    EntityPlayerMP player = (EntityPlayerMP) event.getWorld().getClosestPlayerToEntity(entityLiving, 50);

                    if (player != null)
                    {
                        pigZombie.setRevengeTarget(player);
                    }
                }
                else if (entityLiving instanceof EntityLiving)
                {
                    EntityPlayerMP player = (EntityPlayerMP) event.getWorld().getClosestPlayerToEntity(entityLiving, 50);

                    if (player != null)
                    {
                        ((EntityLiving) entityLiving).setAttackTarget(player);
                    }
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addHeldItem(AttributeMap<?> map)
    {
        List<Pair<Float, ItemStack>> items = AuxFunctions.getItemsWeighted(map.getList(ACTION_HELD_ITEM));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack item = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving != null)
                {
                    if (entityLiving instanceof EntityEnderman)
                    {
                        if (item.getItem() instanceof ItemBlock)
                        {
                            ItemBlock b = (ItemBlock) item.getItem();
                            ((EntityEnderman) entityLiving).setHeldBlockState(b.getBlock().getStateFromMeta(b.getMetadata(item.getItemDamage())));
                        }
                    }
                    else
                    {
                        entityLiving.setHeldItem(EnumHand.MAIN_HAND, item.copy());
                    }
                }
            });
        }
        else
        {
            float total = AuxFunctions.getTotal(items);

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving != null)
                {
                    ItemStack item = AuxFunctions.getRandomItem(items, total);

                    if (entityLiving instanceof EntityEnderman)
                    {
                        if (item.getItem() instanceof ItemBlock)
                        {
                            ItemBlock b = (ItemBlock) item.getItem();
                            ((EntityEnderman) entityLiving).setHeldBlockState(b.getBlock().getStateFromMeta(b.getMetadata(item.getItemDamage())));
                        }
                    }
                    else
                    {
                        entityLiving.setHeldItem(EnumHand.MAIN_HAND, item.copy());
                    }
                }
            });
        }
    }

    /**
     *
     * @param map
     * @param itemKey
     * @param slot
     */
    private void addArmorItem(AttributeMap<?> map, AttributeKey<String> itemKey, EntityEquipmentSlot slot)
    {
        List<Pair<Float, ItemStack>> items = AuxFunctions.getItemsWeighted(map.getList(itemKey));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack item = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving != null)
                {
                    entityLiving.setItemStackToSlot(slot, item.copy());
                }
            });
        }
        else
        {
            float total = AuxFunctions.getTotal(items);

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving != null)
                {
                    entityLiving.setItemStackToSlot(slot, AuxFunctions.getRandomItem(items, total));
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addMobNBT(AttributeMap<?> map)
    {
        String mobNbt = (String)map.get(ACTION_SET_NBT);

        if (mobNbt != null)
        {
            NBTTagCompound tagCompound;

            try
            {
                tagCompound = JsonToNBT.getTagFromJson(mobNbt);
            }
            catch (NBTException exception)
            {
                Log.writeDataToLogFile(2, "Bad NBT for mob!");
                return;
            }

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();
                entityLiving.readEntityFromNBT(tagCompound);
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addHealthAction(AttributeMap<?> map)
    {
        Object multiple = map.has(ACTION_HEALTH_MULTIPLY) ? map.get(ACTION_HEALTH_MULTIPLY) : 1.f;
        Object added = map.has(ACTION_HEALTH_ADD) ? map.get(ACTION_HEALTH_ADD) : 0.f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLiving = event.getEntityLiving();

            if (entityLiving != null)
            {
                if (!entityLiving.getTags().contains("ctrlHealth"))
                {
                    IAttributeInstance entityAttribute = entityLiving.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

                    double newMax = entityAttribute.getBaseValue() * (Float)multiple + (Float)added;
                    entityAttribute.setBaseValue(newMax);
                    entityLiving.setHealth((float)newMax);
                    entityLiving.addTag("ctrlHealth");
                }
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addSpeedAction(AttributeMap<?> map)
    {
        Object multiple = map.has(ACTION_SPEED_MULTIPLY) ? map.get(ACTION_SPEED_MULTIPLY) : 1.f;
        Object added = map.has(ACTION_SPEED_ADD) ? map.get(ACTION_SPEED_ADD) : 0.f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLiving = event.getEntityLiving();

            if (entityLiving != null)
            {
                if (!entityLiving.getTags().contains("ctrlSpeed"))
                {
                    IAttributeInstance entityAttribute = entityLiving.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

                    double newMax = entityAttribute.getBaseValue() * (Float)multiple + (Float)added;
                    entityAttribute.setBaseValue(newMax);
                    entityLiving.addTag("ctrlSpeed");
                }
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addDamageAction(AttributeMap<?> map)
    {
        Object multiple = map.has(ACTION_DAMAGE_MULTIPLY) ? map.get(ACTION_DAMAGE_MULTIPLY) : 1.f;
        Object added = map.has(ACTION_DAMAGE_ADD) ? map.get(ACTION_DAMAGE_ADD) : 0.f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLiving = event.getEntityLiving();

            if (entityLiving != null)
            {
                if (!entityLiving.getTags().contains("ctrlDamage"))
                {
                    IAttributeInstance entityAttribute = entityLiving.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

                    double newMax = entityAttribute.getBaseValue() * (Float)multiple + (Float)added;
                    entityAttribute.setBaseValue(newMax);
                    entityLiving.addTag("ctrlDamage");
                }
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addCustomName(AttributeMap<?> map)
    {
        Object customName = map.get(ACTION_CUSTOM_NAME);

        if (customName != null)
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();
                entityLiving.setCustomNameTag((String)customName);
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addPotionsAction(AttributeMap<?> map)
    {
        List<AuxFunctions.PotionEffectWithChance> effects = new ArrayList<>();

        for (String actionPotion : map.getList(ACTION_POTION))
        {
            String[] split = Arrays.stream(StringUtils.split(actionPotion, ','))
                    .map(String::trim)
                    .toArray(String[]::new);

            if (split.length < 3 || split.length > 4)
            {
                Log.writeDataToLogFile(2, "Bad potion specifier '" + actionPotion + "'! Use <potion>,<duration>,<amplifier>[,<chance>]");
                continue;
            }

            Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(split[0]));

            if (potion == null)
            {
                Log.writeDataToLogFile(2, "Can't find potion '" + actionPotion + "'!");
                continue;
            }

            int duration;
            int amplifier;
            double chance = 1.0D; // Default to 100% chance

            try
            {
                duration = Integer.parseInt(split[1]);
                amplifier = Integer.parseInt(split[2]);

                if (split.length == 4)
                {
                    chance = Double.parseDouble(split[3]);
                }
            }
            catch (NumberFormatException exception)
            {
                Log.writeDataToLogFile(2, "Bad duration, amplifier or chance integer for '" + actionPotion + "'!");
                continue;
            }

            effects.add(new AuxFunctions.PotionEffectWithChance(new PotionEffect(potion, duration, amplifier), chance));
        }

        if (!effects.isEmpty())
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase living = event.getEntityLiving();

                if (living != null)
                {
                    for (AuxFunctions.PotionEffectWithChance effectWithChance : effects)
                    {
                        if (Math.random() <= effectWithChance.Chance)
                        {
                            PotionEffect effect = effectWithChance.Effect;
                            PotionEffect newEffect = new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier());
                            living.addPotionEffect(newEffect);
                        }
                    }
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addGiveAction(AttributeMap<?> map)
    {
        List<Pair<Float, ItemStack>> items = AuxFunctions.getItemsWeighted(map.getList(ACTION_GIVE));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack item = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                EntityPlayerMP player = event.getPlayer();

                if (player != null)
                {
                    if (!player.inventory.addItemStackToInventory(item.copy()))
                    {
                        player.entityDropItem(item.copy(), 1.05f);
                    }
                }
            });
        }
        else
        {
            float total = AuxFunctions.getTotal(items);

            this.ACTIONS.add(event ->
            {
                EntityPlayerMP player = event.getPlayer();

                if (player != null)
                {
                    ItemStack item = AuxFunctions.getRandomItem(items, total);

                    if (!player.inventory.addItemStackToInventory(item.copy()))
                    {
                        player.entityDropItem(item.copy(), 1.05f);
                    }
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addDropAction(AttributeMap<?> map)
    {
        List<Pair<Float, ItemStack>> items = AuxFunctions.getItemsWeighted(map.getList(ACTION_DROP));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack item = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                BlockPos pos = event.getPosition();
                EntityItem entityItem = new EntityItem(event.getWorld(), pos.getX(), pos.getY(), pos.getZ(), item.copy());
                event.getWorld().spawnEntity(entityItem);
            });
        }
        else
        {
            float total = AuxFunctions.getTotal(items);

            this.ACTIONS.add(event ->
            {
                BlockPos pos = event.getPosition();
                ItemStack item = AuxFunctions.getRandomItem(items, total);
                EntityItem entityItem = new EntityItem(event.getWorld(), pos.getX(), pos.getY(), pos.getZ(), item.copy());
                event.getWorld().spawnEntity(entityItem);
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addCommandAction(AttributeMap<?> map)
    {
        Object command = map.get(ACTION_COMMAND);

        this.ACTIONS.add(event ->
        {
            EntityPlayerMP player = event.getPlayer();
            MinecraftServer server = event.getWorld().getMinecraftServer();

            assert server != null;

            server.commandManager.executeCommand(player != null ? player : new Sender(event.getWorld(), null), (String)command);
        });
    }

    /**
     *
     * @param map
     */
    private void addFireAction(AttributeMap<?> map)
    {
        Object fireAction = map.get(ACTION_FIRE);

        this.ACTIONS.add(event ->
        {
            EntityLivingBase living = event.getEntityLiving();

            if (living != null)
            {
                living.attackEntityFrom(DamageSource.ON_FIRE, 0.1f);
                living.setFire((Integer)fireAction);
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addExplosionAction(AttributeMap<?> map)
    {
        String fireAction = (String)map.get(ACTION_EXPLOSION);
        String[] split = StringUtils.split(fireAction, ",");

        float strength = 1.0f;

        boolean flaming = false;
        boolean smoking = false;

        try
        {
            strength = Float.parseFloat(split[0]);
            flaming = "1".equalsIgnoreCase(split[1]) || "true".equals(split[1].toLowerCase()) || "yes".equals(split[1].toLowerCase());
            smoking = "1".equalsIgnoreCase(split[2]) || "true".equals(split[2].toLowerCase()) || "yes".equals(split[2].toLowerCase());
        }
        catch (Exception exception)
        {

        }

        float finalStrength = strength;
        boolean finalFlaming = flaming;
        boolean finalSmoking = smoking;

        this.ACTIONS.add(event ->
        {
            BlockPos pos = event.getPosition();

            if (pos != null)
            {
                event.getWorld().newExplosion(null, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, finalStrength, finalFlaming, finalSmoking);
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addClearAction(AttributeMap<?> map)
    {
        Object clear = map.get(ACTION_CLEAR);

        if ((Boolean)clear)
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase living = event.getEntityLiving();

                if (living != null)
                {
                    living.clearActivePotions();
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addDoDamageAction(AttributeMap<?> map)
    {
        String damage = (String) map.get(ACTION_DAMAGE);
        String[] split = StringUtils.split(damage, "=");
        DamageSource source = AuxFunctions.DamageMap.get(split[0]);

        if (source == null)
        {
            Log.writeDataToLogFile(2, "Can't find damage source '" + split[0] + "'!");
            return;
        }

        float amount = split.length > 1 ? Float.parseFloat(split[1]) : 1.0f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase living = event.getEntityLiving();

            if (living != null)
            {
                living.attackEntityFrom(source, amount);
            }
        });
    }

    /**
     *
     * @param map
     */
    private void addSetBlockAction(AttributeMap<?> map)
    {
        Function<SignalDataGetter, BlockPos> posFunction;

        if (map.has(BLOCK_OFFSET))
        {
            posFunction = (Function<SignalDataGetter, BlockPos>)
                    AuxFunctions.parseOffset((String)map.get(BLOCK_OFFSET));
        }
        else
        {
            posFunction = event -> event.getPosition();
        }

        Object json = map.get(ACTION_SET_BLOCK);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse((String)json);

        if (element.isJsonPrimitive())
        {
            String blockName = element.getAsString();
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

            if (block == null)
            {
                Log.writeDataToLogFile(2, "Block '" + blockName + "' is not valid!");
                return;
            }

            IBlockState state = block.getDefaultState();

            this.ACTIONS.add(event ->
            {
                BlockPos pos = posFunction.apply(event);

                if (pos != null)
                {
                    event.getWorld().setBlockState(pos, state, 3);
                }
            });
        }
        else
        {
            JsonObject obj = element.getAsJsonObject();

            if (!obj.has("block"))
            {
                Log.writeDataToLogFile(2, "Block is not valid!");
                return;
            }

            String blockName = obj.get("block").getAsString();
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

            if (block == null)
            {
                Log.writeDataToLogFile(2, "Block '" + blockName + "' is not valid!");
                return;
            }

            IBlockState state = block.getDefaultState();

            if (obj.has("properties"))
            {
                JsonArray propArray = obj.get("properties").getAsJsonArray();

                for (JsonElement el : propArray)
                {
                    JsonObject propObj = el.getAsJsonObject();
                    String name = propObj.get("name").getAsString();
                    String value = propObj.get("value").getAsString();

                    for (IProperty<?> key : state.getPropertyKeys())
                    {
                        if (name.equals(key.getName()))
                        {
                            state = AuxFunctions.set(state, key, value);
                        }
                    }
                }
            }

            IBlockState finalState = state;

            this.ACTIONS.add(event ->
            {
                BlockPos pos = posFunction.apply(event);

                if (pos != null)
                {
                    event.getWorld().setBlockState(pos, finalState, 3);
                }
            });
        }
    }

    /**
     *
     * @param map
     */
    private void addSetHeldItemAction(AttributeMap<?> map)
    {
        Object json = map.get(ACTION_SET_HELD_ITEM);
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse((String)json);
        ItemStack stack;

        if (element.isJsonPrimitive())
        {
            String name = element.getAsString();
            stack = ItemStackBuilder.parseStack(name);
        }
        else if (element.isJsonObject())
        {
            JsonObject obj = element.getAsJsonObject();
            stack = ItemStackBuilder.parseStack(obj);

            if (stack == null)
            {
                return;
            }
        }
        else
        {
            Log.writeDataToLogFile(2, "Item description '" + json + "' is not valid!");
            return;
        }

        this.ACTIONS.add(event -> event.getPlayer().setHeldItem(EnumHand.MAIN_HAND, stack.copy()));
    }

    /**
     *
     * @param map
     */
    private void addSetHeldAmountAction(AttributeMap<?> map)
    {
        String amount = (String)map.get(ACTION_SET_HELD_AMOUNT);

        int add = 0;
        int set = -1;

        if (amount.startsWith("+"))
        {
            add = Integer.parseInt(amount.substring(1));
        }
        else if (amount.startsWith("-"))
        {
            add = -Integer.parseInt(amount.substring(1));
        }
        else if (amount.startsWith("="))
        {
            set = Integer.parseInt(amount.substring(1));
        }
        else
        {
            set = Integer.parseInt(amount);
        }

        int finalSet = set;

        if (finalSet >= 0)
        {
            this.ACTIONS.add(event ->
            {
                ItemStack item = event.getPlayer().getHeldItemMainhand();
                item.setCount(finalSet);
                event.getPlayer().setHeldItem(EnumHand.MAIN_HAND, item.copy());
            });
        }
        else
        {
            int finalAdd = add;

            this.ACTIONS.add(event ->
            {
                ItemStack item = event.getPlayer().getHeldItemMainhand();

                int newCount = item.getCount() + finalAdd;

                if (newCount < 0)
                {
                    newCount = 0;
                }
                else if (newCount >= item.getMaxStackSize())
                {
                    newCount = item.getMaxStackSize()-1;
                }

                item.setCount(newCount);
                event.getPlayer().setHeldItem(EnumHand.MAIN_HAND, item.copy());
            });
        }
    }
}
