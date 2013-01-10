package it.polimi.swimv2.servlet;

import it.polimi.swimv2.entity.User;
import it.polimi.swimv2.session.FriendShipBeanRemote;
import it.polimi.swimv2.session.NotificationBeanRemote;
import it.polimi.swimv2.session.UserBeanRemote;
import it.polimi.swimv2.session.exceptions.NoSuchUserException;
import it.polimi.swimv2.webutils.AccessRole;
import it.polimi.swimv2.webutils.Controller;
import it.polimi.swimv2.webutils.Navigation;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;

public class UserProfile extends Controller {
	private static final long serialVersionUID = 1L;

	public UserProfile() {
		super(AccessRole.USER);
	}
	
	@EJB
	private NotificationBeanRemote nbr;
	
	@EJB
	private UserBeanRemote ubr;
	
	@EJB
	private FriendShipBeanRemote friendshipBean;

	@Override
	protected void get(Navigation nav) throws IOException, ServletException {
		
		String id = nav.getParam("id");
		try {

			User u = ubr.getUserByID(Integer.parseInt(id));
			nav.setAttribute("showFR", !(friendshipBean.isFriend(nav.getLoggedUser(), u) || nbr.isPending(nav.getLoggedUser(), u)));
			
			nav.setAttribute("profile", u);
			nav.setAttribute("providedList", ubr.getProvidedHelpRequest(u));
			nav.setAttribute("receivedList", ubr.getReceivedHelpRequest(u));
			nav.setAttribute("abilitiesList", u.getAbilities());
			
			nav.fwd("WEB-INF/userprofile.jsp");
		} catch (NoSuchUserException nsue) {
			nav.fwd("WEB-INF/error.jsp");

		}
	}
}
