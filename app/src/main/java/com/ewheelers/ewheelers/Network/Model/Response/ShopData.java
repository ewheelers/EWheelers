package com.ewheelers.ewheelers.Network.Model.Response;

import com.google.gson.annotations.SerializedName;

public class ShopData{

	@SerializedName("totalUnreadMessageCount")
	private String totalUnreadMessageCount;

	@SerializedName("totalUnreadNotificationCount")
	private String totalUnreadNotificationCount;

	@SerializedName("cartItemsCount")
	private String cartItemsCount;

	@SerializedName("currencySymbol")
	private String currencySymbol;

	@SerializedName("shopId")
	private String shopId;

	@SerializedName("langId")
	private String langId;

	@SerializedName("totalFavouriteItems")
	private String totalFavouriteItems;

	public void setTotalUnreadMessageCount(String totalUnreadMessageCount){
		this.totalUnreadMessageCount = totalUnreadMessageCount;
	}

	public String getTotalUnreadMessageCount(){
		return totalUnreadMessageCount;
	}

	public void setTotalUnreadNotificationCount(String totalUnreadNotificationCount){
		this.totalUnreadNotificationCount = totalUnreadNotificationCount;
	}

	public String getTotalUnreadNotificationCount(){
		return totalUnreadNotificationCount;
	}

	public void setCartItemsCount(String cartItemsCount){
		this.cartItemsCount = cartItemsCount;
	}

	public String getCartItemsCount(){
		return cartItemsCount;
	}

	public void setCurrencySymbol(String currencySymbol){
		this.currencySymbol = currencySymbol;
	}

	public String getCurrencySymbol(){
		return currencySymbol;
	}

	public void setShopId(String shopId){
		this.shopId = shopId;
	}

	public String getShopId(){
		return shopId;
	}

	public void setLangId(String langId){
		this.langId = langId;
	}

	public String getLangId(){
		return langId;
	}

	public void setTotalFavouriteItems(String totalFavouriteItems){
		this.totalFavouriteItems = totalFavouriteItems;
	}

	public String getTotalFavouriteItems(){
		return totalFavouriteItems;
	}

	@Override
 	public String toString(){
		return 
			"ShopData{" + 
			"totalUnreadMessageCount = '" + totalUnreadMessageCount + '\'' + 
			",totalUnreadNotificationCount = '" + totalUnreadNotificationCount + '\'' + 
			",cartItemsCount = '" + cartItemsCount + '\'' + 
			",currencySymbol = '" + currencySymbol + '\'' + 
			",shopId = '" + shopId + '\'' + 
			",langId = '" + langId + '\'' + 
			",totalFavouriteItems = '" + totalFavouriteItems + '\'' + 
			"}";
		}
}