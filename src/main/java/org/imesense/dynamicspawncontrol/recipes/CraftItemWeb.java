package org.imesense.dynamicspawncontrol.recipes;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;
import org.imesense.dynamicspawncontrol.core.interfaces.IRecipes;
import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.ObjectHandlerClient;

@TODO(
        value = "Divide this class into different methods",
        priority = TODO.TodoPriority.HIGH,
        showOnce = false
)
public final class CraftItemWeb implements IRecipes
{
    public CraftItemWeb()
    {

    }

    @Override
    public void registry()
    {
        GameRegistry.addShapedRecipe(new ResourceLocation("dynamicspawncontrol", "webbing"), null,
                new ItemStack(ObjectHandlerClient.ItemWebbing),
                "OOO",
                "OGO",
                "OOO",
                'O', Items.STRING,
                'G', Items.SLIME_BALL);

        GameRegistry.addShapedRecipe(
                new ResourceLocation("dynamicspawncontrol", "gunpowder"),
                null,
                new ItemStack(Items.GUNPOWDER, 1),
                "RC ",
                "F  ",
                "   ",
                'R', Items.REDSTONE,
                'C', Items.COAL,
                'F', Items.FLINT
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation("dynamicspawncontrol", "gunpowder_2x"),
                null,
                new ItemStack(Items.GUNPOWDER, 2),
                "RCR",
                "F C",
                "   ",
                'R', Items.REDSTONE,
                'C', Items.COAL,
                'F', Items.FLINT
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation("dynamicspawncontrol", "gunpowder_3x"),
                null,
                new ItemStack(Items.GUNPOWDER, 3),
                "RCR",
                "FCC",
                "RR ",
                'R', Items.REDSTONE,
                'C', Items.COAL,
                'F', Items.FLINT
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation("dynamicspawncontrol", "gunpowder_4x"),
                null,
                new ItemStack(Items.GUNPOWDER, 4),
                "RCR",
                "FCC",
                "RCR",
                'R', Items.REDSTONE,
                'C', Items.COAL,
                'F', Items.FLINT
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation("dynamicspawncontrol", "gunpowder_9x"),
                null,
                new ItemStack(Items.GUNPOWDER, 9),
                "RC ",
                "F  ",
                "   ",
                'R', Blocks.REDSTONE_BLOCK,
                'C', Blocks.COAL_BLOCK,
                'F', Items.FLINT
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation("dynamicspawncontrol", "gunpowder_18x"),
                null,
                new ItemStack(Items.GUNPOWDER, 18),
                "RCR",
                "F C",
                "   ",
                'R', Blocks.REDSTONE_BLOCK,
                'C', Blocks.COAL_BLOCK,
                'F', Items.FLINT
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation("dynamicspawncontrol", "gunpowder_27x"),
                null,
                new ItemStack(Items.GUNPOWDER, 27),
                "RCR",
                "FCC",
                "RR ",
                'R', Blocks.REDSTONE_BLOCK,
                'C', Blocks.COAL_BLOCK,
                'F', Items.FLINT
        );

        GameRegistry.addShapedRecipe(
                new ResourceLocation("dynamicspawncontrol", "gunpowder_36x"),
                null,
                new ItemStack(Items.GUNPOWDER, 36),
                "RCR",
                "FCC",
                "RCR",
                'R', Blocks.REDSTONE_BLOCK,
                'C', Blocks.COAL_BLOCK,
                'F', Items.FLINT
        );
    }
}
