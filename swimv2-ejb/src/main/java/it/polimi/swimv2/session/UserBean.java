package it.polimi.swimv2.session;

import it.polimi.swimv2.entity.Ability;
import it.polimi.swimv2.entity.Feedback;
import it.polimi.swimv2.entity.HelpRequest;
import it.polimi.swimv2.entity.Notification;
import it.polimi.swimv2.entity.User;
import it.polimi.swimv2.enums.RequestStatus;
import it.polimi.swimv2.enums.UserRole;
import it.polimi.swimv2.session.exceptions.NoSuchUserException;
import it.polimi.swimv2.session.remote.UserBeanRemote;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class UserBean implements UserBeanRemote {

	@PersistenceContext(unitName = "swimv2")
	private EntityManager manager;
	
	@Override
	public List<Feedback> getHelperFeedbacks(User u) throws NoSuchUserException {

		List<Feedback> feedbackList = new ArrayList<Feedback>();
		Query q = manager.createNamedQuery("HelpRequest.findByHelper");
		q.setParameter("helper", u);
		try {
			List<?> helpRequestList = q.getResultList();
			for (Object o : helpRequestList) {
				HelpRequest h = (HelpRequest) o;
				feedbackList.add(h.getReceiverFeedback());
			}
			return feedbackList;
		} catch (NoResultException nre) {
			throw new NoSuchUserException(nre);
		}
	}

	@Override
	public List<Feedback> getAskerFeedbacks(User u) throws NoSuchUserException {

		List<Feedback> feedbackList = new ArrayList<Feedback>();
		Query q = manager.createNamedQuery("HelpRequest.findByAsker");
		q.setParameter("asker", u);
		try {

			List<?> helpRequestList = q.getResultList();

			for (Object o : helpRequestList) {

				HelpRequest h = (HelpRequest) o;
				feedbackList.add(h.getReceiverFeedback());

			}

			return feedbackList;

		} catch (NoResultException nre) {
			throw new NoSuchUserException(nre);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Notification> getNotifications(User u)
			throws NoSuchUserException {
		Query q = manager.createNamedQuery("Notification.findBytgtUser");
		try {
			q.setParameter("user", u);
			return (List<Notification>) q.getResultList();
		} catch (NoResultException nre) {
			throw new NoSuchUserException(nre);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HelpRequest> getOpenedHelpRequest(User u)
			throws NoSuchUserException {

		Query q = manager.createNamedQuery("Notification.findByStatus");
		try {
			q.setParameter("status", RequestStatus.WAITING);
			return (List<HelpRequest>) q.getResultList();

		} catch (NoResultException nre) {
			throw new NoSuchUserException(nre);
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<HelpRequest> getClosedHelpRequest(User u)
			throws NoSuchUserException {

		Query q = manager.createNamedQuery("Notification.findByStatus");
		try {
			q.setParameter("status", RequestStatus.CLOSED);
			return (List<HelpRequest>) q.getResultList();

		} catch (NoResultException nre) {
			throw new NoSuchUserException(nre);
		}
	}

	@Override
	public User getUserByID(int id) throws NoSuchUserException {
		Query q = manager.createNamedQuery("User.getUserByID");
		q.setParameter("id", id);
		try {
			return (User) q.getSingleResult();
		} catch (NoResultException nre) {
			throw new NoSuchUserException(nre);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<User> searchUser(String queryString, int page, int pageSize) {
		Query q = manager.createNamedQuery("User.searchUser");
		q.setParameter("name", '%' + queryString.toLowerCase().trim() + '%');
		/* just to make sure page is not negative so that IllegalArgumentException is not thrown */
		if(page <= 0) {
			page = 1;
		}
		q.setFirstResult((page - 1) * pageSize);
		q.setMaxResults(pageSize);
		return q.getResultList();
	}
	
	@Override
	public long countSearchUser(String queryString) {
		Query q = manager.createNamedQuery("User.countSearchUser");
		q.setParameter("name", '%' + queryString.toLowerCase().trim() + '%');
		return (Long) q.getSingleResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<HelpRequest> getProvidedHelpRequest(User u)
			throws NoSuchUserException {
		Query q = manager.createNamedQuery("HelpRequest.findByHelper");
		try {
			q.setParameter("helper", u);
			return (List<HelpRequest>) q.getResultList();

		} catch (NoResultException nre) {
			throw new NoSuchUserException(nre);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<HelpRequest> getReceivedHelpRequest(User u)
			throws NoSuchUserException {
		Query q = manager.createNamedQuery("HelpRequest.findByAsker");
		try {
			q.setParameter("asker", u);
			return (List<HelpRequest>) q.getResultList();

		} catch (NoResultException nre) {
			throw new NoSuchUserException(nre);
		}
	}

	@Override
	public User editProfile(int userId, String name, String surname,
			String website, String birthdate, String location, String minibio,
			String description) throws NoSuchUserException {
		User u = manager.find(User.class, userId);
		// if the name is null, keep the previous name as it's a mandatory field
		if (name != null && !name.trim().equals("")) {
			u.setName(name.trim());
		}
		// surname is a mandatory field, so can't be removed
		if (surname != null && !surname.trim().equals("")) {
			u.setSurname(surname.trim());
		}
		u.setWebsite(website.trim());
		u.setLocation(location.trim());
		u.setDescription(description.trim());
		u.setMinibio(minibio.trim());
		try {
			long t = (new SimpleDateFormat("dd/MM/yyyy").parse(birthdate)).getTime();
			Date date = new Date(t);
			u.setBirthdate(date);
		} catch (ParseException iae) {
			// don't do anything, timestamp is null... in this case we can	
			// silently fail without reporting to the user
		}
		return manager.merge(u);
	}

	@Override
	public void promoteAdmin(User user) {
		user.setUserRole(UserRole.ADMIN);
		manager.merge(user);		
	}

	@Override
	public User addUserAbility(User user, String chosenAb) {
		
		Query q = manager.createQuery("SELECT a FROM Ability a WHERE a.name = :name");
		q.setParameter("name", chosenAb);
		Ability ability = (Ability) q.getSingleResult();
		
		Set<Ability> userAbilities = user.getAbilities();
		userAbilities.add(ability);
		user.setAbilities(userAbilities);
		manager.merge(user);
		return user;
	}

	@Override
	public User removeUserAbility(User user, String abilityString) {
		Ability ability = manager.find(Ability.class, abilityString);
		Set<Ability> set = user.getAbilities();
		set.remove(ability);
		user.setAbilities(set);
		manager.merge(user);
		return user;
	}

}
