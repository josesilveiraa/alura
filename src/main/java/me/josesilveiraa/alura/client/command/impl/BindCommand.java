package me.josesilveiraa.alura.client.command.impl;

import me.josesilveiraa.alura.Alura;
import me.josesilveiraa.alura.client.command.Command;
import me.josesilveiraa.alura.client.module.Module;
import me.josesilveiraa.alura.util.MessageUtil;
import org.lwjgl.input.Keyboard;

import java.util.Locale;

public class BindCommand extends Command {

    public BindCommand() {
        super("Bind", "bind <module> <key>", new String[]{"bind", "b", "setbind"});
    }

    @Override
    public void onCommand(String command, String[] args) {
        String moduleName = args[0];
        String bindKey = args[1].toUpperCase(Locale.ROOT);

        for(Module module : Alura.getModuleManager().getModules()) {
            if(module.getDisplayName().equalsIgnoreCase(moduleName)) {
                if(bindKey.equalsIgnoreCase("none")) {
                    module.setBinding(Keyboard.KEY_NONE);
                    MessageUtil.sendClientMessage("Module " + module.getDisplayName() + " bound to NONE.");
                } else if(bindKey.length() >= 1) {
                    int key = Keyboard.getKeyIndex(bindKey);

                    module.setBinding(key);
                    MessageUtil.sendClientMessage("Module " + module.getDisplayName() + " bound to " + Keyboard.getKeyName(key) + ".");

                } else {
                    MessageUtil.sendClientMessage("Correct usage: " + getSyntax());
                }
            }
        }
    }
}
