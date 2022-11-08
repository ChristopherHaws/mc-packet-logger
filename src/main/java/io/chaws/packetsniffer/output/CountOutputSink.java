package io.chaws.packetsniffer.output;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressWarnings("unused")
public class CountOutputSink implements OutputSink {
	private final ConcurrentLinkedQueue<String> lines = new ConcurrentLinkedQueue<>();

	@Override
	public void appendLine(final @NotNull String line) {
		lines.add(line);
	}

	@Override
	public @Nullable String generateMessage() {
		if (lines.isEmpty()) {
			return null;
		}

		var sb = new StringBuilder();
		var counts = this.getCounts();
		final AtomicBoolean first = new AtomicBoolean(true);

		counts.forEach((string, count) -> {
			if (!first.get()) {
				sb.append('\n');
			}

			sb.append(count).append("x ").append(string);

			if (first.get()) {
				first.set(false);
			}
		});

		return sb.toString();
	}

	private HashMap<String, Integer> getCounts() {
		var counts = new HashMap<String, Integer>();

		while (true) {
			var string = lines.poll();
			if (string == null) {
				break;
			}

			counts.compute(string, (key, value) -> {
				if (value == null) {
					return 1;
				}

				return value + 1;
			});
		}

		return counts;
	}
}
