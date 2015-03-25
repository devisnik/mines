package de.devisnik.web.client.ui;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RootPanel;
import de.devisnik.mine.IGame;
import de.devisnik.mine.SimpleGameFactory;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Mines implements EntryPoint {

    private RootPanel minesPanel;

    /**
     * This is the entry point method.
     */
    public void onModuleLoad() {
        minesPanel = RootPanel.get("Mines");
        initButtonBar();
    }

    private void initButtonBar() {
        RootPanel buttonBar = RootPanel.get("ButtonBar");
        buttonBar.add(createGameButton(10, 10, 10));
        buttonBar.add(createGameButton(15, 15, 35));
        buttonBar.add(createGameButton(20, 20, 80));
    }

    private Button createGameButton(final int dimX, final int dimY,
                                    final int bombs) {
        Button button = new Button(Integer.toString(dimX) + " * " + Integer.toString(dimY));
        button.addClickHandler(new ClickHandler() {

            private GamePanel gamePanel;

            @Override
            public void onClick(ClickEvent event) {
                minesPanel.clear();
                if (gamePanel != null) {
                    gamePanel.dispose();
                }
                IGame game = SimpleGameFactory.create(dimX, dimY, bombs);
                gamePanel = createGamePanel(game);
                minesPanel.add(gamePanel);
            }
        });
        return button;
    }

    private GamePanel createGamePanel(final IGame game) {
        GamePanel vPanel = new GamePanel(game);
        vPanel.setWidth("100%");
        vPanel.setHeight("100%");
        return vPanel;
    }
}
