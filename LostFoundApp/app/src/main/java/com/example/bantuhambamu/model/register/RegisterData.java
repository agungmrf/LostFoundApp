package com.example.bantuhambamu.model.register;

import com.google.gson.annotations.SerializedName;

public class RegisterData {

	@SerializedName("user_id")
	private String userId;

	@SerializedName("name")
	private String name;

	@SerializedName("email")
	private String email;

	@SerializedName("username")
	private String username;

	public String getUserId(){
		return userId;
	}

	public String getName(){
		return name;
	}

	public String getEmail(){
		return email;
	}

	public String getUsername(){
		return username;
	}
}