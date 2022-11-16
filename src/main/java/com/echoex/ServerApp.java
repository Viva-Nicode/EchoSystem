package com.echoex;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerApp {
	public static void main(String[] args) {
		try {
			ServerSocket ss = new ServerSocket(12345);
			System.out.println("client connection waiting...");
			Socket s = ss.accept();
			System.out.println("client connection success!");
			Thread t = new Thread(new ServerStateMachine(s));
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
