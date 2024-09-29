package org.imesense.dynamicspawncontrol.technical.configs;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import org.imesense.dynamicspawncontrol.DynamicSpawnControl;
import org.imesense.dynamicspawncontrol.technical.customlibrary.Log;

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

    public CustomConceptConfig(String nameConfigFile)
    {
        Log.writeDataToLogFile(0, "nameConfigFile: " + nameConfigFile);

        this.nameConfig = this.constructPathToDirectory() + nameConfigFile;

        Log.writeDataToLogFile(0, "this.nameConfig: " + this.nameConfig);
    }
}
