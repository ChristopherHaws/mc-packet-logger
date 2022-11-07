package me.chaws.packetlogger.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.chaws.packetlogger.config.PacketLoggerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

@Environment(EnvType.SERVER)
public class ServerPacketLoggerCommand {
	public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
		var command = literal("serverpacketlogger")
			.then(literal("toggle")
				.executes(ctx -> toggle(ctx.getSource()))
			).then(literal("list")
				.executes(ctx -> list(ctx.getSource()))
			).then(literal("include").then(argument("string", string())
				.executes(ctx -> include(ctx.getSource(), getString(ctx, "string")))
			)).then(literal("exclude").then(argument("string", string())
				.executes(ctx -> exclude(ctx.getSource(), getString(ctx, "string")))
			)).then(literal("clear")
				.executes(ctx -> clear(ctx.getSource()))
			);

		dispatcher.register(command);
		dispatcher.register(literal("spl").executes(command.getCommand()));
	}

	private static int toggle(ServerCommandSource source) {
		PacketLoggerConfig.enabled = !PacketLoggerConfig.enabled;

		if (PacketLoggerConfig.enabled) {
			source.sendFeedback(Text.literal("Packet logging enabled"), false);
		} else {
			source.sendFeedback(Text.literal("Packet logging disabled"), false);
		}

		return 0;
	}

	private static int list(ServerCommandSource source) {
		source.sendFeedback(Text.literal("Registered Logger Filters:"), false);

		return 0;
	}

	private static int include(ServerCommandSource source, String string) {
		if (PacketLoggerConfig.inclusions.contains(string)) {
			source.sendFeedback(Text.literal("Packet inclusion has already been added"), false);
		} else {
			PacketLoggerConfig.inclusions.add(string);
			source.sendFeedback(Text.literal("Packet inclusion added"), false);
		}

		return 0;
	}

	private static int exclude(ServerCommandSource source, String string) {
		if (PacketLoggerConfig.exclusions.contains(string)) {
			source.sendFeedback(Text.literal("Packet exclusion has already been added"), false);
		} else {
			PacketLoggerConfig.exclusions.add(string);
			source.sendFeedback(Text.literal("Packet exclusion added"), false);
		}

		return 0;
	}

	private static int clear(ServerCommandSource source) {
		PacketLoggerConfig.inclusions.clear();
		PacketLoggerConfig.exclusions.clear();
		source.sendFeedback(Text.literal("Packet inclusions and exclusions cleared"), false);

		return 0;
	}
}
