package org.imesense.dynamicspawncontrol.core.script.AuxScript;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public final class Util
{
    public static JsonElement resolveTemplate(JsonElement element, JsonObject templates) {
        if (element.isJsonPrimitive())
        {
            String elementStr = element.getAsString();

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

                    JsonElement templateValue = templates.get(templateKey);

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
                return templates.get(templateKey);
            }
        }

        return element;
    }

    private static boolean containsElement(JsonArray array, JsonElement element)
    {
        for (JsonElement item : array)
        {
            if (item.equals(element))
            {
                return true;
            }
        }

        return false;
    }
}
