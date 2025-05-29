package org.imesense.dynamicspawncontrol;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;
import org.imesense.dynamicspawncontrol.core.annotation.TODO;

import java.util.Set;

@TODO(
        value = "W.I.P. Встроенный менеджер конфигураций, модернезировать диаграмму под класс",
        showOnce = false, priority = TODO.TodoPriority.HIGH
)
public class DynamicSpawnControlGuiFactory implements IModGuiFactory
{
    @Override
    public void initialize(Minecraft minecraft)
    {

    }

    @Override
    public boolean hasConfigGui()
    {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen parent)
    {
        return new DynamicSpawnControlGuiConfig(parent);
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }
}
