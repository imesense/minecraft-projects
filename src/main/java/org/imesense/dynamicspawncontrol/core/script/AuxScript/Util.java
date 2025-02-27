package org.imesense.dynamicspawncontrol.core.script.AuxScript;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Util
{
    public static JsonElement resolveTemplate(JsonElement element, JsonObject templates)
    {
        if (element.isJsonPrimitive() && element.getAsString().startsWith("@"))
        {
            String templateKey = element.getAsString().substring(1);
            return templates.get(templateKey);
        }

        return element;
    }
}
