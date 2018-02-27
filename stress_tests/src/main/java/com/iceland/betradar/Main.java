package com.iceland.betradar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sound.midi.SysexMessage;

import io.socket.client.IO;
import io.socket.client.Socket;

public class Main {

	public static void main(String[] args) throws URISyntaxException, InterruptedException {
		Integer personCount = 500;
		ExecutorService simulatorExecutor = Executors.newFixedThreadPool(personCount);

		for(int i=0 ; i < personCount ; i++) {
			System.out.println("Person started : " + i);
			Thread.sleep(300);
			simulatorExecutor.submit(() -> {
				System.out.println("Simulation started : ");
				Socket icelandUsers = null;
				try{
					icelandUsers = IO.socket("http://173.209.59.66:3000/icelandUsers");
					icelandUsers.on(Socket.EVENT_CONNECT, args1 -> {
						System.out.println("Connected");
					}).on(Socket.EVENT_DISCONNECT, args1 -> {
						System.out.println("Disconnected");
					}).on(ProtocolConstants.BET_RADAR_CLOCK, objects -> {
						System.out.println("BET_RADAR_CLOCK received");
					}).on(ProtocolConstants.BET_RADAR_ALIVE_HEALTH_STATUS, objects -> {
						System.out.println("BET_RADAR_ALIVE_HEALTH_STATUS received");
					}).on(ProtocolConstants.BET_RADAR_CLIENT_ON_INITIALIZED, objects -> {
						System.out.println("BET_RADAR_CLIENT_ON_INITIALIZED received");
					}).on(ProtocolConstants.BET_RADAR_CLIENT_DISCONNECTED, objects -> {
						System.out.println("BET_RADAR_CLIENT_DISCONNECTED received");
					}).on(ProtocolConstants.ALIVE_REMOVED, objects -> {
						System.out.println("ALIVE_REMOVED received");
					}).on(ProtocolConstants.ALIVE_CHANGED, objects -> {
						System.out.println("ALIVE_CHANGED received");
					}).on(ProtocolConstants.ALIVE_NEW_ALIVE_EXISTING_TOURNAMENT_RECEIVED, objects -> {
						System.out.println("ALIVE_NEW_ALIVE_EXISTING_TOURNAMENT_RECEIVED received");
					}).on(ProtocolConstants.ALIVE_NEW_ALIVE_EXISTING_CATEGORY, objects -> {
						System.out.println("ALIVE_NEW_ALIVE_EXISTING_CATEGORY received");
					}).on(ProtocolConstants.ALIVE_NEW_ALIVE_EXISTING_SPORT, objects -> {
						System.out.println("ALIVE_NEW_ALIVE_EXISTING_SPORT received");
					}).on(ProtocolConstants.ALIVE_NEW_SPORT, objects -> {
						System.out.println("ALIVE_NEW_SPORT received");
					}).on(ProtocolConstants.ON_SCORE_CARD_RECEIVED, objects -> {
						System.out.println("ON_SCORE_CARD_RECEIVED received");
					}).on(ProtocolConstants.ON_ODDS_CHANGE, objects -> {
						System.out.println("ON_ODDS_CHANGE received");
					}).on(ProtocolConstants.PRIME_ODDS_CHANGE, objects -> {
						System.out.println("PRIME_ODDS_CHANGE received");
					}).on(ProtocolConstants.PRIME_ODDS_DELETED, objects -> {
						System.out.println("PRIME_ODDS_DELETED received");
					}).on(ProtocolConstants.ON_BET_CANCEL, objects -> {
						System.out.println("ON_BET_CANCEL received");
					}).on(ProtocolConstants.ON_BET_CANCEL_UNDONE, objects -> {
						System.out.println("ON_BET_CANCEL_UNDONE received");
					}).on(ProtocolConstants.ON_BET_CLEAR, objects -> {
						System.out.println("ON_BET_CLEAR received");
					}).on(ProtocolConstants.ON_BET_CLEAR_ROLL_BACK, objects -> {
						System.out.println("ON_BET_CLEAR_ROLL_BACK received");
					}).on(ProtocolConstants.ON_BET_START, objects -> {
						System.out.println("ON_BET_START received");
					}).on(ProtocolConstants.ON_BET_STOP, objects -> {
						System.out.println("ON_BET_STOP received");
					}).on(ProtocolConstants.ON_EVENT_MESSAGE_RECEIVED, objects -> {
						System.out.println("ON_EVENT_MESSAGE_RECEIVED received");
					}).on(ProtocolConstants.ON_EVENT_STATUS_RECEIVED, objects -> {
						System.out.println("ON_EVENT_STATUS_RECEIVED received");
					}).on(ProtocolConstants.ON_IRRELEVANT_ODDS_CHANGE, objects -> {
						System.out.println("ON_IRRELEVANT_ODDS_CHANGE received");
					});
					icelandUsers.connect();
					Thread.sleep(120000);
					icelandUsers.disconnect();
				} catch (URISyntaxException e){
					e.printStackTrace();
				} catch (InterruptedException e){
					e.printStackTrace();
				}
			});
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try {
			reader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
