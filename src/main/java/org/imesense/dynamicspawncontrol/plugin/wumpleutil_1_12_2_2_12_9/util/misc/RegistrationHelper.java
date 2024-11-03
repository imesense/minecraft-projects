package org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.misc;

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
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtil;

import java.util.Objects;

/**
 *
 */
public class RegistrationHelper
{
    /**
     *
     */
    public RegistrationHelper()
    {
        CodeGenericUtil.printInitClassToLog(this.getClass());
    }

    /**
     *
     * @param item
     */
    @SideOnly(Side.CLIENT)
    public static void registerRender(Item item)
    {
        assert item != null;

        ModelResourceLocation modelResourceLocation =
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");

        ModelLoader.setCustomModelResourceLocation(item, 0, modelResourceLocation);
    }

    /**
     *
     * @param iForgeRegistry
     * @param thing
     * @return
     * @param <T>
     */
    public static <T extends IForgeRegistryEntry<T>> T regHelper(IForgeRegistry<T> iForgeRegistry, T thing)
    {
        assert thing != null;

        iForgeRegistry.register(thing);
        return thing;
    }

    /**
     *
     * @param thing
     * @param resourceLocation
     * @param doTransKey
     * @param <T>
     */
    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, ResourceLocation resourceLocation, boolean doTransKey)
    {
        assert thing != null;

        assert resourceLocation != null;

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

    /**
     *
     * @param thing
     * @param name
     * @param doTransKey
     * @param <T>
     */
    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, String name, boolean doTransKey)
    {
        assert thing != null;
        assert name != null;

        @Deprecated
        ResourceLocation loc = GameData.checkPrefix(name);

        nameHelper(thing, loc, doTransKey);
    }

    /**
     *
     * @param thing
     * @param name
     * @param <T>
     */
    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, String name)
    {
        nameHelper(thing, name, true);
    }
}