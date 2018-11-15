package memeFeed.commands;

import memeFeed.types.Command;
import memeFeed.types.CommandPermission;
import memeFeed.types.SentenceCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class TestCommand extends Command {

    public TestCommand() {
        super("test", CommandPermission.USER, new SentenceCommand().addWords("test").addWords("command"));
    }

    @Override
    public void execute(String message, MessageReceivedEvent event) {
        sendResponseMessage("Hello!", null, "Hello, " + event.getMessage().getAuthor().getAsMention());
    }
}
