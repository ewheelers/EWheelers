package com.ewheelers.ewheelers.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.ActivityModels.Stateslist;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class eStoreSubGeneralFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner country_list,state_list,city_list;
    EditText pincode,mobileno;
    ArrayList<Stateslist> countrieslist = new ArrayList<>();
    ArrayList<Stateslist> statelist = new ArrayList<>();
    ArrayList<Stateslist> citieslist = new ArrayList<>();
    ScrollView scril;
    String tokenValue,countryString,countryid, stateString,stateid, cityString,cityid;
    Button saveaddress;
    private ProgressDialog progressDialog;
    public eStoreSubGeneralFragment() {
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
        View v = inflater.inflate(R.layout.fragment_e_store_sub_general, container, false);
        tokenValue = new SessionPreference().getStrings(getActivity(),SessionPreference.tokenvalue);
        country_list = v.findViewById(R.id.contry);
        state_list = v.findViewById(R.id.state);
        city_list = v.findViewById(R.id.city_name);
        scril = v.findViewById(R.id.screi);
        pincode = v.findViewById(R.id.pincode);
        mobileno = v.findViewById(R.id.phoneno);
        country_list.setOnItemSelectedListener(this);
        state_list.setOnItemSelectedListener(this);
        saveaddress = v.findViewById(R.id.saveaddress);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Return - General SetUp ....");
        progressDialog.setCancelable(false);
        getCountries();
        saveaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReturnAddress();
            }
        });
        return v;
    }

    private void getCountries() {
        String url_link = API.countries;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("countries");
                    for (int i = 0; i < jsonArrayCountries.length(); i++) {
                        JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                        String countryid = jsonObjectcountry.getString("id");
                        String countryname = jsonObjectcountry.getString("name");
                        Stateslist stateslist = new Stateslist(countryid,countryname);
                        //countrieslist.add(countryid + " - " + countryname);
                        countrieslist.add(stateslist);
                    }

                    country_list.setAdapter(new ArrayAdapter<Stateslist>(getActivity(), android.R.layout.simple_spinner_dropdown_item, countrieslist));


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
                params.put("X-TOKEN", tokenValue);
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


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Stateslist stateslist = (Stateslist) country_list.getSelectedItem();
        countryString = stateslist.getStatename();
        //countryString = country_list.getSelectedItem().toString();
        countryid = stateslist.getStateid();
        getReturnAddress(countryid);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getStatesNames(final String countryid,final String stat_id,final String cit_id) {
        statelist.clear();
        String url_link = API.states + countryid;
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equals("1")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        //String cartItemsCount = jsonObjectData.getString("cartItemsCount");
                        JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("states");
                        if(jsonArrayCountries.length()!=0) {
                            for (int i = 0; i < jsonArrayCountries.length(); i++) {
                                JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                                String countryid = jsonObjectcountry.getString("id");
                                JSONObject countryname = jsonObjectcountry.getJSONObject("name");
                                String stateid = countryname.getString("state_id");
                                String state_code = countryname.getString("state_code");
                                String state_name = countryname.getString("state_name");
                                Stateslist stateslist = new Stateslist(stateid, state_name);
                                //statelist.add(stateid + " - " + state_name);
                                statelist.add(stateslist);
                            }
                            if(statelist!=null) {
                                state_list.setAdapter(new ArrayAdapter<Stateslist>(getActivity(), android.R.layout.simple_spinner_dropdown_item, statelist));
                                if (!stat_id.isEmpty()) {
                                    for (int j = 0; j < statelist.size(); j++) {
                                        if (statelist.get(j).getStateid().equals(stat_id)) {
                                            state_list.setSelection(j);
                                        }
                                    }
                                }
                            }
                            state_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Stateslist stateslist = (Stateslist) state_list.getSelectedItem();
                                    stateString = stateslist.getStatename();
                                    stateid = stateslist.getStateid();
                                    //stateString = state_list.getSelectedItem().toString();
                                    //Toast.makeText(SetupBillingAddressActivity.this, splitString(stateString), Toast.LENGTH_SHORT).show();
                                    getCityNames(countryid, stateslist.getStateid(),cit_id);

                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }else {
                            Toast.makeText(getActivity(), "No states to select", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
                params.put("X-TOKEN", tokenValue);
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

    private void getCityNames(String countryid, String stateid, final String cityId) {
        citieslist.clear();
        String url_link = API.cities + countryid + "/" + stateid;
        //Log.i("cityurl:", url_link);
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if(status.equals("1")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        //String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                        JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("cities");
                        if (jsonArrayCountries.length() != 0) {
                            for (int i = 0; i < jsonArrayCountries.length(); i++) {
                                JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                                String cityid = jsonObjectcountry.getString("id");
                                String cityname = jsonObjectcountry.getString("name");
                                Stateslist stateslist = new Stateslist(cityid, cityname);
                                //citieslist.add(cityid + " - " + cityname);
                                citieslist.add(stateslist);
                            }
                            if(citieslist!=null) {
                                city_list.setAdapter(new ArrayAdapter<Stateslist>(getActivity(), android.R.layout.simple_spinner_dropdown_item, citieslist));
                                if(!cityId.isEmpty()){
                                    for (int j = 0; j < citieslist.size(); j++) {
                                        if (citieslist.get(j).getStateid().equals(cityId))
                                            city_list.setSelection(j);
                                    }
                                }
                            }
                            city_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Stateslist stateslist = (Stateslist) city_list.getSelectedItem();
                                    //cityString = city_list.getSelectedItem().toString();
                                    cityid = stateslist.getStateid();
                                    cityString = stateslist.getStatename();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });

                        } else {
                            Toast.makeText(getActivity(), "No cities are added to show", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
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
                params.put("X-TOKEN", tokenValue);
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

    private void getReturnAddress(final String countryid) {
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        String serverurl = API.getreturnaddress;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                       //JSONObject jsonObject3 = jsonObject1.getJSONObject("shopDetails");
                        JSONObject jsonObject4 = jsonObject1.getJSONObject("returnAddressData");
                        if(jsonObject4.length()==0){
                            getStatesNames(countryid,"","");
                        }else {
                            String user_id = jsonObject4.getString("ura_user_id");
                            String user_cityid = jsonObject4.getString("ura_city_id");
                            String user_stateid = jsonObject4.getString("ura_state_id");
                            String user_contryid = jsonObject4.getString("ura_country_id");
                            String user_zip = jsonObject4.getString("ura_zip");
                            String user_phone = jsonObject4.getString("ura_phone");
                            mobileno.setText(user_phone);
                            pincode.setText(user_zip);
                            getStatesNames(user_contryid,user_stateid,user_cityid);
                        }
                    }

                    else {
                        Snackbar.make(scril, msg, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
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
                params.put("X-TOKEN", tokenValue);
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


    private void saveReturnAddress() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String url = API.setupreturnaddress;
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
                                JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                                Snackbar.make(scril, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(scril, smsg, Snackbar.LENGTH_LONG)
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
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                //contact_person
                data3.put("ura_country_id", countryid);
                data3.put("ura_state_id", stateid);
                data3.put("ura_city_id", cityid);
                data3.put("ura_zip", pincode.getText().toString());
                data3.put("ura_phone", mobileno.getText().toString());
                return data3;

            }
        };

        queue.add(strRequest);

    }


}
