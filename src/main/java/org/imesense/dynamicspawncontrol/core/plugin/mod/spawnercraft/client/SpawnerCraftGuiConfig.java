package org.imesense.dynamicspawncontrol.core.plugin.mod.spawnercraft.client;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
/* loaded from: input.jar:cad97/spawnercraft/client/gui/SpawnerCraftGuiConfig.class */
public class SpawnerCraftGuiConfig extends GuiConfig {
    public SpawnerCraftGuiConfig(GuiScreen guiScreen) {
        super(guiScreen, getConfigElements(), SpawnerCraft.MOD_ID, false, false, GuiConfig.getAbridgedConfigPath(ConfigHandler.config.toString()));
    }

    private static List<IConfigElement> getConfigElements() {
        List<IConfigElement> elements = new ArrayList<>();
        elements.addAll(new ConfigElement(ConfigHandler.config.getCategory("general")).getChildElements());
        return elements;
    }
}
