package com.echoex;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import static java.lang.System.out;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ClientStateMachine implements Runnable {
	private EchoClientState state = EchoClientState.S0;
	private Boolean exitMachine = false; // End state machine
	private String sound;
	private BufferedReader bufferedReader;
	private PrintWriter printWriter;
	private Socket socket = null;

	public void run() {
		while (exitMachine == false) {
			switch (state) {
				case S1:
					sound = getUserMessage();
					if (sound.equals("exit"))
						state = EchoClientState.S6;
					state = EchoClientState.S2;
					break;
				case S2:
					sendPacket(EchoPacketCode.REQ_YAHO, sound);
					state = EchoClientState.S3;
					break;
				case S3:
					while (true) {
						JSONObject rPacket = receivePacket();
						if (rPacket.get("packetId") == EchoPacketCode.ACK_YAHO) {
							out.println(rPacket.get("message"));
						} else {
							state = EchoClientState.S1;
							break;
						}
					}
					break;
				case S6:
					sendPacket(EchoPacketCode.REQ_END, null);
					state = EchoClientState.S7;
				case S7:
					JSONObject rPacket = receivePacket();
					if (rPacket.get("packetId") == EchoPacketCode.ACK_END) {
						state = EchoClientState.S8;
					}
					break;
				case S8:
					exitMachine = true;
			}
		}
	}

	public String getUserMessage() {
		out.println("input message and loopNum >> ");
		return new Scanner(System.in).nextLine();
	}

	public JSONObject receivePacket() {
		try {
			String request = bufferedReader.readLine();
			return (JSONObject) new JSONParser().parse(request);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void sendPacket(EchoPacketCode c, String sound) {
		String jsonString = JsonObjectCreator.createJsonObjectString(c, sound);
		printWriter.println(jsonString);
		printWriter.flush();
	}

	public ClientStateMachine() throws UnknownHostException, IOException {
		this.socket = new Socket("localhost", 12345);
		this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.printWriter = new PrintWriter(socket.getOutputStream());
	}

}