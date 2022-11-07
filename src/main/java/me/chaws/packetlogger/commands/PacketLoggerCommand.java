package me.chaws.packetlogger.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.LinkedList;
import java.util.List;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class PacketLoggerCommand {
	public static boolean enabled = true;
	public static List<String> registeredLoggerFilters = new LinkedList<>();

	public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
		if (MinecraftClient.getInstance().world == null) {
			return;
		}

		var isClient = MinecraftClient.getInstance().world.isClient;
		var command = literal(isClient ? "packetlogger" : "serverpacketlogger")
			.then(literal("toggle")
				.executes(ctx -> toggle(ctx.getSource()))
			).then(literal("list")
				.executes(ctx -> list(ctx.getSource()))
			).then(literal("add")
				.executes(ctx -> add(ctx.getSource(), getString(ctx, "startswith")))
			).then(literal("remove")
				.executes(ctx -> remove(ctx.getSource(), getString(ctx, "startswith")))
			);

		dispatcher.register(command);
		dispatcher.register(literal(isClient ? "pl" : "spl").executes(command.getCommand()));
	}

	private static int toggle(FabricClientCommandSource source) {
		enabled = !enabled;

		if (enabled) {
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

	private static int add(FabricClientCommandSource source, String startsWith) {
		if (registeredLoggerFilters.contains(startsWith)) {
			source.sendFeedback(Text.literal("Packet logger filter has already been added"));
		} else {
			registeredLoggerFilters.add(startsWith);
			source.sendFeedback(Text.literal("Packet logger filter added"));
		}

		return 0;
	}

	private static int remove(FabricClientCommandSource source, String startsWith) {
		if (registeredLoggerFilters.contains(startsWith)) {
			registeredLoggerFilters.remove(startsWith);
			source.sendFeedback(Text.literal("Packet logger filter removed"));
		} else {
			source.sendFeedback(Text.literal("Packet logger filter could not be found"));
		}

		return 0;
	}
}
