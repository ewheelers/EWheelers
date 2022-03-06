package com.ewheelers.ewheelers.Network.Model.Response;

import com.google.gson.annotations.SerializedName;

public class UpdateShopResponse {

	@SerializedName("msg")
	private String msg;

	@SerializedName("data")
	private ShopData data;

	@SerializedName("status")
	private String status;

	public String getMsg(){
		return msg;
	}

	public ShopData getData(){
		return data;
	}

	public String getStatus(){
		return status;
	}
}