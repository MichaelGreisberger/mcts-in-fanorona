package Fanorona;

import Fanorona.Board.Board;
import Fanorona.Board.BoardSize;
import Fanorona.Move.Move;
import Fanorona.Move.MoveList;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This class was used to analyse the branching factor of Fanorona games played with this Framework.
 */
public class Analytics {

    private int[] branchingCountPerPiece = new int[44];
    private int[] branchingSumPerPiece = new int[44];
    private Map<Integer, List<Integer>> singleBranchingFactorsPerPiecesOnBoard = new HashMap<>();
    private Map<String, String> highBfStates = new HashMap<>();

    public static void main(String[] args) {
        File dir = new File("L:\\UNI\\IT\\BAC_Bachelor_Arbeit\\PerformanceTests\\Analysis\\data");
        List<String> path = new LinkedList<>();
        for (String file : dir.list()) {
            int startExtIndex = file.lastIndexOf('.');
            if (startExtIndex > 0) {
                if (file.substring(startExtIndex, file.length()).equals(".csv")) {
                    path.add(dir.getAbsolutePath() + File.separator + file);
                }
            }
        }
        Analytics analytics = new Analytics();
        try {
            for (int i = 0; i < path.size(); i++) {
                List<List<Move>> games = analytics.GetAllGamesFromResultCsv(analytics.ReadCsv(path.get(i)));
                analytics.AnalyzeGames(games);
            }
            System.out.print("over all branching factor: ");
            System.out.println(analytics.getAvgBranchingFactor());
            System.out.format("  %10s%10s%10s%10s%10s%10s", "bfpp", "stdpp", "cpp", "spp", "min", "max");
            System.out.println();
            double[] bfpp = analytics.getAvgBranchingFactPerPiece();
            double[] stdpp = analytics.getStdPerPiece(bfpp);
            int[] mins = new int[44];
            int[] maxs = new int[44];

            for (int i = 0; i < 44; i++) {
                mins[i] = analytics.min(analytics.singleBranchingFactorsPerPiecesOnBoard.get(i));
                maxs[i] = analytics.max(analytics.singleBranchingFactorsPerPiecesOnBoard.get(i));
            }

            DecimalFormat df = new DecimalFormat("###0.00");
            for (int i = 0; i < 44; i++) {
                System.out.format("%2d%10s%10s%10d%10d%10d%10d", (i + 1), df.format(bfpp[i]), df.format(stdpp[i]), analytics.branchingCountPerPiece[i], analytics.branchingSumPerPiece[i], mins[i], maxs[i]);
                System.out.println();
            }
            System.out.println(analytics.getPythonStyleListString());
            for (String b64S : analytics.highBfStates.values()) {
                Board board = Board.fromB64(b64S, BoardSize.LARGE);
                MoveList possibleMoves = board.getPossibleMoves();
                int pieceCount = board.getBlackPieces() + board.getWhitePieces();
                System.out.println("with " + pieceCount + " pieces on Board there are " + possibleMoves.size() + " possible");
                System.out.println(board.toPrintString());
                System.out.println(possibleMoves);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Analytics() {
        initSinglePiecesMap();
    }

    private void initSinglePiecesMap() {
        for (int i = 0; i < 44; i++) {
            singleBranchingFactorsPerPiecesOnBoard.put(i, new LinkedList<Integer>());
        }
    }

    private void printAllValuesOfSingleBranchingFactor(int pieces) {
        for (int bf : singleBranchingFactorsPerPiecesOnBoard.get(pieces)) {
            System.out.println(bf);
        }
    }

    public void AnalyzeGames(List<List<Move>> games) {
        for (List<Move> game : games) {
            AnalyzeGame(game);
        }
    }

    private int min(Iterable<Integer> values) {
        int min = Integer.MAX_VALUE;
        for (int val :
                values) {
            min = Integer.min(val, min);
        }
        return min;
    }

    private int max(Iterable<Integer> values) {
        int max = Integer.MIN_VALUE;
        for (int val :
                values) {
            max = Integer.max(val, max);
        }
        return max;
    }

    public void AnalyzeGame(List<Move> moves) {
        Board board = new Board(BoardSize.LARGE);
        for (Move move : moves) {
            MoveList possibleMoves = board.getPossibleMoves();
            int pieceCount = board.getBlackPieces() + board.getWhitePieces() - 1;
            if (possibleMoves.size() > 50) {
                if (!highBfStates.containsKey(board.toPrintString())) {
                    highBfStates.put(board.toPrintString(), board.getStateB64());
                }
            }
            singleBranchingFactorsPerPiecesOnBoard.get(pieceCount).add(possibleMoves.size());
            branchingSumPerPiece[pieceCount] += possibleMoves.size();
            branchingCountPerPiece[pieceCount]++;
            board = board.applyMove(move);
        }
    }

    public String getPythonStyleListString() {
        double[] bfpp = getAvgBranchingFactPerPiece();
        double[] stdpp = getStdPerPiece(bfpp);
        int[] mins = new int[44];
        int[] maxs = new int[44];

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < 44; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("(").append(i + 1).append(", ").append(bfpp[i]).append(", ").append(stdpp[i]).append(", ").append(branchingCountPerPiece[i]).append(", ").append(branchingSumPerPiece[i]).append(")");

            mins[i] = min(singleBranchingFactorsPerPiecesOnBoard.get(i));
            maxs[i] = max(singleBranchingFactorsPerPiecesOnBoard.get(i));
        }
        sb.append("]");
        sb.append("\n\n\n------------------------------------------------------------------------------------------------------------\n\n\n");
        sb.append("[").append(Arrays.stream(bfpp).mapToObj(String::valueOf).collect(Collectors.joining(", "))).append("]").append("\n\n");
        sb.append("[").append(Arrays.stream(stdpp).mapToObj(String::valueOf).collect(Collectors.joining(", "))).append("]").append("\n\n");
        sb.append("[").append(Arrays.stream(mins).mapToObj(String::valueOf).collect(Collectors.joining(", "))).append("]").append("\n\n");
        sb.append("[").append(Arrays.stream(maxs).mapToObj(String::valueOf).collect(Collectors.joining(", "))).append("]").append("\n\n\n\n");
        sb.append("[").append(Arrays.stream(branchingCountPerPiece).mapToObj(String::valueOf).collect(Collectors.joining(", "))).append("]").append("\n\n\n\n");
        sb.append("[").append(Arrays.stream(branchingSumPerPiece).mapToObj(String::valueOf).collect(Collectors.joining(", "))).append("]").append("\n\n\n\n");

        return sb.toString();
    }

    public double[] getAvgBranchingFactPerPiece() {
        double[] ret = new double[44];
        for (int i = 0; i < 44; i++) {
            if (branchingCountPerPiece[i] != 0) {
                ret[i] = ((double) branchingSumPerPiece[i]) / branchingCountPerPiece[i];
            } else {
                ret[i] = 0;
            }
        }
        return ret;
    }

    public double getAvgBranchingFactor() {
        int branchingSum = 0;
        int branchingCount = 0;
        for (int i = 0; i < 44; i++) {
            branchingCount += branchingCountPerPiece[i];
            branchingSum += branchingSumPerPiece[i];
        }
        return ((double) branchingSum) / branchingCount;
    }

    public double[] getStdPerPiece(double[] avgs) {
        double[] stdPP = new double[44];
        for (int pieces = 0; pieces < 44; pieces++) {
            int count = singleBranchingFactorsPerPiecesOnBoard.get(pieces).size();
            if (count > 0) {
                int sumSqareDiffs = 0;
                for (int branchingFactor : singleBranchingFactorsPerPiecesOnBoard.get(pieces)) {
                    sumSqareDiffs += Math.pow(branchingFactor - (avgs[pieces]), 2);
                }
                stdPP[pieces] = Math.sqrt(sumSqareDiffs / count);
            } else {
                stdPP[pieces] = 0;
            }
        }
        return stdPP;
    }

    public List<String> ReadCsv(String path) throws IOException {
        List<String> lines = new LinkedList<>();
        File file = new File(path);
        BufferedReader br = new BufferedReader(new FileReader(file));
        String st;
        while ((st = br.readLine()) != null) {
            lines.add(st);
        }
        return lines;
    }


    public List<List<Move>> GetAllGamesFromResultCsv(List<String> lines) {
        int gameStringIndex = 8;
        List<List<Move>> games = new LinkedList<>();
        for (String line : lines) {
            String[] items = line.split(";");
            String gameString = items[gameStringIndex];
            games.add(parseGameString(gameString));
        }
        return games;
    }

    private List<Move> parseGameString(String gameString) {
        List<Move> game = new LinkedList<>();
        for (String move : gameString.split(", ")) {
            game.add(new Move(move));
        }
        return game;
    }
}
