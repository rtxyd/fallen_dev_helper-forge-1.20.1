package net.rtxyd.fallen_lib.service;

import org.objectweb.asm.tree.ClassNode;

interface IPatchSecurityHelper {
    boolean isPatchClassSafe(ClassNode cn);
}
