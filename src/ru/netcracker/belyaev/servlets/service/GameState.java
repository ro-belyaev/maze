package ru.netcracker.belyaev.servlets.service;

import java.util.ArrayList;
import java.util.List;

public class GameState {
	private List<PlayerState> players;
	private List<MoveState> moves;
	private String lastValueOfMovesInTable;
	
	public GameState() {
		players = new ArrayList<>();
		moves = new ArrayList<>();
	}
	public void addPlayer(PlayerState player) {
		this.players.add(player);
	}
	public void addMove(MoveState move) {
		this.moves.add(move);
	}
	public void setLastValueOfMovesInTable(String val) {
		this.lastValueOfMovesInTable = val;
	}
	
}
