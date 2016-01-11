import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import static org.fusesource.jansi.Ansi.Color.*;

public class BoardPrinter {

    public BoardPrinter() {
        AnsiConsole.systemInstall();
    }

    public String print(IBoard board) {
        Point dimension = board.getDimension();
        final StringBuilder sb = new StringBuilder("\n");
        sb.append("  ");
        for (int j = 0; j < dimension.x; j++) {
            sb.append(" ").append(j);
        }
        sb.append("\n");
        sb.append("  ");
        for (int j = 0; j < dimension.x; j++) {
            sb.append("--");
        }
        sb.append("-\n");
        for (int i = 0; i < dimension.y; i++) {
            sb.append(i).append("| ");
            for (int j = 0; j < dimension.x; j++) {
                IField field = board.getField(j, i);
                sb.append(print(field));
                sb.append(" ");
            }
            sb.append("|").append(i).append("\n");
        }
        sb.append("  ");
        for (int j = 0; j < dimension.x; j++) {
            sb.append("--");
        }
        sb.append("-\n");
        sb.append("  ");
        for (int j = 0; j < dimension.x; j++) {
            sb.append(" ").append(j);
        }
        sb.append("\n");
        return sb.toString();
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
