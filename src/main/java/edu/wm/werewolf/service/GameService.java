package edu.wm.werewolf.service;

import java.util.ArrayList;
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
import edu.wm.werewolf.dao.IVoteDAO;
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
	@Autowired private IVoteDAO voteDAO;
//	final private int DEFAULT_GAME_TIME = 15000;
	final private double DEFAULT_KILL_RANGE = 15000;
	final private double DEFAULT_SCENT_RANGE = 15000;
	final private int KILL_POINTS = 2;
	private boolean isRunning = false;
	private List<String> playerUpdateList;
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
	
	public List<Player> killable(String killer)
	{
		Player killerDB = playerDAO.getPlayerByID(killer);
		List<Player> killable = playerDAO.nearPlayers(killerDB, DEFAULT_KILL_RANGE);
		return killable;
	}
	
	public List<Player> getAllPlayers() 
	{
		return playerDAO.getAllPlayers();
	}
	
	public void updatePosition(String userName, GPSLocation location) 
	{
		WerewolfUser user = userDAO.getUserByUsername(userName);
		Player player = playerDAO.getPlayerByID(user.getId());
		player.setLat(location.getLat());
		player.setLng(location.getLng());
		playerDAO.update(player);
		playerUpdateList.add(player.getId());
	}

	public void voteKill(String name) {
		// TODO Auto-generated method stub
		Player player = playerDAO.getPlayerByID(name);
		player.setDead(true);
		playerDAO.update(player);
		
	}

	// Creates a new game.
	public void newGame(long gameTime) {
		gameDAO.removeGame();
		playerDAO.clearPlayers();
		voteDAO.clearVotes();
		game = new Game(gameTime, (new Date()).getTime());
		isRunning = true;
		List<WerewolfUser> users = userDAO.getAllUsers();
		if(users.size() - 1 == 0) {
			isRunning = false;
			game = null;
			return;
		}
		Random random = new Random();
		boolean isWerewolf;
		Collections.shuffle(users, random);
		playerUpdateList = new ArrayList<String>();
		int j = (users.size() - 1)/ 10 * 3;
		for(int i = 0; i < users.size(); i++)
		{
			if(!users.get(i).getUsername().equals("admin")) {
				isWerewolf = false;
				if(i <= j) {
					isWerewolf = true;
				}
				// Original locations set to 0
				playerDAO.createPlayer(new Player(users.get(i).getId(), false, 0, 0, users.get(i).getId(), isWerewolf));
				playerUpdateList.add(users.get(i).getId());
			}
		}
		gameDAO.createGame(game);
	}

	public boolean vote(String voter, String voted) {
		Player player = playerDAO.getPlayerByID(voter);
//		if(game == null) {
		game = gameDAO.getGame();
		if(game.isNight() || player.isDead() || !playerDAO.getAllAlive().contains(playerDAO.getPlayerByID(voted))) {
			logger.info("Failed normal checks");
			logger.info("Is night? " + game.isNight());
			logger.info("Dead man voting? " + player.isDead());
			logger.info("Voting for dead man? " + !playerDAO.getAllAlive().contains(playerDAO.getPlayerByID(voted)));
			return false;
		}
		Vote vote = new Vote(voted, 1, game.getDayNightFreq());
		voteDAO.addVote(vote);
		player.setVotedAgainst(voted);
		playerDAO.update(player);
		return true;
	}
	
	public List<Player> scent(String userId){
		WerewolfUser user = userDAO.getUserByUsername(userId);
		Player werewolf = playerDAO.getPlayerByID(user.getId());
		if(!werewolf.isWerewolf() || werewolf.isDead()) {
			return null;
		}
/*		assert(werewolf.isWerewolf());
		assert(game.isNight());*/
		return playerDAO.nearPlayers(werewolf, DEFAULT_SCENT_RANGE);
	}
	
	public boolean isOver() {
		if(playerDAO.numWolves() >= playerDAO.numTown() || playerDAO.numWolves() == 0) {
			List<Player> pList = playerDAO.getAllPlayers();
			WerewolfUser user;
			for(int i = 0; i < pList.size(); i++) {
				user = userDAO.getUserByUsername(pList.get(i).getUserId());
				user.setScore(user.getScore() + pList.get(i).getScore());
			}
			isRunning = false;
			return true;
		}
		return false;
	}

	public Game getGame() {
		game = gameDAO.getGame();
		return game;
	}
	
	public boolean checkLocationUpdates() {
		if(isRunning && !this.isOver()) {
			List<Player> players = playerDAO.getAllAlive();
			for(int i = 0; i < players.size(); i++) {
				if(!playerUpdateList.contains(players.get(i).getId())) {
					players.get(i).setDead(true);
					playerDAO.update(players.get(i));
				}
			}
			playerUpdateList = new ArrayList<String>();
		}
		return true;
	}
}
