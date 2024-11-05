package org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4;

import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.field.UniqueField;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing.ItemWebbing;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.webbing.EntityWebbing;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.plugin.wumpleutil_1_12_2_2_12_9.util.misc.RegistrationHelper;

/**
 *
 */
@GameRegistry.ObjectHolder(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
public final class ObjectHandlerClient
{
    /**
     *
     */
    private static int entityID = 1;

    /**
     *
     */
    public static Item Webbing = null;

    /**
     *
     */
    public static SoundEvent WebbingShoot;

    /**
     *
     */
    public static SoundEvent WebbingStick;

    /**
     *
     */
    public static SoundEvent WebbingNonStick;

    /**
     *
     */
    @Mod.EventBusSubscriber(modid = DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID)
    public static class RegistrationHandler extends RegistrationHelper
    {
        /**
         *
         */
        public RegistrationHandler()
        {
            CodeGeneric.printInitClassToLog(this.getClass());
        }

        /**
         *
         * @param event
         */
        @SubscribeEvent
        public static void registerItems(Register<Item> event)
        {
            IForgeRegistry<Item> registry = event.getRegistry();
            ObjectHandlerClient.Webbing = regHelper(registry, new ItemWebbing());
        }

        /**
         *
         * @param event
         */
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void registerRenders(ModelRegistryEvent event)
        {
            registerRender(ObjectHandlerClient.Webbing);

            RenderingRegistry.registerEntityRenderingHandler(EntityWebbing.class, (manager) ->
                    new RenderSnowball<>(manager, ObjectHandlerClient.Webbing, UniqueField.CLIENT.getRenderItem()));
        }

        /**
         *
         * @param event
         */
        @SubscribeEvent
        public static void entityRegistration(Register<EntityEntry> event)
        {
            registerEntity(event.getRegistry());
        }

        /**
         *
         * @param registry
         */
        protected static void registerEntity(IForgeRegistry<EntityEntry> registry)
        {
            EntityEntry entry =
                    EntityEntryBuilder.create().entity(EntityWebbing.class).id(
                            new ResourceLocation("dynamicspawncontrol", "webbing"),
                                ObjectHandlerClient.entityID++).name("webbing").tracker(64, 10, true).build();

            registry.register(entry);
        }

        /**
         *
         * @param event
         */
        @SubscribeEvent
        public static void soundRegistration(Register<SoundEvent> event)
        {
            ObjectHandlerClient.WebbingShoot = SoundEvents.ENTITY_SNOWBALL_THROW;
            ObjectHandlerClient.WebbingStick = SoundEvents.BLOCK_SNOW_HIT;
            ObjectHandlerClient.WebbingNonStick = SoundEvents.BLOCK_SNOW_BREAK;
        }
    }
}
