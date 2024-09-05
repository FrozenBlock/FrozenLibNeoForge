/*
 * Copyright 2024 The Quilt Project
 * Copyright 2024 FrozenBlock
 * Modified to work on Fabric
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.quiltmc.qsl.frozenblock.resource.loader.api;

import net.frozenblock.lib.entrypoint.api.CommonEventEntrypoint;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.neoforged.bus.api.Event;
import org.jetbrains.annotations.Nullable;


public class ResourceLoaderEvent extends Event {

    @Nullable public final MinecraftServer server;
    @Nullable public final ResourceManager resourceManager;

    private ResourceLoaderEvent(final @Nullable MinecraftServer server, final @Nullable ResourceManager resourceManager) {
        this.server = server;
        this.resourceManager = resourceManager;
    }

    /**
     * An event indicating the start of the reloading of data packs on a Minecraft server.
     * <p>
     * This event should not be used to load resources.*//*, use {@link ResourceLoader#registerReloader(IdentifiableResourceReloader)} instead.
     */
    public static class StartDataPackReload extends ResourceLoaderEvent {

        public StartDataPackReload(@Nullable MinecraftServer server, @Nullable ResourceManager resourceManager) {
            super(server, resourceManager);
        }
    }

    /**
     * An event indicating the end of the reloading of data packs on a Minecraft server.
     * <p>
     * This event should not be used to load resources.*//*, use {@link ResourceLoader#registerReloader(IdentifiableResourceReloader)} instead.
     */
    public static class EndDataPackReload extends ResourceLoaderEvent {
        @Nullable public final Throwable throwable;
        public EndDataPackReload(@Nullable final MinecraftServer server, @Nullable final ResourceManager resourceManager, @Nullable final Throwable throwable) {
            super(server, resourceManager);
            this.throwable = throwable;
        }
    }
}
