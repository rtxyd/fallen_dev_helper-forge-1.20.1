package net.rtxyd.fallen_lib.service;

import cpw.mods.modlauncher.api.IEnvironment;
import cpw.mods.modlauncher.api.ITransformationService;
import cpw.mods.modlauncher.api.ITransformer;
import net.rtxyd.fallen_lib.type.service.IFallenPatchCtorContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class FallenTransformerService implements ITransformationService {
    boolean initialized;
    private final FallenPatchRegistry registry = new FallenPatchRegistry();
    protected static final Logger LOGGER = LoggerFactory.getLogger("fallen");
    @Override
    public String name() {
        return "fallen";
    }

    @Override
    public void initialize(IEnvironment environment) {
        if (initialized) {
            throw new IllegalStateException("Already initialized");
        }
        initialized = true;

        InitializeHelper helper = new InitializeHelper(environment);
        helper.collectScanners();

        try {
            helper.scanResources();
        } catch (IOException e) {
            LOGGER.error("Unexpected: Error on ResourceScanEngine!");
            return;
        }

        helper.registerPatches(registry);
    }

    @Override
    public void onLoad(IEnvironment env, Set<String> otherServices) {
    }

    @Override
    public @NotNull List<ITransformer> transformers() {
        LOGGER.info("Creating delegating transformer.");
        return List.of(new FallenDelegatingTransformer(registry, new IFallenPatchCtorContext() {}));
    }
}
