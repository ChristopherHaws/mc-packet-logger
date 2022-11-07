package me.chaws.packetlogger.commands;

import com.mojang.brigadier.CommandDispatcher;
import me.chaws.packetlogger.config.PacketLoggerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class ClientPacketLoggerCommand {
	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		var command = literal("packetlogger")
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
		dispatcher.register(literal("pl").executes(command.getCommand()));
	}

	private static int toggle(FabricClientCommandSource source) {
		PacketLoggerConfig.enabled = !PacketLoggerConfig.enabled;

		if (PacketLoggerConfig.enabled) {
			source.sendFeedback(Text.literal("Packet logging enabled"));
		} else {
			source.sendFeedback(Text.literal("Packet logging disabled"));
		}

		return 0;
	}

	private static int list(FabricClientCommandSource source) {
		source.sendFeedback(Text.literal("Registered Logger Filters:"));

		return 0;
	}

	private static int include(FabricClientCommandSource source, String string) {
		if (PacketLoggerConfig.inclusions.contains(string)) {
			source.sendFeedback(Text.literal("Packet inclusion has already been added"));
		} else {
			PacketLoggerConfig.inclusions.add(string);
			source.sendFeedback(Text.literal("Packet inclusion added"));
		}

		return 0;
	}

	private static int exclude(FabricClientCommandSource source, String string) {
		if (PacketLoggerConfig.exclusions.contains(string)) {
			source.sendFeedback(Text.literal("Packet exclusion has already been added"));
		} else {
			PacketLoggerConfig.exclusions.add(string);
			source.sendFeedback(Text.literal("Packet exclusion added"));
		}

		return 0;
	}

	private static int clear(FabricClientCommandSource source) {
		PacketLoggerConfig.inclusions.clear();
		PacketLoggerConfig.exclusions.clear();
		source.sendFeedback(Text.literal("Packet inclusions and exclusions cleared"));

		return 0;
	}
}
