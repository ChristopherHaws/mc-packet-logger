package io.chaws.packetsniffer.utils;

import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class StringUtils {
	public static @NotNull String fromBytes(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		sb.append("[ ");
		for (byte b : bytes) {
			sb.append(String.format("0x%02X ", b));
		}
		sb.append("]");
		return sb.toString();
	}
}
