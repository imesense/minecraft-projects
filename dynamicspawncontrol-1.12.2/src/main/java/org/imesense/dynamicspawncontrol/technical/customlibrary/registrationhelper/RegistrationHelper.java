package org.imesense.dynamicspawncontrol.technical.customlibrary.registrationhelper;

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

        ModelResourceLocation loc =
                new ModelResourceLocation(Objects.requireNonNull(item.getRegistryName()), "inventory");

        ModelLoader.setCustomModelResourceLocation(item, 0, loc);
    }

    /**
     *
     * @param registry
     * @param thing
     * @return
     * @param <T>
     */
    public static <T extends IForgeRegistryEntry<T>> T regHelper(IForgeRegistry<T> registry, T thing)
    {
        assert thing != null;

        registry.register(thing);
        return thing;
    }

    /**
     *
     * @param thing
     * @param loc
     * @param doTransKey
     * @param <T>
     */
    public static <T extends IForgeRegistryEntry<T>> void nameHelper(T thing, ResourceLocation loc, boolean doTransKey)
    {
        assert thing != null;

        assert loc != null;

        thing.setRegistryName(loc);

        if (doTransKey)
        {
            String dotName = loc.getResourceDomain() + "." + loc.getResourcePath();

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