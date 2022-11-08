package io.chaws.packetsniffer;

import io.chaws.packetsniffer.commands.ServerPacketSnifferCommand;
import io.chaws.packetsniffer.utils.PacketCounter;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.world.ServerWorld;

@Environment(EnvType.SERVER)
public class PacketSnifferServer implements DedicatedServerModInitializer {
	private static int ticksSinceLastBroadcast = 0;

	@Override
	public void onInitializeServer() {
		CommandRegistrationCallback.EVENT.register(((dispatcher, access, env) -> {
			ServerPacketSnifferCommand.register(dispatcher);
		}));

		ServerTickEvents.END_WORLD_TICK.register(PacketSnifferServer::onEndTick);
	}

	private static void onEndTick(ServerWorld world) {
		ticksSinceLastBroadcast++;

		if (ticksSinceLastBroadcast >= 10) {
			ticksSinceLastBroadcast = 0;

			PacketCounter.sendPacketCountMessage();
		}
	}
}
