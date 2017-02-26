package de.devisnik.web.client;
import jsinterop.annotations.JsType;
import de.devisnik.mine.SimpleGameFactory;
import de.devisnik.mine.IGame;
import de.devisnik.mine.IField;
import de.devisnik.mine.Point;
import de.devisnik.mine.robot.AutoPlayer;

@JsType(namespace="mines")
public class Model {

    private final IGame game;
    private final AutoPlayer robot;

    public Model(int width, int height, int bombs) {
       game = SimpleGameFactory.create(width, height, bombs);
       robot = new AutoPlayer(game, true);
    }

    public void open(int x, int y) {
        IField field = game.getBoard().getField(x,y);
        game.onRequestOpen(field);
    }

    public void flag(int x, int y) {
        IField field = game.getBoard().getField(x,y);
        if (!game.isRunning()) {
            game.onRequestOpen(field);
        }
        else {
            game.onRequestFlag(field);
        }
    }

    public boolean isRunning() {
        return game.isRunning();
    }

    public boolean isExploded() {
        return game.getBoard().isExploded();
    }

    public void clockTick() {
        game.tickWatch();
    }

    public void robotMove() {
        robot.doNextMove();
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

    public int bombCount() {
        return game.getBombCount() - game.getBoard().getFlagCount();
    }

    public int time() {
        return game.getWatch().getTime();
    }

    public static void main(String[] args) {
        Model model = new Model(5,5,5);
        model.flag(0,0);
        model.robotMove();
        System.out.println(model.board());
        System.out.println(model.state());
        System.out.println(model.bombCount());
    }
}
