package de.devisnik.mine.impl;

public class InitialGameState extends GameState {

    InitialGameState(int dimX, int dimY, int bombs) {
        super(new InitialBoardState(dimX, dimY), bombs, false, 0);
    }
}
