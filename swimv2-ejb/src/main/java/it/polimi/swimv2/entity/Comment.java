package it.polimi.swimv2.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.*;

@Entity
@NamedQueries({ 
	@NamedQuery(name = "Comment.getByHelpRequest", query = "SELECT x FROM Comment x WHERE x.helprequest = :hr"), 
	
})
public class Comment implements Serializable {

	@Id
	@GeneratedValue
	private int id;
	private Timestamp timestamp;
	private String text;
	private static final long serialVersionUID = 1L;

	@ManyToOne
	private User sender;

	@ManyToOne
	private HelpRequest helprequest;

	public Comment() {
		super();
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public User getSender() {
		return sender;
	}

	public void setSender(User sender) {
		this.sender = sender;
	}

	public HelpRequest getHelprequest() {
		return helprequest;
	}

	public void setHelprequest(HelpRequest helprequest) {
		this.helprequest = helprequest;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", timestamp=" + timestamp + ", text="
				+ text + ", sender=" + sender + ", helprequest=" + helprequest
				+ "]";
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
		if (!(obj instanceof Comment)) {
			return false;
		}
		Comment other = (Comment) obj;
		return id != other.id;
	}

}
