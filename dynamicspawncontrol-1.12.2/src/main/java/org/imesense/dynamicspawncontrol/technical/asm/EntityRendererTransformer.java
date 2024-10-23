package org.imesense.dynamicspawncontrol.technical.asm;

import org.objectweb.asm.Type;
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
public final class EntityRendererTransformer implements IClassTransformer
{
    /**
     *
     */
    private static final String ENTITY_RENDERER =
            "net.minecraft.client.renderer.EntityRenderer";

    /**
     *
     */
    private static final String UPDATE_LIGHTMAP_OWNER =
            ENTITY_RENDERER.replace(".", "/");

    /**
     *
     */
    private static final String UPDATE_LIGHTMAP_DESC =
            "(F)V";

    /**
     *
     */
    private static final String UPDATE_LIGHTMAP_NAME =
            FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(
                    UPDATE_LIGHTMAP_OWNER, "func_78472_g", UPDATE_LIGHTMAP_DESC);

    /**
     *
     */
    private static final String DYNAMIC_TEXTURE =
            "net.minecraft.client.renderer.texture.DynamicTexture";

    /**
     *
     */
    private static final String UPDATE_DYNAMIC_TEXTURE_OWNER =
            DYNAMIC_TEXTURE.replace(".", "/");

    /**
     *
     */
    private static final String UPDATE_DYNAMIC_TEXTURE_DESC =
            "()V";

    /**
     *
     */
    private static final String UPDATE_DYNAMIC_TEXTURE_NAME =
            FMLDeobfuscatingRemapper.INSTANCE.mapMethodName(
                    UPDATE_DYNAMIC_TEXTURE_OWNER, "func_110564_a", UPDATE_DYNAMIC_TEXTURE_DESC);

    /**
     *
     */
    private static final String ENTITY_RENDERER_HOOKS =
            EntityRendererHook.class.getName().replace('.', '/');

    /**
     *
     */
    private static final String ON_UPDATE_LIGHTMAP_DESC =
            "(Lnet/minecraft/client/renderer/EntityRenderer;F)V";

    /**
     *
     */
    private static final String ON_UPDATE_LIGHTMAP_NAME =
            "onUpdateLightmap";

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
        if (!ENTITY_RENDERER.equals(transformedName))
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
                            int access,
                            String name,
                            String desc,
                            String signature,
                            String[] exceptions)
                    {
                        MethodVisitor mv =
                                super.visitMethod(access, name, desc, signature, exceptions);

                        if (!UPDATE_LIGHTMAP_NAME.equals(name)
                                || !UPDATE_LIGHTMAP_DESC.equals(desc))
                        {
                            return mv;
                        }

                        return new GeneratorAdapter(api, mv, access, name, desc)
                        {
                            @Override
                            public void visitMethodInsn(
                                    int opcode,
                                    String owner,
                                    String name,
                                    String desc,
                                    boolean itf)
                            {
                                if (!itf
                                        && Opcodes.INVOKEVIRTUAL == opcode
                                        && UPDATE_DYNAMIC_TEXTURE_OWNER.equals(owner)
                                        && UPDATE_DYNAMIC_TEXTURE_NAME.equals(name)
                                        && UPDATE_DYNAMIC_TEXTURE_DESC.equals(desc))
                                {
                                    loadThis();
                                    loadArgs();
                                    invokeStatic(
                                            Type.getObjectType(ENTITY_RENDERER_HOOKS),
                                            new Method(
                                                    ON_UPDATE_LIGHTMAP_NAME,
                                                    ON_UPDATE_LIGHTMAP_DESC));
                                }
                                super.visitMethodInsn(opcode, owner, name, desc, itf);
                            }
                        };
                    }
                },
                ClassReader.SKIP_FRAMES);

        return classWriter.toByteArray();
    }
}
