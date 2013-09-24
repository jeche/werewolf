package edu.wm.werewof.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import edu.wm.werewolf.dao.IPlayerDAO;
import edu.wm.werewolf.dao.IUserDAO;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;

public class GameService {
	@Autowired private IPlayerDAO playerDAO;
	@Autowired private IUserDAO userDAO;
	
	public List<Player>getAllAlive()
	{
		return playerDAO.getAllAlive();
	}
	public void createPlayer(Player player)
	{
		playerDAO.createPlayer(player);
		return;
	}
	
	public void setDead(Player victim, Player killer)
	{
		
		playerDAO.setDead(victim);
		return;
	}
	
	public Player getPlayerByID(String id) throws NoPlayerFoundException
	{
		return playerDAO.getPlayerByID(id);
	}
	public List<Player> getAllPlayers() {
		// TODO Auto-generated method stub
		
		return playerDAO.getAllPlayers();
	}
}
