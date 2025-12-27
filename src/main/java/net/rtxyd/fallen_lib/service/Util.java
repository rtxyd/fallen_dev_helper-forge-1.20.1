package net.rtxyd.fallen_lib.service;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class Util {
    public static ClassNode cloneClassNode(ClassNode original) {
        ClassWriter cw = new ClassWriter(0);
        original.accept(cw);
        byte[] bytes = cw.toByteArray();

        ClassReader cr = new ClassReader(bytes);
        ClassNode copy = new ClassNode();
        cr.accept(copy, 0);
        return copy;
    }
}
