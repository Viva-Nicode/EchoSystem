package com.echoex;

import java.io.IOException;

public class ClientApp {
	public static void main(String[] args) {
		try {
			Thread t = new Thread(new ClientStateMachine());
			t.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
