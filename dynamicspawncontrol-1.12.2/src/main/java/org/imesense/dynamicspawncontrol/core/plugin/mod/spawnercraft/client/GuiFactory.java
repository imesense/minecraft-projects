package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;

@SideOnly(Side.CLIENT)
/* loaded from: input.jar:cad97/spawnercraft/client/gui/GuiFactory.class */
public class GuiFactory extends DefaultGuiFactory
{
    public GuiFactory()
    {
        super(DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID, DynamicSpawnControlStructure.STRUCT_INFO_MOD.NAME);
    }

    public GuiScreen createConfigGui(GuiScreen guiScreen)
    {
        return new SpawnerCraftGuiConfig(guiScreen);
    }
}
