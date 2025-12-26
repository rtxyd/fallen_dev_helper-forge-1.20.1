package net.rtxyd.fallen_lib.type.engine;

import java.io.IOException;

@FunctionalInterface
public interface ResourceConsumer {
    void accept(Resource resource) throws IOException;
}