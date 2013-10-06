package edu.wm.werewolf.dao;

import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import edu.wm.werewolf.model.Game;

public class MongoGameDAO implements IGameDAO {
	@Autowired private MongoClient mongo;
	DB db;
	@Override
	public void createGame(Game game) {
		db = mongo.getDB("werewolf");
		DBCollection table = db.getCollection("Game");
		BasicDBObject document = new BasicDBObject();
		document.put("create", game.getTimer());
		document.put("freq", game.getDayNightFreq());
		table.insert(document);
	}

	@Override
	public Game getGame() {
		db = mongo.getDB("werewolf");
		DBCollection table = db.getCollection("Game");
		BasicDBObject query = (BasicDBObject) table.findOne();
		Game game = new Game((int)query.get("freq"), (long)query.get("create"));
		return game;
	}
	
	@Override
	public void removeGame() {
		db = mongo.getDB("werewolf");
		DBCollection table = db.getCollection("Game");
		table.drop();
	}

}
