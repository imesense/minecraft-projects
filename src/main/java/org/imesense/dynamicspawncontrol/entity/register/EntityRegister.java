package org.imesense.dynamicspawncontrol.entity.register;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.entity.feralzombie.EntityFeralZombie;
import org.imesense.dynamicspawncontrol.entity.render.RenderFeralZombie;

import java.util.function.Supplier;

/**
 *
 */
@InitLog
public final class EntityRegister
{
    /**
     *
     */
    private final Object modInstance;

    /**
     *
     */
    private static volatile EntityRegister _INSTANCE;

    /**
     *
     */
    private static Supplier<EntityRegister> instanceSupplier = null;

    /**
     *
     * @param supplier
     */
    public static void init(Supplier<EntityRegister> supplier)
    {
        instanceSupplier = supplier;
    }

    /**
     *
     * @return
     */
    public static EntityRegister getInstance()
    {
        if (instanceSupplier == null)
        {
            throw new IllegalStateException("EntityRegister not initialized. Call EntityRegister.init() first.");
        }

        return CodeGeneric.getInstance(EntityRegister.class, instanceSupplier);
    }

    /**
     *
     * @param modInstance
     */
    private EntityRegister(Object modInstance)
    {
        this.modInstance = modInstance;

        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    /**
     *
     * @param modInstance
     * @return
     */
    public static EntityRegister create(Object modInstance)
    {
        return new EntityRegister(modInstance);
    }

    /**
     *
     */
    public void preInitStartGame()
    {
        registerEntity("feral_zombie", EntityFeralZombie.class, 120, 50, 0x00FF00, 0x000000);
        RenderingRegistry.registerEntityRenderingHandler(EntityFeralZombie.class, RenderFeralZombie::new);
    }

    /**
     *
     * @param entityName
     * @param entityClass
     * @param id
     * @param range
     * @param primaryColor
     * @param secondaryColor
     */
    private void registerEntity(String entityName, Class<? extends Entity> entityClass, int id, int range, int primaryColor, int secondaryColor)
    {
        EntityRegistry.registerModEntity(
                new ResourceLocation("dynamicspawncontrol", entityName),
                entityClass,
                entityName,
                id,
                modInstance,
                range,
                1,
                true,
                primaryColor,
                secondaryColor
        );
    }
}