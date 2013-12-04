package edu.wm.werewolf.model;

import java.util.List;

public class JsonResponse {
	
	String status;
	String isWerewolf;
	List<Kill> kills;
	long created;
	String isDead;
	long nightFreq;
	String gameStatus;
	List<Player> players;
	int numWolf;
	int numPeep;

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	public JsonResponse(String status) {
		super();
		this.status = status;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "JsonResponse [status=" + status + "]";
	}

	/**
	 * @return the isWerewolf
	 */
	public String getIsWerewolf() {
		return isWerewolf;
	}

	/**
	 * @param isWerewolf the isWerewolf to set
	 */
	public void setIsWerewolf(String isWerewolf) {
		this.isWerewolf = isWerewolf;
	}

	/**
	 * @return the kills
	 */
	public List<Kill> getKills() {
		return kills;
	}

	/**
	 * @param kills the kills to set
	 */
	public void setKills(List<Kill> kills) {
		this.kills = kills;
	}

	/**
	 * @return the created
	 */
	public long getCreated() {
		return created;
	}

	/**
	 * @param created the created to set
	 */
	public void setCreated(long created) {
		this.created = created;
	}

	/**
	 * @return the isDead
	 */
	public String getIsDead() {
		return isDead;
	}

	/**
	 * @param isDead the isDead to set
	 */
	public void setIsDead(String isDead) {
		this.isDead = isDead;
	}

	/**
	 * @return the nightFreq
	 */
	public long getNightFreq() {
		return nightFreq;
	}

	/**
	 * @param nightFreq the nightFreq to set
	 */
	public void setNightFreq(long nightFreq) {
		this.nightFreq = nightFreq;
	}

	/**
	 * @return the gameStatus
	 */
	public String getGameStatus() {
		return gameStatus;
	}

	/**
	 * @param gameStatus the gameStatus to set
	 */
	public void setGameStatus(String gameStatus) {
		this.gameStatus = gameStatus;
	}

	/**
	 * @return the players
	 */
	public List<Player> getPlayers() {
		return players;
	}

	/**
	 * @param players the players to set
	 */
	public void setPlayers(List<Player> players) {
		this.players = players;
	}

	/**
	 * @return the numWolf
	 */
	public int getNumWolf() {
		return numWolf;
	}

	/**
	 * @param numWolf the numWolf to set
	 */
	public void setNumWolf(int numWolf) {
		this.numWolf = numWolf;
	}

	/**
	 * @return the numPeep
	 */
	public int getNumPeep() {
		return numPeep;
	}

	/**
	 * @param numPeep the numPeep to set
	 */
	public void setNumPeep(int numPeep) {
		this.numPeep = numPeep;
	}
}
