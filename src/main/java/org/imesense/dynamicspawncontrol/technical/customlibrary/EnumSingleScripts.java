package org.imesense.dynamicspawncontrol.technical.customlibrary;

public enum EnumSingleScripts
{
    /**
     *
     */
    SCRIPT_MOBS_LIST_SEE_SKY("action_mobs_list_see_sky.json"),

    /**
     *
     */
    SCRIPT_ZOMBIE_SUMMON_AID("action_zombie_summon_aid.json"),

    /**
     *
     */
    SCRIPT_CACHE_MOBS("cache_mobs.json");

    /**
     *
     */
    private final String keyword;

    /**
     *
     * @param keyword
     */
    EnumSingleScripts(String keyword)
    {
        this.keyword = keyword;
    }

    /**
     *
     * @return
     */
    public String getKeyword()
    {
        return this.keyword;
    }
}
