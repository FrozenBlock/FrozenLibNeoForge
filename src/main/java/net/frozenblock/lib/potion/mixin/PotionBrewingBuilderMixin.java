package net.frozenblock.lib.potion.mixin;

import net.frozenblock.lib.potion.api.FrozenPotionBrewingBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

@Mixin(PotionBrewing.Builder.class)
public abstract class PotionBrewingBuilderMixin implements FrozenPotionBrewingBuilder {

    @Shadow @Final private FeatureFlagSet enabledFeatures;

    @Shadow
    private static void expectPotion(Item item) {}

    @Shadow @Final private List<PotionBrewing.Mix<Item>> containerMixes;

    @Shadow @Final private List<PotionBrewing.Mix<Potion>> potionMixes;

    @Override
    public void registerItemRecipe(Item input, Ingredient ingredient, Item output) {
        if(input.isEnabled(this.enabledFeatures) && output.isEnabled(enabledFeatures)) {
            expectPotion(input);
            expectPotion(output);
            this.containerMixes.add(new PotionBrewing.Mix(input.builtInRegistryHolder(), ingredient, output.builtInRegistryHolder()));
        }
    }

    @Override
    public void registerPotionRecipe(Holder<Potion> input, Ingredient ingredient, Holder<Potion> output) {
        if(input.value().isEnabled(this.enabledFeatures) && output.value().isEnabled(this.enabledFeatures)) {
            this.potionMixes.add(new PotionBrewing.Mix<>(input, ingredient, output));
        }
    }

    @Override
    public void registerRecipes(Ingredient ingredient, Holder<Potion> potion) {
        if(potion.value().isEnabled(this.enabledFeatures)) {
            this.registerPotionRecipe(Potions.WATER, ingredient, Potions.MUNDANE);
            this.registerPotionRecipe(Potions.AWKWARD, ingredient, potion);
        }
    }

    @Override
    public FeatureFlagSet getEnableFeatures() {
        return this.enabledFeatures;
    }
}
