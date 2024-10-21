package com.mayb.tutorialmod.item.custom;

import com.mayb.tutorialmod.block.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;

import java.util.Map;

// The ChiselItem class extends the base Minecraft Item class
// This item allows players to "chisel" certain blocks, transforming them into other blocks
public class ChiselItem extends Item {

    // CHISEL_MAP defines a mapping between specific blocks that can be chiseled and the resulting block
    // It maps vanilla Minecraft blocks like STONE to their chiseled version, such as STONE_BRICKS
    // Custom blocks like ModBlocks.PINK_GARNET_BLOCK are also part of this mapping
    private static final Map<Block, Block> CHISEL_MAP =
            Map.of(
                    Blocks.STONE, Blocks.STONE_BRICKS,               // Chisel STONE into STONE_BRICKS
                    Blocks.END_STONE, Blocks.END_STONE_BRICKS,       // Chisel END_STONE into END_STONE_BRICKS
                    Blocks.OAK_LOG, ModBlocks.PINK_GARNET_BLOCK,     // Chisel OAK_LOG into a custom block (PINK_GARNET_BLOCK)
                    Blocks.GOLD_BLOCK, Blocks.NETHERITE_BLOCK        // Chisel GOLD_BLOCK into NETHERITE_BLOCK
            );

    // Constructor for the ChiselItem
    // Takes an 'Item.Settings' object which defines various properties (like durability, max stack size, etc.)
    public ChiselItem(Settings settings) {
        super(settings); // Calls the parent Item class's constructor
    }

    // Overriding the useOnBlock method which is called when the player uses the item on a block
    // 'ItemUsageContext' contains information about where and how the item was used (block position, world, player, etc.)
    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld(); // Get the world where the block interaction is happening
        Block clickedBlock = world.getBlockState(context.getBlockPos()).getBlock(); // Get the block the player clicked on

        // Check if the clicked block is part of the CHISEL_MAP (i.e., it can be chiseled)
        if (CHISEL_MAP.containsKey(clickedBlock)) {
            // Ensure that the following actions only occur on the server side (not on the client)
            if (!world.isClient()) {

                // Change the clicked block to its corresponding chiseled version
                // The new block is retrieved from the CHISEL_MAP
                world.setBlockState(context.getBlockPos(), CHISEL_MAP.get(clickedBlock).getDefaultState());

                // Damage the chisel item (reduce its durability by 1)
                // It also informs the player that the item is being worn down, eventually breaking
                // Uses a lambda function to send an equipment break status when the chisel's durability runs out
                context.getStack().damage(1,
                        ((ServerWorld) world), // Casting to ServerWorld to handle server-specific operations
                        ((ServerPlayerEntity) context.getPlayer()), // Get the player using the chisel
                        item -> context.getPlayer().sendEquipmentBreakStatus(item, EquipmentSlot.MAINHAND) // Notify player when the item breaks
                );

                // Play a sound to indicate the chisel action was successful
                // The sound used is the GRINDSTONE_USE sound, which fits the idea of chiseling
                world.playSound(null, context.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS);
            }
        }

        // Return SUCCESS to indicate the action was completed successfully
        return ActionResult.SUCCESS;
    }
}
