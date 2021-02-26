package Fanorona;

import Fanorona.Move.Move;
import Fanorona.Player.PlayerWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GameStatistics {
    private char csvSeperator = ';';
    private Date startingTime;
    private List<String> arguments;
    private PlayerWrapper p1;
    private PlayerWrapper p2;
    private String result;
    private String userdir = System.getProperty("user.dir");
    private String pathSeparator = File.separator;
    private String basePath = userdir + pathSeparator + "Games" + pathSeparator + "GameLog" + pathSeparator;
    private String newLine = "\n";
    private String tab = "\t";
    private String statFileName;


    public GameStatistics(String statFileName) {
        this.startingTime = Calendar.getInstance().getTime();
        this.statFileName = statFileName;
        System.out.println(basePath);
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void setArguments(List<String> arguments) {
        this.arguments = arguments;
    }

    public void setP1(PlayerWrapper p1) {
        this.p1 = p1;
    }

    public void setP2(PlayerWrapper p2) {
        this.p2 = p2;
    }


    public void setResult(String result) {
        this.result = result;
    }

    public String writeStatisticsToTextFile() {
        String filePath = basePath + Main.version + "_" + p1.name + "_VS_" + p2.name + "_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss").format(startingTime) + ".txt";
        String s = getPrintableString();
        writeFile(s, filePath);
        return s;
    }

    public String getPrintableString() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("This game started at: ").append(new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(startingTime)).append(newLine);
            sb.append("It was started with these arguments: ").append(arguments).append(newLine);
            sb.append("The result is: ").append(result).append(newLine);
            sb.append("player 1:").append(newLine);
            appendPlayerInfoText(sb, p1);
            sb.append("player 2:").append(newLine);
            appendPlayerInfoText(sb, p2);
            sb.append(MovesToString(p1, p2)).append(newLine);
            return sb.toString();
        } catch (Exception e) {
            return "An exception occurred while compiling textual output. " + e.getMessage();
        }
    }

    private void appendPlayerInfoText(StringBuilder sb, PlayerWrapper p) {
        sb.append(tab).append("name: ").append(p.name).append(newLine);
        sb.append(tab).append("time used: ").append(p.timeUsed).append(newLine);
        sb.append(tab).append("number of simulations: ").append(String.join(", ", p.simulatedGames)).append(newLine);
        sb.append(tab).append("number of stored states: ").append(String.join(", ", p.storedStates)).append(newLine);
    }

    private String MovesToString(PlayerWrapper p1, PlayerWrapper p2) {
        StringBuilder sb = new StringBuilder();
        Iterator<Move> movesP1 = p1.selectedMoves.iterator();
        Iterator<Move> movesP2 = p2.selectedMoves.iterator();

        while (true) {
            if (movesP1.hasNext()) {
                sb.append(movesP1.next()).append(", ");
            } else {
                break;
            }
            if (movesP2.hasNext()) {
                sb.append(movesP2.next()).append(", ");
            } else {
                break;
            }
        }
        return sb.toString();
    }

    public void writeStatisticsToCsvFile() {
        StringBuilder sb = new StringBuilder();
        csvAppend(sb, Main.version);
        csvAppend(sb, new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(startingTime));
        csvAppend(sb, String.join(", ", arguments));
        csvAppend(sb, result);
        csvAppend(sb, p1.name);
        csvAppend(sb, Double.toString(p1.timeUsed));
        csvAppend(sb, p2.name);
        csvAppend(sb, Double.toString(p2.timeUsed));
        csvAppend(sb, MovesToString(p1, p2));
        csvAppend(sb, String.join(", ", p1.simulatedGames));
        csvAppend(sb, String.join(", ", p1.storedStates));
        csvAppend(sb, String.join(", ", p2.simulatedGames));
        csvAppend(sb, String.join(", ", p2.storedStates));
        sb.append(newLine);
        writeFile(sb.toString(), basePath + statFileName);
    }

    private void csvAppend(StringBuilder sb, String text) {
        sb.append(text).append(csvSeperator);
    }

    private void writeFile(String content, String filepath) {
        FileOutputStream fileOutputStream = null;
        FileChannel channel = null;
        try {
            fileOutputStream = new FileOutputStream(filepath, true);
            channel = fileOutputStream.getChannel();
            FileLock lock = channel.lock();
            channel.write(ByteBuffer.wrap(content.getBytes()));
            lock.release();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
                if (channel != null) {
                    channel.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void reset() {
        this.startingTime = Calendar.getInstance().getTime();
        this.p1 = null;
        this.p2 = null;
        this.result = null;
    }
}
