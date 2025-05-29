package org.imesense.dynamicspawncontrol;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;

import java.util.ArrayList;
import java.util.List;

@TODO(
        value = "W.I.P. Встроенный менеджер конфигураций, модернезировать диаграмму под класс",
        showOnce = false, priority = TODO.TodoPriority.HIGH
)
public class DynamicSpawnControlGuiConfig extends GuiConfig
{
    public DynamicSpawnControlGuiConfig(GuiScreen parent)
    {
        super(parent, getConfigElements(),
                DynamicSpawnControlStructure.STRUCT_INFO_MOD.MOD_ID,
                false, false, "DynamicSpawnControl Configuration [W.I.P.]");
    }

    private static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> elements = new ArrayList<>();

        return elements;
    }
}
