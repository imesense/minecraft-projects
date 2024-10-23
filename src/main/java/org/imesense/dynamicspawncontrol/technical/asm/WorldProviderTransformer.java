package org.imesense.dynamicspawncontrol.technical.asm;

import org.objectweb.asm.Type;
import org.objectweb.asm.Label;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.commons.GeneratorAdapter;

import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;

/**
 *
 */
public final class WorldProviderTransformer implements IClassTransformer
{
    /**
     *
     */
    private static final String WORLD_PROVIDER =
            "net.minecraft.world.WorldProvider";

    /**
     *
     */
    private static final String WORLD_PROVIDER_END =
            "net.minecraft.world.WorldProviderEnd";

    /**
     *
     */
    private static final String WORLD_PROVIDER_HELL =
            "net.minecraft.world.WorldProviderHell";

    /**
     *
     */
    private static final String GET_FOG_COLOR_OWNER =
            WORLD_PROVIDER.replace('.', '/');

    /**
     *
     */
    private static final String GET_FOG_COLOR_DESC =
            "(FF)Lnet/minecraft/util/math/Vec3d;";

    /**
     *
     */
    private static final String GET_FOG_COLOR_NAME =
            FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(
                    GET_FOG_COLOR_OWNER, "func_76562_b", GET_FOG_COLOR_DESC);

    /**
     *
     */
    private static final String WORLD_PROVIDER_HOOKS =
            WorldProviderHook.class.getName().replace('.', '/');

    /**
     *
     */
    private static final String ON_GET_FOG_COLOR_DESC =
            "(Lnet/minecraft/world/WorldProvider;FF)Lnet/minecraft/util/math/Vec3d;";

    /**
     *
     */
    private static final String ON_GET_FOG_COLOR_NAME =
            "onGetFogColor";

    /**
     *
     * @param name
     * @param transformedName
     * @param basicClass
     * @return
     */
    @Override
    public byte[] transform(String name, String transformedName, byte... basicClass)
    {
        if (!WORLD_PROVIDER_END.equals(transformedName)
                && !WORLD_PROVIDER_HELL.equals(transformedName))
        {
            return basicClass;
        }

        ClassReader classReader = new ClassReader(basicClass);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        classReader.accept(
                new ClassVisitor(Opcodes.ASM5, classWriter)
                {
                    @Override
                    public MethodVisitor visitMethod(
                            int _access,
                            String _name,
                            String _desc,
                            String _signature,
                            String[] _exceptions)
                    {
                        MethodVisitor mv =
                                super.visitMethod(_access, _name, _desc, _signature, _exceptions);

                        if (!GET_FOG_COLOR_NAME.equals(_name) || !GET_FOG_COLOR_DESC.equals(_desc))
                        {
                            return mv;
                        }

                        return new GeneratorAdapter(api, mv, _access, _name, _desc)
                        {
                            @Override
                            public void visitCode()
                            {
                                super.visitCode();

                                loadThis();
                                loadArgs();
                                invokeStatic(
                                        Type.getObjectType(WORLD_PROVIDER_HOOKS),
                                        new Method(ON_GET_FOG_COLOR_NAME, ON_GET_FOG_COLOR_DESC));
                                Label label = newLabel();
                                dup();
                                ifNull(label);
                                returnValue();
                                mark(label);
                                pop();
                            }
                        };
                    }
                },
                ClassReader.SKIP_FRAMES);

        return classWriter.toByteArray();
    }
}
