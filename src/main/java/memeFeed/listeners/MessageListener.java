package memeFeed.listeners;

import memeFeed.MemeFeed;
import memeFeed.types.Command;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (!event.getAuthor().isBot() && !event.getAuthor().isFake()) {
            // Check for permissions
            if (!event.getTextChannel().canTalk()) return;
            if (!event.getGuild().getSelfMember().hasPermission(event.getTextChannel(), Permission.MESSAGE_EMBED_LINKS)) return;
            // Check for command and run
            Message message = event.getMessage();
            // Disabled the normal command system
            /*if (message.getContentStripped().toLowerCase().startsWith(MemeFeed._botPrefix.toLowerCase())) {
                for (Command command : MemeFeed._commands.values()) {
                    if (command._command.startsWith(message.getContentStripped().toLowerCase().replace(MemeFeed._botPrefix, ""))) {
                        command.runCommand(message.getContentStripped().replace(MemeFeed._botPrefix, ""), event);
                        return;
                    }
                }
            } else */if (message.isMentioned(event.getGuild().getSelfMember(), Message.MentionType.USER)) {
                for (Command command : MemeFeed._commands.values()) {
                    if (command._sentenceCommand.isCommandFound(message.getContentDisplay())) {
                        command.runCommand(message.getContentDisplay(), event);
                        return;
                    }
                }
            }
        }
    }
}
