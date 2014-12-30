package de.devisnik.mine.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StatePersistence {

	static byte[] toByteArray(int number) {
		// ByteBuffer buffer = ByteBuffer.allocate(4);
		// buffer.putInt(number);
		// return buffer.array();

		byte[] bytes = new byte[4];
		for (int index = 0; index < bytes.length; index++) {
			bytes[index] = (byte) (number & 0xff);
			number >>>= 8;
		}
		return bytes;
	}

	static int fromByteArray(byte[] bytes) {
		// ByteBuffer buffer = ByteBuffer.allocate(4);
		// buffer.put(bytes);
		// return buffer.getInt(0);

		int value = 0;
		for (int index = 0; index < bytes.length; index++)
			value += (bytes[index] & 0xff) << (8 * index);
		return value;
	}

	static int readInt(InputStream stream) throws IOException {
		byte[] bytes = new byte[4];
		stream.read(bytes);
		return fromByteArray(bytes);
	}

	static void writeInt(int value, OutputStream stream) throws IOException {
		stream.write(toByteArray(value));
	}

	public static void writeBoolean(boolean value, OutputStream stream)
			throws IOException {
		stream.write(value ? 1 : 0);
	}

	public static void writeBoardState(BoardState boardState,
			OutputStream stream) throws IOException {
		writeInt(boardState.getDimX(), stream);
		writeInt(boardState.getDimY(), stream);
		for (int x = 0; x < boardState.getDimX(); x++)
			for (int y = 0; y < boardState.getDimY(); y++)
				stream.write(boardState.getFieldState(x, y));
	}

	public static BoardState readBoardState(InputStream stream)
			throws IOException {
		int dimX = readInt(stream);
		int dimY = readInt(stream);
		int[] fieldStates = new int[dimX * dimY];
		for (int x = 0; x < dimX; x++)
			for (int y = 0; y < dimY; y++)
				fieldStates[x * dimY + y] = stream.read();
		return new BoardState(dimX, dimY, fieldStates);
	}

	public static boolean readBoolean(InputStream stream) throws IOException {
		return stream.read() != 0;
	}

	public static void writeGameState(GameState state, OutputStream stream)
			throws IOException {
		writeInt(state.getWatchState(), stream);
		writeInt(state.getBombsCount(), stream);
		writeBoolean(state.isMined(), stream);
		writeBoardState(state.getBoardState(), stream);
	}

	public static GameState readGameState(InputStream stream)
			throws IOException {
		int watchState = readInt(stream);
		int bombsCount = readInt(stream);
		boolean isMined = readBoolean(stream);
		BoardState boardState = readBoardState(stream);
		return new GameState(boardState, bombsCount, isMined, watchState);
	}
}
