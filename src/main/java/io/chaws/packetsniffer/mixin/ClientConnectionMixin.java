package io.chaws.packetsniffer.mixin;

import io.chaws.packetsniffer.config.PacketSnifferConfig;
import io.chaws.packetsniffer.utils.PacketCounter;
import net.minecraft.network.*;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
	@Shadow
	@Final
	private NetworkSide side;

	@Inject(method = "sendImmediately", at = @At("HEAD"))
	private void logSentPacket(Packet<?> packet, @Nullable PacketCallbacks callbacks, CallbackInfo ci) {
		if (!PacketSnifferConfig.enabled) {
			return;
		}

		var sideName = getSideName(side);

		logPacket(sideName + "->", packet);
	}

	@Inject(method = "handlePacket", at = @At("HEAD"))
	private static void logReceivedPacket(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
		if (!PacketSnifferConfig.enabled) {
			return;
		}

		var side = ((ClientConnectionAccessor)listener.getConnection()).getSide();
		var sideName = getSideName(side);

		logPacket("->" + sideName, packet);
	}

	private static void logPacket(String direction, Packet<?> packet) {
		var channel = getChannel(packet);
		var packetName = StringUtils.removeStart(packet.getClass().getName(), "net.minecraft.network.packet.");

		if (PacketSnifferConfig.inclusions.stream().noneMatch(packetName::contains)){
			return;
		}

		if (PacketSnifferConfig.exclusions.stream().anyMatch(packetName::contains)){
			return;
		}

		if (channel != null) {
			PacketCounter.add(direction + " " + channel + " " + packetName);
		} else {
			PacketCounter.add(direction + " " + packetName);
		}
	}

	private static String getSideName(NetworkSide side) {
		if (side == NetworkSide.CLIENTBOUND) return "s";
		if (side == NetworkSide.SERVERBOUND) return "c";

		return String.valueOf(side.name().toLowerCase(Locale.ROOT).charAt(0));
	}

	@Nullable
	private static Identifier getChannel(Packet<?> packet) {
		if (packet instanceof CustomPayloadC2SPacketAccessor) {
			return ((CustomPayloadC2SPacketAccessor) packet).getChannel();
		} else if (packet instanceof CustomPayloadS2CPacketAccessor) {
			return ((CustomPayloadS2CPacketAccessor) packet).getChannel();
		}
		return null;
	}
}
