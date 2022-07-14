package me.josesilveiraa.alura.module.impl.movement;

import me.josesilveiraa.alura.module.Module;
import me.josesilveiraa.alura.setting.impl.BooleanSetting;
import me.josesilveiraa.alura.setting.impl.KeybindSetting;
import me.josesilveiraa.alura.util.Utils;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

public class Sprint extends Module {

    private final KeybindSetting binding = new KeybindSetting("Bind", "bind", "The module's bind.", () -> true, Keyboard.KEY_NONE);

    private final BooleanSetting allDirections = new BooleanSetting("All directions", "allDirections", "Check it if you want to sprint to all directions.", () -> true, false);


    public Sprint() {
        super("Sprint", "Automatically sprints for you.", () -> true, true);

        settings.add(allDirections);
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        if (Utils.isInGame() && mc.inGameHasFocus) {

            if(allDirections.get()) {
                mc.thePlayer.setSprinting(true);
            } else {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
            }
        }
    }

    @Override
    public void onDisable() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindSprint.getKeyCode(), true);
    }

}
