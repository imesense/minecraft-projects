package org.imesense.dynamicspawncontrol.technical.customlibrary;

import net.minecraft.client.Minecraft;
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
import org.imesense.dynamicspawncontrol.ProjectStructure;
import org.imesense.dynamicspawncontrol.gameplay.throwingobjects.DSCThrowItemWeb;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.gameplay.items.DSCWeb;
import org.imesense.dynamicspawncontrol.technical.customlibrary.registrationhelpers.RegistrationHelpers;

/**
 *
 */
@GameRegistry.ObjectHolder(ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
public final class ObjectHandler
{
    /**
     *
     */
    private static int entityID = 1;

    /**
     *
     */
    public static Item webbing = null;

    /**
     *
     */
    public static SoundEvent WEBBING_SHOOT;

    /**
     *
     */
    public static SoundEvent WEBBING_STICK;

    /**
     *
     */
    public static SoundEvent WEBBING_NONSTICK;

    /**
     *
     */
    @Mod.EventBusSubscriber(modid = ProjectStructure.STRUCT_INFO_MOD.MOD_ID)
    public static class RegistrationHandler extends RegistrationHelpers
    {
        /**
         *
         */
        public RegistrationHandler()
        {
            CodeGenericUtils.printInitClassToLog(this.getClass());
        }

        /**
         *
         * @param event
         */
        @SubscribeEvent
        public static void registerItems(Register<Item> event)
        {
            IForgeRegistry<Item> registry = event.getRegistry();
            ObjectHandler.webbing = regHelper(registry, new DSCWeb());
        }

        /**
         *
         * @param event
         */
        @SubscribeEvent
        @SideOnly(Side.CLIENT)
        public static void registerRenders(ModelRegistryEvent event)
        {
            registerRender(ObjectHandler.webbing);

            RenderingRegistry.registerEntityRenderingHandler(DSCThrowItemWeb.class, (manager) ->
                    new RenderSnowball<>(manager, ObjectHandler.webbing, Minecraft.getMinecraft().getRenderItem()));
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
                    EntityEntryBuilder.create().entity(DSCThrowItemWeb.class).id(
                            new ResourceLocation("dynamicspawncontrol", "webbing"),
                                ObjectHandler.entityID++).name("webbing").tracker(64, 10, true).build();

            registry.register(entry);
        }

        /**
         *
         * @param event
         */
        @SubscribeEvent
        public static void soundRegistration(Register<SoundEvent> event)
        {
            ObjectHandler.WEBBING_SHOOT = SoundEvents.ENTITY_SNOWBALL_THROW;
            ObjectHandler.WEBBING_STICK = SoundEvents.BLOCK_SNOW_HIT;
            ObjectHandler.WEBBING_NONSTICK = SoundEvents.BLOCK_SNOW_BREAK;
        }
    }
}
