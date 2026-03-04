package org.imesense.dynamicspawncontrol.core.text;

import net.minecraft.util.text.TextFormatting;

public enum TextColor
{
    RED(TextFormatting.RED),
    AQUA(TextFormatting.AQUA),
    GOLD(TextFormatting.GOLD),
    GRAY(TextFormatting.GRAY),
    BLUE(TextFormatting.BLUE),
    WHITE(TextFormatting.WHITE),
    GREEN(TextFormatting.GREEN),
    BLACK(TextFormatting.BLACK),
    PURPLE(TextFormatting.DARK_PURPLE),
    YELLOW(TextFormatting.YELLOW);

    private final TextFormatting formatting;

    TextColor(TextFormatting formatting)
    {
        this.formatting = formatting;
    }

    public TextFormatting getFormatting()
    {
        return formatting;
    }

    @Override
    public String toString()
    {
        return formatting.toString();
    }
}
