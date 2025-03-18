package org.imesense.dynamicspawncontrol.core.config.mainwindow;

import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.ConceptConfig;
import org.imesense.dynamicspawncontrol.core.baseconfig.BaseJsonConfig;
import org.imesense.dynamicspawncontrol.core.util.CodeGeneric;

@Getter
@Setter
@ConceptConfig(fileName = "cfg_main_window_title")
public final class MainWindowTitleConfig extends BaseJsonConfig
{
    private String windowTitle =
            String.format("Minecraft: %s + %s",
                    DynamicSpawnControlStructure.STRUCT_INFO_MOD.VERSION,
                    DynamicSpawnControlStructure.STRUCT_INFO_MOD.NAME);

    public MainWindowTitleConfig(String configPath)
    {
        super(configPath, true);

        CodeGeneric.printInitClassToLog(this.getClass());

        loadOrCreateConfig();
    }

    @Override
    protected JsonObject createDefaultConfig()
    {
        JsonObject config = new JsonObject();

        config.addProperty("main_window_title", windowTitle);

        return config;
    }

    @Override
    protected void applyConfig(JsonObject jsonObject)
    {
        if (jsonObject.has("main_window_title"))
        {
            windowTitle = jsonObject.get("main_window_title").getAsString();
        }
    }
}
