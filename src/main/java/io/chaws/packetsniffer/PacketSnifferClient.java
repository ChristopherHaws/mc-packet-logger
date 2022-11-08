package io.chaws.packetsniffer;

import io.chaws.packetsniffer.commands.ClientPacketSnifferCommand;
import io.chaws.packetsniffer.utils.PacketCounter;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.world.ClientWorld;

@Environment(EnvType.CLIENT)
public class PacketSnifferClient implements ClientModInitializer {
	private static int ticksSinceLastBroadcast = 0;

	@Override
	public void onInitializeClient() {
		ClientCommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess) -> {
			ClientPacketSnifferCommand.register(dispatcher);
		}));

		ClientTickEvents.END_WORLD_TICK.register(PacketSnifferClient::onEndTick);
	}

	private static void onEndTick(ClientWorld world) {
		ticksSinceLastBroadcast++;

		if (ticksSinceLastBroadcast >= 10) {
			ticksSinceLastBroadcast = 0;

			PacketCounter.sendPacketCountMessage();
		}
	}
}
