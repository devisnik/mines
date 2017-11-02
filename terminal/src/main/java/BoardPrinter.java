import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;

import static org.fusesource.jansi.Ansi.Color.BLUE;
import static org.fusesource.jansi.Ansi.Color.CYAN;
import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.MAGENTA;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.Color.WHITE;

public class BoardPrinter {

    public BoardPrinter() {
        AnsiConsole.systemInstall();
    }

    public String print(IBoard board) {
        Point dimension = board.getDimension();
        final StringBuilder sb = new StringBuilder("\n");

        printColumnNumbers(dimension, sb);
        printLineSeparator(dimension, sb);

        for (int i = 0; i < dimension.y; i++) {
            if (dimension.y > 10)
                sb.append(i / 10).append(i % 10).append(" | ");
            else
                sb.append(i).append(" | ");
            for (int j = 0; j < dimension.x; j++) {
                IField field = board.getField(j, i);
                sb.append(print(field));
                sb.append(" ");
            }
            if (dimension.y > 10)
                sb.append("| ").append(i / 10).append(i % 10).append("\n");
            else
                sb.append("| ").append(i).append("\n");
        }

        printLineSeparator(dimension, sb);
        printColumnNumbers(dimension, sb);

        return sb.toString();
    }

    private void printLineSeparator(Point dimension, StringBuilder sb) {
        printNumberIndent(dimension, sb);
        for (int j = 0; j < dimension.x; j++) {
            sb.append("--");
        }
        sb.append("-\n");
    }

    private void printNumberIndent(Point dimension, StringBuilder sb) {
        if (dimension.y > 10)
            sb.append("    ");
        else
            sb.append("   ");
    }

    private void printColumnNumbers(Point dimension, StringBuilder sb) {
        if (dimension.x > 10) {
            printNumberIndent(dimension, sb);
            for (int j = 0; j < dimension.x; j++) {
                sb.append(" ").append(j / 10);
            }
            sb.append("\n");
        }
        printNumberIndent(dimension, sb);
        for (int j = 0; j < dimension.x; j++) {
            sb.append(" ").append(j % 10);
        }
        sb.append("\n");
    }

    private String print(IField field) {
        return IMAGE_CHARS[field.getImage()];
    }

    private static final String FLAG = "\u26F3";
    private static final String BOMB = "\uD83D\uDCA3";

    private static final String[] IMAGE_CHARS = {
            " ",
            fg(BLUE, "1"),
            fg(GREEN, "2"),
            fg(RED, "3"),
            fg(CYAN, "4"),
            fg(MAGENTA, "5"),
            fg(WHITE, "6"),
            fg(WHITE, "7"),
            fg(WHITE, "8"),
            BOMB,
            "\u25FD",
            FLAG,
            fg(RED, "\u26D4"),
            "\uD83D\uDD25"
    };

    private static String fg(Ansi.Color color, String character) {
        return Ansi.ansi().fg(color).a(character).reset().toString();
    }
}
