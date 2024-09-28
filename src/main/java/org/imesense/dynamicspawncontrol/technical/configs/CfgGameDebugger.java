package org.imesense.dynamicspawncontrol.technical.configs;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
import org.imesense.dynamicspawncontrol.technical.configs.cfgGameDebugger.DataCategories;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public final class CfgGameDebugger extends CustomConceptConfig
{
    public static CfgGameDebugger instance;

    public CfgGameDebugger(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        this.nameConfig = this.constructPathToDirectory() + nameConfigFile;

        DataCategories.DebugMonitor.instance = new DataCategories.DebugMonitor("monitor");
        DataCategories.DebugEvent.instance = new DataCategories.DebugEvent("event");

        if (Files.exists(Paths.get(this.nameConfig)))
        {
            loadFromFile();
        }
        else
        {
            saveToFile();
        }

        CodeGenericUtils.printInitClassToLog(CfgGameDebugger.class);
    }

    public void saveToFile() throws IOException
    {
        JsonObject jsonObject = new JsonObject();
        JsonObject monitorObject = new JsonObject();

        monitorObject.addProperty("monitorDebug", DataCategories.DebugMonitor.instance.getDebugMonitorOpt());
        jsonObject.add("monitor", monitorObject);

        JsonObject eventObject = new JsonObject();

        eventObject.addProperty("drop_all_exp", DataCategories.DebugEvent.instance.getDebugDropAllExpOpt());
        jsonObject.add("event", eventObject);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig))
        {
            gson.toJson(jsonObject, file);
        }
    }

    public void loadFromFile() throws IOException
    {
        FileReader reader = new FileReader(this.nameConfig);
        JsonElement jsonElement = new JsonParser().parse(reader);
        JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.has("monitor"))
        {
            JsonObject monitorObject = jsonObject.getAsJsonObject("monitor");
            DataCategories.DebugMonitor.instance.setDebugMonitorOpt(monitorObject.get("monitorDebug").getAsBoolean());
        }

        if (jsonObject.has("event"))
        {
            JsonObject eventObject = jsonObject.getAsJsonObject("event");
            DataCategories.DebugEvent.instance.setDebugDropAllExpOpt(eventObject.get("drop_all_exp").getAsBoolean());
        }
    }
}
