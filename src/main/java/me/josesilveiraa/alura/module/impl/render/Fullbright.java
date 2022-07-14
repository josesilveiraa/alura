package me.josesilveiraa.alura.module.impl.render;

import me.josesilveiraa.alura.module.Module;
import me.josesilveiraa.alura.util.Utils;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class Fullbright extends Module {

    private float oldGamma;

    public Fullbright() {
        super("Fullbright", "Makes everything brighter.", () -> true, true);
    }

    @Override
    public void onEnable() {
        oldGamma = mc.gameSettings.gammaSetting;
    }

    @Override
    public void onDisable() {
        mc.gameSettings.gammaSetting = oldGamma;
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(!Utils.isInGame()) {
            onDisable();
            return;
        }

        mc.gameSettings.gammaSetting = 10000;
    }
}
