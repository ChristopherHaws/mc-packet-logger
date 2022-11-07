package me.chaws.packetlogger.output;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

public class RawOutputSink implements OutputSink {
	private final ConcurrentLinkedQueue<String> lines = new ConcurrentLinkedQueue<>();

	@Override
	public void appendLine(final @NotNull String line) {
		var timeFormat = DateTimeFormatter.ofPattern("HH:mm:ss");
		var time = LocalDateTime.now();
		lines.add(time.format(timeFormat) + " " + line);
	}

	@Override
	public @Nullable String generateMessage() {
		if (this.lines.isEmpty()) {
			return null;
		}

		var sb = new StringBuilder();
		var first = true;

		while (true) {
			var line = lines.poll();
			if (line == null) {
				break;
			}

			if (!first) {
				sb.append('\n');
			}

			sb.append(line);

			if (first) {
				first = false;
			}
		}

		return sb.toString();
	}
}
