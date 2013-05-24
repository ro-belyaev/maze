package ru.netcracker.belyaev.model.models;

import java.util.ArrayList;
import java.util.List;

import ru.netcracker.belyaev.enums.Direction;
import ru.netcracker.belyaev.enums.GameCases;
import ru.netcracker.belyaev.enums.PlayerActions;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;

public class InformerModel {
	private List<PrepareInformer> prepareInformer;
	private List<ActionInformer> actionInformer;
	private List <ResultInformer> resultInformer;
	private String currentPlayerName;
	
	public InformerModel(String playerName) {
		prepareInformer = new ArrayList<>();
		actionInformer = new ArrayList<>();
		resultInformer = new ArrayList<>();
		this.currentPlayerName = playerName;
	}
	
	public String getCurrentPlayerName() {
		return this.currentPlayerName;
	}
	
	public List<PrepareInformer> getPrepareInformer() {
		return this.prepareInformer;
	}
	public List<ActionInformer> getActionInformer() {
		return this.actionInformer;
	}
	public List<ResultInformer> getResultInformer() {
		return this.resultInformer;
	}
	
	public void addPrepareInformer(GameCases gameCase) {
		prepareInformer.add(new PrepareInformer(gameCase));
	}
	public void addPrepareInformer(GameCases gameCase, int colorId) {
		prepareInformer.add(new PrepareInformer(gameCase, colorId));
	}
	
	public void addActionInformer(PlayerActions action) {
		actionInformer.add(new ActionInformer(action));
	}
	public void addActionInformer(PlayerActions action, Direction direction) {
		actionInformer.add(new ActionInformer(action, direction));
	}
	public void addActionInformer(PlayerActions action, int colorId) {
		actionInformer.add(new ActionInformer(action, colorId));
	}
	
	public void addResultInformer(GameCases gameCase) {
		resultInformer.add(new ResultInformer(gameCase));
	}
	public void addResultInformer(GameCases gameCase, String victimName) {
		resultInformer.add(new ResultInformer(gameCase, victimName));
	}
	public void addResultInformer(GameCases gameCase, OnePointOnMap startPoint, OnePointOnMap destPoint) {
		int x1 = startPoint.getX();
		int y1 = startPoint.getY();
		int x2 = destPoint.getX();
		int y2 = destPoint.getY();
		resultInformer.add(new ResultInformer(gameCase, x1, y1, x2, y2));
	}
	

	public class PrepareInformer {
		private GameCases gameCase;
		private int treasureColorId;
		
		public PrepareInformer(GameCases gameCase) {
			this.gameCase = gameCase;
		}
		public PrepareInformer(GameCases gameCase, int treasureColorId) {
			this.gameCase = gameCase;
			this.treasureColorId = treasureColorId;
		}
		
		public GameCases getGameCase() {
			return this.gameCase;
		}
		public int getTreasureColorId() {
			return this.treasureColorId;
		}
	}
	
	public class ActionInformer {
		private PlayerActions playerAction;
		private Direction actionDirection;
		private int treasureColorId;
		
		public ActionInformer(PlayerActions action) {
			this.playerAction = action;
		}
		public ActionInformer(PlayerActions action, Direction direction) {
			this.playerAction = action;
			this.actionDirection = direction;
		}
		public ActionInformer(PlayerActions action, int colorId) {
			this.playerAction = action;
			this.treasureColorId = colorId;
		}
		public PlayerActions getPlayerAction() {
			return this.playerAction;
		}
		public Direction getActionDirection() {
			return this.actionDirection;
		}
		public int getTreasureColorId() {
			return this.treasureColorId;
		}
	}
	
	public class ResultInformer {
		private GameCases gameCase;
		private String victimName;
		private int x1, y1, x2, y2;
		
		public ResultInformer(GameCases gameCase) {
			this.gameCase = gameCase;
		}
		public ResultInformer(GameCases gameCase, String victimName) {
			this.gameCase = gameCase;
			this.victimName = victimName;
		}
		public ResultInformer(GameCases gameCase, int x1, int y1, int x2, int y2) {
			this.gameCase = gameCase;
			this.x1 = x1;
			this.y1 = y1;
			this.x2 = x2;
			this.y2 = y2;
		}
		
		public GameCases getGameCase() {
			return this.gameCase;
		}
		public int[] getCoordinates() {
			int[] coord = {this.x1, this.y1, this.x2, this.y2};
			return coord;
		}
		public String getVictimName() {
			return this.victimName;
		}
	}
}

