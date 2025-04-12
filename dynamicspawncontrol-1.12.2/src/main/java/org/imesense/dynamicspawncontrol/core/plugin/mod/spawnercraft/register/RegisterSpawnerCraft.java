package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.register;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.handler.ConfigHandler;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.handler.DropsListener;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftBlocks;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftItems;
import org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.init.SpawnerCraftRecipes;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

public final class RegisterSpawnerCraft
{
    private static volatile RegisterSpawnerCraft _INSTANCE;

    public static RegisterSpawnerCraft getInstance()
    {
        return CodeGeneric.getInstance(RegisterSpawnerCraft.class);
    }

    public RegisterSpawnerCraft()
    {

    }

    public void preInit(FMLPreInitializationEvent event)
    {
        SpawnerCraftBlocks.registerBlocks();
        SpawnerCraftItems.registerItems();

        SpawnerCraftBlocks.registerModels();
        SpawnerCraftItems.registerModels();
    }

    public void init(FMLInitializationEvent event)
    {
        SpawnerCraftRecipes.registerRecipes();
        SpawnerCraftItems.registerColors(Minecraft.getMinecraft().getItemColors());
    }
}
