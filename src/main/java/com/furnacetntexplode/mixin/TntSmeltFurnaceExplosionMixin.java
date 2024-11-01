package com.furnacetntexplode.mixin;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class TntSmeltFurnaceExplosionMixin {

	private static void Explode(World world, BlockPos pos) {
		Explosion explosion = world.createExplosion(null, pos.getX(), pos.getY(), pos.getZ(), 6, World.ExplosionSourceType.TNT);
		explosion.collectBlocksAndDamageEntities();
	}

	@Inject(at = @At("HEAD"), method = "tick")
	private static void ExplosionTick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
		if(blockEntity != null) {
			Vec3d position3 = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
			MinecraftServer server = world.getServer();
			assert server != null;

			List<Recipe<?>> d = blockEntity.getRecipesUsedAndDropExperience(server.getOverworld(), position3);

			if (!d.isEmpty()) {
				Recipe<?> recipe = d.get(0);
				assert recipe != null;

				if (Objects.equals(recipe.getId().toString(), "furnacetntexplode:tntsmelt")) {
					Explode(world, pos);
					System.out.println(recipe.getId().toString());
				}
			}
		}
	}
}