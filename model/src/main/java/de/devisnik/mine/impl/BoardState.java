package de.devisnik.mine.impl;

import java.util.Arrays;

import de.devisnik.mine.Point;

public class BoardState {

    private int itsDimX;
    private int itsDimY;
    private int[] itsFieldStates;

    public static BoardState createFromBoard(Board board) {
        Point dimension = board.getDimension();
        int[] fieldStates = initStates(board);
        return new BoardState(dimension.x, dimension.y, fieldStates);
    }

    BoardState(int dimX, int dimY, int[] fieldStates) {
        if (fieldStates.length != dimX*dimY)
            throw new IllegalStateException("fieldStates must have length dimX*dimY");
        itsDimX = dimX;
        itsDimY = dimY;
        itsFieldStates = fieldStates;
    }

    private static int[] initStates(Board board) {
        Point dimension = board.getDimension();
        int[] fieldStates = new int[dimension.x*dimension.y];
        for (int x = 0; x < dimension.x; x++) {
            for (int y = 0; y < dimension.y; y++) {
                Field field = (Field) board.getField(x, y);
                fieldStates[x*dimension.y+y] = field.getState();
            }
        }
        return fieldStates;
    }

    public int getDimX() {
        return itsDimX;
    }

    public int getDimY() {
        return itsDimY;
    }

    public int getFieldState(int x, int y) {
        return itsFieldStates[x*itsDimY+y];
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + itsDimX;
        result = prime * result + itsDimY;
        result = prime * result + Arrays.hashCode(itsFieldStates);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof BoardState))
            return false;
        BoardState other = (BoardState) obj;
        if (itsDimX != other.itsDimX)
            return false;
        if (itsDimY != other.itsDimY)
            return false;
        if (!Arrays.equals(itsFieldStates, other.itsFieldStates))
            return false;
        return true;
    }

}
