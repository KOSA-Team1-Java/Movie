package command;

import controller.MainController;

import java.util.Set;

public class CommandListCommand implements Command {

    private final Set<String> commands;

    public CommandListCommand(Set<String> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(MainController context) {
        for (String command : commands) {
            System.out.println(command);
        }
    }
}
