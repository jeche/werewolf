package edu.wm.werewolf.service;

import java.util.ArrayList;
import java.util.Collection;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import edu.wm.werewolf.dao.IUserDAO;
import edu.wm.werewolf.dao.authUser;
import edu.wm.werewolf.model.WerewolfUser;

@Service("userService")
public class UserServiceImpl implements UserDetailsService, IUserService {
	@Autowired IUserDAO userDAO;
	private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		
		WerewolfUser user = userDAO.getUserByUsername(username);
		BCryptPasswordEncoder encoded = new BCryptPasswordEncoder();
		// TODO: Remove admin functionality
//		logger.info(user.toString());
		if(user == null && username.equals("admin")) {
			// Ignore simply used for setup
			userDAO.createUser(new WerewolfUser("admin", "admin", "admin", "admin", encoded.encode("admin"), "admin"));
			user.setAdmin(true);
			userDAO.update(user);
			user = userDAO.getUserByUsername(username);
		}
		else if(user == null && !username.equals("admin")) {
			System.out.println("Grabbed null user.");
			return null;
		}

		Collection<GrantedAuthorityImpl> authorities = new ArrayList<GrantedAuthorityImpl>();
		authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
		// remove || later after setting up database
		if(user.isAdmin() || username.equals("admin")) {
			authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
		}
		return new authUser(user.getUsername(), user.getHashedPassword(), true, true, true, true, authorities);
	}

}
