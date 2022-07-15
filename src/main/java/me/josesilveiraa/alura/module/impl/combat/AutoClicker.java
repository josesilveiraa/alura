package me.josesilveiraa.alura.module.impl.combat;

import me.josesilveiraa.alura.module.Module;
import me.josesilveiraa.alura.setting.impl.BooleanSetting;
import me.josesilveiraa.alura.setting.impl.DoubleSetting;
import me.josesilveiraa.alura.setting.impl.EnumSetting;
import me.josesilveiraa.alura.setting.impl.IntegerSetting;

public class AutoClicker extends Module {

    private final IntegerSetting minimumCps = new IntegerSetting("Minimum CPS", "minCps", "Minimum amount of clicks.", () -> true, 1, 60, 18);
    private final IntegerSetting maximumCps = new IntegerSetting("Maximum CPS", "maxCps", "Maximum amount of clicks.", () -> true, 1, 60, 20);
    private final EnumSetting<Event> eventType = new EnumSetting<>("Event", "eventType", "Which event do you want the module to work.", () -> true, Event.TICK, Event.class);
    private final BooleanSetting breakBlocks = new BooleanSetting("Break blocks", "brkBlocks", "Gives you the ability to break blocks.", () -> true, true);
    private final BooleanSetting invFill = new BooleanSetting("Inventory fill", "invFill", null, () -> true, false);
    private final BooleanSetting weaponOnly = new BooleanSetting("Weapon only", "wpnOnly", "The module just works with weapons.", () -> true, false);

    private final DoubleSetting jitterLeft = new DoubleSetting("Left jitter", "leftJitter", null, () -> true, 0.0, 3.0, 0.0);

    public AutoClicker() {
        super("AutoClicker", "Automatically clicks for you.", () -> true, true);

        settings.add(minimumCps);
        settings.add(maximumCps);
        settings.add(eventType);
        settings.add(breakBlocks);
        settings.add(invFill);
        settings.add(weaponOnly);
        settings.add(jitterLeft);
    }

    enum Event {
        RENDER,
        TICK;
    }

}
