package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.ActivityModels.InventoryModel;
import com.ewheelers.ewheelers.ActivtiesAdapters.InventoryAdapter;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageInventory extends AppCompatActivity implements TextWatcher {
    RecyclerView recyclerView;
    InventoryAdapter inventoryAdapter;
    InventoryModel inventoryModel;
    List<InventoryModel> inventoryModels = new ArrayList<>();
    EditText searchProd;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);
        recyclerView = findViewById(R.id.list_of_prods);
        searchProd = findViewById(R.id.search_prod);
        searchProd.addTextChangedListener(this);
        button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getList(searchProd.getText().toString());
                hideKeyboardFrom(getApplicationContext(),v);
            }
        });
        getList("");
    }


    public void getList(String s) {
        inventoryModels.clear();
        final RequestQueue queue = Volley.newRequestQueue(ManageInventory.this);
        String serverurl = API.sellerProducts;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonData.getJSONArray("arrListing");
                        if (jsonArray.length() == 0) {

                        } else {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonListObj = jsonArray.getJSONObject(i);
                                String prodName = jsonListObj.getString("product_name");
                                String sellPrice = jsonListObj.getString("selprod_price");
                                String availQty = jsonListObj.getString("selprod_stock");
                                String rentQty = jsonListObj.getString("sprodata_rental_stock");
                                String statusOfprod = jsonListObj.getString("selprod_active");
                                inventoryModel = new InventoryModel();
                                inventoryModel.setName(prodName);
                                inventoryModel.setSell_price(sellPrice);
                                inventoryModel.setAvail_qty(availQty);
                                inventoryModel.setRental_qty(rentQty);
                                inventoryModel.setStatusOfprod(statusOfprod);
                                inventoryModels.add(inventoryModel);
                            }
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            inventoryAdapter = new InventoryAdapter(getApplicationContext(), inventoryModels);
                            recyclerView.setAdapter(inventoryAdapter);
                            inventoryAdapter.notifyDataSetChanged();

                        }

                    } else {
                        Toast.makeText(ManageInventory.this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", new SessionPreference().getStrings(ManageInventory.this, SessionPreference.tokenvalue));
                return params;
            }


            @Override
            public Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("keyword", s);
                return data3;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);

    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if(count==0){
            getList("");
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
