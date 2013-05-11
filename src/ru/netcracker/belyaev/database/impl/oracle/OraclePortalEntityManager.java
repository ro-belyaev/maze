package ru.netcracker.belyaev.database.impl.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.Portal;
import ru.netcracker.belyaev.model.entities.PortalEntity;
import ru.netcracker.belyaev.model.entities.River;
import ru.netcracker.belyaev.model.entities.Wall;
import ru.netcracker.belyaev.model.models.Game;

public class OraclePortalEntityManager {
	public static void registerPortalEntities(OracleConnection conn, List<PortalEntity> entities,
			String gameId) {
		String registerEntity = "begin insert into portal_entity " +
				"(game_id, entity_type, start_x, start_y, dest_x, dest_y, step) " +
				"values(?, ?, ?, ?, ?, ?, ?); end;";
		List<Portal> portals = null;
		for(PortalEntity entity : entities) {
			portals = entity.getPortals();
			for(Portal portal : portals) {
				String type = null;
				String entityClassName = entity.getClass().getSimpleName();
				if(entityClassName.equals("River")) {
					type = "river";
				} else if(entityClassName.equals("Wall")) {
					type = "wall";
				}
				String s_x = String.valueOf(portal.getStartPoint().getX());
				String s_y = String.valueOf(portal.getStartPoint().getY());
				String f_x = String.valueOf(portal.getDestinationPoint().getX());
				String f_y = String.valueOf(portal.getDestinationPoint().getY());
				String step = String.valueOf(portal.getStep());
				conn.executePreparedStatement(registerEntity, gameId, type, s_x, s_y, f_x, f_y, step);
			}
		}
	}
	
	public static void restorePortalEntities(OracleConnection conn, Game game, String gameId) {
		String restoreEntities = "select entity_type, start_x, start_y, " +
				"dest_x, dest_y, step from portal_entity where game_id=?";
		ResultSet rs = conn.executePreparedStatement(restoreEntities, gameId);
		Wall wall = null;
		River river = null;
		try {
			while(rs.next()) {
				String type = rs.getString(1);
				int startX = rs.getInt(2);
				int startY = rs.getInt(3);
				int destX = rs.getInt(4);
				int destY = rs.getInt(5);
				int step = rs.getInt(6);
				OnePointOnMap startPoint = new OnePointOnMap(startX, startY);
				OnePointOnMap destPoint = new OnePointOnMap(destX, destY);
				if(type.equals("wall")) {
					if(wall == null) {
						wall = new Wall();
					}
					wall.addPortal(startPoint, destPoint, step);
				} else if(type.equals("river")) {
					if(river == null) {
						river = new River();
					}
					river.addPortal(startPoint, destPoint, step);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		game.getBoard().setWall(wall);
		game.getBoard().setRiver(river);
	}
}
