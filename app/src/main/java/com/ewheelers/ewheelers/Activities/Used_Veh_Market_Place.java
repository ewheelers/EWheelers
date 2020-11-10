package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.ActivityModels.buyingList;
import com.ewheelers.ewheelers.ActivtiesAdapters.BuyListAdapter;
import com.ewheelers.ewheelers.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Used_Veh_Market_Place extends AppCompatActivity {
    RecyclerView show_list;
    BuyListAdapter buyListAdapter;
    List<buyingList> buyListList = new ArrayList<>();
    TextView empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used__veh__market__place);
        show_list = findViewById(R.id.buy_list);
        empty_view = findViewById(R.id.emptyview);
        getExcelList();
    }

    private void getExcelList() {
        final ProgressDialog loading = ProgressDialog.show(this, "loading...", "Please wait...", false, false);
        String url_link = "https://script.google.com/macros/s/AKfycbwGLwgCPJQHX2BzUBI31HljTpNCd2qHU8cPQGao6g2bXiVt90vd/exec?action=getItems";
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.startsWith("<")) {
                    loading.dismiss();
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        if (jsonArray.length() == 0) {
                            empty_view.setVisibility(View.VISIBLE);
                            show_list.setVisibility(View.GONE);
                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectdata = jsonArray.getJSONObject(i);
                                String buyerid = jsonObjectdata.getString("BuyerId");
                                String phno = jsonObjectdata.getString("PhoneNumber");
                                String image = jsonObjectdata.getString("Image");
                                String city = jsonObjectdata.getString("City");
                                String state = jsonObjectdata.getString("State");
                                String pincode = jsonObjectdata.getString("Pincode");
                                String brand = jsonObjectdata.getString("Brand");
                                String model = jsonObjectdata.getString("Model");
                                String manyr = jsonObjectdata.getString("ManufacturingYear");
                                String regyr = jsonObjectdata.getString("RegistrationYear");
                                String poston = jsonObjectdata.getString("PostedOn");
                                buyingList myOrdersModel = new buyingList();
                                myOrdersModel.setUserid(buyerid);
                                myOrdersModel.setPhone(phno);
                                myOrdersModel.setImgUrl(image);
                                myOrdersModel.setCity(city);
                                myOrdersModel.setState(state);
                                myOrdersModel.setPincode(pincode);
                                myOrdersModel.setBrand(brand);
                                myOrdersModel.setModel(model);
                                myOrdersModel.setManufactur_yr(manyr);
                                myOrdersModel.setReg_yr(regyr);
                                myOrdersModel.setPostedOn(poston);
                                buyListList.add(myOrdersModel);
                           /* if (orderid.equals(myOrdersModel.getOrder_id())) {
                                myOrdersModel.setVehiclemodel(vehmodel);
                                myOrdersModel.setVehicleno(vehno);
                                myOrdersModel.setTimings(timing);
                                myOrdersModel.setStatAddress(address);
                                myOrdersModel.setBitmap(responseQr);
                                myOrdersModelList.add(myOrdersModel);
                            }*/
                            }
                            empty_view.setVisibility(View.GONE);
                            show_list.setVisibility(View.VISIBLE);
                            buyListAdapter = new BuyListAdapter(Used_Veh_Market_Place.this, buyListList);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Used_Veh_Market_Place.this, RecyclerView.VERTICAL, false);
                            show_list.setLayoutManager(linearLayoutManager);
                            show_list.setAdapter(buyListAdapter);
                            loading.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
                loading.dismiss();
            }
        }) {

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }*/

            @Override
            public Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                return data3;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }

}
