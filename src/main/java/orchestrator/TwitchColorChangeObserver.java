package orchestrator;

import graphics.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class TwitchColorChangeObserver implements Observer {
    private static final Logger LOG = LoggerFactory.getLogger(TwitchColorChangeObserver.class);

    private static final String draw = "!draw";

    // isApplicable -> createArgument -> execute operation with argument.
    private final Map<String, Function<String[], Boolean>> isApplicable = new HashMap<>();
    private final Map<String, Function<String[], Arguments>> createArgument = new HashMap<>();
    private final Map<String, Consumer<Arguments>> operations = new HashMap<>();

    public TwitchColorChangeObserver(Display display) {
        setupDrawCommand(display);
    }

    public void notify(String message) {
        String[] parts = message.split("\\s+");
        if (isApplicable.getOrDefault(draw, (args -> false)).apply(parts)) {
            executeOperation(draw, createArgument.get(draw).apply(parts));
        }
    }

    private void executeOperation(String operation, Arguments args) {
        try {
            operations.get(operation).accept(args);
        } catch (Exception e) {
            LOG.debug("Unable to execute operation {} with args {}", operation, args.arguments);
        }
    }

    private void setupDrawCommand(Display display) {
        // draw argument is applicable if 4 arguments or more, and 0th argument equals draw command.
        isApplicable.put(draw, (args -> {
            if (args.length < 4) return false;
            if (!draw.equals(args[0])) return false;
            return true;
        }));
        // include all arguments except first. Since first is !draw.
        createArgument.put(draw, (args -> {
            String[] subArgs = new String[args.length - 1];
            System.arraycopy(args, 1, subArgs, 0, args.length - 1);
            return new Arguments(subArgs);
        }));
        // operation should set colour accordingly.
        operations.put(draw, (args -> display.setColour(args.getInt(0), args.getInt(1), args.getString(2).toUpperCase())));
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
