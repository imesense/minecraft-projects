package org.imesense.dynamicspawncontrol.core.doccompiler;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
@InitLog
public final class DocJSONToHTMLCompiler
{
    /**
     *
     */
    public DocJSONToHTMLCompiler()
    {

    }

    /**
     *
     * @param PATH
     * @param isDebugMode
     */
    public static void createHTMLFile(final String PATH, boolean isDebugMode)
    {
        File logsDir = new File(PATH, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_DOC_COMPILE);

        logsDir.mkdirs();

        File htmlFile = new File(logsDir, "entity_cache_settings_report.html");

        try
        {
            String jsonContent = readJSONFile(PATH);

            String htmlContent = generateHTMLFromJSON(jsonContent);

            writeHTMLFile(htmlFile, htmlContent);

            Log.write(0, "HTML отчет успешно создан: " + htmlFile.getAbsolutePath());

        }
        catch (Exception exception)
        {
            Log.write(2, "Ошибка при создании HTML отчета: " + exception.getMessage());
        }
    }

    /**
     *
     * @param PATH
     * @return
     * @throws IOException
     */
    private static String readJSONFile(final String PATH) throws IOException
    {
        File jsonFile = new File(PATH,
                DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_CACHE +
                        File.separator +
                        "parser_event_cache_settings.json");

        if (!jsonFile.exists())
        {
            throw new FileNotFoundException("JSON файл не найден: " + jsonFile.getAbsolutePath());
        }

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = new BufferedReader(new FileReader(jsonFile)))
        {
            String line;

            while ((line = reader.readLine()) != null)
            {
                content.append(line).append("\n");
            }
        }

        return content.toString();
    }

    /**
     *
     * @param jsonContent
     * @return
     */
    private static String generateHTMLFromJSON(String jsonContent)
    {
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonContent, JsonArray.class);

        if (jsonArray == null)
        {
            return "<html><body><h1>Ошибка: Некорректный JSON файл</h1></body></html>";
        }

        StringBuilder html = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        int totalEntities = 0;
        int continueCount = 0;
        int denyCount = 0;
        int defaultCount = 0;

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"ru\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Отчет по настройкам кэша сущностей</title>\n");
        html.append("    <style>\n");
        html.append("        * { margin: 0; padding: 0; box-sizing: border-box; }\n");
        html.append("        body {\n");
        html.append("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n");
        html.append("            line-height: 1.6;\n");
        html.append("            color: #333;\n");
        html.append("            background-color: #f5f5f5;\n");
        html.append("            padding: 20px;\n");
        html.append("        }\n");
        html.append("        .container {\n");
        html.append("            max-width: 1200px;\n");
        html.append("            margin: 0 auto;\n");
        html.append("            background: white;\n");
        html.append("            border-radius: 10px;\n");
        html.append("            box-shadow: 0 2px 20px rgba(0,0,0,0.1);\n");
        html.append("            overflow: hidden;\n");
        html.append("        }\n");
        html.append("        header {\n");
        html.append("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n");
        html.append("            color: white;\n");
        html.append("            padding: 30px;\n");
        html.append("            text-align: center;\n");
        html.append("        }\n");
        html.append("        header h1 {\n");
        html.append("            font-size: 2.5rem;\n");
        html.append("            margin-bottom: 10px;\n");
        html.append("        }\n");
        html.append("        .subtitle {\n");
        html.append("            font-size: 1.1rem;\n");
        html.append("            opacity: 0.9;\n");
        html.append("        }\n");
        html.append("        .stats {\n");
        html.append("            display: grid;\n");
        html.append("            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));\n");
        html.append("            gap: 20px;\n");
        html.append("            padding: 30px;\n");
        html.append("            background: #f8f9fa;\n");
        html.append("            border-bottom: 1px solid #e9ecef;\n");
        html.append("        }\n");
        html.append("        .stat-card {\n");
        html.append("            background: white;\n");
        html.append("            padding: 20px;\n");
        html.append("            border-radius: 8px;\n");
        html.append("            box-shadow: 0 1px 3px rgba(0,0,0,0.1);\n");
        html.append("            text-align: center;\n");
        html.append("        }\n");
        html.append("        .stat-number {\n");
        html.append("            font-size: 2.5rem;\n");
        html.append("            font-weight: bold;\n");
        html.append("            color: #667eea;\n");
        html.append("            margin-bottom: 5px;\n");
        html.append("        }\n");
        html.append("        .stat-label {\n");
        html.append("            color: #6c757d;\n");
        html.append("            font-size: 0.9rem;\n");
        html.append("            text-transform: uppercase;\n");
        html.append("            letter-spacing: 1px;\n");
        html.append("        }\n");
        html.append("        .entity-table {\n");
        html.append("            padding: 30px;\n");
        html.append("        }\n");
        html.append("        table {\n");
        html.append("            width: 100%;\n");
        html.append("            border-collapse: collapse;\n");
        html.append("            margin-top: 20px;\n");
        html.append("        }\n");
        html.append("        th {\n");
        html.append("            background: #667eea;\n");
        html.append("            color: white;\n");
        html.append("            padding: 15px;\n");
        html.append("            text-align: left;\n");
        html.append("            font-weight: 600;\n");
        html.append("            border: none;\n");
        html.append("        }\n");
        html.append("        td {\n");
        html.append("            padding: 15px;\n");
        html.append("            border-bottom: 1px solid #e9ecef;\n");
        html.append("            vertical-align: top;\n");
        html.append("        }\n");
        html.append("        tr:hover {\n");
        html.append("            background-color: #f8f9fa;\n");
        html.append("        }\n");
        html.append("        .entity-name {\n");
        html.append("            font-weight: 600;\n");
        html.append("            color: #495057;\n");
        html.append("        }\n");
        html.append("        .result-deny {\n");
        html.append("            color: #dc3545;\n");
        html.append("            font-weight: 600;\n");
        html.append("            background: #f8d7da;\n");
        html.append("            padding: 5px 10px;\n");
        html.append("            border-radius: 4px;\n");
        html.append("            display: inline-block;\n");
        html.append("        }\n");
        html.append("        .result-default {\n");
        html.append("            color: #28a745;\n");
        html.append("            font-weight: 600;\n");
        html.append("            background: #d4edda;\n");
        html.append("            padding: 5px 10px;\n");
        html.append("            border-radius: 4px;\n");
        html.append("            display: inline-block;\n");
        html.append("        }\n");
        html.append("        .continue-mode {\n");
        html.append("            color: #ffc107;\n");
        html.append("            font-weight: 600;\n");
        html.append("            background: #fff3cd;\n");
        html.append("            padding: 5px 10px;\n");
        html.append("            border-radius: 4px;\n");
        html.append("            display: inline-block;\n");
        html.append("        }\n");
        html.append("        .param-badge {\n");
        html.append("            display: inline-block;\n");
        html.append("            padding: 3px 8px;\n");
        html.append("            margin: 2px;\n");
        html.append("            border-radius: 12px;\n");
        html.append("            font-size: 0.85rem;\n");
        html.append("            background: #e9ecef;\n");
        html.append("            color: #495057;\n");
        html.append("        }\n");
        html.append("        .param-true {\n");
        html.append("            background: #d4edda;\n");
        html.append("            color: #155724;\n");
        html.append("        }\n");
        html.append("        .param-false {\n");
        html.append("            background: #f8d7da;\n");
        html.append("            color: #721c24;\n");
        html.append("        }\n");
        html.append("        .timestamp {\n");
        html.append("            text-align: center;\n");
        html.append("            padding: 20px;\n");
        html.append("            color: #6c757d;\n");
        html.append("            font-size: 0.9rem;\n");
        html.append("            border-top: 1px solid #e9ecef;\n");
        html.append("        }\n");
        html.append("        .category {\n");
        html.append("            font-size: 1.2rem;\n");
        html.append("            font-weight: 600;\n");
        html.append("            color: #495057;\n");
        html.append("            margin: 30px 0 15px 0;\n");
        html.append("            padding-bottom: 10px;\n");
        html.append("            border-bottom: 2px solid #667eea;\n");
        html.append("        }\n");
        html.append("        .note {\n");
        html.append("            background: #e7f3ff;\n");
        html.append("            padding: 15px;\n");
        html.append("            border-radius: 8px;\n");
        html.append("            margin: 20px 0;\n");
        html.append("            border-left: 4px solid #007bff;\n");
        html.append("        }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <div class=\"container\">\n");
        html.append("        <header>\n");
        html.append("            <h1>Настройки кэша сущностей Minecraft 1.12.2</h1>\n");
        html.append("            <div class=\"subtitle\">Dynamic Spawn Control - Отчет по конфигурации</div>\n");
        html.append("        </header>\n");
        html.append("        \n");
        html.append("        <div class=\"stats\">\n");

        Map<String, List<JsonObject>> categorizedEntities = new LinkedHashMap<>();

        categorizedEntities.put("Hostile Mobs", new ArrayList<>());
        categorizedEntities.put("Passive Mobs", new ArrayList<>());
        categorizedEntities.put("Neutral Mobs", new ArrayList<>());
        categorizedEntities.put("Boss Mobs", new ArrayList<>());
        categorizedEntities.put("Divine RPG", new ArrayList<>());
        categorizedEntities.put("Dynamic Spawn Control", new ArrayList<>());
        categorizedEntities.put("Scape and Run Parasites", new ArrayList<>());
        categorizedEntities.put("Other", new ArrayList<>());

        for (JsonElement element : jsonArray)
        {
            totalEntities++;

            JsonObject entityObj = element.getAsJsonObject();
            JsonObject data = entityObj.getAsJsonObject("data");

            String result = data.get("result").getAsString();

            if ("deny".equals(result))
            {
                denyCount++;
            }

            if ("default".equals(result))
            {
                defaultCount++;
            }

            boolean isContinue = data.has("continue") && data.get("continue").getAsBoolean();

            if (isContinue)
            {
                continueCount++;
            }

            String entityName = data.has("entity") ? data.get("entity").getAsString() : "";
            String category = categorizeEntity(entityName);

            categorizedEntities.get(category).add(entityObj);
        }

        html.append("            <div class=\"stat-card\">\n");
        html.append("                <div class=\"stat-number\">").append(totalEntities).append("</div>\n");
        html.append("                <div class=\"stat-label\">Всего сущностей</div>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"stat-card\">\n");
        html.append("                <div class=\"stat-number\">").append(denyCount).append("</div>\n");
        html.append("                <div class=\"stat-label\">Результат DENY</div>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"stat-card\">\n");
        html.append("                <div class=\"stat-number\">").append(defaultCount).append("</div>\n");
        html.append("                <div class=\"stat-label\">Результат DEFAULT</div>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"stat-card\">\n");
        html.append("                <div class=\"stat-number\">").append(continueCount).append("</div>\n");
        html.append("                <div class=\"stat-label\">Режим CONTINUE</div>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");
        html.append("        \n");
        html.append("        <div class=\"note\">\n");
        html.append("            <strong>Примечание:</strong> Сущности в режиме CONTINUE пропускают проверки per_player, per_chunk и max_entity_count.\n");
        html.append("            Результат применяется сразу.\n");
        html.append("        </div>\n");

        html.append("        <div class=\"entity-table\">\n");

        for (Map.Entry<String, List<JsonObject>> category : categorizedEntities.entrySet())
        {
            if (!category.getValue().isEmpty())
            {
                html.append("            <div class=\"category\">").append(category.getKey()).append("</div>\n");

                html.append("            <table>\n");
                html.append("                <thead>\n");
                html.append("                    <tr>\n");
                html.append("                        <th>Сущность</th>\n");
                html.append("                        <th>Параметры</th>\n");
                html.append("                        <th>Лимит</th>\n");
                html.append("                        <th>Результат</th>\n");
                html.append("                        <th>Режим</th>\n");
                html.append("                    </tr>\n");
                html.append("                </thead>\n");
                html.append("                <tbody>\n");

                for (JsonObject entityObj : category.getValue())
                {
                    JsonObject data = entityObj.getAsJsonObject("data");

                    String entityName = data.has("entity") ? data.get("entity").getAsString() : "instanceof";
                    boolean isContinue = data.has("continue") && data.get("continue").getAsBoolean();

                    String result = data.get("result").getAsString();

                    html.append("                <tr>\n");
                    html.append("                    <td>\n");
                    html.append("                        <div class=\"entity-name\">").append(formatEntityName(entityName)).append("</div>\n");
                    html.append("                    </td>\n");
                    html.append("                    <td>\n");

                    if (!isContinue)
                    {
                        boolean perPlayer = data.has("per_player") && data.get("per_player").getAsBoolean();
                        boolean perChunk = data.has("per_chunk") && data.get("per_chunk").getAsBoolean();

                        html.append("                        <span class=\"param-badge ").append(perPlayer ? "param-true" : "param-false").append("\">\n");
                        html.append("                            per_player: ").append(perPlayer ? "✓" : "✗").append("\n");
                        html.append("                        </span>\n");
                        html.append("                        <span class=\"param-badge ").append(perChunk ? "param-true" : "param-false").append("\">\n");
                        html.append("                            per_chunk: ").append(perChunk ? "✓" : "✗").append("\n");
                        html.append("                        </span>\n");
                    }
                    else
                    {
                        html.append("                        <span class=\"param-badge\">Параметры пропущены</span>\n");
                    }

                    html.append("                    </td>\n");
                    html.append("                    <td>\n");

                    if (!isContinue && data.has("max_entity_count"))
                    {
                        int maxCount = data.get("max_entity_count").getAsInt();
                        html.append("                        <span class=\"param-badge\" style=\"background:#cfe2ff;color:#084298;\">\n");
                        html.append("                            Макс: ").append(maxCount).append("\n");
                        html.append("                        </span>\n");
                    }
                    else if (isContinue)
                    {
                        html.append("                        <span class=\"param-badge\" style=\"background:#fff3cd;color:#856404;\">\n");
                        html.append("                            Нет лимита\n");
                        html.append("                        </span>\n");
                    }

                    html.append("                    </td>\n");
                    html.append("                    <td>\n");

                    if ("deny".equals(result))
                    {
                        html.append("                        <span class=\"result-deny\">DENY</span>\n");
                    }
                    else
                    {
                        html.append("                        <span class=\"result-default\">DEFAULT</span>\n");
                    }

                    html.append("                    </td>\n");
                    html.append("                    <td>\n");

                    if (isContinue)
                    {
                        html.append("                        <span class=\"continue-mode\">CONTINUE</span>\n");
                    }
                    else
                    {
                        html.append("                        <span class=\"param-badge\">STANDARD</span>\n");
                    }

                    html.append("                    </td>\n");
                    html.append("                </tr>\n");
                }

                html.append("                </tbody>\n");
                html.append("            </table>\n");
            }
        }

        html.append("        </div>\n");
        html.append("        \n");
        html.append("        <div class=\"timestamp\">\n");
        html.append("            Отчет сгенерирован: ").append(currentDate).append("\n");
        html.append("            <br>\n");
        html.append("            Minecraft 1.12.2 | Dynamic Spawn Control Mod\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }

    /**
     *
     * @param entityName
     * @return
     */
    private static String categorizeEntity(String entityName)
    {
        if (entityName.startsWith("dynamicspawncontrol:"))
        {
            return "Dynamic Spawn Control";
        }
        else if (entityName.startsWith("divinerpg:"))
        {
            return "Divine RPG";
        }
        else if (entityName.startsWith("srparasites:"))
        {
            return "Scape and Run Parasites";
        }
        else if (entityName.contains(":"))
        {
            if (!entityName.startsWith("minecraft:"))
            {
                return "Custom Mod Mobs";
            }
        }

        if (entityName.contains("zombie") || entityName.contains("skeleton") || entityName.contains("creeper") ||
                entityName.contains("spider") || entityName.contains("witch") || entityName.contains("ghast") ||
                entityName.contains("blaze") || entityName.contains("guardian") || entityName.contains("shulker") ||
                entityName.contains("vex") || entityName.contains("evocation") || entityName.contains("vindication") ||
                entityName.contains("husk") || entityName.contains("stray") || entityName.contains("wither_skeleton") ||
                entityName.contains("silverfish") || entityName.contains("endermite") || entityName.contains("magma_cube") ||
                entityName.contains("slime")) {
            return "Hostile Mobs";
        }
        else if (entityName.contains("cow") || entityName.contains("pig") || entityName.contains("chicken") ||
                entityName.contains("sheep") || entityName.contains("rabbit") || entityName.contains("horse") ||
                entityName.contains("donkey") || entityName.contains("mule") || entityName.contains("mooshroom") ||
                entityName.contains("llama") || entityName.contains("parrot") || entityName.contains("bat") ||
                entityName.contains("squid"))
        {
            return "Passive Mobs";
        }
        else if (entityName.contains("wolf") || entityName.contains("polar_bear") || entityName.contains("enderman") ||
                entityName.contains("zombie_pigman") || entityName.contains("ocelot") || entityName.contains("villager_golem") ||
                entityName.contains("villager"))
        {
            return "Neutral Mobs";
        }
        else if (entityName.contains("elder_guardian") || entityName.contains("ender_dragon") ||
                entityName.contains("wither"))
        {
            return "Boss Mobs";
        }
        else
        {
            return "Other";
        }
    }

    /**
     *
     * @param entityName
     * @return
     */
    private static String formatEntityName(String entityName)
    {
        if (entityName.startsWith("minecraft:"))
        {
            entityName = entityName.substring(10);
        }

        String[] words = entityName.split("[_:]");
        StringBuilder formatted = new StringBuilder();

        for (String word : words)
        {
            if (!word.isEmpty())
            {
                formatted.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return formatted.toString().trim();
    }

    /**
     *
     * @param htmlFile
     * @param content
     * @throws IOException
     */
    private static void writeHTMLFile(File htmlFile, String content) throws IOException
    {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(htmlFile)))
        {
            writer.write(content);
        }
    }
}