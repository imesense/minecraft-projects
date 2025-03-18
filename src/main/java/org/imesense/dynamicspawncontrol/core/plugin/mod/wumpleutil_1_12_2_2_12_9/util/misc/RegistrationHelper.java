package org.imesense.dynamicspawncontrol.core.plugin.mod.wumpleutil_1_12_2_2_12_9.util.misc;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

import java.util.Objects;

public class RegistrationHelper
{
    public RegistrationHelper()
    {
        CodeGeneric.printInitClassToLog(this.getClass());
    }

    @SideOnly(Side.CLIENT)
    public static void registerRender(Item item)
    {
        ModelResourceLocation modelResourceLocation =
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");

        ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);
    }

    public static <T extends IForgeRegistryEntry<T>> T regHelper(IForgeRegistry<T> iForgeRegistry, T thing)
    {
        iForgeRegistry.register(thing);

        return thing;
    }

    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, ResourceLocation resourceLocation, boolean doTransKey)
    {
        thing.setRegistryName(resourceLocation);

        if (doTransKey)
        {
            String dotName = resourceLocation.getResourceDomain() + "." + resourceLocation.getResourcePath();

            if (thing instanceof Block)
            {
                ((Block)thing).setUnlocalizedName(dotName);
            }
            else if (thing instanceof Item)
            {
                ((Item)thing).setUnlocalizedName(dotName);
            }
        }
    }

    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, String name, boolean doTransKey)
    {
        @Deprecated
        ResourceLocation resourceLocation = GameData.checkPrefix(name);

        nameHelper(thing, resourceLocation, doTransKey);
    }

    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, String name)
    {
        nameHelper(thing, name, true);
    }
}