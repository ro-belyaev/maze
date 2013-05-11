package ru.netcracker.belyaev.database.impl.oracle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ru.netcracker.belyaev.model.entities.Arch;
import ru.netcracker.belyaev.model.entities.Exit;
import ru.netcracker.belyaev.model.entities.Mage;
import ru.netcracker.belyaev.model.entities.OnePointOnMap;
import ru.netcracker.belyaev.model.entities.SimpleEntity;
import ru.netcracker.belyaev.model.models.Game;

public class OracleSimpleEntityManager {
	public static void registerSimpleEntities(OracleConnection conn, List<SimpleEntity> entities,
			String gameId) {
		String registerEntity = "begin insert into simple_entity (game_id, entity_type, " +
				"position_x, position_y) values(?, ?, ?, ?); end;";
		for(SimpleEntity entity : entities) {
			List<OnePointOnMap> positions = entity.getEntity();
			for(OnePointOnMap point : positions) {
				String x = String.valueOf(point.getX());
				String y = String.valueOf(point.getY());
				String type = null;
				String entityClassName = entity.getClass().getSimpleName();
				if(entityClassName.equals("Arch")) {
					type = "arch";
				} else if(entityClassName.equals("Exit")) {
					type = "exit";
				} else if(entityClassName.equals("Mage")) {
					type = "mage";
				}
				conn.executePreparedStatement(registerEntity, gameId, type, x, y);
			}
		}
	}
	
	public static void restoreSimpleEntities(OracleConnection conn, Game game, String gameId) {
		String restoreEntities = "select entity_type, position_x, position_y " +
				"from simple_entity where game_id=?";
		ResultSet rs = conn.executePreparedStatement(restoreEntities, gameId);
		Arch arch = null;
		Exit exit = null;
		Mage mage = null;
		try {
			while(rs.next()) {
				String type = rs.getString(1);
				int posX = rs.getInt(2);
				int posY = rs.getInt(3);
				OnePointOnMap position = new OnePointOnMap(posX, posY);
				if(type.equals("arch")) {
					if(arch == null) {
						arch = new Arch();
					}
					arch.addArch(position);
				} else if(type.equals("exit")) {
					if(exit == null) {
						exit = new Exit();
					}
					exit.addExit(position);
				} else if(type.equals("mage")) {
					if(mage == null) {
						mage = new Mage();
					}
					mage.addMage(position);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		game.getBoard().setArch(arch);
		game.getBoard().setExit(exit);
		game.getBoard().setMage(mage);
	}
}
