package me.josesilveiraa.alura.api.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.josesilveiraa.alura.Alura;
import me.josesilveiraa.alura.api.clickgui.GUIConfig;
import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.setting.Setting;
import me.josesilveiraa.alura.client.setting.impl.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {

    private static final String MAIN_DIR = "Alura/";
    private static final String MODULES_DIR = "modules/";
    private static final String MAIN_SUB_DIR = "main/";
    private static final String MISC_DIR = "misc/";

    public static void load() {
        try {
            loadClickGUIPositions();
            loadModules();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void loadModules() throws IOException {
        String modLocation = MAIN_DIR + MODULES_DIR;

        for (Module module : Alura.getModuleManager().getModules()) {
            loadModule(modLocation, module);
        }
    }

    private static void loadModule(String modLocation, Module module) throws IOException {
        if (!Files.exists(Paths.get(modLocation + module.getDisplayName() + ".json"))) return;

        InputStream is = Files.newInputStream(Paths.get(modLocation + module.getDisplayName() + ".json"));
        JsonObject obj;

        try {
            obj = new JsonParser().parse(new InputStreamReader(is)).getAsJsonObject();
        } catch (IllegalStateException e) {
            return;
        }

        if (obj.get("module") == null) return;
        if(obj.get("enabled") == null) return;
        if(obj.get("enabled").getAsBoolean()) {
            module.enable();
        } else module.disable();

        JsonObject settingObj = obj.get("settings").getAsJsonObject();
        for (Setting<?> setting : Alura.getModuleManager().getSettingsForModule(module)) {
            JsonElement dataObj = settingObj.get(setting.configName);
            try {
                if (dataObj != null && dataObj.isJsonPrimitive()) {
                    if (setting instanceof BooleanSetting) {
                        ((BooleanSetting) setting).setValue(dataObj.getAsBoolean());
                    } else if (setting instanceof IntegerSetting) {
                        ((IntegerSetting) setting).setValue(dataObj.getAsInt());
                    } else if (setting instanceof DoubleSetting) {
                        ((DoubleSetting) setting).setValue(dataObj.getAsDouble());
                    } else if (setting instanceof ColorSetting) {
                        ((ColorSetting) setting).fromInteger(dataObj.getAsInt());
                    } else if (setting instanceof EnumSetting) {
                        ((EnumSetting<?>) setting).setValueIndex(dataObj.getAsInt());
                    } else if (setting instanceof KeybindSetting) {
                        ((KeybindSetting) setting).setKey(dataObj.getAsInt());
                    } else if (setting instanceof StringSetting) {
                        ((StringSetting) setting).setValue(dataObj.getAsString());
                    }
                }
            } catch (NumberFormatException ignored) {
            }
        }
        is.close();
    }

    public static void loadClickGUIPositions() throws IOException {
        Alura.getClickGUI().gui.loadConfig(new GUIConfig(MAIN_DIR + MAIN_SUB_DIR));
    }

}
