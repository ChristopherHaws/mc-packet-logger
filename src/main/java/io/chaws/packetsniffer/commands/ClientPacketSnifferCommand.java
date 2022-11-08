package io.chaws.packetsniffer.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.chaws.packetsniffer.config.PacketSnifferConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

@Environment(EnvType.CLIENT)
public class ClientPacketSnifferCommand {
	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		var command = literal("packetsniffer")
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
		dispatcher.register(literal("ps").executes(command.getCommand()));
	}

	private static int toggle(FabricClientCommandSource source) {
		PacketSnifferConfig.enabled = !PacketSnifferConfig.enabled;

		if (PacketSnifferConfig.enabled) {
			source.sendFeedback(Text.literal("Packet sniffing enabled"));
		} else {
			source.sendFeedback(Text.literal("Packet sniffing disabled"));
		}

		return 0;
	}

	private static int list(FabricClientCommandSource source) {
		source.sendFeedback(Text.literal("Registered Logger Filters:"));

		return 0;
	}

	private static int include(FabricClientCommandSource source, String string) {
		if (PacketSnifferConfig.inclusions.contains(string)) {
			source.sendFeedback(Text.literal("Packet inclusion has already been added"));
		} else {
			PacketSnifferConfig.inclusions.add(string);
			source.sendFeedback(Text.literal("Packet inclusion added"));
		}

		return 0;
	}

	private static int exclude(FabricClientCommandSource source, String string) {
		if (PacketSnifferConfig.exclusions.contains(string)) {
			source.sendFeedback(Text.literal("Packet exclusion has already been added"));
		} else {
			PacketSnifferConfig.exclusions.add(string);
			source.sendFeedback(Text.literal("Packet exclusion added"));
		}

		return 0;
	}

	private static int clear(FabricClientCommandSource source) {
		PacketSnifferConfig.inclusions.clear();
		PacketSnifferConfig.exclusions.clear();
		source.sendFeedback(Text.literal("Packet inclusions and exclusions cleared"));

		return 0;
	}
}
