package com.example.winteq.model.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserData {

	@Expose
	@SerializedName("id")
	private String Id;

	@Expose
	@SerializedName("npk")
	private String npk;

	@Expose
	@SerializedName("fullname")
	private String fullname;

	@Expose
	@SerializedName("email")
	private String email;

	@Expose
	@SerializedName("no_telp")
	private String no_telp;

	@Expose
	@SerializedName("username")
	private String username;

	@Expose
	@SerializedName("stat")
	private String stat;

	@Expose
	@SerializedName("image")
	private String image;

	@Expose
	@SerializedName("status")
	private boolean status;

	@Expose
	@SerializedName("message")
	private String[] message;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String[] getMessage() {
		return message;
	}

	public void setMessage(String[] message) {
		this.message = message;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getNo_telp() {
		return no_telp;
	}

	public void setNo_telp(String no_telp) {
		this.no_telp = no_telp;
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

	public String getNpk() {
		return npk;
	}

	public void setNpk(String npk) {
		this.npk = npk;
	}
}