package org.imesense.dynamicspawncontrol.technical.asmclasstransformer;

import java.util.Iterator;
import net.minecraft.launchwrapper.IClassTransformer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

public final class ClassTransformerBloodMoon implements IClassTransformer {
    Logger logger = LogManager.getLogger("Bloodmoon");

    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (transformedName.equals("net.minecraft.client.renderer.EntityRenderer")) {
            return this.patchEntityRendererClass(basicClass);
        } else {
            return transformedName.equals("net.minecraft.world.World") ? this.patchWorld(basicClass) : basicClass;
        }
    }

    private byte[] patchWorld(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        this.logger.log(Level.DEBUG, "Found World Class: " + classNode.name);
        MethodNode getSkyColor = null;
        MethodNode getMoonPhase = null;

        for (MethodNode mn : classNode.methods) {
            if (mn.name.equals("getSkyColor")) {
                getSkyColor = mn;
            } else if (mn.name.equals("getMoonPhase")) {
                getMoonPhase = mn;
            }
        }

        InsnList toInsert;
        AbstractInsnNode ain;

        if (getSkyColor != null) {
            this.logger.log(Level.DEBUG, " - Found getSkyColor");

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

        if (getMoonPhase != null) {
            this.logger.log(Level.DEBUG, " - Found getMoonPhase");
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

        ClassWriter writer = new ClassWriter(3);
        classNode.accept(writer);
        return writer.toByteArray();
    }

    private byte[] patchEntityRendererClass(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        this.logger.log(Level.DEBUG, "Found EntityRenderer Class: " + classNode.name);

        MethodNode updateLightmap = null;
        for (MethodNode mn : classNode.methods) {
            if (mn.name.equals("updateLightmap")) {
                updateLightmap = mn;
            }
        }

        if (updateLightmap != null) {
            this.logger.log(Level.DEBUG, " - Found updateLightmap");

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

        ClassWriter writer = new ClassWriter(1);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}

