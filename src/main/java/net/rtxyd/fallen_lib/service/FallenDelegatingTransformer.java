package net.rtxyd.fallen_lib.service;

import cpw.mods.modlauncher.api.ITransformer;
import cpw.mods.modlauncher.api.ITransformerVotingContext;
import cpw.mods.modlauncher.api.TransformerVoteResult;
import net.rtxyd.fallen_lib.api.IFallenPatch;
import net.rtxyd.fallen_lib.type.service.IFallenPatchContext;
import net.rtxyd.fallen_lib.type.service.IFallenPatchCtorContext;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.tree.ClassNode;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class FallenDelegatingTransformer implements ITransformer<ClassNode> {
    private final FallenPatchRegistry registry;
    private final IFallenPatchCtorContext ctorContext;
    private final IFallenPatchContext patchContext;
    private BytecodeClassLoader classLoader;

    FallenDelegatingTransformer(FallenPatchRegistry registry, IFallenPatchCtorContext ctorContext) {
        this.registry = registry;
        this.ctorContext = ctorContext;
        this.patchContext = new IFallenPatchContext() {};
    }

    @Override
    public @NotNull ClassNode transform(ClassNode cn, ITransformerVotingContext context) {
        if (classLoader == null) {
            // must ensure the classloader's parent is the same with other transformers.
            // so it's controllable to handle not allowed class loading.
            classLoader = new BytecodeClassLoader(Thread.currentThread().getContextClassLoader());
        }
        for (FallenPatchEntry e : registry.match(cn.name)) {
            Optional<byte[]> cbOpt = registry.getClassBytes(e.getClassName());
            IFallenPatch t = e.getOrCreateInstance(classLoader, cbOpt.orElse(null), ctorContext);
            if (t == null) continue;
            // must make sure there is a fallback if transformer failed.
            // but we can't actually restrain its behavior, this is a safeguard.
            ClassNode fallback = Util.cloneClassNode(cn);
            try {
                t.apply(cn, patchContext);
            } catch (Throwable ex) {
                FallenBootstrap.LOGGER.error("Transformer {} failed on {}",
                        e.getClassName(), cn.name, ex);
                e.disable();
                cn = fallback;
            }
        }
        return cn;
    }

    @Override
    public @NotNull TransformerVoteResult castVote(ITransformerVotingContext context) {
        return TransformerVoteResult.YES;
    }

    @Override
    public @NotNull Set<Target> targets() {
        return registry.targets().stream().map(Target::targetClass).collect(Collectors.toSet());
    }
}
