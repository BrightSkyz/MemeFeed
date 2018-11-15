package memeFeed.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import memeFeed.types.Command;
import memeFeed.types.CommandPermission;
import memeFeed.types.SentenceCommand;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MemeCommand extends Command {

    public MemeCommand() {
        super("meme", CommandPermission.USER, new SentenceCommand().addWords("get", "find").addWords("meme"));
    }

    @Override
    public void execute(String message, MessageReceivedEvent event) {
        Matcher matcher = Pattern.compile("\\d+").matcher(message);
        matcher.find();
        Integer memeId = 0;
        try {
            memeId = Integer.valueOf(matcher.group());
        } catch (Exception e) {
            // Ignored
        }
        File memeFile = new File("memes/" + memeId.toString().replace("/", "") + ".png");
        if (memeFile.isFile()) {
            JsonObject json = null;
            try {
                Scanner in = new Scanner(new FileReader("memes/" + memeId + ".json"));
                StringBuilder sb = new StringBuilder();
                while (in.hasNext()) {
                    sb.append(in.next());
                }
                in.close();
                json = new JsonParser().parse(sb.toString()).getAsJsonObject();
            } catch (FileNotFoundException e) {
                //e.printStackTrace();
                sendResponseMessage("Meme Archive", "Error", "The meme you requested was not found.");
                return;
            }
            sendResponseMessageImage("Meme Archive", null,
                    "Meme ID: " + json.get("id").getAsString() +
                    "\nTitle: [" + json.get("title").getAsString() + "](" + json.get("permalink").getAsString() + ")" +
                    "\nPosted to r/" + json.get("subreddit").getAsString() + " by u/" + json.get("author").getAsString()
                    , memeFile);
        } else {
            sendResponseMessage("Meme Archive", "Error", "The meme you requested was not found.");
        }
    }
}
