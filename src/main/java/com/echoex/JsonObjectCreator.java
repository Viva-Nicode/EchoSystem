package com.echoex;
import org.json.simple.JSONObject;

public class JsonObjectCreator {
	public static String createJsonObjectString(EchoPacketCode c, String sound) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("packetId", c);
		jsonObject.put("message", sound.split(",")[0]);
		jsonObject.put("loopNum", sound.split(",")[1]);
		return jsonObject.toJSONString();
	}
}
