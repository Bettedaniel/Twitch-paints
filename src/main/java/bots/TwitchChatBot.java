package bots;

import orchestrator.Observer;
import org.jibble.pircbot.PircBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class TwitchChatBot extends PircBot {
    private static final Logger LOG = LoggerFactory.getLogger(TwitchChatBot.class);

    private static final String TWITCH_IRC_HOST = "irc.chat.twitch.tv";
    private static final int TWITCH_IRC_PORT = 6667;

    private List<Observer> observers = new ArrayList<>();

    public TwitchChatBot(String password) {
        joinIRC(password);
    }

    @Override
    public void onMessage(String channel, String sender, String login, String hostname, String message) {
        notifyObservers(message);
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    private void joinIRC(String password) {
        try {
            this.connect(TWITCH_IRC_HOST, TWITCH_IRC_PORT, password);
        } catch (Exception e) {
            LOG.error("Unable to connect to twitch irc on {}:{}", TWITCH_IRC_HOST, TWITCH_IRC_PORT, e);
        }
    }

    private void notifyObservers(String message) {
        observers.forEach(observer -> observer.notify(message));
    }

}
