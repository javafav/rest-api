package com.unit.conversion.user;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {

	@Id  @GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	
	@Column(length = 30, nullable = false, unique = true)
	private String userName;
	
	@Column(length = 80, nullable = false)
	private String password;

	public User() {}
	
	
	public User(String userName, String password) {
		super();
		this.userName = userName;
		this.password = password;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
