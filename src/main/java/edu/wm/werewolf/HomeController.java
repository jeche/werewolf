package edu.wm.werewolf;

import java.security.Principal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Locale;

import org.apache.taglibs.standard.tag.common.xml.SetTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.wm.werewolf.dao.IUserDAO;
import edu.wm.werewolf.dao.IVoteDAO;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;
import edu.wm.werewolf.exceptions.PlayerAlreadyExistsException;
import edu.wm.werewolf.model.GPSLocation;
import edu.wm.werewolf.model.JsonResponse;
import edu.wm.werewolf.model.Kill;
import edu.wm.werewolf.model.Player;
import edu.wm.werewolf.model.WerewolfUser;
import edu.wm.werewolf.model.Vote;
import edu.wm.werewolf.service.GameService;
import edu.wm.werewolf.service.UserServiceImpl;
import edu.wm.werewolf.service.VoteListener;



/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	@Autowired private IUserDAO userDAO;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired IVoteDAO voteDAO;
	@Autowired private GameService gameService;
	private boolean wasDay = true;
	// Autowired finds a satisfied bean of being a playerDAO and connects the controller to it
	// Beans are defined in root-context (these are the Singleton classes)
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/newgame", method = {RequestMethod.POST})
	public String newGame(@RequestParam("dayNight") int dayNight, Model model) {
		dayNight *= 60000;
		logger.info("Starting new game with time interval: " + dayNight);
		try {
			gameService.newGame(dayNight);
			wasDay = true;
		} catch (Exception e) {
			// TODO: handle exception
			return e.toString();
		}
		return "Success";
	}
	
	@RequestMapping(value = "/players/alive", method=RequestMethod.GET)
	public @ResponseBody List<Player> getAllAlive()
	{
		// add responseBody to package as a JSON object
		List<Player> players = gameService.getAllAlive();
		if(!gameService.isOver()) {
			for(int i = 0; i < players.size(); i++) {
				players.get(i).setScore(0);
				players.get(i).setLat(0);
				players.get(i).setLng(0);
				players.get(i).setVotedAgainst(null);
				players.get(i).setWerewolf(false);
				players.get(i).setUserId(null);
			}
		}
		return players;
	}
	
	@RequestMapping(value = "/players/all", method=RequestMethod.GET)
	public @ResponseBody List<Player> getAllPlayers(Principal principal)
	{
		List<Player> players = gameService.getAllPlayers();
		for(int i = 0; i < players.size(); i++) {
			players.get(i).setScore(0);
			players.get(i).setLat(0);
			players.get(i).setLng(0);
			players.get(i).setVotedAgainst(null);
			players.get(i).setWerewolf(false);
			players.get(i).setUserId(null);
		}
		return players;
	}
	
	@RequestMapping(value = "/players/vote", method=RequestMethod.POST)
	public @ResponseBody boolean voteForPlayer(@RequestParam("voted") String voted, Principal principal)
	{
		WerewolfUser voter = userDAO.getUserByUsername(principal.getName());
		try {
			return gameService.vote(voter.getId(), voted);
		} catch (Exception e) {
			return false;
		}
	}
	
	@RequestMapping(value = "/players/kill", method=RequestMethod.POST)
	public @ResponseBody boolean killPlayerById(@RequestParam("victim") String victim, Principal principal) throws NoPlayerFoundException
	{
		WerewolfUser user = userDAO.getUserByUsername(principal.getName());
		try {
			return gameService.kill(user.getId(), victim);
		} catch (Exception e) {
			logger.info("Exception thrown.");
			e.printStackTrace();
			return false;
		}
//		Player killer = gameService.getPlayerByID(id);
//		String killerID = killer.getId();
//		Player victim = gameService.getPlayerByID(vId);
//		if(killer.isWerewolf() == true)
//		{
//			gameService.setDead(victim, killer);
//			return true;
//		}
		/*String victimID = victim.getId();
		Date day = new Date();
		Kill kill = new Kill();
		kill.setKillerID(killerID);
		kill.setVictimID(victimID);
		kill.setTimestampDate(day);
		kill.setLat(killer.getLat());
		kill.setLng(killer.getLng());*/
	}
	
	@RequestMapping(value = "/location", method=RequestMethod.POST)
	public @ResponseBody JsonResponse setLocation(@ModelAttribute GPSLocation location, Principal principal) {
		return null;
	}
	
	@RequestMapping(value = "/welcome", method=RequestMethod.GET)
	public String home(ModelMap model, Principal principal) {
		System.out.println(principal.getName());
		System.out.println(principal.toString());
		return "home";
	}
	
	@RequestMapping(value = "/logout", method=RequestMethod.GET)
	public String rage(ModelMap model, Principal principal) {
		System.out.println(principal.getName());
		System.out.println(principal.toString());
		return "logout";
	}
	
	@RequestMapping(value = "/admin", method=RequestMethod.GET)
	public String admin(ModelMap model) {
		return "admin";
	}
	
	@RequestMapping(value = "/addUser", method=RequestMethod.POST)
	public @ResponseBody String addUser(@RequestParam("userName")String username, @RequestParam("id")String id,
			@RequestParam("firstName")String firstName, @RequestParam("lastName")String lastName,
			@RequestParam("hashedPassword")String hashedPassword, Principal principal)
	{
		principal.getName();
		String imageURL = "";
		BCryptPasswordEncoder encoded = new BCryptPasswordEncoder();
		Collection<GrantedAuthorityImpl> auth = new ArrayList<GrantedAuthorityImpl>();
		auth.add(new GrantedAuthorityImpl("ROLE_USER"));
		WerewolfUser user = new WerewolfUser(id, firstName, lastName, username, encoded.encode(hashedPassword), imageURL);
		try {
			userDAO.createUser(user);
		} catch (Exception e) {
			// TODO: handle exception
			return "Failure";
		}
		return "Success";
	}
	
	@RequestMapping(value = "/restartGame", method=RequestMethod.GET)
	public boolean restartGame()
	{
		gameService.newGame(0);
		return true;
	}
	
	@RequestMapping(value = "/players/getVotable", method=RequestMethod.GET)
	public List<Player> getVotable(Principal principal)
	{
		return gameService.getAllAlive();

	}
	
	public void timeIteration()
	{
		if(gameService.getGame()!=null && wasDay && gameService.getGame().isNight()) {
			logger.info("Going to get the most votes for day " + (int)(new Date()).getTime() / (gameService.getGame().getDayNightFreq()*2));
			List<Vote> voteList = voteDAO.mostVotes((int)(new Date()).getTime() / (gameService.getGame().getDayNightFreq()*2));
			logger.info("Vote List: " + voteList.toString());
			gameService.checkLocationUpdates();
		}
	}
	
	
}
