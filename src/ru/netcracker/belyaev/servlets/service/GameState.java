package ru.netcracker.belyaev.servlets.service;

import java.util.ArrayList;
import java.util.List;

public class GameState {
	private List<PlayerInGame> players;
	private List<MoveInGame> moves;
	
	public GameState() {
		players = new ArrayList<>();
		moves = new ArrayList<>();
	}
	public void addPlayer(PlayerInGame player) {
		this.players.add(player);
	}
	public void addMove(MoveInGame move) {
		this.moves.add(move);
	}
	
}
