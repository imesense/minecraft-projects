package org.imesense.dynamicspawncontrol.technical.customlibrary;

/**
 *
 */
public class AuxFunctions
{
    /**
     *
     */
    public enum NameSingleScript
    {
        SCRIPT_MOBS_LIST_SEE_SKY("action_mobs_list_see_sky.json"),

        SCRIPT_ZOMBIE_SUMMON_AID("action_zombie_summon_aid.json"),

        SCRIPT_CACHE_MOBS("cache_mobs.json");

        private final String _keyword;

        NameSingleScript(String keyword)
        {
            this._keyword = keyword;
        }

        public String getKeyword()
        {
            return this._keyword;
        }
    }
}
