package me.chaws.packetlogger.mixin;

import net.minecraft.network.*;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Locale;

import static me.chaws.packetlogger.PacketLogger.sendMessage;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
	@Shadow
	@Final
	private NetworkSide side;

	@Inject(method = "sendImmediately", at = @At("HEAD"))
	private void logSentPacket(Packet<?> packet, @Nullable PacketCallbacks callbacks, CallbackInfo ci) {
		var sideName = getSideName(side);
		var channel = getChannel(packet);
		var packetName = packet.getClass().getName();

		if (channel != null) {
			sendMessage(sideName + " sent - " + channel + " - " + packetName);
		} else {
			sendMessage(sideName + " sent - " + packetName);
		}
	}

	@Inject(method = "handlePacket", at = @At("HEAD"))
	private static void logReceivedPacket(Packet<?> packet, PacketListener listener, CallbackInfo ci) {
		var side = ((ClientConnectionAccessor)listener.getConnection()).getSide();
		var sideName = getSideName(side);
		var channel = getChannel(packet);
		var packetName = packet.getClass().getName();

		if (channel != null) {
			sendMessage(sideName + " received - " + channel + " - " + packetName);
		} else {
			sendMessage(sideName + " received - " + packetName);
		}
	}

	private static String getSideName(NetworkSide side) {
		if (side == NetworkSide.CLIENTBOUND) return "server";
		if (side == NetworkSide.SERVERBOUND) return "client";

		return side.name().toLowerCase(Locale.ROOT);
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
