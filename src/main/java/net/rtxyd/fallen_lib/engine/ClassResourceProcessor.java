package net.rtxyd.fallen_lib.engine;

import net.rtxyd.fallen_lib.type.engine.Resource;
import net.rtxyd.fallen_lib.type.engine.ResourceProcessor;
import org.objectweb.asm.ClassReader;

import java.io.InputStream;

class ClassResourceProcessor implements ResourceProcessor {

    @Override
    public boolean supports(Resource r) {
        return r.path().endsWith(".class");
    }

    @Override
    public void process(Resource r, ScanContext ctx) {
        try (InputStream is = r.open()) {
            ClassView cv = new ClassView(ClassView.SKIP_ANNOTATION);
            new ClassReader(is).accept(cv, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
            ctx.classIndex.add(r.path().replace(".class", ""), new ClassInfo(cv.superName, cv.interfaces, cv.nestMembers));
        } catch (Exception e) {
            ResourceScanEngine.LOGGER.warn("Failed processing class file: {}", r.path());
        }
    }
}