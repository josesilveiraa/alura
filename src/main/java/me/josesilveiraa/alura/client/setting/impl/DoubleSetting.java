package me.josesilveiraa.alura.client.setting.impl;

import com.lukflug.panelstudio.base.IBoolean;
import com.lukflug.panelstudio.setting.INumberSetting;
import me.josesilveiraa.alura.client.setting.Setting;

public class DoubleSetting extends Setting<Double> implements INumberSetting {
    public final double min, max;

    public DoubleSetting(String displayName, String configName, String description, IBoolean visible, double min, double max, double value) {
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
        setValue(value);
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
        return 2;
    }
}
