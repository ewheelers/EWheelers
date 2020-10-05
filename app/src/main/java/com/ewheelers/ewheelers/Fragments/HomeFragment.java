package com.ewheelers.ewheelers.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.Activities.BankAccountDetails;
import com.ewheelers.ewheelers.Activities.eStoreSettings;
import com.ewheelers.ewheelers.ActivityModels.Attributes;
import com.ewheelers.ewheelers.ActivityModels.DashboardList;
import com.ewheelers.ewheelers.ActivtiesAdapters.AttributesAdapter;
import com.ewheelers.ewheelers.ActivtiesAdapters.DashBoardSettingsAdapter;
import com.ewheelers.ewheelers.ActivtiesAdapters.HomeRecyclerAdapter;
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

public class HomeFragment extends Fragment {
    RecyclerView recyclerView,recyclerview_hub;
    HomeRecyclerAdapter homeRecyclerAdapter;
    DashBoardSettingsAdapter hubRecyclerAdapter;
    List<Attributes> attributesList = new ArrayList<>();
    String completedSales,inprocessSales,amount,creditEarnedToday,completedOrders,pendingOrders,refundedOrders,refundedAmount,cancelledOrders,cancelledAmount;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = v.findViewById(R.id.recycler_list);
        recyclerview_hub = v.findViewById(R.id.recycler_list_hub);
        getList();
        return v;
    }

    private void getList() {
        attributesList.clear();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
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
                        String currency = jsonObject1.getString("currencySymbol");
                        amount = currency+jsonObject1.getString("userBalance");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("ordersStats");
                        creditEarnedToday = currency+jsonObject2.getString("todayOrderCount");
                        inprocessSales = currency+jsonObject2.getString("totalInprocessSales");
                        completedSales = currency+jsonObject2.getString("totalSoldSales");
                        completedOrders = jsonObject2.getString("totalSoldCount");
                        refundedOrders = jsonObject2.getString("refundedOrderCount");
                        refundedAmount = currency+jsonObject2.getString("refundedOrderAmount");
                        cancelledOrders = jsonObject2.getString("cancelledOrderCount");
                        cancelledAmount = currency+jsonObject2.getString("cancelledOrderAmount");
                        pendingOrders = jsonObject2.getString("totalPendingOrders");

                        homeRecyclerAdapter = new HomeRecyclerAdapter(getActivity(), stringList(amount,creditEarnedToday,
                                inprocessSales,completedSales,completedOrders,refundedOrders,refundedAmount,cancelledOrders,
                                cancelledAmount,pendingOrders));
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        recyclerView.setAdapter(homeRecyclerAdapter);
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("SellerProductAttribute");
                        JSONObject serviceattributes = jsonObject3.getJSONObject("Service Type");
                        Iterator iterator = serviceattributes.keys();
                        while (iterator.hasNext()){
                            String keys = (String) iterator.next();
                            String value = serviceattributes.getString(keys);
                            Attributes attributes = new Attributes();
                            attributes.setProductid(keys);
                            attributes.setProductname(value);
                            attributesList.add(attributes);
                        }
                        hubRecyclerAdapter = new DashBoardSettingsAdapter(getActivity(),attributesList);
                        LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                        recyclerview_hub.setLayoutManager(linearLayoutManager2);
                        recyclerview_hub.setAdapter(hubRecyclerAdapter);

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
                params.put("X-TOKEN", new SessionPreference().getStrings(getActivity(),SessionPreference.tokenvalue));
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

    public List<DashboardList> stringList(String amount, String creditEarnedToday, String inprocessSales, String completedSales, String completedOrders, String refundedOrders, String refundedAmount, String cancelledOrders, String cancelledAmount, String pendingOrders) {
        List<DashboardList> strings = new ArrayList<>();
        strings.add(new DashboardList(getResources().getDrawable(R.drawable.city),"Sales", "Completed Sales", "Inprocess Sales", completedSales, inprocessSales));
        strings.add(new DashboardList(getResources().getDrawable(R.drawable.city),"Credits", "Amount", "Credits Earned Today", amount, creditEarnedToday));
        strings.add(new DashboardList(getResources().getDrawable(R.drawable.city),"Orders", "Completed Orders", "Pending Orders", completedOrders, pendingOrders));
        strings.add(new DashboardList(getResources().getDrawable(R.drawable.city),"Refunds", "Refunded Orders", "Refunded Amount", refundedOrders, refundedAmount));
        strings.add(new DashboardList(getResources().getDrawable(R.drawable.city),"Cancellation", "Cancelled Orders", "Cancelled Orders Amount",cancelledOrders, cancelledAmount));

        return strings;
    }
}
