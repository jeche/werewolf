package edu.wm.werewolf.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bson.BSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoURI;

import edu.wm.werewolf.HomeController;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;
import edu.wm.werewolf.model.Player;

public class MongoPlayerDAO implements IPlayerDAO {
//	@Autowired private MongoURI mongo;
	private static final Logger logger = LoggerFactory.getLogger(MongoPlayerDAO.class);
	@Autowired DB db;
	
	public MongoPlayerDAO(){
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public List<Player> getAllAlive() {
		// TODO Auto-generated method stub
//		db = mongo.getDB("werewolf");
		DBCollection table = db.getCollection("Player");
		BasicDBObject query = new BasicDBObject("isDead", false);
		DBCursor cursor = table.find(query);
		Player alive;
		List<Player> players = new ArrayList<>();
		try {
		while (cursor.hasNext()) {
			DBObject item = cursor.next();
			alive = new Player((String)item.get("_id"), (boolean) item.get("isDead"),(double) ((BasicDBList)item.get("loc")).get(1), (double) ((BasicDBList)item.get("loc")).get(0), (String) item.get("userId"),(boolean) item.get("isWerewolf"), (boolean) item.get("hasUpdated"));
			players.add(alive);
		}
		}
		finally {
			cursor.close();
		}
		logger.info("All alive players:" + players.toString());
		return players;
	}
	
	@Override
	public void update(Player updated) throws NoPlayerFoundException {
		getPlayerByID(updated.getId());
		DBCollection table = db.getCollection("Player");
		BasicDBObject document = new BasicDBObject();
		document.put("_id", updated.getId());
		document.put("loc", new double[]{updated.getLng(), updated.getLat()});
		document.put("userId", updated.getUserId());
		document.put("votedAgainst", updated.getVotedAgainst());
		document.put("isWerewolf", updated.isWerewolf());
		document.put("isDead", updated.isDead());
		document.put("hasUpdated", updated.isHasUpdated());
		table.save(document);
		// Used for indexing to allow for geospatial queries.
		DBObject index2d = BasicDBObjectBuilder.start("loc", "2d").get();
		System.out.println(index2d);
		table.ensureIndex(index2d);
	}

	@Override
	public void createPlayer(Player player) {
		DBCollection table = db.getCollection("Player");
		BasicDBObject document = new BasicDBObject();
		document.put("_id", player.getId());
		document.put("loc", new double[]{player.getLng(), player.getLat()});
		document.put("userId", player.getUserId());
		document.put("votedAgainst", player.getVotedAgainst());
		document.put("isWerewolf", player.isWerewolf());
		document.put("isDead", player.isDead());
		document.put("hasUpdated", player.isHasUpdated());
		table.insert(document);
		DBObject index2d = BasicDBObjectBuilder.start("loc", "2d").get();
		
		table.ensureIndex(index2d);
	}

	@Override
	public Player getPlayerByID(String id) throws NoPlayerFoundException {
		DBCollection table = db.getCollection("Player");
		BasicDBObject query = new BasicDBObject("_id", id);
		DBObject cursor = table.findOne(query);
		if(cursor == null)
			throw new NoPlayerFoundException(id);
		Player retValPlayer = new Player((String)cursor.get("_id"), (boolean) cursor.get("isDead"), (double) ((BasicDBList)cursor.get("loc")).get(1), (double) ((BasicDBList)cursor.get("loc")).get(0), (String) cursor.get("userId"),(boolean) cursor.get("isWerewolf"), (boolean) cursor.get("hasUpdated"));
		return retValPlayer;
	}
	
	@Override
	public List<Player> getAllPlayers() {
		DBCollection table = db.getCollection("Player");
		Player alive;
		List<Player> players = new ArrayList<>();
		DBCursor cursor = table.find();
		try {
		while (cursor.hasNext()) {
			DBObject item = cursor.next();
			alive = new Player((String)item.get("_id"), (boolean) item.get("isDead"),(double) ((BasicDBList)item.get("loc")).get(1), (double) ((BasicDBList)item.get("loc")).get(0), (String) item.get("userId"),(boolean) item.get("isWerewolf"), (boolean) item.get("hasUpdated"));
			players.add(alive);
		}
		}
		finally {
			cursor.close();
		}
		return players;
	}

	@Override
	public List<Player> nearPlayers(Player player, double distance) {
		DBCollection table = db.getCollection("Player");
		BasicDBList v1 = new BasicDBList();
		v1.add(player.getLng());
		v1.add(player.getLat());
		BasicDBObject query = new BasicDBObject();
		query.put("loc", BasicDBObjectBuilder.start().append("$near",v1).append("$maxDistance", distance).get());
		DBObject index2d = BasicDBObjectBuilder.start("loc", "2d").get();
		table.ensureIndex(index2d);
		DBCursor cursor = table.find(query);
		Player alive;
		List<Player> players = new ArrayList<>();
		try {
		while (cursor.hasNext()) {
			DBObject item = cursor.next();
			alive = new Player((String)item.get("_id"), (boolean) item.get("isDead"),(double) ((BasicDBList)item.get("loc")).get(1), (double) ((BasicDBList)item.get("loc")).get(0), (String) item.get("userId"),(boolean) item.get("isWerewolf"), (boolean) item.get("hasUpdated"));
			if(!alive.isDead()) {
				players.add(alive);
			}
		}
		}
		finally {
			cursor.close();
		}
		return players;
	}

	@Override
	public void clearPlayers() {
		DBCollection table = db.getCollection("Player");
		table.drop();
	}

	@Override
	public int numWolves() {
		DBCollection table = db.getCollection("Player");
		BasicDBObject query = new BasicDBObject("isWerewolf", true);
		DBCursor cursor = table.find(query);
		Player alive;
		List<Player> players = new ArrayList<>();
		try {
		while (cursor.hasNext()) {
			DBObject item = cursor.next();
			alive = new Player((String)item.get("_id"), (boolean) item.get("isDead"),(double) ((BasicDBList)item.get("loc")).get(1), (double) ((BasicDBList)item.get("loc")).get(0), (String) item.get("userId"),(boolean) item.get("isWerewolf"), (boolean) item.get("hasUpdated"));
			if(!alive.isDead()) {
				players.add(alive);
			}
		}
		}
		finally {
			cursor.close();
		}
		return players.size();
	}

	@Override
	public int numTown() {
//		db = mongo.getDB("werewolf");
		DBCollection table = db.getCollection("Player");
		BasicDBObject query = new BasicDBObject("isWerewolf", false);
		DBCursor cursor = table.find(query);
		Player alive;
		List<Player> players = new ArrayList<>();
		try {
		while (cursor.hasNext()) {
			DBObject item = cursor.next();
			alive = new Player((String)item.get("_id"), (boolean) item.get("isDead"),(double) ((BasicDBList)item.get("loc")).get(1), (double) ((BasicDBList)item.get("loc")).get(0), (String) item.get("userId"),(boolean) item.get("isWerewolf"), (boolean) item.get("hasUpdated"));
			if(!alive.isDead()) {
				players.add(alive);
			}
		}
		}
		finally {
			cursor.close();
		}
		return players.size();
	}

}
