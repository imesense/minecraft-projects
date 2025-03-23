package org.imesense.dynamicspawncontrol.core.plugin.mod.bloodmoon_mc1_12_2_1_5_3;

import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

public final class ClassTransformer implements IClassTransformer
{
    public byte[] transform(String name, String transformedName, byte... basicClass)
    {
        if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer"))
        {
            return this.patchEntityRendererClass(basicClass);
        }
        else
        {
            return transformedName.equals("net.minecraft.world.World") ? this.patchWorld(basicClass) : basicClass;
        }
    }

    private byte[] patchWorld(byte... basicClass)
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);

        MethodNode getSkyColor = null;
        MethodNode getMoonPhase = null;

        for (MethodNode mn : classNode.methods)
        {
            if (mn.name.equals("getSkyColor"))
            {
                getSkyColor = mn;
            }
            else if (mn.name.equals("getMoonPhase"))
            {
                getMoonPhase = mn;
            }
        }

        InsnList toInsert = null;
        AbstractInsnNode ain = null;

        if (getSkyColor != null)
        {
            /*
            for (int i = 0; i < getSkyColor.instructions.size(); ++i) {
                ain = getSkyColor.instructions.get(i);
                if (ain.getOpcode() == 176) { // Opcodes.ARETURN
                    toInsert = new InsnList();
                    toInsert.add(new FieldInsnNode(178, "lumien/bloodmoon/client/ClientBloodmoonHandler", "INSTANCE", "Llumien/bloodmoon/client/ClientBloodmoonHandler;"));
                    toInsert.add(new InsnNode(95)); // Swap
                    toInsert.add(new MethodInsnNode(182, "lumien/bloodmoon/client/ClientBloodmoonHandler", "skyColorHook", "(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", false));
                    getSkyColor.instructions.insertBefore(ain, toInsert);
                }
            }
             */
        }

        if (getMoonPhase != null)
        {
/*
            for (int i = 0; i < getMoonPhase.instructions.size(); ++i) {
                ain = getMoonPhase.instructions.get(i);
                if (ain.getOpcode() == 172) { // Opcodes.IRETURN
                    toInsert = new InsnList();
                    toInsert.add(new FieldInsnNode(178, "lumien/bloodmoon/client/ClientBloodmoonHandler", "INSTANCE", "Llumien/bloodmoon/client/ClientBloodmoonHandler;"));
                    toInsert.add(new MethodInsnNode(182, "lumien/bloodmoon/client/ClientBloodmoonHandler", "moonColorHook", "()V", false));
                    getMoonPhase.instructions.insertBefore(ain, toInsert);
                }
            }
 */
        }

        ClassWriter classWriter = new ClassWriter(3);
        classNode.accept(classWriter);

        return classWriter.toByteArray();
    }

    /**
     * 
     * @param basicClass
     * @return
     */
    private byte[] patchEntityRendererClass(byte... basicClass)
    {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);

        classReader.accept(classNode, 0);

        MethodNode updateLightmap = null;

        for (MethodNode mn : classNode.methods)
        {
            if (mn.name.equals("updateLightmap"))
            {
                updateLightmap = mn;
            }
        }

        if (updateLightmap != null)
        {
            /*
            boolean insertedHook = false;

            for (int i = 0; i < updateLightmap.instructions.size(); ++i) {
                AbstractInsnNode an = updateLightmap.instructions.get(i);
                if (an instanceof VarInsnNode && !insertedHook) {
                    VarInsnNode iin = (VarInsnNode) an;
                    if (iin.getOpcode() == 54 && iin.var == 23) {
                        InsnList toInsert = new InsnList();
                        toInsert.add(new FieldInsnNode(178, "lumien/bloodmoon/client/ClientBloodmoonHandler", "INSTANCE", "Llumien/bloodmoon/client/ClientBloodmoonHandler;"));
                        toInsert.add(new VarInsnNode(21, 5)); // Load float from local variable 5
                        toInsert.add(new VarInsnNode(21, 21)); // Load int from local variable 21
                        toInsert.add(new MethodInsnNode(182, "lumien/bloodmoon/client/ClientBloodmoonHandler", "manipulateRed", "(II)I", false));
                        toInsert.add(new VarInsnNode(54, 21)); // Store result in local variable 21
                        toInsert.add(new FieldInsnNode(178, "lumien/bloodmoon/client/ClientBloodmoonHandler", "INSTANCE", "Llumien/bloodmoon/client/ClientBloodmoonHandler;"));
                        toInsert.add(new VarInsnNode(21, 5));
                        toInsert.add(new VarInsnNode(21, 22));
                        toInsert.add(new MethodInsnNode(182, "lumien/bloodmoon/client/ClientBloodmoonHandler", "manipulateGreen", "(II)I", false));
                        toInsert.add(new VarInsnNode(54, 22));
                        toInsert.add(new FieldInsnNode(178, "lumien/bloodmoon/client/ClientBloodmoonHandler", "INSTANCE", "Llumien/bloodmoon/client/ClientBloodmoonHandler;"));
                        toInsert.add(new VarInsnNode(21, 5));
                        toInsert.add(new VarInsnNode(21, 23));
                        toInsert.add(new MethodInsnNode(182, "lumien/bloodmoon/client/ClientBloodmoonHandler", "manipulateBlue", "(II)I", false));
                        toInsert.add(new VarInsnNode(54, 23));
                        updateLightmap.instructions.insert(iin, toInsert);
                        insertedHook = true;
                    }
                }
            }
             */
        }

        ClassWriter classWriter = new ClassWriter(1);
        classNode.accept(classWriter);

        return classWriter.toByteArray();
    }
}

