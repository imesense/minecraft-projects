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
public final class JsonServices
{
    /**
     *
     * @param element
     * @param name
     * @return
     */
    public static Optional<JsonElement> getElement(JsonObject element, String name)
    {
        JsonElement el = element.get(name);

        if (el != null)
        {
            return Optional.of(el);
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
     * @param element
     * @return
     */
    public static Stream<Pair<String, String>> asPairs(JsonElement element)
    {
        Stream.Builder<Pair<String, String>> builder = Stream.builder();

        for (Map.Entry<String, JsonElement> entry : element.getAsJsonObject().entrySet())
        {
            builder.add(Pair.of(entry.getKey(), entry.getValue().getAsString()));
        }

        return builder.build();
    }

    /**
     *
     * @param element
     * @return
     */
    public static Stream<JsonElement> asArrayOrSingle(JsonElement element)
    {
        if (element.isJsonArray())
        {
            Stream.Builder<JsonElement> builder = Stream.builder();

            for (JsonElement el : element.getAsJsonArray())
            {
                builder.add(el);
            }

            return builder.build();
        }
        else
        {
            return Stream.of(element);
        }
    }

    /**
     *
     * @param parent
     * @param name
     * @param pairs
     */
    public static void addPairs(JsonObject parent, String name, Map<String, String> pairs)
    {
        if (pairs != null)
        {
            JsonObject object = new JsonObject();

            for (Map.Entry<String, String> entry : pairs.entrySet())
            {
                object.add(entry.getKey(), new JsonPrimitive(entry.getValue()));
            }

            parent.add(name, object);
        }
    }

    /**
     *
     * @param parent
     * @param name
     * @param strings
     */
    public static void addArrayOrSingle(JsonObject parent, String name, Collection<String> strings)
    {
        if (strings != null)
        {
            if (strings.size() == 1)
            {
                parent.add(name, new JsonPrimitive(strings.iterator().next()));
            }
            else
            {
                JsonArray array = new JsonArray();

                for (String value : strings)
                {
                    array.add(new JsonPrimitive(value));
                }

                parent.add(name, array);
            }
        }
    }

    /**
     *
     * @param parent
     * @param name
     * @param integers
     */
    public static void addIntArrayOrSingle(JsonObject parent, String name, Collection<Integer> integers)
    {
        if (integers != null)
        {
            if (integers.size() == 1)
            {
                parent.add(name, new JsonPrimitive(integers.iterator().next()));
            }
            else
            {
                JsonArray array = new JsonArray();

                for (Integer value : integers)
                {
                    array.add(new JsonPrimitive(value));
                }

                parent.add(name, array);
            }
        }
    }

    /**
     *
     * @param jsonObject
     * @param key
     * @param defaultValue
     * @param extractor
     * @return
     * @param <T>
     */
    public static <T> T getValueFromJson(JsonObject jsonObject, String key, T defaultValue, BiFunction<JsonElement, T, T> extractor)
    {
        Log.writeDataToLogFile(Log.TypeLog[0], "Read jsonObject: " + jsonObject);

        if (jsonObject.has(key))
        {
            JsonElement element = jsonObject.get(key);
            if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber())
            {
                return extractor.apply(element, defaultValue);
            }
        }

        return defaultValue;
    }
}
