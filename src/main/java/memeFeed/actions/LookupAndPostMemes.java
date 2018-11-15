package memeFeed.actions;

import memeFeed.MemeFeed;
import net.dean.jraw.models.*;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.references.SubredditReference;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class LookupAndPostMemes {

    private static Timer timer = new Timer();

    public static void startLoop() {
        timer.schedule(new TimerTask() {
            public void run() {
                    Date oneMinuteAgo = new Date();
                    oneMinuteAgo.setTime(System.currentTimeMillis() - (60 * 1000));
                    // r/memes
                    SubredditReference memesSubreddit = MemeFeed.reddit.subreddit("memes");
                    DefaultPaginator<Submission> paginator = memesSubreddit.posts().limit(25).sorting(SubredditSort.NEW).timePeriod(TimePeriod.ALL).build();
                    Listing<Submission> latest25Posts;
                    try {
                        latest25Posts = paginator.next();
                    } catch (Exception e) {
                        return; // Ignored - most likely a 401 error
                    }
                    for (Submission submission : latest25Posts) {
                        if (submission.getCreated().after(oneMinuteAgo)) {
                            try {
                                if (!(submission.getPreview().getImages().size() > 0)) return;
                            } catch (Exception e) {
                                return; // Ignored - Submission doesn't have images
                            }
                            if (submission.isNsfw()) return;
                            PostMemesWithDelay.addMemeToQueue(submission);
                        }
                    }
                //
            }
        }, 0, 60*1000);
    }

    public static void stopLoop() {
        timer.cancel();
    }
}
