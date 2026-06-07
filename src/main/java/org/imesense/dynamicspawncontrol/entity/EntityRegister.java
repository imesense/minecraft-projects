package org.imesense.dynamicspawncontrol.entity;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;
import org.imesense.dynamicspawncontrol.entity.explosionzombie.ExplosionZombieEntity;
import org.imesense.dynamicspawncontrol.entity.feralzombie.FeralZombieEntity;
import org.imesense.dynamicspawncontrol.entity.explosionzombie.ExplosionZombieRender;
import org.imesense.dynamicspawncontrol.entity.feralzombie.FeralZombieRender;

import java.util.function.Supplier;

@InitLog
public final class EntityRegister
{
    private final Object modInstance;

    private static volatile EntityRegister _INSTANCE;

    private static Supplier<EntityRegister> instanceSupplier = null;

    public static void init(Supplier<EntityRegister> entityRegisterSupplier)
    {
        instanceSupplier = entityRegisterSupplier;
    }

    public static EntityRegister getInstance()
    {
        if (instanceSupplier == null)
        {
            throw new IllegalStateException("EntityRegister not initialized. Call EntityRegister.init() first.");
        }

        return CodeGeneric.getInstance(EntityRegister.class, instanceSupplier);
    }

    private EntityRegister(Object object)
    {
        this.modInstance = object;

        if (this.getClass().isAnnotationPresent(InitLog.class))
        {
            CodeGeneric.logInitialization(this.getClass());
        }
    }

    public static EntityRegister create(Object modInstance)
    {
        return new EntityRegister(modInstance);
    }

    public void preInitStartGame()
    {
        registerEntity("feral_zombie", FeralZombieEntity.class, 120, 50, 0x00FF00, 0x000000);
        registerEntity("explosion_zombie", ExplosionZombieEntity.class, 121, 50, 0x00FF01, 0x000001);

        RenderingRegistry.registerEntityRenderingHandler(FeralZombieEntity.class, FeralZombieRender::new);
        RenderingRegistry.registerEntityRenderingHandler(ExplosionZombieEntity.class, ExplosionZombieRender::new);
    }

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
