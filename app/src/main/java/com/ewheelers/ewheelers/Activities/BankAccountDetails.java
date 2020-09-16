package com.ewheelers.ewheelers.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import static com.ewheelers.ewheelers.Activities.Home.drawer;

public class BankAccountDetails extends AppCompatActivity {
EditText bankname,holdername,accno,ifsccode,bankaddress;
Button submitdetails;
ProgressDialog progressDialog;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_account_details);
        bankname = findViewById(R.id.bankname);
        holdername = findViewById(R.id.holdername);
        accno = findViewById(R.id.accno);
        ifsccode = findViewById(R.id.ifsccode);
        bankaddress = findViewById(R.id.bankaddress);
        submitdetails = findViewById(R.id.submit_bankdetails);
        progressDialog = new ProgressDialog(BankAccountDetails.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Bank Details Updating ....");
        progressDialog.setCancelable(false);
        submitdetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String banknam = bankname.getText().toString();
                String holdernam = holdername.getText().toString();
                String accnumber = accno.getText().toString();
                String ifsc_code = ifsccode.getText().toString();
                String bank_address = bankaddress.getText().toString();
                if(banknam.isEmpty()||holdernam.isEmpty()||accnumber.isEmpty()||ifsc_code.isEmpty()||bank_address.isEmpty()){
                    Snackbar.make(v,"Please! Fill all details.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }else {
                    submitBankdetails(v,banknam,holdernam,accnumber,ifsc_code,bank_address);
                }


            }
        });
        getProfile();
    }

    private void getProfile() {
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
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("bankInfo");
                        if(jsonObject2==null||jsonObject2.equals("{}")){

                        }else {
                            String banknam = jsonObject2.getString("ub_bank_name");
                            String holdernam = jsonObject2.getString("ub_account_holder_name");
                            String acco = jsonObject2.getString("ub_account_number");
                            String ifsccod = jsonObject2.getString("ub_ifsc_swift_code");
                            String bankaddre = jsonObject2.getString("ub_bank_address");
                            bankname.setText(banknam);
                            holdername.setText(holdernam);
                            accno.setText(acco);
                            ifsccode.setText(ifsccod);
                            bankaddress.setText(bankaddre);
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
                params.put("X-TOKEN", new SessionPreference().getStrings(BankAccountDetails.this,SessionPreference.tokenvalue));
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


    private void submitBankdetails(final View v,final String banknam, final String holdernam, final String accnumber, final String ifsc_code, final String bank_address) {
        try {
            imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = API.updatebankdetails;
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
                                Snackbar.make(v, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                SessionPreference.clearString(BankAccountDetails.this,SessionPreference.partnerattributes);
                                SessionPreference.saveString(BankAccountDetails.this,SessionPreference.bankstatus,"yes");
                                /*Intent i = new Intent(BankAccountDetails.this,Home.class);
                                startActivity(i);
                                finish();*/
                                /*Snackbar.make(v, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();*/
                                finish();
                                drawer.openDrawer(Gravity.LEFT);

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
                params.put("X-TOKEN", new SessionPreference().getStrings(BankAccountDetails.this,SessionPreference.tokenvalue));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("ub_bank_name", banknam);
                data3.put("ub_account_holder_name", holdernam);
                data3.put("ub_account_number", accnumber);
                data3.put("ub_ifsc_swift_code", ifsc_code);
                data3.put("ub_bank_address", bank_address);
                return data3;

            }
        };

        queue.add(strRequest);

    }

    @Override
    public void onBackPressed(){
        finish();
        drawer.openDrawer(Gravity.LEFT);
    }

}
