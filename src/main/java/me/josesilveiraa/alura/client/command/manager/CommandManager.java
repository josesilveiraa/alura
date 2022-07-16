package me.josesilveiraa.alura.client.command.manager;

import me.josesilveiraa.alura.client.command.Command;
import me.josesilveiraa.alura.client.command.impl.BindCommand;
import me.josesilveiraa.alura.util.MessageUtil;

import java.util.ArrayList;
import java.util.List;

public class CommandManager {

    private String prefix = ".";
    private final List<Command> commands = new ArrayList<>();

    public CommandManager() {
        init();
    }

    private void init() {
        addCommand(new BindCommand());
    }

    public void addCommand(Command command) {
        commands.add(command);
    }

    public List<Command> getCommands() {
        return commands;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    private boolean isValidCommand = false;

    public void callCommand(String input) {
        String[] split = input.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        String command1 = split[0];
        String args = input.substring(command1.length()).trim();

        isValidCommand = false;

        commands.forEach(command -> {
            for (String string : command.getAlias()) {
                if (string.equalsIgnoreCase(command1)) {
                    isValidCommand = true;
                    try {
                        command.onCommand(args, args.split(" (?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"));
                    } catch (Exception e) {
                        MessageUtil.sendClientMessage(command.getSyntax());
                    }
                }
            }
        });

        if (!isValidCommand) {
            MessageUtil.sendClientMessage("Error! Invalid command.");
        }
    }
}
