package com.increff.pos.util;

public class StringUtil {

	public static boolean isEmpty(String s) {
		return s == null || s.trim().length() == 0;
	}

	public static String toLowerCase(String s) {
		return s == null ? null : s.trim().toLowerCase();
	}

	public static String trimZeros(String input) {
		input = input.trim();
		if(input == null || input == "") return "";
		if (input.contains(".")) {
			String[] parts = input.split("\\.");
			if(parts.length > 2) return input;
			String integerPart = parts[0].replaceAll("^0+", "");
			String decimalPart = parts[1].replaceAll("0+$", "");


			if (integerPart.isEmpty() && decimalPart.isEmpty()) {
				return "0";
			} else if (integerPart.isEmpty()) {
				return "0." + decimalPart;
			} else if (decimalPart.isEmpty()) {
				return integerPart;
			} else {
				return integerPart + "." + decimalPart;
			}
		} else {
			input = input.replaceAll("^0+", "");
			if(input.isEmpty()) return "0";
			return input;
		}
	}

}
