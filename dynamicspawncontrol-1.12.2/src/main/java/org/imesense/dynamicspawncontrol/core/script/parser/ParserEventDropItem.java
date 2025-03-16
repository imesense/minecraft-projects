package org.imesense.dynamicspawncontrol.core.script.parser;

import com.google.gson.*;
import net.minecraft.util.ResourceLocation;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
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

public final class ParserEventDropItem extends BaseParser
{
    public ParserEventDropItem(final String NAME_FILE)
    {
        CodeGeneric.printInitClassToLog(this.getClass());
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

        try (FileReader reader = new FileReader(file))
        {
            JsonParser parser = new JsonParser();
            JsonArray jsonArray = parser.parse(reader).getAsJsonArray();

            List<DropItem.Data> dataList = new ArrayList<>();

            for (JsonElement element : jsonArray)
            {
                JsonObject jsonObject = element.getAsJsonObject();

                DropItem.Data data = new DropItem.Data();
                data.entity = new ResourceLocation(jsonObject.get("entity").getAsString());

                JsonArray dropsArray = jsonObject.getAsJsonArray("drop");
                data.drops = new ArrayList<>();

                for (int i = 0; i < dropsArray.size(); i += 2)
                {
                    DropItem.Data.ItemDrop itemDrop = new DropItem.Data.ItemDrop();
                    itemDrop.item = new ResourceLocation(dropsArray.get(i).getAsString());
                    itemDrop.amount = dropsArray.get(i + 1).getAsInt();
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
