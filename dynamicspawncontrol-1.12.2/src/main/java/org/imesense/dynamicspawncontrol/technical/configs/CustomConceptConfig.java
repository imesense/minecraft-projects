package org.imesense.dynamicspawncontrol.technical.configs;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import javax.annotation.Nonnull;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public abstract class CustomConceptConfig
{
    protected String nameConfig;

    public CustomConceptConfig(String nameConfigFile)
    {
        this.nameConfig = nameConfigFile;
    }
}
