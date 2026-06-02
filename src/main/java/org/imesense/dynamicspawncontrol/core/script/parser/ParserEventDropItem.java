package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data.DropItem;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage.GeneralDropItem;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@InitLog
public final class ParserEventDropItem extends BaseParser
{
    public ParserEventDropItem(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;
    }

    @Override
    public void loadConfig(boolean init)
    {
        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            JsonArray jsonArray = new JsonParser().parse(fileReader).getAsJsonArray();

            List<DropItem.Data> dataList = new ArrayList<>();

            for (JsonElement element : jsonArray)
            {
                try
                {
                    JsonObject jsonObject = element.getAsJsonObject();

                    if (!jsonObject.has("entity"))
                    {
                        continue;
                    }

                    String entityId = jsonObject.get("entity").getAsString();

                    DropItem.Data data = new DropItem.Data();
                    data.entity = new ResourceLocation(entityId);

                    if (jsonObject.has("id_dimension"))
                    {
                        data.idDimension = jsonObject.get("id_dimension").getAsInt();
                    }
                    else
                    {
                        data.idDimension = null;
                    }

                    if (!jsonObject.has("drop"))
                    {
                        continue;
                    }

                    JsonArray dropsArray = jsonObject.getAsJsonArray("drop");
                    data.drops = new ArrayList<>();

                    for (Integer i = 0; i < dropsArray.size(); i += 5)
                    {
                        if (i + 4 >= dropsArray.size())
                        {
                            break;
                        }

                        DropItem.Data.ItemDrop itemDrop = new DropItem.Data.ItemDrop();

                        String itemId = dropsArray.get(i).getAsString();
                        itemDrop.item = new ResourceLocation(itemId);

                        itemDrop.minAmount = dropsArray.get(i + 1).getAsInt();
                        itemDrop.maxAmount = dropsArray.get(i + 2).getAsInt();
                        itemDrop.chance = dropsArray.get(i + 3).getAsFloat();

                        String resultStr = dropsArray.get(i + 4).getAsString().toLowerCase();

                        switch (resultStr)
                        {
                            case "allow":
                                itemDrop.result = Event.Result.ALLOW;
                                break;
                            case "deny":
                                itemDrop.result = Event.Result.DENY;
                                break;
                            default:
                                itemDrop.result = Event.Result.DEFAULT;
                                break;
                        }

                        data.drops.add(itemDrop);
                    }

                    dataList.add(data);
                }
                catch (Exception exception)
                {

                }
            }

            GeneralDropItem.getInstance().dropItemList.addAll(dataList);
        }
        catch (IOException exception)
        {

        }
        catch (JsonParseException exception)
        {

        }
    }

    @Override
    public void eraseData()
    {
        GeneralDropItem.getInstance().dropItemList.clear();
    }
}
