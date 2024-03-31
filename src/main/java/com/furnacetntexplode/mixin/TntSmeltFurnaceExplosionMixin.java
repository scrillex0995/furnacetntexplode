package com.furnacetntexplode.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class TntSmeltFurnaceExplosionMixin {
	private static boolean doCrafting = false;
	private static void Explode(World world, BlockPos pos) {
		Explosion explosion = world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 8, World.ExplosionSourceType.TNT);
		explosion.collectBlocksAndDamageEntities();
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private static void ExplosionTick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
		if(doCrafting) {
			Explode(world, pos);
			doCrafting = false;
		}
		//Explosion explosion = world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 3, World.ExplosionSourceType.TNT);
	}

	@Inject(at = @At("HEAD"), method = "craftRecipe")
	private static void CraftEvent(DynamicRegistryManager registryManager, @Nullable Recipe<?> recipe, DefaultedList<ItemStack> slots, int count, CallbackInfoReturnable<Boolean> cir) {

        assert recipe != null;

        System.out.println(recipe.getId().toString());

		if(Objects.equals(recipe.getId().toString(), "furnacetntexplode:tntsmelt")) {
			doCrafting = true;
		}
	}
}