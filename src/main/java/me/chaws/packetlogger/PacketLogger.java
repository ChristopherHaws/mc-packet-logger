package me.chaws.packetlogger;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PacketLogger implements ModInitializer {
	@SuppressWarnings("unused")
	public static final Logger logger = LogManager.getLogger("packet-logger");

	@Override
	public void onInitialize() { }
}
