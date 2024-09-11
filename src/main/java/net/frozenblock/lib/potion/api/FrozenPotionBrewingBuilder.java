package net.frozenblock.lib.potion.api;

import net.minecraft.core.Holder;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;

public interface FrozenPotionBrewingBuilder {
    void registerItemRecipe(Item input, Ingredient ingredient, Item output);
    void registerPotionRecipe(Holder<Potion> input, Ingredient ingredient, Holder<Potion> output);
    void registerRecipes(Ingredient ingredient, Holder<Potion> potion);
    FeatureFlagSet getEnableFeatures();
}
