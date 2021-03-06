package edu.wm.werewolf.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import edu.wm.werewolf.dao.IGameDAO;
import edu.wm.werewolf.dao.IKillDAO;
import edu.wm.werewolf.dao.IPlayerDAO;
import edu.wm.werewolf.dao.IUserDAO;
import edu.wm.werewolf.dao.IVoteDAO;
import edu.wm.werewolf.exceptions.PlayerAlreadyExistsException;
import edu.wm.werewolf.model.GPSLocation;
import edu.wm.werewolf.model.Game;
import edu.wm.werewolf.model.Kill;
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
	final private double DEFAULT_KILL_RANGE = 0.00125;
	final private double DEFAULT_SCENT_RANGE = 0.0015;
	final private int KILL_POINTS = 100;
	final private int VOTE_POINTS = 1;
	final private int ALIVE_POINTS = 10;
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
		victimDB.setDead(true);
		playerDAO.update(victimDB);
		playerDAO.update(killerDB);
		return true;
	}
	
	public List<Player> killable(String killer)
	{
		Player killerDB = playerDAO.getPlayerByID(killer);
		List<Player> killable = new ArrayList<Player>();
		if (killerDB.isWerewolf() && !killerDB.isDead() && this.getGame().isNight()) {
			killable  = playerDAO.nearPlayers(killerDB, DEFAULT_KILL_RANGE);
		}
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
		if(!player.isDead()) {
			player.setLat(location.getLat());
			player.setLng(location.getLng());
			player.setHasUpdated(true);
			playerDAO.update(player);
		}
	}

	public boolean voteKill(String name) {
		// Sets player to dead using a vote
		Player player = playerDAO.getPlayerByID(name);
		player.setDead(true);
		playerDAO.update(player);
		return isOver();
	}

	// Creates a new game.
	public void newGame(long gameTime) {
		gameDAO.removeGame();
		playerDAO.clearPlayers();
		voteDAO.clearVotes();
		killDAO.clearKills();
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
		int j = (users.size() - 1)/ 10 * 3;
		for(int i = 0; i < users.size(); i++)
		{
			if(!users.get(i).getUsername().equals("admin")) {
				isWerewolf = false;
				if(i <= j) {
					isWerewolf = true;
				}
				// Original locations set to 0
				playerDAO.createPlayer(new Player(users.get(i).getId(), false, 0, 0, users.get(i).getId(), isWerewolf, false, users.get(i).getImageURL()));
			}
		}
		gameDAO.createGame(game);
	}
	
	public void newGameTest(long gameTime) {
		gameDAO.removeGame();
		playerDAO.clearPlayers();
		voteDAO.clearVotes();
		killDAO.clearKills();
		game = new Game(gameTime, (new Date()).getTime());
		isRunning = true;
		List<WerewolfUser> users = userDAO.getAllUsers();
		if(users.size() - 1 == 0) {
			isRunning = false;
			game = null;
			System.out.println("hehehe");
			return;
			
		}
		boolean isWerewolf;
		int j = (users.size() - 1)/ 10 * 3;
		for(int i = 0; i < users.size(); i++)
		{
			System.out.println("ohohoho");
			if(!users.get(i).getUsername().equals("admin")) {
				isWerewolf = false;
				if(users.get(i).getUsername().equals("atjones")) {
					isWerewolf = true;
				}
				// Original locations set to 0
				playerDAO.createPlayer(new Player(users.get(i).getId(), false, 0, 0, users.get(i).getId(), isWerewolf, false, users.get(i).getImageURL()));
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
		Vote vote = new Vote(voted, 1, game.getDayNightFreq(), game.getTimer());
		voteDAO.addVote(vote);
		player.setVotedAgainst(voted);
		player.setScore(player.getScore() + VOTE_POINTS);
		playerDAO.update(player);
		return true;
	}
	
	public List<Player> scent(String userId){
		WerewolfUser user = userDAO.getUserByUsername(userId);
		Player werewolf = playerDAO.getPlayerByID(user.getId());
		if(!werewolf.isWerewolf() || werewolf.isDead()|| !this.getGame().isNight()) {
			return null;
		}
		return playerDAO.nearPlayers(werewolf, DEFAULT_SCENT_RANGE);
	}
	
	public boolean isOver() {
		if((playerDAO.numWolves() >= playerDAO.numTown() || playerDAO.numWolves() == 0 )&& isRunning) {
			List<Player> pList = playerDAO.getAllPlayers();
			WerewolfUser user;
			for(int i = 0; i < pList.size(); i++) {
				user = userDAO.getUserByUsername(pList.get(i).getUserId());
				if(user!= null) {
					user.setScore(user.getScore() + pList.get(i).getScore());
					userDAO.update(user);
				}
			}
			isRunning = false;
			return true;
		}
		return false;
	}

	public Game getGame() {
		isRunning = true;
		game = gameDAO.getGame();
		return game;
	}
	
	public boolean checkLocationUpdates() {
		if(isRunning && !this.isOver()) {
			List<Player> players = playerDAO.getAllAlive();
			for(int i = 0; i < players.size(); i++) {
				if(!players.get(i).isHasUpdated()) {
					players.get(i).setDead(true);
					playerDAO.update(players.get(i));
				}else {
					players.get(i).setHasUpdated(false);
					playerDAO.update(players.get(i));
				}
			}
		}
		return true;
	}

	public void endGame() {
//		gameDAO.removeGame();
//		playerDAO.clearPlayers();
//		voteDAO.clearVotes();
		isRunning = false;
	}

	public int getNumKills(Player player) {
		return killDAO.getKillsByPlayerID(player.getId()).size();
		
	}
	
	public int getWolfNum() {
		return playerDAO.numWolves();
	}
	
	public int getPeepNum() {
		return playerDAO.numTown();
	}
	
	public List<Player> getAppropriatePlayers(Player player){
		List<Player> players = this.getAllPlayers();
		if(players == null || !player.isWerewolf()) {
			// Person making a the request is not a werewolf.
			players = this.getAllPlayers();
			for(int i = 0; i < players.size(); i++) {
				players.get(i).setScore(0);
				players.get(i).setLat(0);
				players.get(i).setLng(0);
				players.get(i).setVotedAgainst("");
				players.get(i).setWerewolf(false);
				players.get(i).setUserId("");
			}
			return players;
			
		}
		List<Player> killplayers = this.killable(player.getId());
		List<Player> soManyLists = this.scent(player.getId());; 
		for(int i = 0; i < players.size(); i++) {
			if(killplayers != null && killplayers.size() != 0 && killplayers.contains(players.get(i))&& this.getGame().isNight()) {
				// Is a kill-able player
				players.get(i).setScore(2);
			}
			else if (soManyLists != null && soManyLists.size() != 0 && soManyLists.contains(players.get(i))&& this.getGame().isNight()){
				// Is a nearby, but not a kill-able player
				players.get(i).setScore(1);
			}
			else {
				players.get(i).setScore(0);
			}
			if(players.get(i).isWerewolf()) {
				// If a scented player is a werewolf the score the other player sees is 2.
				players.get(i).setScore(0);
			}
			if(!players.get(i).isDead()) {
				players.get(i).setLat(0);
				players.get(i).setLng(0);
			}
			players.get(i).setVotedAgainst("");
			if(players.get(i).getId().equals(player.getId()) && player.isWerewolf()) {
				players.get(i).setWerewolf(true);
			}else {
				players.get(i).setWerewolf(false);
			}
			players.get(i).setUserId("");
		}
		
		return players;
	}

	public List<Kill> getKills(Player player) {
		// TODO Auto-generated method stub
//		return killDAO.getKillsByPlayerID(player.getId());
		return killDAO.getAllKills();
		
	}
}
