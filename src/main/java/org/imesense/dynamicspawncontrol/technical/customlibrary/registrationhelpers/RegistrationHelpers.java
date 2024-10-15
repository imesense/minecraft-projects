package org.imesense.dynamicspawncontrol.technical.customlibrary.registrationhelpers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class RegistrationHelpers
{
    public static void run(IProcedure proc)
    {
        proc.run();
    }

    public static void cheat(IProcedure proc)
    {
        Loader l = Loader.instance();
        ModContainer k = l.activeModContainer();
        l.setActiveModContainer(l.getMinecraftModContainer());
        proc.run();
        l.setActiveModContainer(k);
    }

    public static void setRegNameIllegally(IForgeRegistryEntry<?> entry, String name)
    {
        cheat(() ->
        {
            entry.setRegistryName(new ResourceLocation("minecraft", name));
        });
    }

    public static <T extends IForgeRegistryEntry<T>> T regHelper(IForgeRegistry<T> registry, T thing)
    {
        assert thing != null;

        registry.register(thing);
        return thing;
    }

    public static Item registerOreNames(Item thing, String[] oreNames)
    {
        assert thing != null;

        String[] var2 = oreNames;
        int var3 = oreNames.length;

        for(int var4 = 0; var4 < var3; ++var4)
        {
            String oreName = var2[var4];
            OreDictionary.registerOre(oreName, thing);
        }

        return thing;
    }

    public static ItemStack registerOreNames(ItemStack thing, String[] oreNames)
    {
        assert thing != null;

        String[] var2 = oreNames;
        int var3 = oreNames.length;

        for(int var4 = 0; var4 < var3; ++var4)
        {
            String oreName = var2[var4];
            OreDictionary.registerOre(oreName, thing);
        }

        return thing;
    }

    public static Item regHelperOre(IForgeRegistry<Item> registry, Item thing, String[] oreNames)
    {
        assert thing != null;

        registry.register(thing);
        return registerOreNames(thing, oreNames);
    }

    public static <T extends IForgeRegistryEntry<T>> T regHelper(IForgeRegistry<T> registry, T thing, String name)
    {
        return regHelper(registry, thing, name, true);
    }

    public static <T extends IForgeRegistryEntry<T>> T regHelper(IForgeRegistry<T> registry, T thing, String name, boolean doTransKey, boolean shouldCheat)
    {
        assert thing != null;

        assert name != null;

        IProcedure nameIt = () ->
        {
            nameHelper(thing, name, doTransKey);
        };

        if (shouldCheat)
        {
            cheat(nameIt);
        }
        else
        {
            run(nameIt);
        }

        return regHelper(registry, thing);
    }

    public static <T extends IForgeRegistryEntry<T>> T regHelper(IForgeRegistry<T> registry, T thing, String name, boolean doTransKey)
    {
        return regHelper(registry, thing, name, doTransKey, false);
    }

    public static <T extends IForgeRegistryEntry<T>> T regHelper(IForgeRegistry<T> registry, T thing, ResourceLocation loc)
    {
        assert thing != null;

        assert loc != null;

        nameHelper(thing, loc, true);
        return regHelper(registry, thing);
    }

    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, ResourceLocation loc, boolean doTransKey)
    {
        assert thing != null;

        assert loc != null;

        thing.setRegistryName(loc);

        if (doTransKey)
        {
            String dotname = loc.getResourceDomain() + "." + loc.getResourcePath();

            if (thing instanceof Block)
            {
                ((Block)thing).setUnlocalizedName(dotname);
            }
            else if (thing instanceof Item)
            {
                ((Item)thing).setUnlocalizedName(dotname);
            }
        }
    }

    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, ResourceLocation loc)
    {
        nameHelper(thing, loc, true);
    }

    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, String name, boolean doTransKey)
    {
        assert thing != null;

        assert name != null;

        ResourceLocation loc = GameData.checkPrefix(name);
        nameHelper(thing, loc, doTransKey);
    }

    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, String name)
    {
        nameHelper(thing, name, true);
    }

    public static SoundEvent registerSound(IForgeRegistry<SoundEvent> registry, String name)
    {
        assert name != null;

        ResourceLocation loc = GameData.checkPrefix(name);
        SoundEvent event = new SoundEvent(loc);
        regHelper(registry, event, (ResourceLocation)loc);
        return event;
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Item item)
    {
        assert item != null;

        ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, loc);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Block block)
    {
        assert block != null;

        Item item = Item.getItemFromBlock(block);
        registerRender(block, item);
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Block block, Item item)
    {
        assert block != null;

        assert item != null;

        ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName(), "inventory");
        ModelLoader.setCustomModelResourceLocation(item, 0, loc);
    }

    public static ItemBlock registerItemBlock(IForgeRegistry<Item> registry, Block block)
    {
        assert block != null;

        ItemBlock item = new ItemBlock(block);
        regHelper(registry, item, (ResourceLocation)block.getRegistryName());
        return item;
    }

    public static ItemBlock registerItemBlockOre(IForgeRegistry<Item> registry, Block block, String[] oreNames)
    {
        assert block != null;

        ItemBlock item = registerItemBlock(registry, block);
        String[] var4 = oreNames;
        int var5 = oreNames.length;

        for(int var6 = 0; var6 < var5; ++var6)
        {
            String oreName = var4[var6];
            OreDictionary.registerOre(oreName, item);
            OreDictionary.registerOre(oreName, block);
        }

        return item;
    }

    public static void registerTileEntity(Class<? extends TileEntity> tileEntityClass, String key)
    {
        assert key != null;

        ResourceLocation loc = GameData.checkPrefix(key);

        assert loc != null;

        GameRegistry.registerTileEntity(tileEntityClass, loc);
    }
}