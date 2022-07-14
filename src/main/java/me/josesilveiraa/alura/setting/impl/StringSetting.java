package me.josesilveiraa.alura.setting.impl;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.IStringSetting;
import me.josesilveiraa.alura.setting.Setting;

public class StringSetting extends Setting<String> implements IStringSetting {
    public StringSetting(String displayName, String configName, String description, IBoolean visible, String value) {
        super(displayName, configName, description, visible, value);
    }
}
