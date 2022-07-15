package me.josesilveiraa.alura.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class MessageUtil {

    private static final String PREFIX = EnumChatFormatting.WHITE + "[" + EnumChatFormatting.DARK_PURPLE + "Alura" + EnumChatFormatting.WHITE + "] " + EnumChatFormatting.RESET;
    protected static final Minecraft mc = Minecraft.getMinecraft();


    public static void sendClientMessage(String message) {
        message(message);
    }

    private static void message(String message) {
        mc.ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(PREFIX + message));
    }
}
