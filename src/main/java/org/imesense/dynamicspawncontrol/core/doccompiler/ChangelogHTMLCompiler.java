package org.imesense.dynamicspawncontrol.core.doccompiler;

import com.google.gson.*;
import org.imesense.dynamicspawncontrol.DynamicSpawnControlStructure;
import org.imesense.dynamicspawncontrol.core.annotation.InitLog;
import org.imesense.dynamicspawncontrol.core.logfile.Logger;

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

            Logger.info("HTML отчет об изменениях успешно создан: " + htmlFile.getAbsolutePath());
        }
        catch (Exception exception)
        {
            Logger.error("Ошибка при создании HTML отчета: " + exception.getMessage());
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

        Logger.info(String.format(
                "[CHANGELOG] Чтение файла по пути: %s", resourcePath));

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
            Logger.error("Ошибка чтения changelog: " + exception.getMessage());
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
            return getErrorHTML("Некорректный JSON файл changelog");
        }

        StringBuilder html = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
        String currentDate = dateFormat.format(new Date());

        String version = changelog.has("version") ? changelog.get("version").getAsString() : "1.12.2";
        String releaseDate = changelog.has("release_date") ? changelog.get("release_date").getAsString() : currentDate;

        int totalCategories = 0;
        int totalChanges = 0;
        int totalWorking = 0;
        int totalInDevelopment = 0;
        int totalDisabled = 0;

        if (changelog.has("categories"))
        {
            JsonArray categories = changelog.getAsJsonArray("categories");
            totalCategories = categories.size();

            for (JsonElement cat : categories)
            {
                JsonObject category = cat.getAsJsonObject();
                if (category.has("items"))
                {
                    JsonArray items = category.getAsJsonArray("items");
                    totalChanges += items.size();
                    for (JsonElement item : items)
                    {
                        String itemText = item.getAsString();
                        if (itemText.endsWith("[работает]"))
                        {
                            totalWorking++;
                        }
                        else if (itemText.endsWith("[в разработке]"))
                        {
                            totalInDevelopment++;
                        }
                        else if (itemText.endsWith("[отключено]"))
                        {
                            totalDisabled++;
                        }
                    }
                }
                if (category.has("subcategories"))
                {
                    for (JsonElement sub : category.getAsJsonArray("subcategories"))
                    {
                        JsonArray items = sub.getAsJsonObject().getAsJsonArray("items");
                        totalChanges += items.size();
                        for (JsonElement item : items)
                        {
                            String itemText = item.getAsString();
                            if (itemText.endsWith("[работает]"))
                            {
                                totalWorking++;
                            }
                            else if (itemText.endsWith("[в разработке]"))
                            {
                                totalInDevelopment++;
                            }
                            else if (itemText.endsWith("[отключено]"))
                            {
                                totalDisabled++;
                            }
                        }
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

        html.append("        <div class=\"status-stats\">\n");
        html.append("            <div class=\"status-item working\">\n");
        html.append("                <span class=\"status-icon\">✅</span>\n");
        html.append("                <span class=\"status-count\">").append(totalWorking).append("</span>\n");
        html.append("                <span class=\"status-label\">Работает</span>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"status-item development\">\n");
        html.append("                <span class=\"status-icon\">🔄</span>\n");
        html.append("                <span class=\"status-count\">").append(totalInDevelopment).append("</span>\n");
        html.append("                <span class=\"status-label\">В разработке</span>\n");
        html.append("            </div>\n");
        html.append("            <div class=\"status-item disabled\">\n");
        html.append("                <span class=\"status-icon\">⏸️</span>\n");
        html.append("                <span class=\"status-count\">").append(totalDisabled).append("</span>\n");
        html.append("                <span class=\"status-label\">Отключено</span>\n");
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
                        String itemText = item.getAsString().trim();

                        if (!itemText.isEmpty())
                        {
                            html.append("                        ").append(parseChangeItem(itemText)).append("\n");
                        }
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

                            boolean isContentSection = false;
                            for (JsonElement item : items)
                            {
                                String itemText = item.getAsString().trim();
                                if (itemText.isEmpty() || itemText.contains("\n"))
                                {
                                    isContentSection = true;
                                    break;
                                }
                            }

                            if (isContentSection)
                            {
                                html.append(parseContentSection(items));
                            }
                            else
                            {
                                html.append("                        <ul class=\"change-list\">\n");
                                for (JsonElement item : items)
                                {
                                    String itemText = item.getAsString().trim();
                                    if (!itemText.isEmpty())
                                    {
                                        html.append("                            ").append(parseChangeItem(itemText)).append("\n");
                                    }
                                }
                                html.append("                        </ul>\n");
                            }
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
     * @param itemText
     * @return
     */
    private static String parseChangeItem(String itemText)
    {
        if (itemText == null || itemText.trim().isEmpty())
        {
            return "<li class=\"empty-item\"></li>";
        }

        String status = "";
        String worldType = "";
        String text = itemText.trim();

        List<String> tags = new ArrayList<>();

        if (itemText.contains("[работает]"))
        {
            status = "working";
            text = text.replace("[работает]", "").trim();
        }
        else if (itemText.contains("[в разработке]"))
        {
            status = "development";
            text = text.replace("[в разработке]", "").trim();
        }
        else if (itemText.contains("[отключено]"))
        {
            status = "disabled";
            text = text.replace("[отключено]", "").trim();
        }

        if (text.contains("[только основной мир]"))
        {
            worldType = "overworld";
            text = text.replace("[только основной мир]", "").trim();
        }
        else if (text.contains("[только ад]"))
        {
            worldType = "nether";
            text = text.replace("[только ад]", "").trim();
        }
        else if (text.contains("[только энд]"))
        {
            worldType = "end";
            text = text.replace("[только энд]", "").trim();
        }

        extractCommandTags(text, tags);

        text = removeCommandTags(text);

        StringBuilder li = new StringBuilder();

        li.append("<li class=\"change-item");

        if (!status.isEmpty())
        {
            li.append(" status-").append(status);
        }

        li.append("\">");

        li.append("<div class=\"item-content\">");

        li.append("<div class=\"item-header\">");

        if (!status.isEmpty())
        {
            li.append("<span class=\"item-status\">");
            switch (status)
            {
                case "working":
                    li.append("✅");
                    break;
                case "development":
                    li.append("🔄");
                    break;
                case "disabled":
                    li.append("⏸️");
                    break;
            }
            li.append("</span>");
        }

        li.append("<span class=\"item-text\">").append(text).append("</span>");

        li.append("</div>");

        if (!worldType.isEmpty() || !status.isEmpty() || !tags.isEmpty())
        {
            li.append("<div class=\"item-tags\">");

            if (!worldType.isEmpty())
            {
                li.append("<span class=\"world-type-badge ").append(worldType).append("\">");
                switch (worldType)
                {
                    case "overworld":
                        li.append("🌍 Основной мир");
                        break;
                    case "nether":
                        li.append("🔥 Ад");
                        break;
                    case "end":
                        li.append("✨ Энд");
                        break;
                }
                li.append("</span>");
            }

            for (String tag : tags)
            {
                li.append("<span class=\"command-tag ").append(getCommandTagClass(tag)).append("\">");
                li.append(getCommandTagIcon(tag)).append(" ").append(getCommandTagText(tag));
                li.append("</span>");
            }

            if (!status.isEmpty())
            {
                li.append("<span class=\"status-badge\">");
                switch (status)
                {
                    case "working":
                        li.append("работает");
                        break;
                    case "development":
                        li.append("в разработке");
                        break;
                    case "disabled":
                        li.append("отключено");
                        break;
                }
                li.append("</span>");
            }

            li.append("</div>");
        }

        li.append("</div>");

        li.append("</li>");
        return li.toString();
    }

    /**
     *
     * @param items
     * @return
     */
    private static String parseContentSection(JsonArray items)
    {
        StringBuilder html = new StringBuilder();
        html.append("                        <div class=\"content-section\">\n");

        for (JsonElement item : items)
        {
            String itemText = item.getAsString().trim();

            if (itemText.isEmpty())
            {
                continue;
            }

            if (itemText.startsWith("## "))
            {
                String title = itemText.substring(3).trim();
                html.append("                            <h4 class=\"content-title\">").append(title).append("</h4>\n");
            }
            else if (!itemText.startsWith("[") && !itemText.endsWith("]"))
            {
                itemText = itemText.replace("Красные черепа", "<span class='skull red'>🔴 Красные черепа</span>");
                itemText = itemText.replace("Оранжевые черепа", "<span class='skull orange'>🟠 Оранжевые черепа</span>");

                itemText = itemText.replace("**", "<strong>").replace("**", "</strong>");

                html.append("                            <p class=\"content-text\">").append(itemText).append("</p>\n");
            }
            else if (itemText.startsWith("- "))
            {
                String listItem = itemText.substring(2).trim();
                listItem = listItem.replace("Красные черепа", "<span class='skull red'>🔴 Красные черепа</span>");
                listItem = listItem.replace("Оранжевые черепа", "<span class='skull orange'>🟠 Оранжевые черепа</span>");

                html.append("                            <div class=\"content-list-item\">\n");
                html.append("                                <span class=\"list-marker\">•</span>\n");
                html.append("                                <span class=\"list-text\">").append(listItem).append("</span>\n");
                html.append("                            </div>\n");
            }
            else if (itemText.startsWith("**Примечания:**"))
            {
                String notes = itemText.replace("**Примечания:**", "").trim();
                html.append("                            <div class=\"notes-section\">\n");
                html.append("                                <h5 class=\"notes-title\">📝 Примечания:</h5>\n");
                html.append("                                <p class=\"notes-text\">").append(notes).append("</p>\n");
                html.append("                            </div>\n");
            }
        }

        html.append("                        </div>\n");
        return html.toString();
    }

    /**
     *
     * @param text
     * @param tags
     */
    private static void extractCommandTags(String text, List<String> tags)
    {
        String[] tagPatterns = {
                "\\[аргументы: [^\\]]+\\]",
                "\\[отладочная команда\\]",
                "\\[клиентская команда\\]",
                "\\[админская команда\\]",
                "\\[тип команды: [^\\]]+\\]",
                "\\[без аргументов\\]",
                "\\[просто ввод\\]",
                "\\[ввод \\([^)]+\\)\\]",
                "\\[мультипле аргументы\\]"
        };

        for (String pattern : tagPatterns)
        {
            java.util.regex.Pattern p = java.util.regex.Pattern.compile(pattern);
            java.util.regex.Matcher m = p.matcher(text);
            while (m.find())
            {
                tags.add(m.group());
            }
        }
    }

    /**
     *
     * @param text
     * @return
     */
    private static String removeCommandTags(String text)
    {
        String[] tagsToRemove = {
                "\\[аргументы: [^\\]]+\\]",
                "\\[отладочная команда\\]",
                "\\[клиентская команда\\]",
                "\\[админская команда\\]",
                "\\[тип команды: [^\\]]+\\]",
                "\\[без аргументов\\]",
                "\\[просто ввод\\]",
                "\\[ввод \\([^)]+\\)\\]",
                "\\[мультипле аргументы\\]"
        };

        String result = text;
        for (String tag : tagsToRemove)
        {
            result = result.replaceAll(tag, "").trim();
        }
        return result;
    }

    /**
     *
     * @param tag
     * @return
     */
    private static String getCommandTagClass(String tag)
    {
        if (tag.contains("отладочная команда")) return "debug";
        if (tag.contains("клиентская команда")) return "client";
        if (tag.contains("админская команда")) return "admin";
        if (tag.contains("аргументы:")) return "args";
        if (tag.contains("тип команды:")) return "type";
        if (tag.contains("без аргументов") || tag.contains("просто ввод")) return "no-args";
        if (tag.contains("мультипле аргументы")) return "multi-args";
        if (tag.contains("ввод (")) return "input";
        return "default";
    }

    /**
     *
     * @param tag
     * @return
     */
    private static String getCommandTagIcon(String tag)
    {
        if (tag.contains("отладочная команда")) return "🐛";
        if (tag.contains("клиентская команда")) return "💻";
        if (tag.contains("админская команда")) return "👑";
        if (tag.contains("аргументы:")) return "📋";
        if (tag.contains("тип команды:")) return "🔧";
        if (tag.contains("без аргументов") || tag.contains("просто ввод")) return "⏺️";
        if (tag.contains("мультипле аргументы")) return "📦";
        if (tag.contains("ввод (")) return "⌨️";
        return "🏷️";
    }

    /**
     *
     * @param tag
     * @return
     */
    private static String getCommandTagText(String tag)
    {
        if (tag.contains("аргументы:"))
        {
            return tag.replace("[аргументы:", "Арг:").replace("]", "");
        }
        if (tag.contains("тип команды:"))
        {
            return tag.replace("[тип команды:", "Тип:").replace("]", "");
        }
        if (tag.contains("ввод ("))
        {
            return tag.replace("[ввод (", "Ввод:").replace(")]", "");
        }

        if (tag.contains("отладочная команда")) return "Отладка";
        if (tag.contains("клиентская команда")) return "Клиент";
        if (tag.contains("админская команда")) return "Админ";
        if (tag.contains("без аргументов")) return "Без арг.";
        if (tag.contains("просто ввод")) return "Просто";
        if (tag.contains("мультипле аргументы")) return "Мульти";
        return tag.replace("[", "").replace("]", "");
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
        css.append("        .status-stats {\n");
        css.append("            display: flex;\n");
        css.append("            justify-content: center;\n");
        css.append("            gap: 30px;\n");
        css.append("            padding: 25px;\n");
        css.append("            background: white;\n");
        css.append("            border-bottom: 1px solid #eaeaea;\n");
        css.append("        }\n");
        css.append("        .status-item {\n");
        css.append("            display: flex;\n");
        css.append("            flex-direction: column;\n");
        css.append("            align-items: center;\n");
        css.append("            padding: 15px 25px;\n");
        css.append("            border-radius: 10px;\n");
        css.append("            min-width: 120px;\n");
        css.append("        }\n");
        css.append("        .status-item.working {\n");
        css.append("            background: rgba(40, 167, 69, 0.1);\n");
        css.append("            border: 2px solid #28a745;\n");
        css.append("        }\n");
        css.append("        .status-item.development {\n");
        css.append("            background: rgba(255, 193, 7, 0.1);\n");
        css.append("            border: 2px solid #ffc107;\n");
        css.append("        }\n");
        css.append("        .status-item.disabled {\n");
        css.append("            background: rgba(108, 117, 125, 0.1);\n");
        css.append("            border: 2px solid #6c757d;\n");
        css.append("        }\n");
        css.append("        .status-icon {\n");
        css.append("            font-size: 2rem;\n");
        css.append("            margin-bottom: 8px;\n");
        css.append("        }\n");
        css.append("        .status-count {\n");
        css.append("            font-size: 1.8rem;\n");
        css.append("            font-weight: bold;\n");
        css.append("            margin-bottom: 5px;\n");
        css.append("        }\n");
        css.append("        .status-item.working .status-count { color: #28a745; }\n");
        css.append("        .status-item.development .status-count { color: #ffc107; }\n");
        css.append("        .status-item.disabled .status-count { color: #6c757d; }\n");
        css.append("        .status-label {\n");
        css.append("            font-size: 0.9rem;\n");
        css.append("            text-transform: uppercase;\n");
        css.append("            letter-spacing: 1px;\n");
        css.append("            font-weight: 600;\n");
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

        // Styles for clients
        css.append("        .content-section {\n");
        css.append("            margin: 20px 0;\n");
        css.append("            padding: 20px;\n");
        css.append("            background: linear-gradient(135deg, #f7fafc 0%, #edf2f7 100%);\n");
        css.append("            border-radius: 10px;\n");
        css.append("            border-left: 4px solid #375369;\n");
        css.append("        }\n");
        css.append("        .content-title {\n");
        css.append("            font-size: 1.3rem;\n");
        css.append("            color: #2d3748;\n");
        css.append("            margin-bottom: 15px;\n");
        css.append("            padding-bottom: 8px;\n");
        css.append("            border-bottom: 2px solid #cbd5e0;\n");
        css.append("            display: flex;\n");
        css.append("            align-items: center;\n");
        css.append("            gap: 10px;\n");
        css.append("        }\n");
        css.append("        .content-text {\n");
        css.append("            font-size: 1.05rem;\n");
        css.append("            line-height: 1.7;\n");
        css.append("            color: #4a5568;\n");
        css.append("            margin-bottom: 12px;\n");
        css.append("            padding-left: 10px;\n");
        css.append("        }\n");
        css.append("        .content-list-item {\n");
        css.append("            display: flex;\n");
        css.append("            align-items: flex-start;\n");
        css.append("            gap: 10px;\n");
        css.append("            margin-bottom: 8px;\n");
        css.append("            padding-left: 10px;\n");
        css.append("        }\n");
        css.append("        .list-marker {\n");
        css.append("            color: #375369;\n");
        css.append("            font-weight: bold;\n");
        css.append("            flex-shrink: 0;\n");
        css.append("            margin-top: 2px;\n");
        css.append("        }\n");
        css.append("        .list-text {\n");
        css.append("            flex: 1;\n");
        css.append("            font-size: 1.05rem;\n");
        css.append("            line-height: 1.6;\n");
        css.append("            color: #4a5568;\n");
        css.append("        }\n");
        css.append("        .notes-section {\n");
        css.append("            margin-top: 20px;\n");
        css.append("            padding: 15px;\n");
        css.append("            background: rgba(255, 245, 157, 0.2);\n");
        css.append("            border-radius: 8px;\n");
        css.append("            border-left: 4px solid #d97706;\n");
        css.append("        }\n");
        css.append("        .notes-title {\n");
        css.append("            font-size: 1.1rem;\n");
        css.append("            color: #92400e;\n");
        css.append("            margin-bottom: 8px;\n");
        css.append("            display: flex;\n");
        css.append("            align-items: center;\n");
        css.append("            gap: 8px;\n");
        css.append("        }\n");
        css.append("        .notes-text {\n");
        css.append("            font-size: 1rem;\n");
        css.append("            line-height: 1.6;\n");
        css.append("            color: #78350f;\n");
        css.append("        }\n");
        css.append("        .skull {\n");
        css.append("            font-weight: 600;\n");
        css.append("            padding: 2px 6px;\n");
        css.append("            border-radius: 12px;\n");
        css.append("            display: inline-flex;\n");
        css.append("            align-items: center;\n");
        css.append("            gap: 4px;\n");
        css.append("        }\n");
        css.append("        .skull.red {\n");
        css.append("            background: rgba(239, 68, 68, 0.15);\n");
        css.append("            color: #dc2626;\n");
        css.append("            border: 1px solid rgba(239, 68, 68, 0.3);\n");
        css.append("        }\n");
        css.append("        .skull.orange {\n");
        css.append("            background: rgba(249, 115, 22, 0.15);\n");
        css.append("            color: #ea580c;\n");
        css.append("            border: 1px solid rgba(249, 115, 22, 0.3);\n");
        css.append("        }\n");
        css.append("        strong {\n");
        css.append("            font-weight: 600;\n");
        css.append("            color: #2d3748;\n");
        css.append("        }\n");

        css.append("        .change-list {\n");
        css.append("            list-style-type: none;\n");
        css.append("            padding-left: 0;\n");
        css.append("        }\n");
        css.append("        .change-item {\n");
        css.append("            display: flex;\n");
        css.append("            padding: 20px;\n");
        css.append("            margin: 12px 0;\n");
        css.append("            background: white;\n");
        css.append("            border-radius: 12px;\n");
        css.append("            box-shadow: 0 3px 10px rgba(0,0,0,0.06);\n");
        css.append("            transition: all 0.3s ease;\n");
        css.append("            border-left: 5px solid #28a745;\n");
        css.append("        }\n");
        css.append("        .change-item.status-development {\n");
        css.append("            border-left-color: #ffc107;\n");
        css.append("            background: rgba(255, 193, 7, 0.03);\n");
        css.append("        }\n");
        css.append("        .change-item.status-disabled {\n");
        css.append("            border-left-color: #6c757d;\n");
        css.append("            background: rgba(108, 117, 125, 0.03);\n");
        css.append("            opacity: 0.85;\n");
        css.append("        }\n");
        css.append("        .change-item:hover {\n");
        css.append("            transform: translateX(5px);\n");
        css.append("            box-shadow: 0 5px 15px rgba(0,0,0,0.1);\n");
        css.append("        }\n");
        css.append("        .change-item:hover.status-development {\n");
        css.append("            background: rgba(255, 193, 7, 0.06);\n");
        css.append("        }\n");
        css.append("        .change-item:hover.status-disabled {\n");
        css.append("            background: rgba(108, 117, 125, 0.06);\n");
        css.append("        }\n");
        css.append("        .item-content {\n");
        css.append("            display: flex;\n");
        css.append("            flex-direction: column;\n");
        css.append("            width: 100%;\n");
        css.append("            gap: 12px;\n");
        css.append("        }\n");
        css.append("        .item-header {\n");
        css.append("            display: flex;\n");
        css.append("            align-items: flex-start;\n");
        css.append("            gap: 12px;\n");
        css.append("        }\n");
        css.append("        .item-status {\n");
        css.append("            font-size: 1.4rem;\n");
        css.append("            min-width: 32px;\n");
        css.append("            height: 32px;\n");
        css.append("            display: flex;\n");
        css.append("            align-items: center;\n");
        css.append("            justify-content: center;\n");
        css.append("            flex-shrink: 0;\n");
        css.append("        }\n");
        css.append("        .item-text {\n");
        css.append("            flex: 1;\n");
        css.append("            font-size: 1.05rem;\n");
        css.append("            line-height: 1.6;\n");
        css.append("            color: #2d3748;\n");
        css.append("            padding-top: 2px;\n");
        css.append("        }\n");
        css.append("        .item-tags {\n");
        css.append("            display: flex;\n");
        css.append("            flex-wrap: wrap;\n");
        css.append("            gap: 10px;\n");
        css.append("            margin-top: 2px;\n");
        css.append("        }\n");
        css.append("        .world-type-badge {\n");
        css.append("            padding: 6px 14px;\n");
        css.append("            border-radius: 16px;\n");
        css.append("            font-size: 0.85rem;\n");
        css.append("            font-weight: 600;\n");
        css.append("            background: rgba(86, 98, 246, 0.12);\n");
        css.append("            color: #4f46e5;\n");
        css.append("            border: 1px solid rgba(86, 98, 246, 0.2);\n");
        css.append("            display: inline-flex;\n");
        css.append("            align-items: center;\n");
        css.append("            gap: 6px;\n");
        css.append("            text-transform: uppercase;\n");
        css.append("            letter-spacing: 0.3px;\n");
        css.append("        }\n");
        css.append("        .world-type-badge.overworld {\n");
        css.append("            background: rgba(34, 197, 94, 0.12);\n");
        css.append("            color: #059669;\n");
        css.append("            border-color: rgba(34, 197, 94, 0.2);\n");
        css.append("        }\n");
        css.append("        .world-type-badge.nether {\n");
        css.append("            background: rgba(239, 68, 68, 0.12);\n");
        css.append("            color: #dc2626;\n");
        css.append("            border-color: rgba(239, 68, 68, 0.2);\n");
        css.append("        }\n");
        css.append("        .world-type-badge.end {\n");
        css.append("            background: rgba(168, 85, 247, 0.12);\n");
        css.append("            color: #7c3aed;\n");
        css.append("            border-color: rgba(168, 85, 247, 0.2);\n");
        css.append("        }\n");
        css.append("        .status-badge {\n");
        css.append("            padding: 6px 14px;\n");
        css.append("            border-radius: 16px;\n");
        css.append("            font-size: 0.85rem;\n");
        css.append("            font-weight: 600;\n");
        css.append("            text-transform: uppercase;\n");
        css.append("            letter-spacing: 0.5px;\n");
        css.append("            white-space: nowrap;\n");
        css.append("        }\n");
        css.append("        .change-item.status-working .status-badge {\n");
        css.append("            background: rgba(34, 197, 94, 0.15);\n");
        css.append("            color: #059669;\n");
        css.append("        }\n");
        css.append("        .change-item.status-development .status-badge {\n");
        css.append("            background: rgba(245, 158, 11, 0.15);\n");
        css.append("            color: #d97706;\n");
        css.append("        }\n");
        css.append("        .change-item.status-disabled .status-badge {\n");
        css.append("            background: rgba(107, 114, 128, 0.15);\n");
        css.append("            color: #4b5563;\n");
        css.append("        }\n");
        css.append("        .command-tag {\n");
        css.append("            padding: 5px 10px;\n");
        css.append("            border-radius: 14px;\n");
        css.append("            font-size: 0.75rem;\n");
        css.append("            font-weight: 600;\n");
        css.append("            text-transform: uppercase;\n");
        css.append("            letter-spacing: 0.3px;\n");
        css.append("            white-space: nowrap;\n");
        css.append("            display: inline-flex;\n");
        css.append("            align-items: center;\n");
        css.append("            gap: 4px;\n");
        css.append("        }\n");
        css.append("        .command-tag.debug {\n");
        css.append("            background: rgba(139, 92, 246, 0.15);\n");
        css.append("            color: #7c3aed;\n");
        css.append("            border: 1px solid rgba(139, 92, 246, 0.2);\n");
        css.append("        }\n");
        css.append("        .command-tag.client {\n");
        css.append("            background: rgba(59, 130, 246, 0.15);\n");
        css.append("            color: #2563eb;\n");
        css.append("            border: 1px solid rgba(59, 130, 246, 0.2);\n");
        css.append("        }\n");
        css.append("        .command-tag.admin {\n");
        css.append("            background: rgba(239, 68, 68, 0.15);\n");
        css.append("            color: #dc2626;\n");
        css.append("            border: 1px solid rgba(239, 68, 68, 0.2);\n");
        css.append("        }\n");
        css.append("        .command-tag.args {\n");
        css.append("            background: rgba(34, 197, 94, 0.15);\n");
        css.append("            color: #059669;\n");
        css.append("            border: 1px solid rgba(34, 197, 94, 0.2);\n");
        css.append("        }\n");
        css.append("        .command-tag.type {\n");
        css.append("            background: rgba(245, 158, 11, 0.15);\n");
        css.append("            color: #d97706;\n");
        css.append("            border: 1px solid rgba(245, 158, 11, 0.2);\n");
        css.append("        }\n");
        css.append("        .command-tag.no-args {\n");
        css.append("            background: rgba(107, 114, 128, 0.15);\n");
        css.append("            color: #4b5563;\n");
        css.append("            border: 1px solid rgba(107, 114, 128, 0.2);\n");
        css.append("        }\n");
        css.append("        .command-tag.multi-args {\n");
        css.append("            background: rgba(168, 85, 247, 0.15);\n");
        css.append("            color: #7c3aed;\n");
        css.append("            border: 1px solid rgba(168, 85, 247, 0.2);\n");
        css.append("        }\n");
        css.append("        .command-tag.input {\n");
        css.append("            background: rgba(14, 165, 233, 0.15);\n");
        css.append("            color: #0284c7;\n");
        css.append("            border: 1px solid rgba(14, 165, 233, 0.2);\n");
        css.append("        }\n");
        css.append("        .command-tag.default {\n");
        css.append("            background: rgba(107, 114, 128, 0.15);\n");
        css.append("            color: #4b5563;\n");
        css.append("            border: 1px solid rgba(107, 114, 128, 0.2);\n");
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
        css.append("            .status-stats {\n");
        css.append("                flex-direction: column;\n");
        css.append("                align-items: center;\n");
        css.append("                gap: 15px;\n");
        css.append("            }\n");
        css.append("            .status-item {\n");
        css.append("                width: 80%;\n");
        css.append("                max-width: 250px;\n");
        css.append("            }\n");
        css.append("            .category-content {\n");
        css.append("                padding: 15px;\n");
        css.append("            }\n");
        css.append("            .change-item {\n");
        css.append("                padding: 16px;\n");
        css.append("            }\n");
        css.append("            .item-content {\n");
        css.append("                gap: 10px;\n");
        css.append("            }\n");
        css.append("            .item-header {\n");
        css.append("                flex-direction: column;\n");
        css.append("                gap: 8px;\n");
        css.append("            }\n");
        css.append("            .item-status {\n");
        css.append("                align-self: flex-start;\n");
        css.append("            }\n");
        css.append("            .item-text {\n");
        css.append("                font-size: 1rem;\n");
        css.append("                line-height: 1.5;\n");
        css.append("            }\n");
        css.append("            .item-tags {\n");
        css.append("                gap: 8px;\n");
        css.append("            }\n");
        css.append("            .world-type-badge,\n");
        css.append("            .status-badge {\n");
        css.append("                font-size: 0.8rem;\n");
        css.append("                padding: 5px 10px;\n");
        css.append("            }\n");
        css.append("            .content-section {\n");
        css.append("                padding: 15px;\n");
        css.append("            }\n");
        css.append("            .content-title {\n");
        css.append("                font-size: 1.2rem;\n");
        css.append("            }\n");
        css.append("            .content-text, .list-text {\n");
        css.append("                font-size: 1rem;\n");
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