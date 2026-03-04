package org.imesense.dynamicspawncontrol.core.text;

public enum CmdCallType
{
    HIT("[Hit]"),
    COMMAND("[Command]"),
    TIMER("[Timer]"),
    SIGNAL("[Signal]"),
    MESSAGE("[Message]"),
    ITEM_DROP("[Item drop]");

    private final String description;

    CmdCallType(String desc)
    {
        this.description = desc;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString()
    {
        return description;
    }
}
