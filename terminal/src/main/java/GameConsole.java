import de.devisnik.mine.*;

import java.util.Timer;
import java.util.TimerTask;

public final class GameConsole {

    private static final BoardPrinter BOARD_PRINTER = new BoardPrinter();

    private Timer timer;
    private IGame game;

    public GameConsole() {
        newGame(10, 10, 10);
    }

    public void newGame(int width, int height, int bombs) {
        if (timer != null) timer.cancel();
        game = GameFactory.create(width, height, bombs);
        game.addListener(new MinesGameAdapter() {
            @Override
            public void onDisarmed() {
                System.out.println("Congrats, you win!");
                timer.cancel();
            }

            @Override
            public void onBusted() {
                System.out.println("Sorry, you exploded!");
                timer.cancel();
            }

            @Override
            public void onStart() {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        game.tickWatch();
                    }
                }, 1000, 1000);
            }
        });
    }

    public void open(int x, int y) {
        if (!game.isRunning() && game.isStarted()) {
            System.out.println("No active Game!");
            return;
        }
        game.onRequestOpen(game.getBoard().getField(x, y));
    }

    public void flag(int x, int y) {
        if (!game.isRunning() && game.isStarted()) {
            System.out.println("No active Game!");
            return;
        }
        game.onRequestFlag(game.getBoard().getField(x, y));
    }

    public void status() {
        print(game);
    }

    private static void print(IGame game) {
        System.out.println("Playtime: " + game.getWatch().getTime() + " seconds");
        System.out.println((game.getBombCount() - game.getBoard().getFlagCount())
                + " of " + game.getBombCount()
                + " Flags left to put!");

        System.out.println(toString(game));
    }

    private static String toString(IGame game) {
        return BOARD_PRINTER.print(game.getBoard());
    }

    public void auto() {
        new de.devisnik.mine.robot.AutoPlayer(game, false).doNextMove();
    }

    public IGame getGame() {
        return game;
    }
}
