package com.example.winteq.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

	@Expose
	@SerializedName("user_id")
	private String userId;

	@Expose
	@SerializedName("fullname")
	private String fullname;

	@Expose
	@SerializedName("email")
	private String email;

	@Expose
	@SerializedName("username")
	private String username;

	@Expose
	@SerializedName("status")
	private boolean status;

	@Expose
	@SerializedName("message")
	private String message;

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setUserId(String userId){
		this.userId = userId;
	}

	public String getUserId(){
		return userId;
	}

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