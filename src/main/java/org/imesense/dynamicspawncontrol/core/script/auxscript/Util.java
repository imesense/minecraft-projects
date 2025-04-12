package org.imesense.dynamicspawncontrol.core.script.auxscript;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class Util
{
    public Util()
    {

    }

    public static JsonElement resolveTemplate(JsonElement jsonElement, JsonObject jsonObject)
    {
        if (jsonElement.isJsonPrimitive())
        {
            String elementStr = jsonElement.getAsString();

            if (elementStr.startsWith("@") && elementStr.contains(","))
            {
                String[] templateKeys = elementStr.split(",");
                JsonArray resultArray = new JsonArray();

                for (String templateKey : templateKeys)
                {
                    templateKey = templateKey.trim();

                    if (templateKey.startsWith("@"))
                    {
                        templateKey = templateKey.substring(1);
                    }

                    JsonElement templateValue = jsonObject.get(templateKey);

                    if (templateValue != null)
                    {
                        if (templateValue.isJsonArray())
                        {
                            for (JsonElement item : templateValue.getAsJsonArray())
                            {
                                if (!containsElement(resultArray, item))
                                {
                                    resultArray.add(item);
                                }
                            }
                        }
                        else
                        {
                            if (!containsElement(resultArray, templateValue))
                            {
                                resultArray.add(templateValue);
                            }
                        }
                    }
                }

                return resultArray;
            }
            else if (elementStr.startsWith("@"))
            {
                String templateKey = elementStr.substring(1);
                return jsonObject.get(templateKey);
            }
        }

        return jsonElement;
    }

    private static boolean containsElement(JsonArray jsonArray, JsonElement jsonElement)
    {
        for (JsonElement item : jsonArray)
        {
            if (item.equals(jsonElement))
            {
                return true;
            }
        }

        return false;
    }
}
