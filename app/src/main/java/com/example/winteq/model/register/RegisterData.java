package com.example.winteq.model.register;

import com.google.gson.annotations.SerializedName;

public class RegisterData {

	@SerializedName("fullname")
	private String fullname;

	@SerializedName("email")
	private String email;

	@SerializedName("username")
	private String username;

	public void setFullname(String fullname){
		this.fullname = fullname;
	}

	public String getFullname(){
		return fullname;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}
}