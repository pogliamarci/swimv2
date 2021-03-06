package it.polimi.swimv2.entity;

import it.polimi.swimv2.enums.FeedbackValue;
import it.polimi.swimv2.enums.Role;
import it.polimi.swimv2.enums.UserRole;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;

import javax.persistence.*;

@Entity
@NamedQueries({
		@NamedQuery(name = "User.findByEmail", query = "SELECT x FROM User x WHERE x.email = :email"),
		@NamedQuery(name = "User.getUserByID", query = "SELECT x FROM User x WHERE x.id = :id"),
		@NamedQuery(name = "User.searchUser", query = "SELECT x FROM User x WHERE "
				+ "CONCAT(TRIM(x.name), CONCAT(' ', TRIM(x.surname))) LIKE :name ORDER BY x.surname, x.id"),
		@NamedQuery(name = "User.countSearchUser", query = "SELECT COUNT(x) FROM User x WHERE "
				+ "CONCAT(TRIM(x.name), CONCAT(' ', TRIM(x.surname))) LIKE :name ORDER BY x.surname, x.id") })
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class User implements Serializable {

	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String surname;

	@Column(unique = true)
	private String email;

	private String passwordHash;
	private Date birthdate;
	private String website;
	private String location;
	private String minibio;
	
	@Lob
	private String description;
	
	private UserRole role;

	// feedback (precomputed aggregate values)
	@Column(columnDefinition = "integer default 0")
	private int posFB;
	@Column(columnDefinition = "integer default 0")
	private int neuFB;
	@Column(columnDefinition = "integer default 0")
	private int negFB;
	@Column(columnDefinition = "integer default 0")
	private int experience;

	@OneToOne
	private UserImage image;

	private static final long serialVersionUID = 1L;

	// manytomany unidirezionale dato che ability non tiene traccia dell'user
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "UserAbility", joinColumns = { @JoinColumn(name = "user") }, inverseJoinColumns = { @JoinColumn(name = "ability") })
	private Set<Ability> abilities;

	public User() {
		super();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPasswordHash() {
		return this.passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getMinibio() {
		return this.minibio;
	}

	public void setMinibio(String minibio) {
		this.minibio = minibio;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserImage getImage() {
		return this.image;
	}

	public void setImage(UserImage image) {
		this.image = image;
	}

	public boolean isAdmin() {
		if (role == UserRole.ADMIN) {
			return true;
		}
		return false;
	}

	
	public int getPosFB() {
		return posFB;
	}

	public int getNeuFB() {
		return neuFB;
	}

	public int getNegFB() {
		return negFB;
	}
	
	public int getExperience() {
		return experience;
	}

	public void addFeedback(FeedbackValue evaluation, Role role) {
		if(role == Role.HELPER) {
			experience += 1;
		}
		if(evaluation == FeedbackValue.NEGATIVE) {
			negFB += 1;
		} else if(evaluation == FeedbackValue.POSITIVE) {
			posFB += 1;
		} else {
			neuFB += 1;
		}
	}

	@Transient
	public int getReputation() {
		int sum = posFB + neuFB + negFB;
		return (sum == 0) ? 0 : (int) ((float) ((posFB - negFB) * 5) / sum) + 5;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof User)) {
			return false;
		}
		User other = (User) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", surname=" + surname
				+ ", email=" + email + ", passwordHash=" + passwordHash
				+ ", birthdate=" + birthdate + ", website=" + website
				+ ", location=" + location + ", minibio=" + minibio
				+ ", description=" + description + "]";
	}

	public Set<Ability> getAbilities() {
		return abilities;
	}

	public void setAbilities(Set<Ability> abilities) {
		this.abilities = abilities;
	}

	public UserRole getUserRole() {
		return role;
	}

	public void setUserRole(UserRole role) {
		this.role = role;
	}

}
