package net.frozenblock.lib.item.api;

import net.frozenblock.lib.FrozenLogUtils;
import net.frozenblock.lib.FrozenSharedConstants;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Consumer;

/**
 * Since forge handles creative mode tabs during an event, we first store all the actions to execute here, and then execute them during the event
 */
@ApiStatus.Experimental
public class FrozenCreativeModeTabs {
    private static boolean frozen = false;
    private static final ArrayList<Consumer<BuildCreativeModeTabContentsEvent>> events = new ArrayList<>();

    public static void build(BuildCreativeModeTabContentsEvent event) {
        frozen = true;
        events.forEach(consumer -> consumer.accept(event));
    }

    public static void register(final Consumer<BuildCreativeModeTabContentsEvent> consumer) {
        if(frozen) FrozenSharedConstants.LOGGER.error("FrozenCreativeModeTabs registered an event after the main executor has been executed");
        events.add(consumer);
    }

    public static void add(ItemLike item, @NotNull ResourceKey<CreativeModeTab>... tabs) {
        if(item != null) {
            item.asItem();
            register(event -> {
                if (contains(tabs, event.getTabKey())) {
                    ItemStack stack = new ItemStack(item);
                    stack.setCount(1);
                    event.accept(stack);
                }
            });
        }
    }

    public static void addBefore(ItemLike comparedItem, ItemLike item, CreativeModeTab.TabVisibility tabVisibility, @NotNull ResourceKey<CreativeModeTab>... tabs) {
        if (comparedItem != null) {
            if (item != null) {
                register(event -> {
                    if (contains(tabs, event.getTabKey())) {
                        ItemStack stack = new ItemStack(item);
                        stack.setCount(1);
                        event.insertBefore(comparedItem.asItem().getDefaultInstance(), stack, tabVisibility);
                    }
                });
            }
        }
    }

    public static void addBefore(ItemLike comparedItem, ItemLike item, String path, CreativeModeTab.TabVisibility tabVisibility, @NotNull ResourceKey<CreativeModeTab>... tabs) {
        if (comparedItem != null) {
            if (item != null) {
                register(event -> {
                    if(contains(tabs, event.getTabKey())) {
                        ItemStack stack = new ItemStack(item);
                        stack.setCount(1);
                        FrozenLogUtils.logError("EMPTY ITEM IN CREATIVE INVENTORY: " + path, stack.isEmpty(), (Throwable) null);
                        event.insertBefore(comparedItem.asItem().getDefaultInstance(), stack, tabVisibility);
                    }
                });
            }
        }
    }

    public static void addAfter(ItemLike comparedItem, ItemLike item, ResourceKey<CreativeModeTab>... tabs) {
        addAfter(comparedItem, item, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS, tabs);
    }

    public static void addAfter(ItemLike comparedItem, ItemLike item, CreativeModeTab.TabVisibility tabVisibility, @NotNull ResourceKey<CreativeModeTab>... tabs) {
        if (comparedItem != null) {
            if (item != null) {
                register(event -> {
                    if(contains(tabs, event.getTabKey())) {
                        ItemStack stack = new ItemStack(item);
                        stack.setCount(1);
                        event.insertAfter(comparedItem.asItem().getDefaultInstance(), stack, tabVisibility);
                    }
                });
            }
        }
    }

    public static void addAfter(ItemLike comparedItem, ItemLike item, String path, CreativeModeTab.TabVisibility tabVisibility, @NotNull ResourceKey<CreativeModeTab>... tabs) {
        if (comparedItem != null) {
            if (item != null) {
                register(event -> {
                    if(contains(tabs, event.getTabKey())) {
                        ItemStack stack = new ItemStack(item);
                        stack.setCount(1);
                        FrozenLogUtils.logError("EMPTY ITEM IN CREATIVE INVENTORY: " + path, stack.isEmpty(), (Throwable) null);
                        event.insertAfter(comparedItem.asItem().getDefaultInstance(), stack, tabVisibility);
                    }
                });
            }
        }
    }

    public static void addInstrument(Item instrument, TagKey<Instrument> tagKey, CreativeModeTab.TabVisibility tabVisibility, @NotNull ResourceKey<CreativeModeTab>... tabs) {
        if (instrument != null) {
            register(event -> {
                if(contains(tabs, event.getTabKey())) {
                    for (Holder<Instrument> instrumentHolder : BuiltInRegistries.INSTRUMENT.getTagOrEmpty(tagKey)) {
                        ItemStack stack = InstrumentItem.create(instrument, instrumentHolder);
                        stack.setCount(1);
                        event.accept(stack, tabVisibility);
                    }
                }
            });
        }
    }

    public static void addInstrumentBefore(ItemLike comparedItem, Item instrument, TagKey<Instrument> tagKey, CreativeModeTab.TabVisibility tabVisibility, @NotNull ResourceKey<CreativeModeTab>... tabs) {
        if (comparedItem != null) {
            if (instrument != null) {
                register(event -> {
                    if (contains(tabs, event.getTabKey())) {
                        for (Holder<Instrument> holder : BuiltInRegistries.INSTRUMENT.getTagOrEmpty(tagKey)) {
                            ItemStack stack = InstrumentItem.create(instrument, holder);
                            stack.setCount(1);
                            event.insertBefore(comparedItem.asItem().getDefaultInstance(), stack, tabVisibility);
                        }

                    }
                });
            }
        }
    }

    public static void addInstrumentAfter(Item comparedItem, Item instrument, TagKey<Instrument> tagKey, CreativeModeTab.TabVisibility tabVisibility, @NotNull ResourceKey<CreativeModeTab>... tabs) {
        if (comparedItem != null) {
            if (instrument != null) {
                register(event -> {
                    if(contains(tabs, event.getTabKey())) {
                        for (Holder<Instrument> instrumentHolder : BuiltInRegistries.INSTRUMENT.getTagOrEmpty(tagKey)) {
                            ItemStack stack = InstrumentItem.create(instrument, instrumentHolder);
                            stack.setCount(1);
                            event.insertAfter(comparedItem.getDefaultInstance(), stack, tabVisibility);
                        }
                    }
                });
            }
        }
    }

    private static <T> boolean contains(T[] list, T entry) {
        return Arrays.stream(list).toList().contains(entry);
    }

}
