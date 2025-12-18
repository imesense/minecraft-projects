package org.imesense.dynamicspawncontrol.core.doccompiler;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Log;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *
 */
@InitLog
public final class ChangelogHTMLCompiler
{
    /**
     *
     */
    public ChangelogHTMLCompiler() {}

    /**
     *
     * @param PATH
     * @param isDebugMode
     */
    public static void createChangelogHTML(final String PATH, boolean isDebugMode)
    {
        File docsDir = new File(PATH, DynamicSpawnControlStructure.STRUCT_FILES_DIRS.NAME_DIR_DOC_COMPILE);
        docsDir.mkdirs();

        File htmlFile = new File(docsDir, "changelog_report_1_12_2.html");

        try
        {
            String jsonContent = readChangelogJSON(PATH);
            String htmlContent = generateChangelogHTML(jsonContent);
            writeHTMLFile(htmlFile, htmlContent);

            Log.write(0, "HTML report on the changes has been successfully created: " + htmlFile.getAbsolutePath());
        }
        catch (Exception exception)
        {
            Log.write(2, "Error when creating an HTML report: " + exception.getMessage());
        }
    }

    /**
     *
     * @param PATH
     * @return
     * @throws IOException
     */
    private static String readChangelogJSON(final String PATH) throws IOException
    {
        String resourcePath = "/assets/dynamicspawncontrol/changelog/changelog_1_12_2_0_1.json";

        Log.write(0, String.format(
                "[CHANGELOG] Read file to path: %s", resourcePath));

        try (InputStream inputStream = DynamicSpawnControlStructure.class
                .getResourceAsStream(resourcePath))
        {
            StringBuilder content = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream, StandardCharsets.UTF_8)))
            {
                String line;
                while ((line = reader.readLine()) != null)
                {
                    content.append(line).append("\n");
                }
            }

            return content.toString();
        }
        catch (IOException exception)
        {
            Log.write(2, "Error reading changelog: " + exception.getMessage());
            return exception.toString();
        }
    }

    /**
     *
     * @param jsonContent
     * @return
     */
    private static String generateChangelogHTML(String jsonContent)
    {
        Gson gson = new Gson();
        JsonObject changelog = gson.fromJson(jsonContent, JsonObject.class);

        if (changelog == null)
        {
            return getErrorHTML("Incorrect JSON changelog file");
        }

        StringBuilder html = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        String version = changelog.has("version") ? changelog.get("version").getAsString() : "1.12.2";
        String releaseDate = changelog.has("release_date") ? changelog.get("release_date").getAsString() : currentDate;

        int totalCategories = 0;
        int totalChanges = 0;

        if (changelog.has("categories"))
        {
            JsonArray categories = changelog.getAsJsonArray("categories");
            totalCategories = categories.size();

            for (JsonElement cat : categories)
            {
                JsonObject category = cat.getAsJsonObject();
                if (category.has("items"))
                {
                    totalChanges += category.getAsJsonArray("items").size();
                }
                if (category.has("subcategories"))
                {
                    for (JsonElement sub : category.getAsJsonArray("subcategories"))
                    {
                        totalChanges += sub.getAsJsonObject().getAsJsonArray("items").size();
                    }
                }
            }
        }

        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"ru\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n");
        html.append("    <title>Changelog Minecraft 1.12.2 - Dynamic Spawn Control</title>\n");
        html.append(getCSSStyles());
        html.append("</head>\n");
        html.append("<body>\n");

        html.append("    <div class=\"container\">\n");
        html.append("        <header>\n");
        html.append("            <div class=\"header-content\">\n");
        html.append("                <h1><span class=\"icon\">📋</span> Список изменений</h1>\n");
        html.append("                <h2>Dynamic Spawn Control Mod - Minecraft ").append(version).append("</h2>\n");
        html.append("                <div class=\"release-info\">\n");
        html.append("                    <span class=\"badge version\">Версия ").append(version).append("</span>\n");
        html.append("                    <span class=\"badge date\">").append(releaseDate).append("</span>\n");
        html.append("                </div>\n");
        html.append("            </div>\n");
        html.append("        </header>\n");

        html.append("        <div class=\"stats\">\n");
        html.append("            <div class=\"stat-card\">\n");
        html.append("                <div class=\"stat-number\">").append(totalCategories).append("</div>\n");
        html.append("                <div class=\"stat-label\">Категорий изменений</div>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"stat-card\">\n");
        html.append("                <div class=\"stat-number\">").append(totalChanges).append("</div>\n");
        html.append("                <div class=\"stat-label\">Всего изменений</div>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"stat-card\">\n");
        html.append("                <div class=\"stat-number\">1.12.2</div>\n");
        html.append("                <div class=\"stat-label\">Версия Minecraft</div>\n");
        html.append("            </div>\n");
        html.append("        </div>\n");

        html.append("        <div class=\"changelog-content\">\n");

        if (changelog.has("categories"))
        {
            JsonArray categories = changelog.getAsJsonArray("categories");

            for (JsonElement catElem : categories)
            {
                JsonObject category = catElem.getAsJsonObject();
                String catName = category.get("name").getAsString();
                String catIcon = category.has("icon") ? category.get("icon").getAsString() : "📄";
                String catColor = category.has("color") ? category.get("color").getAsString() : "#667eea";

                html.append("            <div class=\"category-section\">\n");
                html.append("                <div class=\"category-header\" style=\"border-left-color: ").append(catColor).append(";\">\n");
                html.append("                    <span class=\"category-icon\">").append(catIcon).append("</span>\n");
                html.append("                    <h3>").append(catName).append("</h3>\n");
                html.append("                </div>\n");
                html.append("                <div class=\"category-content\">\n");

                if (category.has("items"))
                {
                    JsonArray items = category.getAsJsonArray("items");
                    html.append("                    <ul class=\"change-list\">\n");
                    for (JsonElement item : items)
                    {
                        html.append("                        <li>").append(item.getAsString()).append("</li>\n");
                    }
                    html.append("                    </ul>\n");
                }

                if (category.has("subcategories"))
                {
                    JsonArray subcats = category.getAsJsonArray("subcategories");
                    for (JsonElement subElem : subcats)
                    {
                        JsonObject subcat = subElem.getAsJsonObject();
                        String subName = subcat.get("name").getAsString();

                        html.append("                    <div class=\"subcategory\">\n");
                        html.append("                        <h4>").append(subName).append("</h4>\n");

                        if (subcat.has("items"))
                        {
                            JsonArray items = subcat.getAsJsonArray("items");
                            html.append("                        <ul class=\"change-list\">\n");
                            for (JsonElement item : items)
                            {
                                html.append("                            <li>").append(item.getAsString()).append("</li>\n");
                            }
                            html.append("                        </ul>\n");
                        }
                        html.append("                    </div>\n");
                    }
                }

                html.append("                </div>\n");
                html.append("            </div>\n");
            }
        }

        html.append("        </div>\n");

        html.append("        <div class=\"footer\">\n");
        html.append("            <p>Отчет сгенерирован: ").append(currentDate).append("</p>\n");
        html.append("            <p>Minecraft 1.12.2 | Dynamic Spawn Control Mod</p>\n");
        html.append("        </div>\n");
        html.append("    </div>\n");
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }

    /**
     *
     * @return
     */
    private static String getCSSStyles()
    {
        StringBuilder css = new StringBuilder();
        css.append("    <style>\n");
        css.append("        * { margin: 0; padding: 0; box-sizing: border-box; }\n");
        css.append("        body {\n");
        css.append("            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n");
        css.append("            line-height: 1.6;\n");
        css.append("            color: #333;\n");
        css.append("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n");
        css.append("            padding: 20px;\n");
        css.append("            min-height: 100vh;\n");
        css.append("        }\n");
        css.append("        .container {\n");
        css.append("            max-width: 1200px;\n");
        css.append("            margin: 0 auto;\n");
        css.append("            background: white;\n");
        css.append("            border-radius: 15px;\n");
        css.append("            box-shadow: 0 10px 40px rgba(0,0,0,0.2);\n");
        css.append("            overflow: hidden;\n");
        css.append("        }\n");
        css.append("        header {\n");
        css.append("            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);\n");
        css.append("            color: white;\n");
        css.append("            padding: 40px 30px;\n");
        css.append("            text-align: center;\n");
        css.append("        }\n");
        css.append("        .header-content h1 {\n");
        css.append("            font-size: 2.8rem;\n");
        css.append("            margin-bottom: 10px;\n");
        css.append("            display: flex;\n");
        css.append("            align-items: center;\n");
        css.append("            justify-content: center;\n");
        css.append("            gap: 15px;\n");
        css.append("        }\n");
        css.append("        .header-content h2 {\n");
        css.append("            font-size: 1.4rem;\n");
        css.append("            opacity: 0.9;\n");
        css.append("            font-weight: 400;\n");
        css.append("            margin-bottom: 20px;\n");
        css.append("        }\n");
        css.append("        .release-info {\n");
        css.append("            display: flex;\n");
        css.append("            justify-content: center;\n");
        css.append("            gap: 15px;\n");
        css.append("            margin-top: 15px;\n");
        css.append("        }\n");
        css.append("        .badge {\n");
        css.append("            padding: 8px 16px;\n");
        css.append("            border-radius: 20px;\n");
        css.append("            font-size: 0.9rem;\n");
        css.append("            font-weight: 600;\n");
        css.append("            text-transform: uppercase;\n");
        css.append("            letter-spacing: 1px;\n");
        css.append("        }\n");
        css.append("        .badge.version {\n");
        css.append("            background: rgba(255,255,255,0.2);\n");
        css.append("            backdrop-filter: blur(10px);\n");
        css.append("        }\n");
        css.append("        .badge.date {\n");
        css.append("            background: rgba(255,255,255,0.1);\n");
        css.append("        }\n");
        css.append("        .stats {\n");
        css.append("            display: grid;\n");
        css.append("            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));\n");
        css.append("            gap: 20px;\n");
        css.append("            padding: 30px;\n");
        css.append("            background: #f8f9fa;\n");
        css.append("        }\n");
        css.append("        .stat-card {\n");
        css.append("            background: white;\n");
        css.append("            padding: 25px;\n");
        css.append("            border-radius: 12px;\n");
        css.append("            box-shadow: 0 4px 12px rgba(0,0,0,0.1);\n");
        css.append("            text-align: center;\n");
        css.append("            transition: transform 0.3s ease;\n");
        css.append("        }\n");
        css.append("        .stat-card:hover {\n");
        css.append("            transform: translateY(-5px);\n");
        css.append("        }\n");
        css.append("        .stat-number {\n");
        css.append("            font-size: 2.8rem;\n");
        css.append("            font-weight: 800;\n");
        css.append("            color: #667eea;\n");
        css.append("            margin-bottom: 10px;\n");
        css.append("        }\n");
        css.append("        .stat-label {\n");
        css.append("            color: #6c757d;\n");
        css.append("            font-size: 0.9rem;\n");
        css.append("            text-transform: uppercase;\n");
        css.append("            letter-spacing: 1.5px;\n");
        css.append("        }\n");
        css.append("        .changelog-content {\n");
        css.append("            padding: 30px;\n");
        css.append("        }\n");
        css.append("        .category-section {\n");
        css.append("            margin-bottom: 40px;\n");
        css.append("            background: white;\n");
        css.append("            border-radius: 12px;\n");
        css.append("            overflow: hidden;\n");
        css.append("            box-shadow: 0 5px 15px rgba(0,0,0,0.05);\n");
        css.append("        }\n");
        css.append("        .category-header {\n");
        css.append("            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);\n");
        css.append("            padding: 20px 25px;\n");
        css.append("            border-left: 6px solid #667eea;\n");
        css.append("            display: flex;\n");
        css.append("            align-items: center;\n");
        css.append("            gap: 15px;\n");
        css.append("        }\n");
        css.append("        .category-icon {\n");
        css.append("            font-size: 1.8rem;\n");
        css.append("        }\n");
        css.append("        .category-header h3 {\n");
        css.append("            font-size: 1.5rem;\n");
        css.append("            color: #495057;\n");
        css.append("            margin: 0;\n");
        css.append("        }\n");
        css.append("        .category-content {\n");
        css.append("            padding: 25px;\n");
        css.append("        }\n");
        css.append("        .subcategory {\n");
        css.append("            margin: 20px 0;\n");
        css.append("            padding: 15px;\n");
        css.append("            background: #f8f9fa;\n");
        css.append("            border-radius: 8px;\n");
        css.append("        }\n");
        css.append("        .subcategory h4 {\n");
        css.append("            color: #495057;\n");
        css.append("            margin-bottom: 10px;\n");
        css.append("            padding-bottom: 8px;\n");
        css.append("            border-bottom: 2px solid #dee2e6;\n");
        css.append("        }\n");
        css.append("        .change-list {\n");
        css.append("            list-style-type: none;\n");
        css.append("            padding-left: 0;\n");
        css.append("        }\n");
        css.append("        .change-list li {\n");
        css.append("            padding: 12px 15px;\n");
        css.append("            margin: 8px 0;\n");
        css.append("            background: white;\n");
        css.append("            border-radius: 8px;\n");
        css.append("            border-left: 4px solid #28a745;\n");
        css.append("            box-shadow: 0 2px 5px rgba(0,0,0,0.05);\n");
        css.append("            transition: all 0.3s ease;\n");
        css.append("        }\n");
        css.append("        .change-list li:hover {\n");
        css.append("            background: #f8f9fa;\n");
        css.append("            transform: translateX(5px);\n");
        css.append("        }\n");
        css.append("        .footer {\n");
        css.append("            text-align: center;\n");
        css.append("            padding: 25px;\n");
        css.append("            background: #f8f9fa;\n");
        css.append("            color: #6c757d;\n");
        css.append("            border-top: 1px solid #dee2e6;\n");
        css.append("        }\n");
        css.append("        .footer p {\n");
        css.append("            margin: 5px 0;\n");
        css.append("            font-size: 0.9rem;\n");
        css.append("        }\n");
        css.append("        @media (max-width: 768px) {\n");
        css.append("            .header-content h1 {\n");
        css.append("                font-size: 2rem;\n");
        css.append("            }\n");
        css.append("            .stats {\n");
        css.append("                grid-template-columns: 1fr;\n");
        css.append("            }\n");
        css.append("            .category-content {\n");
        css.append("                padding: 15px;\n");
        css.append("            }\n");
        css.append("        }\n");
        css.append("    </style>\n");

        return css.toString();
    }

    /**
     *
     * @param message
     * @return
     */
    private static String getErrorHTML(String message)
    {
        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html>\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Ошибка</title>\n");
        html.append("    <style>\n");
        html.append("        body {\n");
        html.append("            font-family: sans-serif;\n");
        html.append("            padding: 50px;\n");
        html.append("            text-align: center;\n");
        html.append("            color: #721c24;\n");
        html.append("            background: #f8d7da;\n");
        html.append("        }\n");
        html.append("        h1 { margin-bottom: 20px; }\n");
        html.append("    </style>\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <h1>⚠️ Ошибка генерации отчета</h1>\n");
        html.append("    <p>").append(message).append("</p>\n");
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }

    /**
     *
     * @param htmlFile
     * @param content
     * @throws IOException
     */
    private static void writeHTMLFile(File htmlFile, String content) throws IOException
    {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(htmlFile), StandardCharsets.UTF_8)))
        {
            writer.write(content);
        }
    }
}