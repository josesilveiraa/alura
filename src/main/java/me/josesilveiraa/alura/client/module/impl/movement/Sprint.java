package me.josesilveiraa.alura.client.module.impl.movement;

import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.setting.impl.BooleanSetting;
import me.josesilveiraa.alura.util.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Sprint extends Module {

    private final BooleanSetting allDirections = new BooleanSetting("All directions", "allDirections", "Check it if you want to sprint to all directions.", () -> true, false);


    public Sprint() {
        super("Sprint", "Automatically sprints for you.", () -> true, true);

        settings.add(allDirections);
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (Utils.isInGame() && mc.inGameHasFocus) {

            if (allDirections.get()) {
                mc.thePlayer.setSprinting(true);
            } else {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            }
        }
    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), false);
    }

}
