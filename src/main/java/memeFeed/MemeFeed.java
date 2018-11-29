package memeFeed;

import memeFeed.actions.LookupAndPostDankMemes;
import memeFeed.actions.LookupAndPostMemes;
import memeFeed.actions.PostMemesWithDelay;
import memeFeed.commands.HelpCommand;
import memeFeed.commands.HelpSentenceCommand;
import memeFeed.commands.MemeCommand;
import memeFeed.commands.TestCommand;
import memeFeed.listeners.MessageListener;
import memeFeed.types.Command;
import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.entities.Game;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class MemeFeed {

    // Discord
    public static String _botPrefix = "m#";

    public static ShardManager shardManager = null;

    // Reddit
    public static UserAgent userAgent = null;
    public static Credentials credentials = null;
    public static RedditClient reddit = null;
    public static NetworkAdapter adapter = null;

    // Other
    public static Map<String, Command> _commands = new HashMap<>();

    public static void main(String[] arguments) throws Exception {

        // Add the commands
        _commands.put("help", new HelpCommand());
        _commands.put("helpsentence", new HelpSentenceCommand());
        _commands.put("meme", new MemeCommand());
        _commands.put("test", new TestCommand());

        System.out.print("\n");
        String botPropertiesFileName = "memefeed-bot.properties";
        Properties prop = new Properties();
        // Create file if it doesn't exist.
        File botPropertiesFile = new File(botPropertiesFileName);
        if (!botPropertiesFile.exists()) {
            botPropertiesFile.createNewFile();
            PrintWriter botPropertiesFileWriter = new PrintWriter(botPropertiesFileName);
            botPropertiesFileWriter.print("# MemeFeed Bot Config\n" +
                    "BOT_TOKEN=BOT_TOKEN\n" +
                    "REDDIT_APP_ID=REDDIT_APP_ID\n" +
                    "REDDIT_USERNAME=REDDIT_USERNAME\n" +
                    "REDDIT_PASSWORD=REDDIT_PASSWORD\n" +
                    "REDDIT_CLIENT_ID=REDDIT_CLIENT_ID\n" +
                    "REDDIT_CLIENT_SECRET=REDDIT_CLIENT_SECRET\n" +
                    "REDDIT_CREATORS_USERNAME=REDDIT_CREATORS_USERNAME");
            botPropertiesFileWriter.close();
            System.out.print("Bot Error > Config file did not exist, it has been created.\n");
            System.exit(0);
        } else {
            // Load properties
            InputStream botPropertiesInput = new FileInputStream(botPropertiesFileName);
            prop.load(botPropertiesInput);
            if (!prop.containsKey("BOT_TOKEN") && !prop.containsKey("REDDIT_APP_ID") && !prop.containsKey("REDDIT_USERNAME") && !prop.containsKey("REDDIT_PASSWORD") && !prop.containsKey("REDDIT_CLIENT_ID") && !prop.containsKey("REDDIT_CLIENT_SECRET") && !prop.containsKey("REDDIT_CREATORS_USERNAME")) {
                System.out.print("Bot Error > The configuration isn't set.\n");
                System.exit(0);
            }
            // Set properties

            // Discord
            String BOT_TOKEN = prop.getProperty("BOT_TOKEN");
            // Reddit Bot Account
            String REDDIT_APP_ID = prop.getProperty("REDDIT_APP_ID");
            String REDDIT_USERNAME = prop.getProperty("REDDIT_USERNAME");
            String REDDIT_PASSWORD = prop.getProperty("REDDIT_PASSWORD");
            String REDDIT_CLIENT_ID = prop.getProperty("REDDIT_CLIENT_ID");
            String REDDIT_CLIENT_SECRET = prop.getProperty("REDDIT_CLIENT_SECRET");
            // Reddit Extra Configuration
            String REDDIT_CREATORS_USERNAME = prop.getProperty("REDDIT_CREATORS_USERNAME");

            // Start bot
            DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
            builder.setToken(BOT_TOKEN);
            builder.setGame(Game.of(Game.GameType.DEFAULT, "@MemeFeed help menu"));
            builder.addEventListeners(new MessageListener());
            builder.setShardsTotal(3);
            shardManager = builder.build();

            // Setup configuration for Reddit
            userAgent = new UserAgent("bot", REDDIT_APP_ID, "1.0.0", REDDIT_CREATORS_USERNAME);
            credentials = Credentials.script(REDDIT_USERNAME, REDDIT_PASSWORD, REDDIT_CLIENT_ID, REDDIT_CLIENT_SECRET);
        }

        // This is what really sends HTTP requests to Reddit
        adapter = new OkHttpNetworkAdapter(userAgent);
        reddit = OAuthHelper.automatic(adapter, credentials);

        // Start the loop for meme getting and posting
        LookupAndPostMemes.startLoop();
        LookupAndPostDankMemes.startLoop();
        PostMemesWithDelay.startLoop();
    }
}
