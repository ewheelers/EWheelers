package com.ewheelers.ewheelers.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BankAccountDetails extends AppCompatActivity {
EditText bankname,holdername,accno,ifsccode,bankaddress;
Button submitdetails;
ProgressDialog progressDialog;
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
    }

    private void submitBankdetails(final View v,final String banknam, final String holdernam, final String accnumber, final String ifsc_code, final String bank_address) {
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
                                Intent i = new Intent(BankAccountDetails.this,Home.class);
                                startActivity(i);
                                finish();
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

}
