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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
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
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class eStoreSubEnglishFragment extends Fragment {
    EditText address_one, address_two,nameIs;
    private String language_id;
    ScrollView scrollView;
    String tokenValue,cityIs;
    Button saveChnages;
    ProgressDialog progressDialog;
    public eStoreSubEnglishFragment() {
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
        View v = inflater.inflate(R.layout.fragment_e_store_sub_english, container, false);
        address_one = v.findViewById(R.id.address_one);
        address_two = v.findViewById(R.id.address_two);
        nameIs = v.findViewById(R.id.name);
        scrollView = v.findViewById(R.id.scrolIs);
        saveChnages = v.findViewById(R.id.save_return_lang);
        tokenValue = new SessionPreference().getStrings(getActivity(),SessionPreference.tokenvalue);
        getHomeLang();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Return - English SetUp ....");
        progressDialog.setCancelable(false);
        saveChnages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLangAddress();
            }
        });
        return v;
    }



    private void getHomeLang() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String serverurl = API.gethomelanguage;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray jsonArray = jsonObject1.getJSONArray("languages");
                        JSONObject jsonObject2 = jsonArray.getJSONObject(0);
                        language_id = jsonObject2.getString("language_id");
                        //Log.e("langid",language_id);
                        getLanguageForm(language_id);
                    } else {
                        Snackbar.make(scrollView, msg, Snackbar.LENGTH_LONG)
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

    private void getLanguageForm(String langid) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String serverurl = API.getreturnlangform + langid;
        Log.e("getlanguri",serverurl);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("shopDetails");
                        JSONObject jsonObject4 = jsonObject1.getJSONObject("returnAddressLangData");
                        if (jsonObject4.length() == 0) {
                            Toast.makeText(getActivity(), "No data to show", Toast.LENGTH_SHORT).show();
                        } else {
                            String ua_name = jsonObject4.getString("ura_name");
                            String cityOf = jsonObject4.getString("ura_city");
                            String addre_oneIs = jsonObject4.getString("ura_address_line_1");
                            String addre_twoIs = jsonObject4.getString("ura_address_line_2");
                            nameIs.setText(ua_name);
                            address_one.setText(addre_oneIs);
                            address_two.setText(addre_twoIs);
                            cityIs = cityOf;
                        }

                    } else {
                        Snackbar.make(scrollView, msg, Snackbar.LENGTH_LONG)
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

    private void saveLangAddress() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String url = API.getreturnaddresslang;
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

                                Snackbar.make(scrollView, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            } else {
                                progressDialog.dismiss();
                                Snackbar.make(scrollView, smsg, Snackbar.LENGTH_LONG)
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
                data3.put("ura_name", nameIs.getText().toString());
                if(cityIs==null){
                    data3.put("ura_city", "");
                }else {
                    data3.put("ura_city", cityIs);
                }
                data3.put("ura_address_line_1", address_one.getText().toString());
                data3.put("ura_address_line_2", address_two.getText().toString());
                data3.put("lang_id", language_id);
                return data3;

            }
        };

        queue.add(strRequest);

    }



}
