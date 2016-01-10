import de.devisnik.mine.IBoard;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

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
                sb.append(Ansi.ansi().fg(Ansi.Color.GREEN).a(print(field)).reset());
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

    private static final String[] IMAGE_CHARS = {
            " ", "1", "2", "3", "4", "5", "6", "7", "8"
            , "b", "#", "f", "F", "B"};

}
