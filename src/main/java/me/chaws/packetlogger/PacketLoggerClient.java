package me.chaws.packetlogger;

import me.chaws.packetlogger.commands.ClientPacketLoggerCommand;
import me.chaws.packetlogger.utils.PacketCounter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
public class PacketLoggerClient implements ClientModInitializer {
	private static int ticksSinceLastBroadcast = 0;

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
			ClientPacketLoggerCommand.register(dispatcher);
		}));

		ClientTickEvents.END_WORLD_TICK.register(PacketLoggerClient::onEndTick);
	}

	private static void onEndTick(ClientWorld world) {
		ticksSinceLastBroadcast++;

		if (ticksSinceLastBroadcast >= 10) {
			ticksSinceLastBroadcast = 0;

			PacketCounter.sendPacketCountMessage();
		}
	}
}
