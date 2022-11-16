package com.echoex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import static java.lang.Integer.*;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ServerStateMachine implements Runnable {
	private EchoServerState state = EchoServerState.SA;
	private Boolean exitMachine = false; // End state machine
	private String sound;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private Socket socket = null;
	private int loopNum = 0;

	public void run() {
		while (exitMachine == false) {
			switch (state) {
				case SA:
					JSONObject rPacket = receivePacket();
					if ((rPacket.get("packetId") + "").equals("REQ_YAHO")) {
						sound = rPacket.get("message") + "";
						loopNum = parseInt(rPacket.get("loopNum") + "");
						state = EchoServerState.SB;
					} else if (parseInt(rPacket.get("packetId") + "") == 3) {
						state = EchoServerState.SE;
					}
					break;
				case SB:
					if (loopNum != 0) {
						sendPacket(sound);
						loopNum--;
					} else {
						sendLoopEndPacket();
						state = EchoServerState.SD;
					}
					break;
				case SD:
					state = EchoServerState.SA;
					break;
				case SE:
					sendPacket(EchoPacketCode.ACK_END, null);
					state = EchoServerState.SF;
					break;
				case SF:
					exitMachine = true;
					break;
			}
		}
	}

	public JSONObject receivePacket() {
		try {
			String request = bufferedReader.readLine();
			System.out.println(request);
			return (JSONObject) new JSONParser().parse(request);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public ServerStateMachine(Socket c) {
		try {
			this.socket = c;
			this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			this.printWriter = new PrintWriter(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendPacket(EchoPacketCode c, String sound) {
		String jsonString = JsonObjectCreator.createJsonObjectString(c, sound);
		printWriter.println(jsonString);
		printWriter.flush();
	}

	public void sendLoopEndPacket() {
		String jsonString = JsonObjectCreator.createJsonObjectString("ACK_LOOPEND", "");
		printWriter.println(jsonString);
		printWriter.flush();
	}

	public void sendPacket(String sound) {
		String jsonString = JsonObjectCreator.createJsonObjectString(sound);
		printWriter.println(jsonString);
		printWriter.flush();
	}
}