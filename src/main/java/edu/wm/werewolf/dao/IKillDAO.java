package edu.wm.werewolf.dao;

import java.util.List;

import edu.wm.werewolf.domain.Kill;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;

public interface IKillDAO {
	void createKill(Player victim, Player killer);
	List<Kill> getAllKills();
	List<Kill> getKillsByPlayerID(String id) throws NoPlayerFoundException;

}
