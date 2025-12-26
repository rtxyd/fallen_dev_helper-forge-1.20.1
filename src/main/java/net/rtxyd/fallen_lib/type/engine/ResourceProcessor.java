package net.rtxyd.fallen_lib.type.engine;

import net.rtxyd.fallen_lib.engine.ScanContext;

import java.io.IOException;

public interface ResourceProcessor {
    boolean supports(Resource r);
    void process(Resource r, ScanContext ctx) throws IOException;
}