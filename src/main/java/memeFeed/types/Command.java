package memeFeed.types;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.InputStream;

public abstract class Command {

    public String _command;
    public CommandPermission _commandPermission;
    public SentenceCommand _sentenceCommand;

    public MessageReceivedEvent _event = null;

    public Command(String command, CommandPermission commandPermission, SentenceCommand sentenceCommand) {
        this._command = command;
        this._commandPermission = commandPermission;
        this._sentenceCommand = sentenceCommand;
    }

    public void runCommand(String message, MessageReceivedEvent event) {
        this._event = event;
        execute(message, event);
    }

    public abstract void execute(String message, MessageReceivedEvent event);

    public void sendResponseMessage(String header, String footer, String body) {
        if (header == null && footer != null) {
            sendResponseFooterBody(footer, body);
        } else if (header != null && footer == null) {
            sendResponseHeaderBody(header, body);
        } else if (header == null && footer == null) {
            sendResponseBody(body);
        }
    }

    public void sendResponseMessageImage(String header, String footer, String body, File file) {
        MessageBuilder message = new MessageBuilder();
        EmbedBuilder embed = new EmbedBuilder().setTitle(header).setFooter(footer, null).setDescription(body);
        embed.setImage("attachment://" + file.getName());
        message.setEmbed(embed.build());
        _event.getTextChannel().sendFile(file, file.getName(), message.build()).queue();
    }

    private void sendResponseBody(String body) {
        _event.getTextChannel().sendMessage(
                new EmbedBuilder().setDescription(body).build()
        ).queue();
    }

    private void sendResponseHeaderBody(String header, String body) {
        _event.getTextChannel().sendMessage(
                new EmbedBuilder().setTitle(header).setDescription(body).build()
        ).queue();
    }

    private void sendResponseFooterBody(String footer, String body) {
        _event.getTextChannel().sendMessage(
                new EmbedBuilder().setFooter(footer, null).setDescription(body).build()
        ).queue();
    }
}
