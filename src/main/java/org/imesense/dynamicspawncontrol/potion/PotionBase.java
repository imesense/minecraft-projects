package org.imesense.dynamicspawncontrol.potion;

import net.minecraft.client.Minecraft;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

public abstract class PotionBase extends Potion
{
    public static final ResourceLocation EXTRA_EFFECTS_ALT = new ResourceLocation(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID, "textures/icons/dsc_potion_icons.png");

    public PotionBase(boolean isBadEffectIn, int liquidColorIn, String name)
    {
        super(isBadEffectIn, liquidColorIn);

        setPotionName("effect." + name);

        if (!isBadEffectIn)
        {
            setBeneficial();
        }

        setRegistryName(new ResourceLocation(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID, "effect." + name));
    }

    @SideOnly(Side.CLIENT)
    public int getStatusIconIndex()
    {
        Minecraft.getMinecraft().renderEngine.bindTexture(EXTRA_EFFECTS_ALT);
        return super.getStatusIconIndex();
    }
}
