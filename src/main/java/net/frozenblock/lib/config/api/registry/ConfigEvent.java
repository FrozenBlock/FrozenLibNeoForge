package net.frozenblock.lib.config.api.registry;

import net.frozenblock.lib.config.api.instance.Config;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.Event;

public class ConfigEvent extends Event {
    public final Config<?> config;
    public ConfigEvent(final Config<?> config) {
        this.config = config;
    }

    public static class Load extends ConfigEvent {
        public Load(Config<?> config) {
            super(config);
        }

        @OnlyIn(Dist.CLIENT)
        public static class Client extends Load {

            public Client(Config<?> config) {
                super(config);
            }
        }
    }

    public static class Save extends ConfigEvent {

        public Save(Config<?> config) {
            super(config);
        }

        @OnlyIn(Dist.CLIENT)
        public static class Client extends Save {

            public Client(Config<?> config) {
                super(config);
            }
        }
    }
}
