package Fanorona;

import Fanorona.Board.Board;
import Fanorona.Board.BoardSize;
import Fanorona.Player.MsgDelegates.SchaddsPlayerCommunicationDelegate;
import Fanorona.Player.Player;
import Fanorona.Player.PlayerWrapper;
import Fanorona.Player.RandomMctsPlayer;
import Fanorona.Player.SteeredMaterialPlayer;
import Fanorona.Player.SteeredMctsPlayer;
import Fanorona.Player.SteeredRandomMctsPlayer;
import Fanorona.Player.StreamPlayer;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

public class Main {
    public static String version = "0.1.4 - PUCT!";
    private static Map<String, String> defaults = new TreeMap<>();

    /**
     * Main entry point for the application. Initializes the game from the Console.
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            initDefaults();
            List<String> argsList = List.of(args);
            Game game = null;
            try {
                int numberOfGames = Integer.parseInt(getArgValueOrDefault(argsList, "ng"));
                game = initGameFromArgs(argsList);
                int x = 0;
                while (x < numberOfGames) {
                    try {
                        game.startGame();
                        game.switchSeats();
                    } catch (Exception e) {
                        System.out.println("An Error occurred during execution of the game!");
                        System.out.println(e.getMessage());
                        e.printStackTrace();
                    }
                    x++;
                }
            } catch (NoSuchElementException | SocketException e) {
                //connection lost
            } catch (Exception e) {
                e.printStackTrace();
                printUsage(argsList);
            }
            if (game != null) {
                game.shutDown();
            }
        } else {
            try {
                Game game = initializeFromStreams();
                game.startGame();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * Initializes all available default-values
     */
    private static void initDefaults() {
        defaults.put("-p1t", "utc");
        defaults.put("-p2t", "utc");
        defaults.put("-p1m", "5000");
        defaults.put("-p2m", "5000");
        defaults.put("-p1n", "white");
        defaults.put("-p2n", "black");
        defaults.put("-p", "32667");
        defaults.put("-s", "l");
        defaults.put("-p1Ef", "1 / 2^(1/2)");
        defaults.put("-p2Ef", "1 / 2^(1/2)");
        defaults.put("-p1D", "10");
        defaults.put("-p2D", "10");
        defaults.put("-d", "7");
        defaults.put("-sp1t", "1");
        defaults.put("-sp2t", "1");
        defaults.put("-ng", "1");
    }

    /**
     * Initializes the game with the provided streams in Question/Answer kind of way so only use this for streams that
     * are somehow connected to either a human being or HAL 9000.
     */
    public static Game initializeFromStreams() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PlayerWrapper p1;
        PlayerWrapper p2;
        boolean verbose = true;
        if (answerWasYes("Do you want to play against AI?", in)) {
            if (answerWasYes("Would you like to be the starting player?", in)) {
                System.out.println("Whats your Name?");
                p1 = new PlayerWrapper(new StreamPlayer(System.in, System.out), in.readLine().trim(), "white");
                p2 = new PlayerWrapper(new SteeredMctsPlayer(1000, true), "AI", "black");
            } else {
                System.out.println("Whats your Name?");
                p1 = new PlayerWrapper(new SteeredMctsPlayer(1000, true), "AI", "white");
                p2 = new PlayerWrapper(new StreamPlayer(System.in, System.out), in.readLine().trim(), "black");
            }
        } else {
            if (answerWasYes("Do you want to play against another human player?", in)) {
                System.out.println("Whats the name of player1?");
                p1 = new PlayerWrapper(new StreamPlayer(System.in, System.out), in.readLine().trim(), "white");
                System.out.println("Whats the name of player2?");
                p2 = new PlayerWrapper(new StreamPlayer(System.in, System.out), in.readLine().trim(), "black");
            } else {
                if (answerWasYes("Do you want to watch two AI players play against each other?!", in)) {
                    if (!answerWasYes("Do you want to see moves and player outputs?", in)) {
                        verbose = false;
                    }
                    p1 = new PlayerWrapper(new SteeredMctsPlayer(1500, verbose), "UCT 1", "white");
                    p2 = new PlayerWrapper(new SteeredMctsPlayer(1500, verbose), "UCT 2", "black");
                } else {
                    System.out.println("What the fuck do you want than?!?");
                    System.out.println("AHH never mind.. I'm out!");
                    throw new RuntimeException("Game doesn't want to play with you any longer.");
                }
            }
        }
        Game game = new Game(BoardSize.LARGE, verbose, new LinkedList<>(), false);
        game.player1 = p1;
        game.player2 = p2;
        return game;
    }

    /**
     * Writes the {@param question} to the Console and reads the answer. If the answer started with "y" then true is returned.
     *
     * @param question The question to answer
     * @return the answer to the question
     */
    private static boolean answerWasYes(String question, BufferedReader in) throws IOException {
        System.out.println(question + " (y/n)");
        return in.readLine().toLowerCase().trim().startsWith("y");
    }

    /**
     * Prints usage-information to the command line
     * @param argsList
     */
    private static void printUsage(List<String> argsList) {
        System.out.println();
        System.out.println("Invalid arguments in " + String.join(" ", argsList));
        System.out.println("Usage: You can either start this application by providing command line arguments or without any and answer some questions in order to configure the game");
        printOptions();
        printDefaults();
    }

    /**
     * Prints all available default options plus their values to the command line
     */
    private static void printDefaults() {
        System.out.println();
        System.out.println("If you omit certain options the following defaults are used:");
        for (Map.Entry<String, String> e : defaults.entrySet()) {
            System.out.println(printOptionString(e.getKey(), e.getValue()));
        }
    }

    /**
     * Prints all possible options to the command line
     */
    private static void printOptions() {
        System.out.println();
        System.out.println("This are the currently available options (every usage of 'x' stands for a player number):");
        System.out.println(printOptionString("-s:", "the size of the board."));
        System.out.println(printOptionString("-v:", "the game writes information to the console."));
        System.out.println(printOptionString("-pxt:", "player x is this type of player. Possible values: uct, random, human, socket"));
        System.out.println(printOptionString("-pxn:", "player x has the following name."));
        System.out.println(printOptionString("-pxm:", "player x has that much time (in milliseconds) to make a decision (only effective for computer player)"));
        System.out.println(printOptionString("-uctpxEf:", "player x applies this exploration factor. (only effective for uct computer player)"));
        System.out.println(printOptionString("-pxv:", "the game writes output regarding the decision of player x to the console (only effective for computer player)"));
        System.out.println(printOptionString("-p:", "the port that's used to initialize the socket player. (only effective for socket player)"));
        System.out.println(printOptionString("-is:", "the initial state of the board."));
        System.out.println(printOptionString("-d:", "depth of alpha better player (only effective for socket player)."));

    }

    /**
     * Initializes the game from arguments
     *
     * @param argsList command line arguments
     * @return the initialized game
     */
    private static Game initGameFromArgs(List<String> argsList) throws Exception {
        BoardSize size = getBoardSizeFromArgs(argsList);
        boolean verbose = argsList.contains("-v");
        Game game = getGameWithInitialStateOrFromArgs(argsList, size, verbose);
        game.player1 = new PlayerWrapper(initPlayerFromArgs(argsList, "p1"), getArgValueOrDefault(argsList, "p1n"), "white");
        game.player2 = new PlayerWrapper(initPlayerFromArgs(argsList, "p2"), getArgValueOrDefault(argsList, "p2n"), "black");
        return game;
    }

    /**
     * prints an option string and description
     *
     * @param optionName
     * @param text
     * @return
     */
    private static String printOptionString(String optionName, String text) {
        return String.format("\t%1$-15s%2$s", optionName, text);
    }

    /**
     * Initializes a Game from arguments with an initial state
     *
     * @param argsList arguments
     * @param size     board size
     * @param verbose  verbosity of the game
     * @return the initialized game
     */
    @NotNull
    private static Game getGameWithInitialStateOrFromArgs(List<String> argsList, BoardSize size, boolean verbose) throws Exception {
        Game game;
        if (argsList.contains("-is")) {
            game = new Game(Board.fromB64(getArgValueOrDefault(argsList, "is"), size), argsList, argsList.contains("-wo"));
        } else {
            game = new Game(size, verbose, argsList, argsList.contains("-wo"));
        }
        return game;
    }

    /**
     * Initializes a player from command line arguments
     *
     * @param argsList   arguments
     * @param playerName player number (p1, p2), needed for argument-retrieval
     * @return the initialized player
     */
    private static Player initPlayerFromArgs(List<String> argsList, String playerName) throws Exception {
        int millisToRun = Integer.parseInt(getArgValueOrDefault(argsList, playerName + "m"));
        String playerType = getArgValueOrDefault(argsList, playerName + "t");

        switch (playerType) {
            case "uct":
                if (argsList.contains(playerName + "Ef")) {
                    double ef = Double.parseDouble(getArgValueOrDefault(argsList, playerName + "Ef"));
                    return new SteeredMctsPlayer(millisToRun, argsList.contains("-" + playerName + "v"), ef, "uct");
                } else {
                    return new SteeredMctsPlayer(millisToRun, argsList.contains("-" + playerName + "v"));
                }
            case "uctR":
                if (argsList.contains(playerName + "Ef")) {
                    double ef = Double.parseDouble(getArgValueOrDefault(argsList, playerName + "Ef"));
                    return new SteeredRandomMctsPlayer(millisToRun, argsList.contains("-" + playerName + "v"), ef, Integer.parseInt(getArgValueOrDefault(argsList, playerName + "D")));
                } else {
                    return new SteeredRandomMctsPlayer(millisToRun, argsList.contains("-" + playerName + "v"), Integer.parseInt(getArgValueOrDefault(argsList, playerName + "D")));
                }
            case "uctM":
                if (argsList.contains(playerName + "Ef")) {
                    double ef = Double.parseDouble(getArgValueOrDefault(argsList, playerName + "Ef"));
                    return new SteeredMaterialPlayer(millisToRun, argsList.contains("-" + playerName + "v"), ef, Integer.parseInt(getArgValueOrDefault(argsList, playerName + "D")), "uct");
                } else {
                    return new SteeredMaterialPlayer(millisToRun);
                }
            case "puctM":
                if (argsList.contains(playerName + "Ef")) {
                    double ef = Double.parseDouble(getArgValueOrDefault(argsList, playerName + "Ef"));
                    return new SteeredMaterialPlayer(millisToRun, argsList.contains("-" + playerName + "v"), ef, Integer.parseInt(getArgValueOrDefault(argsList, playerName + "D")), "puct");
                } else {
                    return new SteeredMaterialPlayer(millisToRun, argsList.contains("-" + playerName + "v"), Integer.parseInt(getArgValueOrDefault(argsList, playerName + "D")), "puct");
                }
            case "puct":
                if (argsList.contains(playerName + "Ef")) {
                    double ef = Double.parseDouble(getArgValueOrDefault(argsList, playerName + "Ef"));
                    return new SteeredMctsPlayer(millisToRun, argsList.contains("-" + playerName + "v"), ef, "puct");
                } else {
                    return new SteeredMctsPlayer(millisToRun, argsList.contains("-" + playerName + "v"), "puct");
                }
            case "random":
                return new RandomMctsPlayer(millisToRun, argsList.contains("-" + playerName + "v"));
            case "socket":
                int port = Integer.parseInt(getArgValueOrDefault(argsList, "p"));
                int alphaBetaDepth = Integer.parseInt(getArgValueOrDefault(argsList, "d"));
                return StreamPlayer.initFromSocket(port, alphaBetaDepth);
            case "schadd":
                String executionPath = System.getProperty("user.dir");
                int schaddPlayerType = Integer.parseInt(getArgValueOrDefault(argsList, "s" + playerName + "t"));
                return StreamPlayer.initWithExecutable(millisToRun, "\"" + executionPath + "\\Fanorona_improved.jar\"", new SchaddsPlayerCommunicationDelegate(), schaddPlayerType, argsList.contains("-" + playerName + "v"));
            case "human":
                return new StreamPlayer(System.in, System.out);
            default:
                throw new Exception("Unknown type of player");
        }

    }

    /**
     * Returns the value of a certain argument or its default value
     *
     * @param argsList arguments
     * @param argName  name of the argument
     * @return the argument value
     */
    private static String getArgValueOrDefault(List<String> argsList, String argName) throws Exception {
        argName = "-" + argName;
        if (argsList.contains(argName)) {
            return argsList.get(argsList.indexOf(argName) + 1);
        } else if (defaults.containsKey(argName)) {
            return defaults.get(argName);
        } else {
            throw new Exception("Unknown option: " + argName);
        }
    }

    /**
     * Returns the board size as defined in the arguments or its default value
     *
     * @param argsList arguments
     * @return the size of the board
     */
    private static BoardSize getBoardSizeFromArgs(List<String> argsList) throws Exception {
        switch (getArgValueOrDefault(argsList, "s")) {
            case "s":
                return BoardSize.SMALL;
            case "m":
                return BoardSize.MEDIUM;
            case "l":
                return BoardSize.LARGE;
            default:
                System.out.println("Invalid board size");
                return BoardSize.LARGE;
        }
    }
}
