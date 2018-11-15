package memeFeed.commands;

import memeFeed.types.Command;
import memeFeed.types.CommandPermission;
import memeFeed.types.SentenceCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelpSentenceCommand extends Command {

        public HelpSentenceCommand() {
            super("helpsentence", CommandPermission.USER, new SentenceCommand().addWords("help").addWords("sentence").addWords("command", "cmd"));
        }

        @Override
        public void execute(String message, MessageReceivedEvent event) {
            sendResponseMessage("Help for MemeFeed", null, "This bot has a sentence command system so you can speak more naturally to the " +
                    "bot and execute what you want.\n\n**Usage**\nThe way the sentence command system works is you simply mention the bot somewhere in the message " +
                    "and use the words specified for the command you want.\n\n**Examples**\n" +
                    "*hey @MemeFeed, can you find me the meme 67?* - Displays meme #67\n" +
                    "*help me out with sentence commands @MemeFeed* - Displays help for sentence commands\n" +
                    "*@MemeFeed help menu* - Displays the standard help menu");
        }
}
