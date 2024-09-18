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
        private final String _keyword;

        /**
         *
         * @param keyword
         */
        NameSingleScript(String keyword)
        {
            this._keyword = keyword;
        }

        /**
         *
         * @return
         */
        public String getKeyword()
        {
            return this._keyword;
        }
    }

    /**
     *
     */
    public static final class UNICODE
    {
        /**
         *
         */
        public static char WHITE_SPACE = ' ';

        /**
         *
         */
        public static char SECTION = '\u00A7';
    }
}
