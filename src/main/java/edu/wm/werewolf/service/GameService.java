package edu.wm.werewolf.service;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.swing.Timer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import edu.wm.werewolf.HomeController;
import edu.wm.werewolf.dao.IGameDAO;
import edu.wm.werewolf.dao.IKillDAO;
import edu.wm.werewolf.dao.IPlayerDAO;
import edu.wm.werewolf.dao.IUserDAO;
import edu.wm.werewolf.exceptions.PlayerAlreadyExistsException;
import edu.wm.werewolf.model.GPSLocation;
import edu.wm.werewolf.model.Game;
import edu.wm.werewolf.model.Player;
import edu.wm.werewolf.model.Vote;
import edu.wm.werewolf.model.WerewolfUser;

public class GameService {
	private static final Logger logger = LoggerFactory.getLogger(GameService.class);
	@Autowired private IPlayerDAO playerDAO;
	@Autowired private IUserDAO userDAO;
	@Autowired private IKillDAO killDAO;
	@Autowired private IGameDAO gameDAO;
	final private int DEFAULT_GAME_TIME = 15000;
	final private double DEFAULT_KILL_RANGE = 15000;
	final private double DEFAULT_SCENT_RANGE = 15000;
	final private int KILL_POINTS = 2;
	private boolean isRunning = false;
	Timer timer;
	Game game;
	
	public List<Player>getAllAlive()
	{
		return playerDAO.getAllAlive();
	}
	
	public void createPlayer(Player player) throws PlayerAlreadyExistsException
	{
		playerDAO.createPlayer(player);
		return;
	}
	
	public boolean kill(String killer, String victim)
	{
		Player killerDB = playerDAO.getPlayerByID(killer);
		Player victimDB = playerDAO.getPlayerByID(victim);
		if(!killerDB.isWerewolf()) {
			logger.info("Killer " + killerDB.getId() + " is not a werewolf");
			return false;
		}
		List<Player> killable = playerDAO.nearPlayers(killerDB, DEFAULT_KILL_RANGE);
		logger.info("Killable Player: " + killable);
		if(!killable.contains(victimDB)) {
			logger.info("Victim " + victimDB.getId() + " is not alive");
			return false;
		}
		if(game == null) {
			game = gameDAO.getGame();
		}
		if(!game.isNight()) {
			logger.info("Not yet night time.");
			return false;
		}
		// Gotten past kill checks
		killDAO.createKill(victimDB, killerDB);
		killerDB.setScore((killerDB.getScore() + KILL_POINTS));
		playerDAO.update(killerDB);
		return true;
	}
	
	public List<Player> getAllPlayers() 
	{
		return playerDAO.getAllPlayers();
	}
	
	public void updatePosition(String userName, GPSLocation location) 
	{
		Player player = playerDAO.getPlayerByID(userName);
		player.setLat(location.getLat());
		player.setLng(location.getLng());
		playerDAO.update(player);
	}

	public void voteKill(String name) {
		// TODO Auto-generated method stub
		Player player = playerDAO.getPlayerByID(name);
		player.setDead(true);
		playerDAO.update(player);
		
	}

	// Creates a new game.
	public void newGame(int gameTime) {
		playerDAO.clearPlayers();
		game = new Game(DEFAULT_GAME_TIME, (new Date()).getTime());
		isRunning = true;
		List<WerewolfUser> users = userDAO.getAllUsers();
		if(users.size() == 0) {
			isRunning = false;
			game = null;
			return;
		}
		Random random = new Random();
		boolean isWerewolf;
		Collections.shuffle(users, random);
		int j = users.size() / 10 * 3;
		for(int i = 0; i < users.size(); i++)
		{
			isWerewolf = false;
			if(i <= j) {
				isWerewolf = true;
			}
			// Original locations set to 0
			playerDAO.createPlayer(new Player(users.get(i).getId(), false, 0, 0, users.get(i).getId(), isWerewolf));
		}
		gameDAO.createGame(game);
	}

	public boolean vote(String voter, String voted) {
		Player player = playerDAO.getPlayerByID(voter);
		if(game.isNight() || player.isDead() || !playerDAO.getAllAlive().contains(playerDAO.getPlayerByID(voted))) {
			return false;
		}
		Vote vote = new Vote(voted, 1);
		player.setVotedAgainst(voted);
		playerDAO.update(player);
		return true;
	}
	
	public List<Player> scent(String userId){
		WerewolfUser user = userDAO.getUserByUsername(userId);
		Player werewolf = playerDAO.getPlayerByID(user.getId());
		assert(werewolf.isWerewolf());
		assert(game.isNight());
		return playerDAO.nearPlayers(werewolf, DEFAULT_SCENT_RANGE);
	}
	
	public boolean isOver() {
		if(playerDAO.numWolves() >= playerDAO.numTown() || playerDAO.numWolves() == 0) {
			List<Player> pList = playerDAO.getAllPlayers();
			WerewolfUser user;
			for(int i = 0; i <pList.size(); i++) {
				user = userDAO.getUserByUsername(pList.get(i).getUserId());
				user.setScore(user.getScore() + pList.get(i).getScore());
			}
			isRunning = false;
			return true;
		}
		return false;
	}

	public Game getGame() {
		// TODO Auto-generated method stub
		return game;
	}
}
