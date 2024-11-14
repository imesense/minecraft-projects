package org.imesense.dynamicspawncontrol.eventprocessor.listaction;

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
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeKey;
import org.imesense.dynamicspawncontrol.core.attributefactory.AttributeMap;
import org.imesense.dynamicspawncontrol.core.api.SignalDataGetter;
import org.imesense.dynamicspawncontrol.technical.customlibrary.AuxFunction;
import org.imesense.dynamicspawncontrol.core.builder.ItemStackBuilder;
import org.imesense.dynamicspawncontrol.core.sender.Sender;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.imesense.dynamicspawncontrol.eventprocessor.generic.keyword.CommonKeyWord.*;

/**
 *
 * @param <T>
 */
public abstract class ListActionConsumer<T extends SignalDataGetter>
{
    /**
     *
     */
    protected final List<Consumer<T>> ACTIONS = new ArrayList<>();

    /**
     *
     */
    public ListActionConsumer()
    {

    }

    /**
     *
     * @param attributeMap
     */
    protected void addActions(AttributeMap<?> attributeMap)
    {
        if (attributeMap.has(ACTION_MESSAGE))
        {
            this.addDoMessageAction(attributeMap);
        }

        if (attributeMap.has(ACTION_ANGRY))
        {
            this.addAngryAction(attributeMap);
        }

        if (attributeMap.has(ACTION_HELD_ITEM))
        {
            this.addHeldItem(attributeMap);
        }

        if (attributeMap.has(ACTION_ARMOR_BOOTS))
        {
            this.addArmorItem(attributeMap, ACTION_ARMOR_BOOTS, EntityEquipmentSlot.FEET);
        }

        if (attributeMap.has(ACTION_ARMOR_LEGS))
        {
            this.addArmorItem(attributeMap, ACTION_ARMOR_LEGS, EntityEquipmentSlot.LEGS);
        }

        if (attributeMap.has(ACTION_ARMOR_HELMET))
        {
            this.addArmorItem(attributeMap, ACTION_ARMOR_HELMET, EntityEquipmentSlot.HEAD);
        }

        if (attributeMap.has(ACTION_ARMOR_CHEST))
        {
            this.addArmorItem(attributeMap, ACTION_ARMOR_CHEST, EntityEquipmentSlot.CHEST);
        }

        if (attributeMap.has(ACTION_SET_NBT))
        {
            this.addMobNBT(attributeMap);
        }

        if (attributeMap.has(ACTION_HEALTH_MULTIPLY) && attributeMap.has(ACTION_HEALTH_ADD))
        {
            this.addHealthAction(attributeMap);
        }

        if (attributeMap.has(ACTION_SPEED_MULTIPLY) && attributeMap.has(ACTION_SPEED_ADD))
        {
            this.addSpeedAction(attributeMap);
        }

        if (attributeMap.has(ACTION_DAMAGE_MULTIPLY) && attributeMap.has(ACTION_DAMAGE_ADD))
        {
            this.addDamageAction(attributeMap);
        }

        if (attributeMap.has(ACTION_CUSTOM_NAME))
        {
            this.addCustomName(attributeMap);
        }

        if (attributeMap.has(ACTION_POTION))
        {
            this.addPotionsAction(attributeMap);
        }

        if (attributeMap.has(ACTION_GIVE))
        {
            this.addGiveAction(attributeMap);
        }

        if (attributeMap.has(ACTION_DROP))
        {
            this.addDropAction(attributeMap);
        }

        if (attributeMap.has(ACTION_COMMAND))
        {
            this.addCommandAction(attributeMap);
        }

        if (attributeMap.has(ACTION_FIRE))
        {
            this.addFireAction(attributeMap);
        }

        if (attributeMap.has(ACTION_EXPLOSION))
        {
            this.addExplosionAction(attributeMap);
        }

        if (attributeMap.has(ACTION_CLEAR))
        {
            this.addClearAction(attributeMap);
        }

        if (attributeMap.has(ACTION_DAMAGE))
        {
            this.addDoDamageAction(attributeMap);
        }

        if (attributeMap.has(ACTION_SET_BLOCK))
        {
            this.addSetBlockAction(attributeMap);
        }

        if (attributeMap.has(ACTION_SET_HELD_ITEM))
        {
            this.addSetHeldItemAction(attributeMap);
        }

        if (attributeMap.has(ACTION_SET_HELD_AMOUNT))
        {
            this.addSetHeldAmountAction(attributeMap);
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addDoMessageAction(AttributeMap<?> attributeMap)
    {
        Object message = attributeMap.get(ACTION_MESSAGE);

        this.ACTIONS.add(event ->
        {
            EntityPlayerMP entityPlayerMP = event.getPlayer();

            if (entityPlayerMP == null)
            {
                entityPlayerMP = (EntityPlayerMP) event.getWorld().getClosestPlayerToEntity(event.getEntityLiving(), 100.00);
            }

            if (entityPlayerMP != null)
            {
                entityPlayerMP.sendStatusMessage(new TextComponentString((String) message), false);
            }
        });
    }

    /**
     *
     * @param attributeMap
     */
    public void addAngryAction(AttributeMap<?> attributeMap)
    {
        Object actionAngry = attributeMap.get(ACTION_ANGRY);

        if ((Boolean)actionAngry)
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLivingBase = event.getEntityLiving();

                if (entityLivingBase instanceof EntityPigZombie)
                {
                    EntityPigZombie entityPigZombie = (EntityPigZombie) entityLivingBase;
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.getWorld().getClosestPlayerToEntity(entityLivingBase, 50.00);

                    if (entityPlayerMP != null)
                    {
                        entityPigZombie.setRevengeTarget(entityPlayerMP);
                    }
                }
                else if (entityLivingBase instanceof EntityLiving)
                {
                    EntityPlayerMP entityPlayerMP = (EntityPlayerMP) event.getWorld().getClosestPlayerToEntity(entityLivingBase, 50.00);

                    if (entityPlayerMP != null)
                    {
                        ((EntityLiving) entityLivingBase).setAttackTarget(entityPlayerMP);
                    }
                }
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addHeldItem(AttributeMap<?> attributeMap)
    {
        List<Pair<Float, ItemStack>> items = AuxFunction.getItemsWeighted(attributeMap.getList(ACTION_HELD_ITEM));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack itemStack = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLiving = event.getEntityLiving();

                if (entityLiving != null)
                {
                    if (entityLiving instanceof EntityEnderman)
                    {
                        if (itemStack.getItem() instanceof ItemBlock)
                        {
                            ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

                            ((EntityEnderman) entityLiving).setHeldBlockState(itemBlock.getBlock().
                                    getStateFromMeta(itemBlock.getMetadata(itemStack.getItemDamage())));
                        }
                    }
                    else
                    {
                        entityLiving.setHeldItem(EnumHand.MAIN_HAND, itemStack.copy());
                    }
                }
            });
        }
        else
        {
            float total = AuxFunction.getTotal(items);

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLivingBase = event.getEntityLiving();

                if (entityLivingBase != null)
                {
                    ItemStack itemStack = AuxFunction.getRandomItem(items, total);

                    if (entityLivingBase instanceof EntityEnderman)
                    {
                        if (itemStack.getItem() instanceof ItemBlock)
                        {
                            ItemBlock itemBlock = (ItemBlock) itemStack.getItem();

                            ((EntityEnderman) entityLivingBase).setHeldBlockState(itemBlock.getBlock().
                                    getStateFromMeta(itemBlock.getMetadata(itemStack.getItemDamage())));
                        }
                    }
                    else
                    {
                        entityLivingBase.setHeldItem(EnumHand.MAIN_HAND, itemStack.copy());
                    }
                }
            });
        }
    }

    /**
     *
     * @param attributeMap
     * @param stringAttributeKey
     * @param entityEquipmentSlot
     */
    private void addArmorItem(AttributeMap<?> attributeMap, AttributeKey<String> stringAttributeKey, EntityEquipmentSlot entityEquipmentSlot)
    {
        List<Pair<Float, ItemStack>> items =
                AuxFunction.getItemsWeighted(attributeMap.getList(stringAttributeKey));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack itemStack = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLivingBase = event.getEntityLiving();

                if (entityLivingBase != null)
                {
                    entityLivingBase.setItemStackToSlot(entityEquipmentSlot, itemStack.copy());
                }
            });
        }
        else
        {
            float total = AuxFunction.getTotal(items);

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLivingBase = event.getEntityLiving();

                if (entityLivingBase != null)
                {
                    entityLivingBase.setItemStackToSlot(entityEquipmentSlot, AuxFunction.getRandomItem(items, total));
                }
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addMobNBT(AttributeMap<?> attributeMap)
    {
        String mobNbt = (String)attributeMap.get(ACTION_SET_NBT);

        if (mobNbt != null)
        {
            NBTTagCompound nbtTagCompound;

            try
            {
                nbtTagCompound = JsonToNBT.getTagFromJson(mobNbt);
            }
            catch (NBTException exception)
            {
                Log.writeDataToLogFile(2, "Bad NBT for mob!");
                return;
            }

            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLivingBase = event.getEntityLiving();
                entityLivingBase.readEntityFromNBT(nbtTagCompound);
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addHealthAction(AttributeMap<?> attributeMap)
    {
        Object multiple = attributeMap.has(ACTION_HEALTH_MULTIPLY) ? attributeMap.get(ACTION_HEALTH_MULTIPLY) : 1.f;
        Object added = attributeMap.has(ACTION_HEALTH_ADD) ? attributeMap.get(ACTION_HEALTH_ADD) : 0.f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLivingBase = event.getEntityLiving();

            if (entityLivingBase != null)
            {
                if (!entityLivingBase.getTags().contains("ctrlHealth"))
                {
                    IAttributeInstance iAttributeInstance = entityLivingBase.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH);

                    double newMax = iAttributeInstance.getBaseValue() * (Float) multiple + (Float) added;

                    iAttributeInstance.setBaseValue(newMax);

                    entityLivingBase.setHealth((float)newMax);
                    entityLivingBase.addTag("ctrlHealth");
                }
            }
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addSpeedAction(AttributeMap<?> attributeMap)
    {
        Object multiple = attributeMap.has(ACTION_SPEED_MULTIPLY) ? attributeMap.get(ACTION_SPEED_MULTIPLY) : 1.f;
        Object added = attributeMap.has(ACTION_SPEED_ADD) ? attributeMap.get(ACTION_SPEED_ADD) : 0.f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLivingBase = event.getEntityLiving();

            if (entityLivingBase != null)
            {
                if (!entityLivingBase.getTags().contains("ctrlSpeed"))
                {
                    IAttributeInstance iAttributeInstance = entityLivingBase.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

                    double newMax = iAttributeInstance.getBaseValue() * (Float) multiple + (Float) added;
                    iAttributeInstance.setBaseValue(newMax);
                    entityLivingBase.addTag("ctrlSpeed");
                }
            }
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addDamageAction(AttributeMap<?> attributeMap)
    {
        Object multiple = attributeMap.has(ACTION_DAMAGE_MULTIPLY) ? attributeMap.get(ACTION_DAMAGE_MULTIPLY) : 1.f;
        Object added = attributeMap.has(ACTION_DAMAGE_ADD) ? attributeMap.get(ACTION_DAMAGE_ADD) : 0.f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLiving = event.getEntityLiving();

            if (entityLiving != null)
            {
                if (!entityLiving.getTags().contains("ctrlDamage"))
                {
                    IAttributeInstance iAttributeInstance = entityLiving.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);

                    double newMax = iAttributeInstance.getBaseValue() * (Float) multiple + (Float) added;
                    iAttributeInstance.setBaseValue(newMax);
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
                EntityLivingBase entityLivingBase = event.getEntityLiving();
                entityLivingBase.setCustomNameTag((String) customName);
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addPotionsAction(AttributeMap<?> attributeMap)
    {
        List<AuxFunction.PotionEffectWithChance> effects = new ArrayList<>();

        for (String actionPotion : attributeMap.getList(ACTION_POTION))
        {
            String[] split = Arrays.stream(StringUtils.split(actionPotion, ','))
                    .map(String::trim)
                    .toArray(String[]::new);

            if (split.length < 3 || split.length > 4)
            {
                Log.writeDataToLogFile(2, "Bad potion specifier '" +
                        actionPotion + "'! Use <potion>,<duration>,<amplifier>[,<chance>]");

                continue;
            }

            Potion potion = ForgeRegistries.POTIONS.getValue(new ResourceLocation(split[0]));

            if (potion == null)
            {
                Log.writeDataToLogFile(2, "Can't find potion '" + actionPotion + "'!");
                continue;
            }

            int duration, amplifier;
            double chance = 1.0D;

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

            effects.add(new AuxFunction.PotionEffectWithChance(new PotionEffect(potion, duration, amplifier), chance));
        }

        if (!effects.isEmpty())
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLivingBase = event.getEntityLiving();

                if (entityLivingBase != null)
                {
                    for (AuxFunction.PotionEffectWithChance effectWithChance : effects)
                    {
                        if (UniqueField.RANDOM.nextDouble() <= effectWithChance.Chance)
                        {
                            PotionEffect effect = effectWithChance.Effect;
                            PotionEffect newEffect = new PotionEffect(effect.getPotion(), effect.getDuration(), effect.getAmplifier());

                            entityLivingBase.addPotionEffect(newEffect);
                        }
                    }
                }
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addGiveAction(AttributeMap<?> attributeMap)
    {
        List<Pair<Float, ItemStack>> items = AuxFunction.getItemsWeighted(attributeMap.getList(ACTION_GIVE));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack itemStack = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                EntityPlayerMP entityPlayerMP = event.getPlayer();

                if (entityPlayerMP != null)
                {
                    if (!entityPlayerMP.inventory.addItemStackToInventory(itemStack.copy()))
                    {
                        entityPlayerMP.entityDropItem(itemStack.copy(), 1.05f);
                    }
                }
            });
        }
        else
        {
            float total = AuxFunction.getTotal(items);

            this.ACTIONS.add(event ->
            {
                EntityPlayerMP entityPlayerMP = event.getPlayer();

                if (entityPlayerMP != null)
                {
                    ItemStack itemStack = AuxFunction.getRandomItem(items, total);

                    if (!entityPlayerMP.inventory.addItemStackToInventory(itemStack.copy()))
                    {
                        entityPlayerMP.entityDropItem(itemStack.copy(), 1.05f);
                    }
                }
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addDropAction(AttributeMap<?> attributeMap)
    {
        List<Pair<Float, ItemStack>> items = AuxFunction.getItemsWeighted(attributeMap.getList(ACTION_DROP));

        if (items.isEmpty())
        {
            return;
        }

        if (items.size() == 1)
        {
            ItemStack itemStack = items.get(0).getRight();

            this.ACTIONS.add(event ->
            {
                BlockPos blockPos = event.getPosition();
                EntityItem entityItem = new EntityItem(event.getWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack.copy());
                event.getWorld().spawnEntity(entityItem);
            });
        }
        else
        {
            float total = AuxFunction.getTotal(items);

            this.ACTIONS.add(event ->
            {
                BlockPos blockPos = event.getPosition();
                ItemStack itemStack = AuxFunction.getRandomItem(items, total);
                EntityItem entityItem = new EntityItem(event.getWorld(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), itemStack.copy());
                event.getWorld().spawnEntity(entityItem);
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addCommandAction(AttributeMap<?> attributeMap)
    {
        Object command = attributeMap.get(ACTION_COMMAND);

        this.ACTIONS.add(event ->
        {
            EntityPlayerMP entityPlayerMP = event.getPlayer();
            MinecraftServer minecraftServer = event.getWorld().getMinecraftServer();

            assert minecraftServer != null;

            minecraftServer.commandManager.executeCommand(entityPlayerMP != null ? entityPlayerMP :
                    new Sender(event.getWorld(), null), (String) command);
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addFireAction(AttributeMap<?> attributeMap)
    {
        Object fireAction = attributeMap.get(ACTION_FIRE);

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLivingBase = event.getEntityLiving();

            if (entityLivingBase != null)
            {
                entityLivingBase.attackEntityFrom(DamageSource.ON_FIRE, 0.1f);
                entityLivingBase.setFire((Integer) fireAction);
            }
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addExplosionAction(AttributeMap<?> attributeMap)
    {
        String fireAction = (String) attributeMap.get(ACTION_EXPLOSION);
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
            BlockPos blockPos = event.getPosition();

            if (blockPos != null)
            {
                event.getWorld().newExplosion(null,
                        blockPos.getX() + .5, blockPos.getY() + .5, blockPos.getZ() + .5, finalStrength, finalFlaming, finalSmoking);
            }
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addClearAction(AttributeMap<?> attributeMap)
    {
        Object clear = attributeMap.get(ACTION_CLEAR);

        if ((Boolean) clear)
        {
            this.ACTIONS.add(event ->
            {
                EntityLivingBase entityLivingBase = event.getEntityLiving();

                if (entityLivingBase != null)
                {
                    entityLivingBase.clearActivePotions();
                }
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addDoDamageAction(AttributeMap<?> attributeMap)
    {
        String damage = (String) attributeMap.get(ACTION_DAMAGE);
        String[] split = StringUtils.split(damage, "=");
        DamageSource damageSource = AuxFunction.DAMAGE_MAP.get(split[0]);

        if (damageSource == null)
        {
            Log.writeDataToLogFile(2, "Can't find damage source '" + split[0] + "'!");
            return;
        }

        float amount = split.length > 1 ? Float.parseFloat(split[1]) : 1.0f;

        this.ACTIONS.add(event ->
        {
            EntityLivingBase entityLivingBase = event.getEntityLiving();

            if (entityLivingBase != null)
            {
                entityLivingBase.attackEntityFrom(damageSource, amount);
            }
        });
    }

    /**
     *
     * @param attributeMap
     */
    private void addSetBlockAction(AttributeMap<?> attributeMap)
    {
        Function<SignalDataGetter, BlockPos> signalDataGetterBlockPosFunction;

        if (attributeMap.has(BLOCK_OFFSET))
        {
            signalDataGetterBlockPosFunction = (Function<SignalDataGetter, BlockPos>)
                    AuxFunction.parseOffset((String) attributeMap.get(BLOCK_OFFSET));
        }
        else
        {
            signalDataGetterBlockPosFunction = event -> event.getPosition();
        }

        Object object = attributeMap.get(ACTION_SET_BLOCK);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse((String) object);

        if (jsonElement.isJsonPrimitive())
        {
            String blockName = jsonElement.getAsString();
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

            if (block == null)
            {
                Log.writeDataToLogFile(2, "Block '" + blockName + "' is not valid!");
                return;
            }

            IBlockState iBlockState = block.getDefaultState();

            this.ACTIONS.add(event ->
            {
                BlockPos blockPos = signalDataGetterBlockPosFunction.apply(event);

                if (blockPos != null)
                {
                    event.getWorld().setBlockState(blockPos, iBlockState, 3);
                }
            });
        }
        else
        {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (!jsonObject.has("block"))
            {
                Log.writeDataToLogFile(2, "Block is not valid!");
                return;
            }

            String blockName = jsonObject.get("block").getAsString();
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));

            if (block == null)
            {
                Log.writeDataToLogFile(2, "Block '" + blockName + "' is not valid!");
                return;
            }

            IBlockState iBlockState = block.getDefaultState();

            if (jsonObject.has("properties"))
            {
                JsonArray propArray = jsonObject.get("properties").getAsJsonArray();

                for (JsonElement jsonElement1 : propArray)
                {
                    JsonObject jsonObject1 = jsonElement1.getAsJsonObject();

                    String name = jsonObject1.get("name").getAsString();
                    String value = jsonObject1.get("value").getAsString();

                    for (IProperty<?> iProperty : iBlockState.getPropertyKeys())
                    {
                        if (name.equals(iProperty.getName()))
                        {
                            iBlockState = AuxFunction.set(iBlockState, iProperty, value);
                        }
                    }
                }
            }

            IBlockState iBlockState1 = iBlockState;

            this.ACTIONS.add(event ->
            {
                BlockPos blockPos = signalDataGetterBlockPosFunction.apply(event);

                if (blockPos != null)
                {
                    event.getWorld().setBlockState(blockPos, iBlockState1, 3);
                }
            });
        }
    }

    /**
     *
     * @param attributeMap
     */
    private void addSetHeldItemAction(AttributeMap<?> attributeMap)
    {
        Object json = attributeMap.get(ACTION_SET_HELD_ITEM);
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse((String)json);
        ItemStack itemStack;

        if (jsonElement.isJsonPrimitive())
        {
            String name = jsonElement.getAsString();
            itemStack = ItemStackBuilder.parseStack(name);
        }
        else if (jsonElement.isJsonObject())
        {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            itemStack = ItemStackBuilder.parseStack(jsonObject);

            if (itemStack == null)
            {
                return;
            }
        }
        else
        {
            Log.writeDataToLogFile(2, "Item description '" + json + "' is not valid!");
            return;
        }

        this.ACTIONS.add(event -> event.getPlayer().setHeldItem(EnumHand.MAIN_HAND, itemStack.copy()));
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
                ItemStack itemStack = event.getPlayer().getHeldItemMainhand();
                itemStack.setCount(finalSet);
                event.getPlayer().setHeldItem(EnumHand.MAIN_HAND, itemStack.copy());
            });
        }
        else
        {
            int finalAdd = add;

            this.ACTIONS.add(event ->
            {
                ItemStack itemStack = event.getPlayer().getHeldItemMainhand();

                int newCount = itemStack.getCount() + finalAdd;

                if (newCount < 0)
                {
                    newCount = 0;
                }
                else if (newCount >= itemStack.getMaxStackSize())
                {
                    newCount = itemStack.getMaxStackSize()-1;
                }

                itemStack.setCount(newCount);
                event.getPlayer().setHeldItem(EnumHand.MAIN_HAND, itemStack.copy());
            });
        }
    }
}
