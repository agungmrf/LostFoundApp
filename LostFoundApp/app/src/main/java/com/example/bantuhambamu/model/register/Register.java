package com.example.bantuhambamu.model.register;

import com.google.gson.annotations.SerializedName;

public class Register{

	@SerializedName("data")
	private RegisterData registerData;

	@SerializedName("message")
	private String message;

	@SerializedName("status")
	private boolean status;

	public RegisterData getData(){
		return registerData;
	}

	public String getMessage(){
		return message;
	}

	public boolean isStatus(){
		return status;
	}
}