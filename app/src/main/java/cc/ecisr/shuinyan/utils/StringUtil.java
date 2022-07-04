package cc.ecisr.shuinyan.utils;

import java.util.Arrays;
import java.util.regex.Pattern;

public class StringUtil {
	
	/**
	 * 判斷字符串是否由純廿六英字字母([a-zA-Z])及數字([0-9])組成
	 * @param s 被檢驗的字符串
	 * @return {@code true}，若 {@code s} 由純字母及數字組成
	 */
	public static boolean isAsciiString(final String s) {
		Pattern pattern = Pattern.compile("^[\u0000-\u00FF]+$");
		return pattern.matcher(s).matches();
	}
	
	/**
	 * 判斷字符串是否由純廿六英字字母([a-zA-Z])及數字([0-9])組成
	 * @param s 被檢驗的字符串
	 * @return {@code true}，若 {@code s} 由純字母及數字組成
	 */
	public static boolean isAlphaString(final String s) {
		Pattern pattern = Pattern.compile("^[0-9a-zA-Z]+$");
		return pattern.matcher(s).matches();
	}
	
	public static <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
}
