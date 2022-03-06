package com.ewheelers.ewheelers.Network.Model;

import com.google.gson.annotations.SerializedName;

public class ShopPostData{

	@SerializedName("shop_country_id")
	private int shopCountryId;

	@SerializedName("shop_longitude")
	private String shopLongitude;

	@SerializedName("shop_state")
	private int shopState;

	@SerializedName("shop_max_sell_radius")
	private int shopMaxSellRadius;

	@SerializedName("shop_supplier_display_status")
	private int shopSupplierDisplayStatus;

	@SerializedName("shop_identifier")
	private String shopIdentifier;

	@SerializedName("shop_auto_complete")
	private String shopAutoComplete;

	@SerializedName("shop_id")
	private int shopId;

	@SerializedName("urlrewrite_custom")
	private String urlrewriteCustom;

	@SerializedName("shop_latitude")
	private String shopLatitude;

	@SerializedName(" shop_city_id")
	private int shopCityId;

	@SerializedName("shop_phone")
	private String shopPhone;

	@SerializedName("shop_free_ship_upto")
	private int shopFreeShipUpto;

	@SerializedName("shop_postalcode")
	private String shopPostalcode;

	@SerializedName("shop_max_rent_radius")
	private int shopMaxRentRadius;

	public Boolean getShop_isDoorToDoor() {
		return shop_isDoorToDoor;
	}

	public void setShop_isDoorToDoor(Boolean shop_isDoorToDoor) {
		this.shop_isDoorToDoor = shop_isDoorToDoor;
	}

	@SerializedName("shop_isDoorToDoor")
	private Boolean shop_isDoorToDoor;

	public void setShopCountryId(int shopCountryId){
		this.shopCountryId = shopCountryId;
	}

	public int getShopCountryId(){
		return shopCountryId;
	}

	public void setShopLongitude(String shopLongitude){
		this.shopLongitude = shopLongitude;
	}

	public String getShopLongitude(){
		return shopLongitude;
	}

	public void setShopState(int shopState){
		this.shopState = shopState;
	}

	public int getShopState(){
		return shopState;
	}

	public void setShopMaxSellRadius(int shopMaxSellRadius){
		this.shopMaxSellRadius = shopMaxSellRadius;
	}

	public int getShopMaxSellRadius(){
		return shopMaxSellRadius;
	}

	public void setShopSupplierDisplayStatus(int shopSupplierDisplayStatus){
		this.shopSupplierDisplayStatus = shopSupplierDisplayStatus;
	}

	public int getShopSupplierDisplayStatus(){
		return shopSupplierDisplayStatus;
	}

	public void setShopIdentifier(String shopIdentifier){
		this.shopIdentifier = shopIdentifier;
	}

	public String getShopIdentifier(){
		return shopIdentifier;
	}

	public void setShopAutoComplete(String shopAutoComplete){
		this.shopAutoComplete = shopAutoComplete;
	}

	public String getShopAutoComplete(){
		return shopAutoComplete;
	}

	public void setShopId(int shopId){
		this.shopId = shopId;
	}

	public int getShopId(){
		return shopId;
	}

	public void setUrlrewriteCustom(String urlrewriteCustom){
		this.urlrewriteCustom = urlrewriteCustom;
	}

	public String getUrlrewriteCustom(){
		return urlrewriteCustom;
	}

	public void setShopLatitude(String shopLatitude){
		this.shopLatitude = shopLatitude;
	}

	public String getShopLatitude(){
		return shopLatitude;
	}

	public void setShopCityId(int shopCityId){
		this.shopCityId = shopCityId;
	}

	public int getShopCityId(){
		return shopCityId;
	}

	public void setShopPhone(String shopPhone){
		this.shopPhone = shopPhone;
	}

	public String getShopPhone(){
		return shopPhone;
	}

	public void setShopFreeShipUpto(int shopFreeShipUpto){
		this.shopFreeShipUpto = shopFreeShipUpto;
	}

	public int getShopFreeShipUpto(){
		return shopFreeShipUpto;
	}

	public void setShopPostalcode(String shopPostalcode){
		this.shopPostalcode = shopPostalcode;
	}

	public String getShopPostalcode(){
		return shopPostalcode;
	}

	public void setShopMaxRentRadius(int shopMaxRentRadius){
		this.shopMaxRentRadius = shopMaxRentRadius;
	}

	public int getShopMaxRentRadius(){
		return shopMaxRentRadius;
	}

	@Override
 	public String toString(){
		return 
			"ShopPostData{" + 
			"shop_country_id = '" + shopCountryId + '\'' + 
			",shop_longitude = '" + shopLongitude + '\'' + 
			",shop_state = '" + shopState + '\'' + 
			",shop_max_sell_radius = '" + shopMaxSellRadius + '\'' + 
			",shop_supplier_display_status = '" + shopSupplierDisplayStatus + '\'' + 
			",shop_identifier = '" + shopIdentifier + '\'' + 
			",shop_auto_complete = '" + shopAutoComplete + '\'' + 
			",shop_id = '" + shopId + '\'' + 
			",urlrewrite_custom = '" + urlrewriteCustom + '\'' + 
			",shop_latitude = '" + shopLatitude + '\'' + 
			", shop_city_id = '" + shopCityId + '\'' + 
			",shop_phone = '" + shopPhone + '\'' + 
			",shop_free_ship_upto = '" + shopFreeShipUpto + '\'' + 
			",shop_postalcode = '" + shopPostalcode + '\'' + 
			",shop_max_rent_radius = '" + shopMaxRentRadius + '\'' + 
			"}";
		}
}