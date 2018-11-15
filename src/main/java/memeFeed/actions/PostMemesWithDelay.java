package memeFeed.actions;

import com.google.gson.JsonObject;
import memeFeed.MemeFeed;
import net.dean.jraw.models.Submission;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PostMemesWithDelay {

    private static Timer timer = new Timer();
    private static List<Submission> memeQueue = new ArrayList<>();

    public static void addMemeToQueue(Submission submission) {
        memeQueue.add(submission);
    }

    public static void startLoop() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (memeQueue.size() == 0) return; // No memes in the queue
                Integer memeId = saveMeme(memeQueue.get(0));
                postMeme(memeQueue.get(0), memeId);
                memeQueue.remove(0);
            }
        }, 0, 2400); // Loop every 2.4 seconds (aka 60 seconds divided by 25 submissions)
    }

    public static Integer saveMeme(Submission submission) {
        // Save Image
        Integer memeId = null;
        try {
            Integer imageCount = (new File("memes").list().length / 2);
            memeId = imageCount + 1;
            URL url = new URL(submission.getPreview().getImages().get(0).getSource().getUrl());
            InputStream in = url.openStream();
            // Convert and save image
            File outputFile = new File("memes/" + memeId + ".png");
            BufferedImage image = ImageIO.read(in);
            // Try to make OutputStream to save image
            try (OutputStream os = new FileOutputStream(outputFile)) {
                ImageIO.write(image, "png", os);
                // Save JSON to file
                PrintWriter writer = new PrintWriter("memes/" + memeId + ".json", "UTF-8");
                JsonObject json = new JsonObject();
                // Start JSON
                json.addProperty("id", memeId);
                json.addProperty("source", "reddit");
                json.addProperty("permalink", "https://www.reddit.com" + submission.getPermalink());
                json.addProperty("subreddit", submission.getSubreddit());
                json.addProperty("title", submission.getTitle());
                json.addProperty("author", submission.getAuthor());
                json.addProperty("image", submission.getPreview().getImages().get(0).getSource().getUrl());
                // End JSON + Save
                writer.print(json.toString());
                writer.close();
            } catch (Exception e2) {
                e2.printStackTrace();
                return null;
            } finally {
                in.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null; // Most likely no image supplied in post
        }
        return memeId;
    }

    public static void postMeme(Submission submission, Integer memeId) {
        // Send message to guilds
        for (Guild guild : MemeFeed.shardManager.getGuilds()) {
            if (guild.getTextChannelsByName("memefeed", true).size() > 0) {
                TextChannel channel = guild.getTextChannelsByName("memefeed", true).get(0);
                // Permission check
                if (!channel.canTalk()) return;
                if (!guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_EMBED_LINKS)) return;
                if (!guild.getSelfMember().hasPermission(channel, Permission.MESSAGE_ATTACH_FILES)) return;
                // Send Message
                EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(submission.getTitle(), "https://www.reddit.com" + submission.getPermalink())
                        .setFooter("Posted to r/" + submission.getSubreddit() + " by u/" + submission.getAuthor() + " - ID: " + memeId, null)
                        .setImage(submission.getPreview().getImages().get(0).getSource().getUrl());
                channel.sendMessage(embed.build()).queue();
            }
        }
    }
}
