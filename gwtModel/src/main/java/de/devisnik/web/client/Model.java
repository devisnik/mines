package de.devisnik.web.client;
import jsinterop.annotations.JsType;
import de.devisnik.mine.SimpleGameFactory;
import de.devisnik.mine.IGame;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;

@JsType(namespace="mines")
public class Model {

    private final IGame game;

    public Model(int width, int height, int bombs) {
       game = SimpleGameFactory.create(width, height, bombs);
    }

    public void open(int x, int y) {
        IField field = game.getBoard().getField(x,y);
        game.onRequestOpen(field);
    }

    public void flag(int x, int y) {
        IField field = game.getBoard().getField(x,y);
        game.onRequestFlag(field);
    }

    public String state() {
        return game.toString();
    }

    public int[][] board() {
        Point dim = game.getBoard().getDimension();
        int[][] board = new int[dim.y][dim.x];
        for(int y=0; y<dim.y; y++) {
            int[] row = new int[dim.x];
            for(int x=0; x<dim.x; x++) {
                row[x] = game.getBoard().getField(x,y).getImage();
            }
            board[y] = row;
        }
        return board;
    }

    public int[] numbers() {
        return new int[] {1, 2, 3, 4, 5};
    }

    public static void main(String[] args) {
        Model model = new Model(5,5,5);
        model.open(0,0);
        System.out.println(model.board());
        System.out.println(model.state());
    }
}
