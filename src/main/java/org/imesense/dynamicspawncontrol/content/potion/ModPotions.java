package org.imesense.dynamicspawncontrol.content.potion;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.logfile.LogManager;

@Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public class ModPotions
{
    public static Potion SATIETY;

    public static Potion getSatiety()
    {
        return SATIETY;
    }

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event)
    {
        SATIETY = new SatietyPotion();
        event.getRegistry().register(SATIETY);

        LogManager.info("[DynamicSpawnControl] Registered satiety potion: " + SATIETY.getRegistryName());
    }
}
