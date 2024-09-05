package net.frozenblock.lib.sound.impl.block_sound_group;

import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public interface SimpleResourceReloadListener<T> extends ResourceManagerReloadListener {
    default CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier helper, ResourceManager manager, ProfilerFiller loadProfiler, ProfilerFiller applyProfiler, Executor loadExecutor, Executor applyExecutor) {
        CompletableFuture<T> var10000 = this.load(manager, loadProfiler, loadExecutor);
        Objects.requireNonNull(helper);
        return var10000.thenCompose(helper::wait).thenCompose((o) -> this.apply(o, manager, applyProfiler, applyExecutor));
    }

    CompletableFuture<T> load(ResourceManager var1, ProfilerFiller var2, Executor var3);

    CompletableFuture<Void> apply(T var1, ResourceManager var2, ProfilerFiller var3, Executor var4);
}
