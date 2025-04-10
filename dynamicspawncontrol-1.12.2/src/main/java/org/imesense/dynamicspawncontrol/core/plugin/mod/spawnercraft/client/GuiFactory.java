package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.DefaultGuiFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
/* loaded from: input.jar:cad97/spawnercraft/client/gui/GuiFactory.class */
public class GuiFactory extends DefaultGuiFactory {
    public GuiFactory() {
        super(SpawnerCraft.MOD_ID, SpawnerCraft.MOD_NAME);
    }

    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        return new SpawnerCraftGuiConfig(parentScreen);
    }
}
