package memeFeed.commands;

import memeFeed.types.Command;
import memeFeed.types.CommandPermission;
import memeFeed.types.SentenceCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", CommandPermission.USER, new SentenceCommand().addWords("help").addWords("menu"));
    }

    @Override
    public void execute(String message, MessageReceivedEvent event) {
        sendResponseMessage("Help for MemeFeed", null, "Hello, " + event.getMessage().getAuthor().getAsMention() + "! " +
                "Thank you for using MemeFeed by BrightSkyz.\nThis bot has a sentence command system which you can learn how to use with the " +
                "following command: ``@MemeFeed sentence command help``\n\n" +
                "**Commands**:\n" +
                "help menu - Displays this help menu\n" +
                "help sentence command/cmd - Sentence Command Help\n" +
                "get/find meme [id] - Get the meme with the ID [id]\n\n" +
                "**Want to get a constant feed of memes?**:\n" +
                "Create a text channel called #memefeed which the bot can post to."
        );
    }
}
