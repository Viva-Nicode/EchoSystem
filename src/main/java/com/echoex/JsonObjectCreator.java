package com.echoex;

import org.json.simple.JSONObject;

public class JsonObjectCreator {
	public static String createJsonObjectString(EchoPacketCode c, String sound) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("packetId", c + "");
		jsonObject.put("message", sound.split(",")[0]);
		jsonObject.put("loopNum", sound.split(",")[1]);
		return jsonObject.toJSONString();
	}

	public static String createJsonObjectString(String sound) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("packetId", "ACK_YAHO");
		jsonObject.put("message", sound);
		return jsonObject.toJSONString();
	}

	public static String createJsonObjectString(String loopEnd, String sound) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("packetId", loopEnd);
		return jsonObject.toJSONString();
	}
}
