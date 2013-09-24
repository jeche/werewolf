package edu.wm.werewolf;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.wm.werewof.service.GameService;
import edu.wm.werewolf.domain.Kill;
import edu.wm.werewolf.domain.Player;
import edu.wm.werewolf.exceptions.NoPlayerFoundException;



/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@Autowired GameService gameService;
	// Autowired finds a satisfied bean of being a playerDAO and connects the controller to it
	// Beans are defined in root-context (these are the Singleton classes)
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		return "home";
	}
	
	@RequestMapping(value = "/players/alive", method=RequestMethod.GET)
	public @ResponseBody List<Player> getAllAlive()
	{
		// add responseBody to package as a JSON object
		List<Player> players = gameService.getAllAlive();
		return players;
	}
	
	@RequestMapping(value = "/players/all", method=RequestMethod.GET)
	public @ResponseBody List<Player> getAllPlayers()
	{
		List<Player> players = gameService.getAllPlayers();
		return players;
	}
	@RequestMapping(value = "/players/kill/{id}/{vId}", method=RequestMethod.POST)
	public @ResponseBody boolean killPlayerById(@PathVariable("id") String id, @PathVariable("vId") String vId) throws NoPlayerFoundException
	{
		Player killer = gameService.getPlayerByID(id);
		String killerID = killer.getId();
		Player victim = gameService.getPlayerByID(vId);
		if(killer.isWerewolf() == true)
		{
			gameService.setDead(victim, killer);
			return true;
		}
		/*String victimID = victim.getId();
		Date day = new Date();
		Kill kill = new Kill();
		kill.setKillerID(killerID);
		kill.setVictimID(victimID);
		kill.setTimestampDate(day);
		kill.setLat(killer.getLat());
		kill.setLng(killer.getLng());*/
		return false;
	
	}
	
	
}
