/**
 * Created on Jul 4, 2006
 *
 * Project: demo03_BasicEchoClientandServerExercises
 */
package utility;

import java.io.*;
import java.util.*;

/**
 * @author dwatson
 * @version 1.0
 * Sep 8, 2008
 *
 * Class Description: A basic message class that can be transported across
 * the network.
 * 
 */
public class Message implements Serializable
{
	//Constants
	static final long serialVersionUID = 5488945625178844229L;
	//Attributes
	private String			name = "Message";
	private String 			user; //name of sender
	private String			msg; //message body
	private Date			timeStamp; //timestamp of when message was sent
	
	//Constructors
	public Message()
	{
	}
	
	/**
	 * 
	 * @param user set to this user
	 * @param msg set to this msg
	 * @param timeStamp set to this timeStamp
	 */
	public Message(String user, String msg, Date timeStamp)
	{
		this.user = user;
		this.msg = msg;
		this.timeStamp = timeStamp;
	}

	/**
	 * @return the user
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user)
	{
		this.user = user;
	}

	/**
	 * @return the msg
	 */
	public String getMsg()
	{
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp)
	{
		this.timeStamp = timeStamp;
	}
	
	/**
	 * 
	 * @return name of sender
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	//Operational Methods
	public String toString()
	{
		return "User Name: "+user+"        "+"Date and Time: "+timeStamp+
				"\nMessage: "+msg + "\n";
	}
}
