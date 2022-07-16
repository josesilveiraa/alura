package me.josesilveiraa.alura.client.module.impl.hud;

import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.setting.impl.BooleanSetting;
import me.josesilveiraa.alura.client.setting.impl.KeybindSetting;
import org.lwjgl.input.Keyboard;

public class HUDEditorModule extends Module {
    public static final BooleanSetting showHUD = new BooleanSetting("Show HUD Panels", "showHUD", "Whether to show the HUD panels in the ClickGUI.", () -> true, true);

    public HUDEditorModule() {
        super("HUDEditor", "Module containing HUDEditor settings.", () -> true, false);
        settings.add(showHUD);
    }
}
