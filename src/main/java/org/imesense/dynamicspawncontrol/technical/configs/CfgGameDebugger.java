package org.imesense.dynamicspawncontrol.technical.configs;

import com.google.gson.*;
import com.google.gson.annotations.JsonAdapter;
import org.imesense.dynamicspawncontrol.debug.CodeGenericUtils;
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

    private final DebugMonitor monitor;
    private final DebugEvent event;

    public static final class DebugMonitor
    {
        final String setCategory;

        private static boolean monitorDebug = false;

        public DebugMonitor(@Nonnull final String category)
        {
            this.setCategory = category;
        }

        public static Boolean getDebugMonitorOpt()
        {
            return monitorDebug;
        }

        public static void setDebugMonitorOpt(Boolean monitorDebug_)
        {
            monitorDebug = monitorDebug_;
        }
    }

    public static final class DebugEvent
    {
        final String setCategory;
        private boolean drop_all_experience = false;

        public DebugEvent(@Nonnull final String category)
        {
            this.setCategory = category;
        }

        public Boolean getDebugDropAllExpOpt()
        {
            return this.drop_all_experience;
        }

        public void setDebugDropAllExpOpt(Boolean drop_all_experience)
        {
            this.drop_all_experience = drop_all_experience;
        }
    }

    public CfgGameDebugger(String nameConfigFile) throws IOException
    {
        super(nameConfigFile);

        this.nameConfig = this.constructPathToDirectory() + nameConfigFile;
        this.monitor = new DebugMonitor("monitor");
        this.event = new DebugEvent("event");

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
        monitorObject.addProperty("monitorDebug", monitor.getDebugMonitorOpt());
        jsonObject.add("monitor", monitorObject);

        JsonObject eventObject = new JsonObject();
        eventObject.addProperty("drop_all_exp", event.getDebugDropAllExpOpt());
        jsonObject.add("event", eventObject);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try (FileWriter file = new FileWriter(this.nameConfig))
        {
            gson.toJson(jsonObject, file);
        }
    }

    public void loadFromFile() throws IOException
    {
        try (FileReader reader = new FileReader(this.nameConfig))
        {
            JsonElement jsonElement = new JsonParser().parse(reader);
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            if (jsonObject.has("monitor"))
            {
                JsonObject monitorObject = jsonObject.getAsJsonObject("monitor");
                this.monitor.setDebugMonitorOpt(monitorObject.get("monitorDebug").getAsBoolean());
            }

            if (jsonObject.has("event"))
            {
                JsonObject eventObject = jsonObject.getAsJsonObject("event");
                this.event.setDebugDropAllExpOpt(eventObject.get("drop_all_exp").getAsBoolean());
            }
        }
    }

    // Геттеры для доступа к внутренним объектам
    public DebugMonitor getMonitor()
    {
        return monitor;
    }

    public DebugEvent getEvent()
    {
        return event;
    }
}
