package de.devisnik.mine.impl;

public class InitialBoardState extends BoardState {

    InitialBoardState(int dimX, int dimY) {
        super(dimX, dimY, new int[dimX * dimY]);
    }

}
