package me.josesilveiraa.alura.module.impl.combat;

import me.josesilveiraa.alura.module.Module;
import me.josesilveiraa.alura.util.Utils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class OldHitReg extends Module {

    private final Field leftClickCounterField;

    public OldHitReg() {
        super("OldHitReg", "Gives you the old 1.7.10 hit style.", () -> true, true);

        this.leftClickCounterField = ReflectionHelper.findField(Minecraft.class, "field_71429_W", "leftClickCounter");
        if(this.leftClickCounterField != null) this.leftClickCounterField.setAccessible(true);
    }

    @SubscribeEvent
    public void tickEvent(TickEvent.PlayerTickEvent event) {
        if(Utils.isInGame() && this.leftClickCounterField != null) {
            if(!mc.inGameHasFocus || mc.thePlayer.capabilities.isCreativeMode) return;

            try {
                this.leftClickCounterField.set(mc, 0);
            } catch (IllegalAccessException | IndexOutOfBoundsException exception) {
                exception.printStackTrace();
                disable();
            }
        }
    }

}
