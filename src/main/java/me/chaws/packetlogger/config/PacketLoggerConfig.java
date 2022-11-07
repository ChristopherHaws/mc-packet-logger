package me.chaws.packetlogger.config;

import java.util.LinkedList;
import java.util.List;

public class PacketLoggerConfig {
	public static boolean enabled = false;
	public static List<String> inclusions = new LinkedList<>(List.of(
		"c2s",
		"s2c"
	));
	public static List<String> exclusions = new LinkedList<>(List.of(
		// c2s
		"PlayerMove",
		"KeepAlive",
		"ClientCommand",
		"UpdatePlayerAbilities",
		"RequestCommandCompletions",
		"RequestChatPreview",

		// s2c
		"ClientSettings",
		"Entity",
		"EntitySetHead",
		"EntitySpawn",
		"EntityTracker",
		"EntitiesDestroy",
		"PlaySound",
		"PlayerList",
		"WorldTimeUpdate",
		"WorldEvent",
		"BlockUpdate",
		"GameStateChange",

		// mine
		"HandSwing"
	));
}
