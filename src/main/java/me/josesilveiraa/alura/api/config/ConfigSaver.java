package me.josesilveiraa.alura.api.config;

import com.google.gson.*;
import me.josesilveiraa.alura.Alura;
import me.josesilveiraa.alura.api.clickgui.GUIConfig;
import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.setting.impl.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigSaver {

    private static final String MAIN_DIR = "Alura/";
    private static final String MODULES_DIR = "modules/";
    private static final String MAIN_SUB_DIR = "main/";
    private static final String MISC_DIR = "misc/";

    public static void save() {
        try {
            saveConfigs();
            saveClickGUIPositions();
            saveModules();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveModules() throws IOException {
        for (Module module : Alura.getModuleManager().getModules()) {
            saveModule(module);
        }
    }

    private static void saveModule(Module module) throws IOException {
        createFiles(MODULES_DIR, module.getDisplayName());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter osw = new OutputStreamWriter(Files.newOutputStream(Paths.get(MAIN_DIR + MODULES_DIR + module.getDisplayName() + ".json")));
        JsonObject modObj = new JsonObject();
        JsonObject settingsObj = new JsonObject();

        modObj.add("module", new JsonPrimitive(module.getDisplayName()));
        modObj.add("enabled", new JsonPrimitive(module.isModuleEnabled()));

        Alura.getModuleManager().getSettingsForModule(module).forEach(setting -> {
            if (setting instanceof BooleanSetting) {
                settingsObj.add(setting.configName, new JsonPrimitive(((BooleanSetting) setting).getValue()));
            } else if (setting instanceof IntegerSetting) {
                settingsObj.add(setting.configName, new JsonPrimitive(((IntegerSetting) setting).getValue()));
            } else if (setting instanceof DoubleSetting) {
                settingsObj.add(setting.configName, new JsonPrimitive(((DoubleSetting) setting).getValue()));
            } else if (setting instanceof ColorSetting) {
                settingsObj.add(setting.configName, new JsonPrimitive(((ColorSetting) setting).toInteger()));
            } else if (setting instanceof EnumSetting) {
                settingsObj.add(setting.configName, new JsonPrimitive(((EnumSetting<?>) setting).getValueIndex()));
            } else if (setting instanceof KeybindSetting) {
                settingsObj.add(setting.configName, new JsonPrimitive(((KeybindSetting) setting).getKey()));
            } else if (setting instanceof StringSetting) {
                settingsObj.add(setting.configName, new JsonPrimitive(((StringSetting) setting).getValue()));
            }
        });

        modObj.add("settings", settingsObj);
        String json = gson.toJson(new JsonParser().parse(modObj.toString()));
        osw.write(json);
        osw.close();
    }

    private static void saveConfigs() throws IOException {
        Path path = Paths.get(MAIN_DIR);
        if (!Files.exists(path)) Files.createDirectories(path);

        Path path1 = Paths.get(MAIN_DIR + MODULES_DIR);
        if (!Files.exists(path1)) Files.createDirectories(path1);

        Path path2 = Paths.get(MAIN_DIR + MAIN_SUB_DIR);
        if (!Files.exists(path2)) Files.createDirectories(path2);

        Path path3 = Paths.get(MAIN_DIR + MISC_DIR);
        if (!Files.exists(path3)) Files.createDirectories(path3);
    }

    private static void saveClickGUIPositions() throws IOException {
        createFiles(MAIN_SUB_DIR, "ClickGUI");
        Alura.getClickGUI().gui.saveConfig(new GUIConfig(MAIN_DIR + MAIN_SUB_DIR));
    }

    private static void createFiles(String location, String name) throws IOException {
        Path path = Paths.get(MAIN_DIR + location + name + ".json");
        if (Files.exists(path)) {
            File file = new File(MAIN_DIR + location + name + ".json");

            if (!file.delete()) return;
        }
        Files.createFile(path);
    }

}
