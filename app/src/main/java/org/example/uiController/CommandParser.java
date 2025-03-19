package org.example.uiController;

/**
 * Parser that handles parsing strings to their Command counterpart.
 */
public class CommandParser {
    /**
     * Parses strings to their Command counterpart.
     * @param input The input string.
     * @return The Command.
     */
    public static Command parseCommand(String input) {
        if (input == null || input.isEmpty()) {
            return Command.UNKNOWN;  // Default to unknown if input is empty
        }

        return switch (input.toLowerCase()) {
            case "run" -> Command.RUN;
            case "list" -> Command.LIST;
            case "policy_change" -> Command.POLICY_CHANGE;
            case "help" -> Command.HELP;
            case "exit" -> Command.EXIT;
            default -> Command.UNKNOWN;
        };
    }
}
