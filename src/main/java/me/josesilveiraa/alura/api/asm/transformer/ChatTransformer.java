package me.josesilveiraa.alura.api.asm.transformer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;
import me.josesilveiraa.alura.api.event.ClientPreChatEvent;
import net.minecraft.launchwrapper.IClassTransformer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;

public class ChatTransformer implements IClassTransformer {
    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if(!transformedName.equals("net.minecraft.client.entity.EntityPlayerSP")) return basicClass;

        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();

        reader.accept(node, 0);

        for(MethodNode method : node.methods) {
            FMLDeobfuscatingRemapper remapper = FMLDeobfuscatingRemapper.INSTANCE;

            String mappedName = remapper.mapMethodName(node.name, method.name, method.desc);
            if(!mappedName.equals("sendChatMessage") && !mappedName.equals("func_71165_d")) continue;

            System.out.println("[Transformer] Found method " + method.name);

            String eventName = Type.getInternalName(ClientPreChatEvent.class);
            String eventBusName = Type.getInternalName(EventBus.class);

            InsnList inst = new InsnList();

            inst.add(new FieldInsnNode(Opcodes.GETSTATIC, Type.getInternalName(MinecraftForge.class), "EVENT_BUS", Type.getDescriptor(EventBus.class)));

            inst.add(new TypeInsnNode(Opcodes.NEW, eventName));
            inst.add(new InsnNode(Opcodes.DUP));
            inst.add(new VarInsnNode(Opcodes.ALOAD, 1));

            inst.add(new MethodInsnNode(Opcodes.INVOKESPECIAL, eventName, "<init>", Type.getMethodDescriptor(Type.VOID_TYPE, Type.getType(String.class)), false));
            inst.add(new MethodInsnNode(Opcodes.INVOKEVIRTUAL, eventBusName, "post", Type.getMethodDescriptor(Type.BOOLEAN_TYPE, Type.getType(Event.class)), false));

            LabelNode label = new LabelNode();
            inst.add(new JumpInsnNode(Opcodes.IFEQ, label));
            inst.add(new InsnNode(Opcodes.RETURN));
            inst.add(label);

            method.instructions.insertBefore(method.instructions.getFirst(), inst);

            break;
        }
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        node.accept(writer);

        return writer.toByteArray();
    }
}
