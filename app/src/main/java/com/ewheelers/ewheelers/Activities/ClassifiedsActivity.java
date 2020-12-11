package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.ActivityModels.Attributes;
import com.ewheelers.ewheelers.ActivityModels.ClassifiedModel;
import com.ewheelers.ewheelers.ActivtiesAdapters.Classifieds_Adapter;
import com.ewheelers.ewheelers.ActivtiesAdapters.DashBoardSettingsAdapter;
import com.ewheelers.ewheelers.ActivtiesAdapters.HomeRecyclerAdapter;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.Network.VolleySingleton;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ClassifiedsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    String bannerUrl;
    Classifieds_Adapter classifieds_adapter;
    List<ClassifiedModel> classifiedModels = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classifieds);
        recyclerView = findViewById(R.id.list_of_banners);
        getList();
    }

    private void getList() {
        classifiedModels.clear();
        RequestQueue queue = Volley.newRequestQueue(ClassifiedsActivity.this);
        String serverurl = API.dashboard;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String sellerApproval = jsonObject1.getString("is_seller_approved");
                        JSONObject jsonObjectBann = jsonObject1.getJSONObject("banners");
                        JSONObject jsonObjectSub = jsonObjectBann.getJSONObject("Partner_App_Homepage_Top_Banner");
                        JSONArray jsonArray = jsonObjectSub.getJSONArray("banners");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectFromArra = jsonArray.getJSONObject(i);
                            String imageUrl = jsonObjectFromArra.getString("banner_image_url");
                            bannerUrl = jsonObjectFromArra.getString("banner_url");
                            ClassifiedModel classifiedModel = new ClassifiedModel();
                            classifiedModel.setImageurl(imageUrl);
                            classifiedModel.setUrl(bannerUrl);
                            classifiedModels.add(classifiedModel);
                        }

                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ClassifiedsActivity.this,RecyclerView.VERTICAL,false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        classifieds_adapter = new Classifieds_Adapter(ClassifiedsActivity.this,classifiedModels);
                        recyclerView.setAdapter(classifieds_adapter);
                        classifieds_adapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(ClassifiedsActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                params.put("X-TOKEN", new SessionPreference().getStrings(ClassifiedsActivity.this, SessionPreference.tokenvalue));
                return params;
            }


            @Override
            public Map<String, String> getParams() {
                return null;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);

    }

}
