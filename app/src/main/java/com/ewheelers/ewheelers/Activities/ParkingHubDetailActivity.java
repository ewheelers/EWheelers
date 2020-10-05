package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.ewheelers.ewheelers.ActivityModels.Attributes;
import com.ewheelers.ewheelers.ActivityModels.HubOrdersModel;
import com.ewheelers.ewheelers.ActivtiesAdapters.DashBoardSettingsAdapter;
import com.ewheelers.ewheelers.ActivtiesAdapters.HomeRecyclerAdapter;
import com.ewheelers.ewheelers.ActivtiesAdapters.ParkingHubOrdersAdapter;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ParkingHubDetailActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textView;
    String title,titleId,completedOrders,completedAmount,inprocessOrder,inprocessAmoount,pendingOrder,pendingAmount,totalOrder,totalAmount;
    List<HubOrdersModel> hubOrdersModels = new ArrayList<>();
    ParkingHubOrdersAdapter parkingHubOrdersAdapter;
    Button scan_park_pass;TextView viewall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_hub_detail);
        recyclerView = findViewById(R.id.recycler_list);
        textView = findViewById(R.id.hub_title);
        scan_park_pass = findViewById(R.id.scan_park_pass);
        viewall = findViewById(R.id.view_all);
        titleId = getIntent().getStringExtra("hubid");
        title = getIntent().getStringExtra("hubname");
        textView.setText(title);
        getOrdersList();
        viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ViewParkOrders.class);
                startActivity(i);
            }
        });
        scan_park_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),ScanQRCode.class);
                startActivity(i);
            }
        });
    }

    private void getOrdersList() {
        RequestQueue queue = Volley.newRequestQueue(ParkingHubDetailActivity.this);
        String serverurl = API.hub_orders;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("stats");
                        completedOrders = jsonObject2.getString("completedOrderCount");
                        completedAmount = jsonObject2.getString("completedOrderAmount");
                        inprocessOrder = jsonObject2.getString("inProcessOrderCount");
                        inprocessAmoount = jsonObject2.getString("inProcessOrderAmount");
                        pendingOrder = jsonObject2.getString("pendingOrderCount");
                        pendingAmount = jsonObject2.getString("pendingOrderAmount");
                        totalOrder = jsonObject2.getString("totalOrderCount");
                        totalAmount = jsonObject2.getString("totalOrderAmount");
                        HubOrdersModel hubOrdersModel = new HubOrdersModel(completedOrders,completedAmount,inprocessOrder,inprocessAmoount,pendingOrder,pendingAmount,totalOrder,totalAmount);
                        hubOrdersModels.add(hubOrdersModel);
                        parkingHubOrdersAdapter = new ParkingHubOrdersAdapter(ParkingHubDetailActivity.this,hubOrdersModels);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ParkingHubDetailActivity.this,RecyclerView.HORIZONTAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(parkingHubOrdersAdapter);
                    }else {
                        Toast.makeText(ParkingHubDetailActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                params.put("X-TOKEN", new SessionPreference().getStrings(ParkingHubDetailActivity.this,SessionPreference.tokenvalue));
                return params;
            }


            @Override
            public Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("opao_option_id", titleId);
                return data3;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }
}
