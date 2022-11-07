package me.chaws.packetlogger.output;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface OutputSink {
	void appendLine(@NotNull String line);

	@Nullable String generateMessage();
}
