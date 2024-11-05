package org.imesense.dynamicspawncontrol.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.core.api.IRecipes;
import org.imesense.dynamicspawncontrol.plugin.webslinger_1_12_2_2_2_4.ObjectHandlerClient;

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
                new ItemStack(ObjectHandlerClient.Webbing),
                "SSS",
                "SWS",
                "SSS",
                'S', Items.STRING,
                'W', Items.SLIME_BALL);
    }
}
