package org.imesense.dynamicspawncontrol.technical.configs;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomConceptConfig
{
    protected String nameConfig;

    protected String constructPathToDirectory()
    {
        return DynamicSpawnControl.getGlobalPathToConfigs().getPath() + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIRECTORY + File.separator +
                DynamicSpawnControl.STRUCT_FILES_DIRS.NAME_DIR_CONFIGS + File.separator;
    }

    public CustomConceptConfig(String nameConfigFile) throws IOException
    {
        this.nameConfig = this.constructPathToDirectory() + nameConfigFile;

        JsonObject jsonObject = new JsonObject();

        Gson gson = new Gson();

        FileWriter file = new FileWriter(this.nameConfig);

        gson.toJson(jsonObject, file);

        file.close();
    }
}
