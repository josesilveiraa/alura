package me.josesilveiraa.alura.util;

import net.minecraft.client.Minecraft;

public class Utils {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static boolean isInGame() {
        return mc.thePlayer != null && mc.theWorld != null;
    }

}
