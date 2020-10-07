package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
import com.ewheelers.ewheelers.ActivityModels.HubOrdersModel;
import com.ewheelers.ewheelers.ActivityModels.OrdersModel;
import com.ewheelers.ewheelers.ActivtiesAdapters.OrdersAdapter;
import com.ewheelers.ewheelers.ActivtiesAdapters.ParkingHubOrdersAdapter;
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

public class ViewParkOrders extends AppCompatActivity {
    RecyclerView recyclerView;
    List<OrdersModel> ordersModels = new ArrayList<>();
    OrdersAdapter ordersAdapter;
    TextView tit;
    ImageView goback;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_park_orders);
        progressDialog = new ProgressDialog(this);
        //progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading...");
        recyclerView = findViewById(R.id.recycler_list);
        tit = findViewById(R.id.hub_title);
        ;
        goback = findViewById(R.id.homeBack);
        String titleId = getIntent().getStringExtra("titleid");
        String titleIs = getIntent().getStringExtra("title");
        tit.setText(titleIs + "Orders");
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getOrdersList(titleId);

    }

    private void getOrdersList(final String titleId) {
        progressDialog.show();
        ordersModels.clear();
        RequestQueue queue = Volley.newRequestQueue(ViewParkOrders.this);
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
                        JSONArray jsonArray = jsonObject1.getJSONArray("orders");
                        if (jsonArray.length() != 0) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                String orderid = jsonObject2.getString("order_id");
                                String invoiceno = jsonObject2.getString("op_invoice_number");
                                String productname = jsonObject2.getString("op_product_name");
                                String netamount = jsonObject2.getString("order_net_amount");
                                String statusname = jsonObject2.getString("orderstatus_name");
                                String orderDate = jsonObject2.getString("order_date_added");
                                String prod_options = jsonObject2.getString("op_selprod_options");
                                OrdersModel ordersModel = new OrdersModel(orderid, invoiceno, productname, prod_options, netamount, orderDate, statusname);
                                ordersModels.add(ordersModel);
                            }
                            ordersAdapter = new OrdersAdapter(ViewParkOrders.this, ordersModels);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ViewParkOrders.this, RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            recyclerView.setAdapter(ordersAdapter);
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ViewParkOrders.this, "No Orders", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(ViewParkOrders.this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());

            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", new SessionPreference().getStrings(ViewParkOrders.this, SessionPreference.tokenvalue));
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
