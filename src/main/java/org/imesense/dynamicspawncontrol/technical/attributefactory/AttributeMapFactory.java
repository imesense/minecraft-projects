package org.imesense.dynamicspawncontrol.technical.attributefactory;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.technical.customlibrary.InlineJsonService;

import java.util.*;
import javax.annotation.Nonnull;
import java.util.function.Function;

/**
 *
 * @param <T>
 */
public final class AttributeMapFactory<T>
{
    /**
     *
     */
    private final ArrayList<Attribute<T>> ATTRIBUTES = new ArrayList<>();

    /**
     *
     * @param attribute
     * @return
     */
    public AttributeMapFactory<T> attribute(@Nonnull Attribute<T> attribute)
    {
        this.ATTRIBUTES.add(attribute);
        return this;
    }

    /**
     *
     * @param element
     * @return
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public AttributeMap<T> parse(@Nonnull JsonElement element)
    {
        AttributeMap<T> map = new AttributeMap<>();
        JsonObject jsonObject = element.getAsJsonObject();

        for (Attribute<T> attribute : this.ATTRIBUTES)
        {
            AttributeKey<T> key = attribute.getKey();
            AttributeType<T> type = key.getType();

            if (attribute.isMulti())
            {
                Map<AttributeType<T>, Function<JsonElement, Object>> transformers = new HashMap<>();

                transformers.put((AttributeType<T>)AttributeType.INTEGER, JsonElement::getAsInt);
                transformers.put((AttributeType<T>)AttributeType.FLOAT, JsonElement::getAsFloat);
                transformers.put((AttributeType<T>)AttributeType.BOOLEAN, JsonElement::getAsBoolean);
                transformers.put((AttributeType<T>)AttributeType.STRING, JsonElement::getAsString);
                transformers.put((AttributeType<T>)AttributeType.JSON, JsonElement::toString);

                InlineJsonService.getElement(jsonObject, key.getName())
                        .ifPresent(e ->
                                InlineJsonService.asArrayOrSingle(e)
                                        .map(transformers.getOrDefault(type, x -> "INVALID"))
                                        .forEach(s -> map.addListNonnull(key, (T) s)));
            }
            else
            {
                if (type == AttributeType.INTEGER)
                {
                    map.setNonnull(key, (T) InlineJsonService.parseInt(jsonObject, key.getName()));
                }
                else if (type == AttributeType.FLOAT)
                {
                    map.setNonnull(key, (T) InlineJsonService.parseFloat(jsonObject, key.getName()));
                }
                else if (type == AttributeType.BOOLEAN)
                {
                    map.setNonnull(key, (T) InlineJsonService.parseBool(jsonObject, key.getName()));
                }
                else if (type == AttributeType.STRING)
                {
                    if (jsonObject.has(key.getName()))
                    {
                        map.setNonnull(key, (T)jsonObject.get(key.getName()).getAsString());
                    }
                }
                else if (type == AttributeType.JSON)
                {
                    if (jsonObject.has(key.getName()))
                    {
                        JsonElement el = jsonObject.get(key.getName());

                        if (el.isJsonObject())
                        {
                            JsonObject obj = el.getAsJsonObject();
                            map.setNonnull(key, (T)obj.toString());
                        }
                        else if (el.isJsonPrimitive())
                        {
                            JsonPrimitive prim = el.getAsJsonPrimitive();

                            if (prim.isString())
                            {
                                map.setNonnull(key, (T)prim.getAsString());
                            }
                            else if (prim.isNumber())
                            {
                                map.setNonnull(key, (T)("" + prim.getAsInt()));
                            }
                            else
                            {
                                throw new RuntimeException("Неверный тип для ключа '" + key.getName() + "'!");
                            }
                        }
                    }
                }
            }
        }

        return map;
    }
}
