package me.josesilveiraa.alura.setting.impl;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.INumberSetting;
import me.josesilveiraa.alura.setting.Setting;

public class IntegerSetting extends Setting<Integer> implements INumberSetting {
    public final int min, max;

    public IntegerSetting(String displayName, String configName, String description, IBoolean visible, int min, int max, int value) {
        super(displayName, configName, description, visible, value);
        this.min = min;
        this.max = max;
    }

    @Override
    public double getNumber() {
        return getValue();
    }

    @Override
    public void setNumber(double value) {
        setValue((int) Math.round(value));
    }

    @Override
    public double getMaximumValue() {
        return max;
    }

    @Override
    public double getMinimumValue() {
        return min;
    }

    @Override
    public int getPrecision() {
        return 0;
    }
}
