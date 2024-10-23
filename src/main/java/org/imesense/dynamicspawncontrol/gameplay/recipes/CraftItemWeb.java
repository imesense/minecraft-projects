package org.imesense.dynamicspawncontrol.gameplay.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.technical.handler.ObjectHandlerClient;

/**
 *
 */
public final class CraftItemWeb implements IRecipes
{
    /**
     *
     */
    @Override
    public void registry()
    {
        GameRegistry.addShapedRecipe(new ResourceLocation("dynamicspawncontrol", "webbing"), null,
                new ItemStack(ObjectHandlerClient.webbing),
                "SSS",
                "SWS",
                "SSS",
                'S', Items.STRING,
                'W', Items.SLIME_BALL);
    }
}
