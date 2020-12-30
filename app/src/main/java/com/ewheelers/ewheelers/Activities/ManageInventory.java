package com.ewheelers.ewheelers.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import static android.view.View.GONE;

public class ManageInventory extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener,TextWatcher {
    RecyclerView recyclerView;
    InventoryAdapter inventoryAdapter;
    InventoryModel inventoryModel;
    List<InventoryModel> inventoryModels = new ArrayList<>();
    EditText searchProd;
    Button button;
    SwipeRefreshLayout swipeRefreshLayout;
    private int pagenumber;
    private int itemcount = 10;
    private int visibleitemcount, totalitemcount, pastvisibleitems;
    private boolean loading = true;
    ProgressBar progressBar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);
        progressBar = findViewById(R.id.progres);
        swipeRefreshLayout = findViewById(R.id.swipelayout);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorAccent, null));
        pagenumber = 1;
        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.list_of_prods);
        searchProd = findViewById(R.id.search_prod);
        searchProd.addTextChangedListener(this);
        button = findViewById(R.id.search);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //inventoryModels.clear();
                getList(searchProd.getText().toString(), 1,"1");
                hideKeyboardFrom(getApplicationContext(), v);
            }
        });
        //getList(searchProd.getText().toString(), pagenumber);
        if (inventoryModels != null) {
            inventoryAdapter = new InventoryAdapter(getApplicationContext(), inventoryModels);
            final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(inventoryAdapter);
            swipeRefreshLayout.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            //swipeRefreshLayout.setRefreshing(true);
                                            pagenumber = 1;
                                            loading = true;
                                            //inventoryModels.clear();
                                            getList(searchProd.getText().toString(), pagenumber,"1");
                                        }
                                    }
            );
           /* swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    pagenumber = 1;
                    loading = true;
                    getList(searchProd.getText().toString(), pagenumber);
                }
            });*/

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0) {
                        visibleitemcount = layoutManager.getChildCount();
                        totalitemcount = layoutManager.getItemCount();
                        pastvisibleitems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

//if loading is true which means there is data to be fetched from the database

                        if (loading) {
                            if ((visibleitemcount + pastvisibleitems) >= totalitemcount) {
                                progressBar.setVisibility(View.VISIBLE);
                                //swipeRefreshLayout.setRefreshing(true);
                                loading = false;
                                pagenumber += 1;
                                getList(searchProd.getText().toString(), pagenumber,"0");
                            }
                        }
                    }
                }
            });
        }
    }


    public void getList(String s, int pagenumer,String clean) {
        swipeRefreshLayout.setRefreshing(true);
        final RequestQueue queue = Volley.newRequestQueue(ManageInventory.this);
        String serverurl = API.sellerProducts;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(clean.equals("1")){
                    inventoryModels.clear();
                }
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        progressBar.setVisibility(GONE);
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonData.getJSONArray("arrListing");
                        if (jsonArray.length() == 0) {
                            loading = false;
                            swipeRefreshLayout.setRefreshing(false);
                            pagenumber = 0;
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
                            /*LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                            recyclerView.setLayoutManager(linearLayoutManager);
                            inventoryAdapter = new InventoryAdapter(getApplicationContext(), inventoryModels);
                            recyclerView.setAdapter(inventoryAdapter);
                            inventoryAdapter.notifyDataSetChanged();*/
                            if (s.isEmpty()) {
                                if (!inventoryModels.isEmpty()) {
                                    loading = true;
                                    swipeRefreshLayout.setRefreshing(false);
                                    //Toast.makeText(ManageInventory.this, "size:" + inventoryModels.size(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "no more data available...", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                loading = false;
                                swipeRefreshLayout.setRefreshing(false);
                            }

                        }

                    } else {
                        Toast.makeText(ManageInventory.this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                inventoryAdapter.notifyDataSetChanged();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Main", "Error: " + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
                swipeRefreshLayout.setRefreshing(false);
                progressBar.setVisibility(GONE);
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
                /*if (!s.isEmpty() || s != null) {
                    data3.put("keyword", s);
                }else {
                    data3.put("keyword", "");
                }*/
                data3.put("keyword", s);
                data3.put("page", String.valueOf(pagenumer));
                data3.put("pageCount", String.valueOf(itemcount));
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
        s = s.toString();
        if(s.equals(searchProd.getEditableText().toString())){
            if(s.length()==0){
                //inventoryModels.clear();
                getList(searchProd.getText().toString(),1,"1");
            }
        }

    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public void onRefresh() {
        //inventoryModels.clear();
        getList(searchProd.getText().toString(), 1,"1");
    }
}
