package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.baseparser.BaseParser;
import org.imesense.dynamicspawncontrol.core.logfile.Log;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.data.DropItem;
import org.imesense.dynamicspawncontrol.core.script.storage.dropitem.storage.GeneralDropItem;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

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
        Log.writeDataToLogFile(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (!file.exists())
        {
            Log.writeDataToLogFile(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();

            List<DropItem.Data> dataList = new ArrayList<>();

            for (JsonElement element : jsonArray)
            {
                JsonObject jsonObject = element.getAsJsonObject();

                DropItem.Data data = new DropItem.Data();
                data.entity = new ResourceLocation(jsonObject.get("entity").getAsString());

                JsonArray dropsArray = jsonObject.getAsJsonArray("drop");
                data.drops = new ArrayList<>();

                for (Integer i = 0; i < dropsArray.size(); i += 5)
                {
                    DropItem.Data.ItemDrop itemDrop = new DropItem.Data.ItemDrop();

                    itemDrop.item = new ResourceLocation(dropsArray.get(i).getAsString());

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

            GeneralDropItem.getInstance().dropItemList.addAll(dataList);
        }
        catch (IOException exception)
        {
            Log.writeDataToLogFile(0, "Failed to load drop item config: " + exception.getMessage());
        }
    }

    @Override
    public void eraseData()
    {
        GeneralDropItem.getInstance().dropItemList.clear();
    }
}
