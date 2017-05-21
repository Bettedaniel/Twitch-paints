import bots.TwitchChatBot;
import graphics.Display;
import graphics.PixelatedCanvas;
import orchestrator.TwitchColorChangeObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.util.Properties;

public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);

    private static final int DEFAULT_WIDTH = 500;
    private static final int DEFAULT_HEIGHT = 500;
    private static final String DEFAULT_CHANNEL = "#nanilul";

    public static void main(String[] args) {
        PixelatedCanvas canvas = new PixelatedCanvas(readWidth(args), readHeight(args));
        Display display = new Display(canvas);
        TwitchColorChangeObserver twitchColorChangeObserver = new TwitchColorChangeObserver(display);
        String oauth = readOAuth();
        if (oauth == null) {
            LOG.info("No oauth found. Exiting.");
            return;
        }
        TwitchChatBot twitchChatBot = new TwitchChatBot(oauth);
        twitchChatBot.registerObserver(twitchColorChangeObserver);
        twitchChatBot.joinChannel(readChannel(args));
    }

    private static int readWidth(String[] args) {
        int width = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_WIDTH;
        LOG.info("Selected width {}", width);
        return width;
    }

    private static int readHeight(String[] args) {
        int height = args.length > 2 ? Integer.parseInt(args[2]) : DEFAULT_HEIGHT;
        LOG.info("Selected height {}", height);
        return height;
    }

    private static String readChannel(String[] args) {
        String channel = args.length > 3 ? args[3] : DEFAULT_CHANNEL;
        LOG.info("Selected channel {}", channel);
        return channel;
    }

    private static String readOAuth() {
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = Main.class.getClassLoader();
            FileInputStream fileInputStream = new FileInputStream(classLoader.getResource("application.properties").getFile());
            properties.load(fileInputStream);
            return (String) properties.get("oauth");
        } catch (Exception e) {
            LOG.warn("Unable to read oauth property!", e);
        }
        return null;
    }
}
