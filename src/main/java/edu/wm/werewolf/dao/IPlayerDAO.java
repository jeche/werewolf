package edu.wm.werewolf.dao;

import java.util.List;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;

public interface IPlayerDAO {
	
	void createPlayer(Player player);
	List<Player> getAllAlive();
	void setDead(Player p);
	
	Player getPlayerByID(String id) throws NoPlayerFoundException;
	List<Player> getAllPlayers();
	
}
