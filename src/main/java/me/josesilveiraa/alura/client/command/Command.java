package me.josesilveiraa.alura.client.command;

import net.minecraft.client.Minecraft;

public abstract class Command {

    protected static final Minecraft mc = Minecraft.getMinecraft();

    private final String name;
    private final String syntax;
    private final String[] alias;

    public Command(String name, String syntax, String[] alias) {
        this.name = name;
        this.syntax = syntax;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getSyntax() {
        return syntax;
    }

    public String[] getAlias() {
        return alias;
    }

    public abstract void onCommand(String command, String[] args);

}
