import bots.TwitchChatBot;
import graphics.Display;
import util.Input;
import graphics.PixelatedCanvas;
import orchestrator.TwitchColorChangeObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        int width = getWidth(args);
        int height = getHeight(args);
        String channel = getChannel(args);
        LOG.debug("Width: {}, height: {}, channel: {}", width, height, channel);

        PixelatedCanvas canvas = new PixelatedCanvas(width, height);
        Display display = new Display(canvas);
        TwitchColorChangeObserver twitchColorChangeObserver = new TwitchColorChangeObserver(display);
        String oauth = readOAuth();
        if (oauth == null) {
            LOG.info("No oauth found. Exiting.");
            return;
        }
        TwitchChatBot twitchChatBot = new TwitchChatBot(oauth);
        twitchChatBot.registerObserver(twitchColorChangeObserver);
        twitchChatBot.joinChannel(checkChannel(channel.toLowerCase()));
    }

    private static String checkChannel(String channel) {
        if ('#' != channel.charAt(0)) return "#" + channel;
        return channel;
    }

    // Change the way OAuth is read.
    private static String readOAuth() {
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            FileInputStream fileInputStream = new FileInputStream(classLoader.getResource("account.properties").getFile());
            properties.load(fileInputStream);
            return (String) properties.get("oauth");
        } catch (Exception e) {
            LOG.warn("Unable to read oauth property!", e);
        }
        return null;
    }

    // Look for width from cmd line argument, if not there, prompt for it.
    private static int getWidth(String... args) {
        if (args.length > 1) {
            return Integer.parseInt(args[1]);
        }
        return Input.promptForInteger("Width");
    }

    // Look for height from cmd line argument, if not there, prompt for it.
    private static int getHeight(String... args) {
        if (args.length > 2) {
            return Integer.parseInt(args[2]);
        }
        return Input.promptForInteger("Height");
    }

    // Look for channel from cmd line argument, if not there, prompt for it.
    private static String getChannel(String... args) {
        if (args.length > 3) {
            return args[3];
        }
        return Input.prompt("Channel");
    }
}
