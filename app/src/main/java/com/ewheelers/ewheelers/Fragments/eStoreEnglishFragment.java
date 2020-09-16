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
import java.util.Objects;

public class eStoreEnglishFragment extends Fragment {
    EditText shopname, contact_person, descript, payment_pol, delivery_pol, refund_poly, add_info, seller_info;
    Button save_language;
    String tokenValue, shopid, language_id;
    ScrollView scrollView;
    private ProgressDialog progressDialog;

    public eStoreEnglishFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_e_store_english, container, false);
        tokenValue = new SessionPreference().getStrings(getActivity(), SessionPreference.tokenvalue);
        shopid = new SessionPreference().getStrings(getActivity(), SessionPreference.shopid);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("English SetUp ....");
        progressDialog.setCancelable(false);
        shopname = v.findViewById(R.id.shopname);
        contact_person = v.findViewById(R.id.contactperson);
        descript = v.findViewById(R.id.description);
        payment_pol = v.findViewById(R.id.paymentpolicy);
        delivery_pol = v.findViewById(R.id.deliverypolicy);
        refund_poly = v.findViewById(R.id.refundpolicy);
        add_info = v.findViewById(R.id.additionalinfor);
        seller_info = v.findViewById(R.id.sellerinfor);
        save_language = v.findViewById(R.id.savelanguage);
        scrollView = v.findViewById(R.id.scrol);
        getHomeLang();
        save_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguage();
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
                        getLanguageForm(shopid,language_id);
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

    private void getLanguageForm(String shopid,String langid) {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String serverurl = API.getshoplangaugeform + shopid + "/" + langid;
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
                        JSONObject jsonObject4 = jsonObject1.getJSONObject("langData");
                        if (jsonObject4.length() == 0) {
                            Toast.makeText(getActivity(), "No data to show", Toast.LENGTH_SHORT).show();
                        } else {
                            String shop_id = jsonObject4.getString("shoplang_shop_id");
                            String lang_id = jsonObject4.getString("shoplang_lang_id");
                            String shop_name = jsonObject4.getString("shop_name");
                            String shop_cont_person = jsonObject4.getString("shop_contact_person");
                            String shop_descript = jsonObject4.getString("shop_description");
                            String shop_addr_one = jsonObject4.getString("shop_address_line_1");
                            String shop_addr_two = jsonObject4.getString("shop_address_line_2");
                            String shop_city = jsonObject4.getString("shop_city");
                            String shop_pay_pol = jsonObject4.getString("shop_payment_policy");
                            String shop_del_pol = jsonObject4.getString("shop_delivery_policy");
                            String shop_ref_pol = jsonObject4.getString("shop_refund_policy");
                            String shop_addi_info = jsonObject4.getString("shop_additional_info");
                            String shop_seller_info = jsonObject4.getString("shop_seller_info");
                            shopname.setText(shop_name);
                            contact_person.setText(shop_cont_person);
                            descript.setText(shop_descript);
                            payment_pol.setText(shop_pay_pol);
                            delivery_pol.setText(shop_del_pol);
                            refund_poly.setText(shop_ref_pol);
                            add_info.setText(shop_addi_info);
                            seller_info.setText(shop_seller_info);
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

    private void saveLanguage() {
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        final String url = API.setupshoplangaugeform;
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
                data3.put("shop_name", shopname.getText().toString());
                data3.put("shop_contact_person",contact_person.getText().toString());
                data3.put("shop_description", descript.getText().toString());
                data3.put("shop_payment_policy", payment_pol.getText().toString());
                data3.put("shop_delivery_policy", delivery_pol.getText().toString());
                data3.put("shop_refund_policy", refund_poly.getText().toString());
                data3.put("shop_additional_info", add_info.getText().toString());
                data3.put("shop_seller_info", seller_info.getText().toString());
                if(shopid==null){
                    data3.put("shop_id", "");
                }else {
                    data3.put("shop_id", shopid);
                }
                data3.put("lang_id", language_id);
                return data3;

            }
        };

        queue.add(strRequest);

    }

}
