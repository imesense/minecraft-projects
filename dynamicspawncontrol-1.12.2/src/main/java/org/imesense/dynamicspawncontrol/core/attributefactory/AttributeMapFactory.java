package org.imesense.dynamicspawncontrol.core.attributefactory;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.core.json.Service;

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
     * @param jsonElement
     * @return
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    public AttributeMap<T> parse(@Nonnull JsonElement jsonElement)
    {
        AttributeMap<T> attributeMap = new AttributeMap<>();
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        for (Attribute<T> attribute : this.ATTRIBUTES)
        {
            AttributeKey<T> attributeKey = attribute.getKey();
            AttributeType<T> attributeType = attributeKey.getType();

            if (attribute.isMulti())
            {
                Map<AttributeType<T>, Function<JsonElement, Object>> transformers = new HashMap<>();

                transformers.put((AttributeType<T>)AttributeType.INTEGER, JsonElement::getAsInt);
                transformers.put((AttributeType<T>)AttributeType.FLOAT, JsonElement::getAsFloat);
                transformers.put((AttributeType<T>)AttributeType.BOOLEAN, JsonElement::getAsBoolean);
                transformers.put((AttributeType<T>)AttributeType.STRING, JsonElement::getAsString);
                transformers.put((AttributeType<T>)AttributeType.JSON, JsonElement::toString);

                Service.getElement(jsonObject, attributeKey.getName())
                        .ifPresent(e ->
                                Service.asArrayOrSingle(e)
                                        .map(transformers.getOrDefault(attributeType, x -> "INVALID"))
                                        .forEach(s -> attributeMap.addListNonnull(attributeKey, (T) s)));
            }
            else
            {
                if (attributeType == AttributeType.INTEGER)
                {
                    attributeMap.setNonnull(attributeKey, (T) Service.parseInt(jsonObject, attributeKey.getName()));
                }
                else if (attributeType == AttributeType.FLOAT)
                {
                    attributeMap.setNonnull(attributeKey, (T) Service.parseFloat(jsonObject, attributeKey.getName()));
                }
                else if (attributeType == AttributeType.BOOLEAN)
                {
                    attributeMap.setNonnull(attributeKey, (T) Service.parseBool(jsonObject, attributeKey.getName()));
                }
                else if (attributeType == AttributeType.STRING)
                {
                    if (jsonObject.has(attributeKey.getName()))
                    {
                        attributeMap.setNonnull(attributeKey, (T) jsonObject.get(attributeKey.getName()).getAsString());
                    }
                }
                else if (attributeType == AttributeType.JSON)
                {
                    if (jsonObject.has(attributeKey.getName()))
                    {
                        JsonElement jsonElement1 = jsonObject.get(attributeKey.getName());

                        if (jsonElement1.isJsonObject())
                        {
                            JsonObject jsonObject1 = jsonElement1.getAsJsonObject();
                            attributeMap.setNonnull(attributeKey, (T) jsonObject1.toString());
                        }
                        else if (jsonElement1.isJsonPrimitive())
                        {
                            JsonPrimitive jsonPrimitive = jsonElement1.getAsJsonPrimitive();

                            if (jsonPrimitive.isString())
                            {
                                attributeMap.setNonnull(attributeKey, (T) jsonPrimitive.getAsString());
                            }
                            else if (jsonPrimitive.isNumber())
                            {
                                attributeMap.setNonnull(attributeKey, (T) ("" + jsonPrimitive.getAsInt()));
                            }
                            else
                            {
                                throw new RuntimeException("Неверный тип для ключа '" + attributeKey.getName() + "'!");
                            }
                        }
                    }
                }
            }
        }

        return attributeMap;
    }
}
