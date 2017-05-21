package orchestrator;

import graphics.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class TwitchColorChangeObserver implements Observer {
    private static final Logger LOG = LoggerFactory.getLogger(TwitchColorChangeObserver.class);

    private final Map<String, Consumer<Arguments>> operations = new HashMap<>();

    public TwitchColorChangeObserver(Display display) {
        operations.put("!draw", (args -> display.setColour(args.getInt(0), args.getInt(1), args.getString(2).toUpperCase())));
    }

    public void notify(String message) {
        String[] parts = message.split("\\s+");
        if (!operations.containsKey(parts[0])) return;
        if (parts.length >= 4) {
            String[] args = new String[parts.length - 1];
            System.arraycopy(parts, 1, args, 0, parts.length - 1);
            executeOperation(parts[0], new Arguments(args));
        }
    }

    private void executeOperation(String operation, Arguments args) {
        try {
            operations.get(operation).accept(args);
        } catch (Exception e) {
            LOG.debug("Unable to execute operation {} with args {}", operation, args.arguments);
        }
    }

    class Arguments {
        String[] arguments;

        public Arguments(String[] arguments) {
            this.arguments = arguments;
        }

        public int getInt(int i) {
            return Integer.parseInt(arguments[i]);
        }

        public String getString(int i) {
            return arguments[i];
        }

    }
}
