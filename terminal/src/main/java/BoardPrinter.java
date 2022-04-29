import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;

public class BoardPrinter {

    private static final String FLAG = "\u26F3";
    private static final String BOMB = "\uD83D\uDCA3";
    private static final String COVER = "\u25FD";
    private static final String FIRE = "\uD83D\uDD25";

    private static final String RED = "\u001b[31m";
    private static final String GREEN = "\u001b[32m";
    private static final String BLUE = "\u001b[34m";
    private static final String CYAN = "\u001b[36m";
    private static final String MAGENTA = "\u001b[35m";
    private static final String WHITE = "\u001b[37m";
    private static final String RESET = "\u001b[0m";

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
            sb.append("---");
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
                sb.append("  ").append(j / 10);
            }
            sb.append("\n");
        }
        printNumberIndent(dimension, sb);
        for (int j = 0; j < dimension.x; j++) {
            sb.append("  ").append(j % 10);
        }
        sb.append("\n");
    }

    private String print(IField field) {
        return IMAGE_CHARS[field.getImage()];
    }

    private static final String[] IMAGE_CHARS = {
            "  ",
            fg(BLUE, " 1"),
            fg(GREEN, " 2"),
            fg(RED, " 3"),
            fg(CYAN, " 4"),
            fg(MAGENTA, " 5"),
            fg(WHITE, " 6"),
            fg(WHITE, " 7"),
            fg(WHITE, " 8"),
            BOMB,
            COVER,
            FLAG,
            fg(RED, "\u26D4"),
            FIRE
    };

    private static String fg(String color, String character) {
        return color + character + RESET;
    }
}
