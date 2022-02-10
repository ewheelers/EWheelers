package com.ewheelers.ewheelers.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.ActivityModels.Attributes;
import com.ewheelers.ewheelers.ActivtiesAdapters.AttributesAdapter;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ewheelers.ewheelers.Activities.Home.drawer;

public class UpdateAttributes extends AppCompatActivity {
    RecyclerView recyclerView;
    AttributesAdapter attributesAdapter;
    List<Attributes> attributes = new ArrayList<>();
    List<Attributes> optionIdsList=new ArrayList<>();
    String tokenvalue,attributes_list;
    private int mStatusCode = 0;
    ProgressDialog progressDialog;
    Button savedetails;
    String fulname,phonno,dob,countryid,stateid,cityid,cityname,organisation,profilebrief;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_attributes);
        tokenvalue = new SessionPreference().getStrings(this, SessionPreference.tokenvalue);
        fulname = new SessionPreference().getStrings(this, SessionPreference.name);
        phonno = new SessionPreference().getStrings(this, SessionPreference.mobile_no);
        dob = new SessionPreference().getStrings(this, SessionPreference.dobs);
        organisation = new SessionPreference().getStrings(this, SessionPreference.organise);
        profilebrief = new SessionPreference().getStrings(this, SessionPreference.profileIs);
        countryid = new SessionPreference().getStrings(this, SessionPreference.contryid);
        stateid = new SessionPreference().getStrings(this, SessionPreference.stateid);
        cityname = new SessionPreference().getStrings(this, SessionPreference.cityname);
        cityid = new SessionPreference().getStrings(this, SessionPreference.cityid);
        recyclerView = findViewById(R.id.attributes_list);
        savedetails = findViewById(R.id.save_attributes);
        getListOfAttributes();
        progressDialog = new ProgressDialog(UpdateAttributes.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Account SetUp ....");
        progressDialog.setCancelable(false);
        savedetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpAccount(v);
            }
        });
        getProfile();
    }

    private void getProfile() {
        optionIdsList.clear();
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = API.getProfileinfo;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("selectedOptionsArr");
                        if(jsonArray.length()!=0){
                            for(int i=0;i<jsonArray.length();i++){
                                JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                                String optionIdIs = jsonObject2.getString("option_id");
                                Attributes attributeList = new Attributes();
                                attributeList.setProductid(optionIdIs);
                                optionIdsList.add(attributeList);
                            }
                            /*Log.e("optionsList",optionIdsList.toString());
                            new AttributesAdapter(UpdateAttributes.this,null,optionIdsList);*/

                        }else {
                            Toast.makeText(UpdateAttributes.this, "Not Selected yet", Toast.LENGTH_SHORT).show();
                        }
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
                params.put("X-TOKEN", tokenvalue);
                return params;
            }


            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        Log.e("Request...",stringRequest.toString());
        queue.add(stringRequest);

    }

    private void getListOfAttributes() {
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = API.getAttributes;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String smsg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    JSONObject jsonObject1 = jsonObjectData.getJSONObject("sellerProfileAttributes");
                    JSONArray jsonArray = jsonObject1.getJSONArray("Partner Type");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject2 = jsonArray.getJSONObject(i);
                        String prod_id = jsonObject2.getString("sattropt_id");
                        String prod_name = jsonObject2.getString("sattropt_identifier");
                        String prod_type = jsonObject2.getString("sattropt_type");
                        Attributes attributes1 = new Attributes(prod_id,prod_name,prod_type);
                        attributes.add(attributes1);
                    }

                    attributesAdapter = new AttributesAdapter(UpdateAttributes.this,attributes,optionIdsList);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(UpdateAttributes.this,RecyclerView.VERTICAL,false);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    recyclerView.setAdapter(attributesAdapter);

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
                params.put("X-TOKEN", tokenvalue);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response != null) {
                    mStatusCode = response.statusCode;
                }
                assert response != null;
                return super.parseNetworkResponse(response);
            }

            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        Log.e("Request...",stringRequest.toString());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);

    }

    public void attributeslist(String list){
        attributes_list = list;
    }

    private  void setUpAccount(final View v){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = API.setupaccount;
        progressDialog.show();
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String smsg = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                progressDialog.dismiss();
                                SessionPreference.clearString(UpdateAttributes.this,SessionPreference.accountsetup);
                                SessionPreference.saveString(UpdateAttributes.this,SessionPreference.partnerattributes,"yes");
                                /*Intent i = new Intent(UpdateAttributes.this,Home.class);
                                i.putExtra("fromsetup","1");
                                startActivity(i);*/
                                Snackbar snack = Snackbar.make(v, "Account Setup Successful", Snackbar.LENGTH_LONG);
                                View view = snack.getView();
                                TextView tv = (TextView) view.findViewById(R.id.snackbar_text);
                                tv.setTextColor(Color.BLACK);
                                snack.setBackgroundTint(Color.WHITE);
                                snack.setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                        drawer.openDrawer(Gravity.LEFT);
                                    }
                                }).show();

                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(v, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", new SessionPreference().getStrings(UpdateAttributes.this,SessionPreference.tokenvalue));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("user_name", fulname);
                data3.put("user_dob", dob);
                data3.put("user_phone", phonno);
                data3.put("user_country_id", countryid);
                data3.put("user_state_id", stateid);
                data3.put("user_city_id", cityid);
                if(cityname!=null){
                    data3.put("user_city", cityname);
                }else {
                    data3.put("user_city", cityid);
                }
                data3.put("user_company", organisation);
                data3.put("user_profile_info", profilebrief);
                //data3.put("user_products_services", "");
                if(attributes_list!=null) {
                    data3.put("attribute_options", attributes_list);
                }
                Log.e("postData...",data3.toString());
                return data3;

            }
        };
        Log.e("Request...",strRequest.toString());
        queue.add(strRequest);

    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(getApplicationContext(),SetupAccount.class);
        startActivity(i);
        finish();
    }

}
