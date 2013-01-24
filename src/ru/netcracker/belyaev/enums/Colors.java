package ru.netcracker.belyaev.enums;

public enum Colors {
	GREEN,
	BLUE,
	RED,
	YELLOW,
	ORANGE,
	VIOLET;
	
	public static int recognizeColorID(String colour) {
		try {
			return Colors.valueOf(colour.toUpperCase()).ordinal();
		} catch(IllegalArgumentException e) {
			return -1;
		}
	}
}
