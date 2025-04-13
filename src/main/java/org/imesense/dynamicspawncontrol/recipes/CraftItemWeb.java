package org.imesense.dynamicspawncontrol.recipes;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.imesense.dynamicspawncontrol.core.interfaces.IRecipes;
import org.imesense.dynamicspawncontrol.core.plugin.mod.webslinger_1_12_2_2_2_4.ObjectHandlerClient;

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
    }
}
