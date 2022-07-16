package me.josesilveiraa.alura.client.module.impl.player;

import com.lukflug.panelstudio.base.IBoolean;
import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.setting.impl.BooleanSetting;

public class AutoTool extends Module {

    private final BooleanSetting switchBack = new BooleanSetting("Switch back", "switchBack", "Switch back from the tool.", () -> true, true);

    boolean shouldMoveBack = false;
    int lastSlot = 0;
    long lastChange = 0;

    public AutoTool(String displayName, String description, IBoolean visible, boolean toggleable) {
        super(displayName, description, visible, toggleable);
    }

    private static void equip(int slot) {
        mc.thePlayer.inventory.currentItem = slot;
    }
}
