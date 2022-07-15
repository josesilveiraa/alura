package me.josesilveiraa.alura.client.module.impl.combat;

import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.client.setting.impl.BooleanSetting;
import me.josesilveiraa.alura.client.setting.impl.DoubleSetting;
import me.josesilveiraa.alura.util.Utils;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

public class Velocity extends Module {

    private final DoubleSetting horizontal = new DoubleSetting("Horizontal", "horizontal", null, () -> true, 0.0, 100.0, 90.0);
    private final DoubleSetting vertical = new DoubleSetting("Vertical", "vertical", null, () -> true, 0.0, 100.0, 100.0);
    private final DoubleSetting chance = new DoubleSetting("Chance", "chance", null, () -> true, 0.0, 100.0, 100.0);

    private final BooleanSetting onlyWhileTargeting = new BooleanSetting("Only while targeting", "onlyWhileTargeting", null, () -> true, false);
    private final BooleanSetting disableWhileS = new BooleanSetting("Disable while S is pressed", "disableWhileS", null, () -> true, false);

    public Velocity() {
        super("Velocity", "Changes your character velocity.", () -> true, true);

        settings.add(horizontal);
        settings.add(vertical);
        settings.add(chance);
        settings.add(onlyWhileTargeting);
        settings.add(disableWhileS);
    }

    @SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent event) {
        if (Utils.isInGame() && mc.thePlayer.maxHurtTime > 0 && mc.thePlayer.hurtTime == mc.thePlayer.maxHurtTime) {
            if (onlyWhileTargeting.isOn() && (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null)) {
                return;
            }

            if (disableWhileS.isOn() && Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode())) {
                return;
            }

            if (chance.getValue() != 100) {
                double ch = Math.random();

                if (ch > chance.getValue() / 100) {
                    return;
                }
            }

            if (horizontal.getValue() != 100) {
                mc.thePlayer.motionX *= horizontal.getValue() / 100;
                mc.thePlayer.motionZ *= horizontal.getValue() / 100;
            }

            if (vertical.getValue() != 100) {
                mc.thePlayer.motionY *= vertical.getValue() / 100;
            }

        }
    }

}
