package me.josesilveiraa.alura.module.impl.gui;

import me.josesilveiraa.alura.module.Module;
import me.josesilveiraa.alura.setting.impl.EnumSetting;
import me.josesilveiraa.alura.setting.impl.IntegerSetting;
import me.josesilveiraa.alura.setting.impl.KeybindSetting;
import org.lwjgl.input.Keyboard;

public class ClickGUIModule extends Module {
    public static final EnumSetting<ColorModel> colorModel = new EnumSetting<ColorModel>("Color Model", "colorModel", "Whether to use RGB or HSB.", () -> true, ColorModel.RGB, ColorModel.class);
    public static final IntegerSetting rainbowSpeed = new IntegerSetting("Rainbow Speed", "rainbowSpeed", "The speed of the color hue cycling.", () -> true, 1, 100, 32);
    public static final IntegerSetting scrollSpeed = new IntegerSetting("Scroll Speed", "scrollSpeed", "The speed of scrolling.", () -> true, 0, 20, 10);
    public static final IntegerSetting animationSpeed = new IntegerSetting("Animation Speed", "animationSpeed", "The speed of GUI animations.", () -> true, 0, 1000, 200);
    public static final EnumSetting<Theme> theme = new EnumSetting<>("Theme", "theme", "What theme to use.", () -> true, Theme.Impact, Theme.class);
    public static final EnumSetting<Layout> layout = new EnumSetting<>("Layout", "layout", "What layout to use.", () -> true, Layout.CSGOCategory, Layout.class);
    public static final KeybindSetting keybind = new KeybindSetting("Keybind", "keybind", "The key to toggle the module.", () -> true, Keyboard.KEY_O);

    public ClickGUIModule() {
        super("ClickGUI", "Module containing ClickGUI settings.", () -> true, false);
        settings.add(colorModel);
        settings.add(rainbowSpeed);
        settings.add(scrollSpeed);
        settings.add(animationSpeed);
        settings.add(theme);
        settings.add(layout);
        settings.add(keybind);
    }

    public enum ColorModel {
        RGB, HSB;
    }

    public enum Theme {
        Clear, GameSense, Rainbow, Windows31, Impact;
    }

    public enum Layout {
        ClassicPanel, CSGOCategory, SearchableCSGO;
    }
}
