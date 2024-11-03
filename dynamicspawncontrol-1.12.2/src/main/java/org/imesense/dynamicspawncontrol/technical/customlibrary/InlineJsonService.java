package org.imesense.dynamicspawncontrol.technical.customlibrary;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 *
 */
public final class InlineJsonService
{
    /**
     *
     * @param jsonObject
     * @param name
     * @return
     */
    public static Optional<JsonElement> getElement(JsonObject jsonObject, String name)
    {
        JsonElement jsonElement = jsonObject.get(name);

        if (jsonElement != null)
        {
            return Optional.of(jsonElement);
        }
        else
        {
            return Optional.empty();
        }
    }

    /**
     *
     * @param jsonObject
     * @param name
     * @return
     */
    @Nullable
    public static Float parseFloat(JsonObject jsonObject, String name)
    {
        if (jsonObject.has(name))
        {
            return jsonObject.get(name).getAsFloat();
        }
        else
        {
            return null;
        }
    }

    /**
     *
     * @param jsonObject
     * @param name
     * @return
     */
    @Nullable
    public static Integer parseInt(JsonObject jsonObject, String name)
    {
        if (jsonObject.has(name))
        {
            return jsonObject.get(name).getAsInt();
        }
        else
        {
            return null;
        }
    }

    /**
     *
     * @param jsonObject
     * @param name
     * @return
     */
    @Nullable
    public static Boolean parseBool(JsonObject jsonObject, String name)
    {
        if (jsonObject.has(name))
        {
            return jsonObject.get(name).getAsBoolean();
        }
        else
        {
            return null;
        }
    }

    /**
     *
     * @param jsonElement
     * @return
     */
    public static Stream<Pair<String, String>> asPairs(JsonElement jsonElement)
    {
        Stream.Builder<Pair<String, String>> builder = Stream.builder();

        for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet())
        {
            builder.add(Pair.of(entry.getKey(), entry.getValue().getAsString()));
        }

        return builder.build();
    }

    /**
     *
     * @param jsonElement
     * @return
     */
    public static Stream<JsonElement> asArrayOrSingle(JsonElement jsonElement)
    {
        if (jsonElement.isJsonArray())
        {
            Stream.Builder<JsonElement> builder = Stream.builder();

            for (JsonElement el : jsonElement.getAsJsonArray())
            {
                builder.add(el);
            }

            return builder.build();
        }
        else
        {
            return Stream.of(jsonElement);
        }
    }

    /**
     *
     * @param jsonObject
     * @param name
     * @param pair
     */
    public static void addPairs(JsonObject jsonObject, String name, Map<String, String> pair)
    {
        if (pair != null)
        {
            JsonObject jsonObject1 = new JsonObject();

            for (Map.Entry<String, String> entry : pair.entrySet())
            {
                jsonObject1.add(entry.getKey(), new JsonPrimitive(entry.getValue()));
            }

            jsonObject.add(name, jsonObject1);
        }
    }

    /**
     *
     * @param jsonObject
     * @param name
     * @param stringCollection
     */
    public static void addArrayOrSingle(JsonObject jsonObject, String name, Collection<String> stringCollection)
    {
        if (stringCollection != null)
        {
            if (stringCollection.size() == 1)
            {
                jsonObject.add(name, new JsonPrimitive(stringCollection.iterator().next()));
            }
            else
            {
                JsonArray jsonArray = new JsonArray();

                for (String value : stringCollection)
                {
                    jsonArray.add(new JsonPrimitive(value));
                }

                jsonObject.add(name, jsonArray);
            }
        }
    }

    /**
     *
     * @param jsonObject
     * @param name
     * @param integerCollection
     */
    public static void addIntArrayOrSingle(JsonObject jsonObject, String name, Collection<Integer> integerCollection)
    {
        if (integerCollection != null)
        {
            if (integerCollection.size() == 1)
            {
                jsonObject.add(name, new JsonPrimitive(integerCollection.iterator().next()));
            }
            else
            {
                JsonArray jsonArray = new JsonArray();

                for (Integer value : integerCollection)
                {
                    jsonArray.add(new JsonPrimitive(value));
                }

                jsonObject.add(name, jsonArray);
            }
        }
    }

    /**
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @param biFunction
     * @return
     * @param <T>
     */
    public static <T> T getValueFromJson(JsonObject jsonObject, String key, T defaultValue, BiFunction<JsonElement, T, T> biFunction)
    {
        Log.writeDataToLogFile(0, "Read jsonObject: " + jsonObject);

        if (jsonObject.has(key))
        {
            JsonElement jsonElement = jsonObject.get(key);

            if (jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isNumber())
            {
                return biFunction.apply(jsonElement, defaultValue);
            }
        }

        return defaultValue;
    }
}
