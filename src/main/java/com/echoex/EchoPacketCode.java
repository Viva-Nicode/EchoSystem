package com.echoex;

public enum EchoPacketCode {
	REQ_YAHO(1), ACK_YAHO(2), REQ_END(3), ACK_END(4);

	private final int requestKind;

	EchoPacketCode(int requestKind) {
		this.requestKind = requestKind;
	}
};