package com.cs.domain;

import java.util.Objects;

import org.assertj.core.util.VisibleForTesting;

public class User {
	private int id;
	private String firstName;
	private String lastName;
	private String contact;
	private String email;
	private Role role;
	private String address;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}

	@VisibleForTesting
	public User(int id, String firstName, String lastName, String contact, String email, Role role,
				String address) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.contact = contact;
		this.email = email;
		this.role = role;
		this.address = address;
	}
	
	
	public User(String firstName, String lastName, String contact, String email, Role role, String address) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.contact = contact;
		this.email = email;
		this.role = role;
		this.address = address;
	}
	public User() {
		super();
	}
	
	@Override
	public boolean equals(Object o) {
		User another = (User) o;
		return another.id == this.id;
	}
	
	@Override
    public int hashCode() {
        return Objects.hash(id);
    }
	
}
