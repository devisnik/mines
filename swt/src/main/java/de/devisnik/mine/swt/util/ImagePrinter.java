package de.devisnik.mine.swt.util;

import java.io.FileInputStream;
import java.io.IOException;

public class ImagePrinter {
	public void print() throws IOException {
		FileInputStream stream = new FileInputStream("F:\\gwt\\workspace\\gwtMines\\src\\de\\devisnik\\web\\client\\ui\\image_13.gif");
		int counter = 0;
		while (stream.available() > 0) {
			if (counter % 8 == 0) {
				System.out.println();
			}
			counter++;
			int byteValue = stream.read();
			System.out.print("(byte) 0x"+getHexString(byteValue)+",");
		}
	}
	
	private String getHexString(int value) {
		int v = value;
		if (v < 0) v+=128;
		return Integer.toHexString(v/16)+Integer.toHexString(v%16);
	}
	
	public static void main(String[] args) throws IOException {
		new ImagePrinter().print();
	}
}
