package com.ewheelers.ewheelers.Network;

import com.ewheelers.ewheelers.Network.Model.Response.UpdateShopResponse;
import com.ewheelers.ewheelers.Network.Model.ShopPostData;

import org.json.JSONObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public class RestAPI {
    public interface SetupShop {

        @FormUrlEncoded
        @POST("/app-api/2.0/seller/setup-shop")
        Call<UpdateShopResponse> call(@FieldMap Map<String, String> params, @Header("X-TOKEN") String token);

    }
}
