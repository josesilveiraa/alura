package me.josesilveiraa.alura.api.config;

import me.josesilveiraa.alura.Alura;
import me.josesilveiraa.alura.clickgui.GUIConfig;

import java.io.IOException;

public class ConfigLoader {

    public static void load() {
        try {
            loadClickGUIPositions();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static final String MAIN_DIR = "Alura/";
    private static final String MODULES_DIR = "modules/";
    private static final String MAIN_SUB_DIR = "main/";
    private static final String MISC_DIR = "misc/";

    public static void loadClickGUIPositions() throws IOException {
        Alura.getClickGUI().gui.loadConfig(new GUIConfig(MAIN_DIR + MAIN_SUB_DIR));
    }

}
