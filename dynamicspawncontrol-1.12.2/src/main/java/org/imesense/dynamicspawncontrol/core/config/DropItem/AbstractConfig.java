package org.imesense.dynamicspawncontrol.core.config.DropItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public abstract class AbstractConfig<T> {

  // private final Class<T> configClass; // Класс конфигурации
  // private final String filePath;     // Путь к файлу конфигурации
  // private final Gson gson;           // Объект Gson для работы с JSON

  // public AbstractConfig(Class<T> configClass, String filePath) {
  //     this.configClass = configClass;
  //     this.filePath = filePath;
  //     this.gson = new GsonBuilder().setPrettyPrinting().create();
  // }

  // // Метод для загрузки или создания конфига
  // public T loadOrCreateConfig() {
  //     File configFile = new File(filePath);

  //     // Если файл не существует, создаем его с дефолтными значениями
  //     if (!configFile.exists()) {
  //         System.out.println("Конфиг файл не найден. Создаем новый с дефолтными значениями...");
  //         T defaultConfig = createDefaultConfig(); // Создаем дефолтный конфиг
  //         saveConfig(defaultConfig);              // Сохраняем его в файл
  //         return defaultConfig;
  //     }

  //     // Если файл существует, читаем его
  //     try (Reader reader = new FileReader(configFile)) {
  //         return gson.fromJson(reader, configClass); // Преобразуем JSON в объект
  //     } catch (IOException e) {
  //         e.printStackTrace();
  //         return null;
  //     }
  // }

  // // Метод для сохранения конфига в файл
  // public void saveConfig(T config) {
  //     try (Writer writer = new FileWriter(filePath)) {
  //         gson.toJson(config, writer); // Сохраняем объект в JSON-файл
  //         System.out.println("Конфиг файл создан/обновлен: " + filePath);
  //     } catch (IOException e) {
  //         e.printStackTrace();
  //     }
  // }

  // // Абстрактный метод для создания дефолтного конфига
  // protected abstract T createDefaultConfig();
}
