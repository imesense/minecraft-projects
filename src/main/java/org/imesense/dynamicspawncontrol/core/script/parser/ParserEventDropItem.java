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
    private static final boolean DEBUG_AND_CHECK_SYNTAX = true;

    public ParserEventDropItem(final String NAME_FILE)
    {
        this.nameFile = NAME_FILE;

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "ParserEventDropItem constructor called with file: " + NAME_FILE);
        }
    }

    @Override
    public void loadConfig(boolean init)
    {
        Log.write(0, "Reading the config for the first time: " + init + " " + "file: " + this.nameFile);

        File file = getConfigFile(init,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_GAME_SCRIPTS, this.nameFile);

        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Config file path: " + file.getAbsolutePath());
        }

        if (!file.exists())
        {
            Log.write(0, "Config file not found, creating new: " + file);
            this.createNewConfigFile(file);
            return;
        }

        try (FileReader fileReader = new FileReader(file))
        {
            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Reading and parsing JSON file");
            }

            JsonArray jsonArray = JsonParser.parseReader(fileReader).getAsJsonArray();

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Found " + jsonArray.size() + " entity entries in JSON");
            }

            List<DropItem.Data> dataList = new ArrayList<>();

            for (JsonElement element : jsonArray)
            {
                try
                {
                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Processing new entity drop configuration");
                    }

                    JsonObject jsonObject = element.getAsJsonObject();

                    if (!jsonObject.has("entity"))
                    {
                        Log.write(0, "Missing required 'entity' field in JSON object");
                        continue;
                    }

                    String entityId = jsonObject.get("entity").getAsString();

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Processing drops for entity: " + entityId);
                    }

                    DropItem.Data data = new DropItem.Data();
                    data.entity = new ResourceLocation(entityId);

                    if (!jsonObject.has("drop"))
                    {
                        Log.write(0, "Missing required 'drop' array for entity: " + entityId);
                        continue;
                    }

                    JsonArray dropsArray = jsonObject.getAsJsonArray("drop");
                    data.drops = new ArrayList<>();

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Found " + dropsArray.size() + " drop entries (will process in chunks of 5)");
                    }

                    for (Integer i = 0; i < dropsArray.size(); i += 5)
                    {
                        if (i + 4 >= dropsArray.size())
                        {
                            Log.write(0, "Incomplete drop entry (needs 5 elements) at position " + i);
                            break;
                        }

                        DropItem.Data.ItemDrop itemDrop = new DropItem.Data.ItemDrop();

                        String itemId = dropsArray.get(i).getAsString();
                        itemDrop.item = new ResourceLocation(itemId);

                        itemDrop.minAmount = dropsArray.get(i + 1).getAsInt();
                        itemDrop.maxAmount = dropsArray.get(i + 2).getAsInt();
                        itemDrop.chance = dropsArray.get(i + 3).getAsFloat();

                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, String.format(
                                    "Drop item: %s, amount: %d-%d, chance: %.2f",
                                    itemId,
                                    itemDrop.minAmount,
                                    itemDrop.maxAmount,
                                    itemDrop.chance
                            ));
                        }

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

                        if (DEBUG_AND_CHECK_SYNTAX)
                        {
                            Log.write(0, "Drop result set to: " + itemDrop.result);
                        }

                        data.drops.add(itemDrop);
                    }

                    dataList.add(data);

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        Log.write(0, "Successfully processed drops for entity: " + entityId);
                    }
                }
                catch (Exception exception)
                {
                    Log.write(0, "Error processing drop item entry: " + exception.getMessage());

                    if (DEBUG_AND_CHECK_SYNTAX)
                    {
                        exception.printStackTrace();
                    }
                }
            }

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                Log.write(0, "Adding " + dataList.size() + " entity drop configurations to storage");
            }

            GeneralDropItem.getInstance().dropItemList.addAll(dataList);
        }
        catch (IOException exception)
        {
            Log.write(0, "Failed to load drop item config: " + exception.getMessage());

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                exception.printStackTrace();
            }
        }
        catch (JsonParseException exception)
        {
            Log.write(0, "Malformed JSON in drop item config: " + exception.getMessage());

            if (DEBUG_AND_CHECK_SYNTAX)
            {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void eraseData()
    {
        if (DEBUG_AND_CHECK_SYNTAX)
        {
            Log.write(0, "Clearing all drop item configurations");
        }

        GeneralDropItem.getInstance().dropItemList.clear();
    }
}
