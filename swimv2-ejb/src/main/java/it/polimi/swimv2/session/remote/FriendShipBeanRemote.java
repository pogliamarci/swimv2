package it.polimi.swimv2.session.remote;

import it.polimi.swimv2.entity.User;

import java.util.List;

public interface FriendShipBeanRemote {
	
	
	//public List<Friendship> getFriendship(User u);
	
	void createFriendship(String notificationSrc, User receiver);
	
	boolean isFriend(User asker, User receiver);

	List<User> getFriends(User u);

	boolean isRequestAllowed(User loggedUser, User u);
	

}