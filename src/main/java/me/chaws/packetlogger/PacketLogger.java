package me.chaws.packetlogger;

import me.chaws.packetlogger.commands.PacketLoggerCommand;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketLogger implements ModInitializer {
	private static final Logger LOGGER = LogManager.getLogger("packet-logger");

	@Override
	public void onInitialize() {
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
			PacketLoggerCommand.register(dispatcher);
		}));
	}

	public static void sendMessage(String string) {
		var mc = MinecraftClient.getInstance();

		if (mc.world != null && mc.world.isClient) {
			var player = MinecraftClient.getInstance().player;
			if (player != null) {
				player.sendMessage(Text.of(string));
				return;
			}
		}

		LOGGER.info(string);
	}
}
