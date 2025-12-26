package net.rtxyd.fallen_lib.service;

import org.objectweb.asm.tree.AnnotationNode;

interface AnnotationDataFactory {
    AnnotationData create(AnnotationNode node);

}
