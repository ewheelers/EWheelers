package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

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

import static com.ewheelers.ewheelers.Activities.Home.drawer;
import static java.lang.String.*;

public class signup_two extends AppCompatActivity {
    Button next;
    EditText userName,business_name, person_name, mobile_no, address_one, address_two,city,state,pincode;
    private InputMethodManager imm;
    ProgressDialog progressDialog;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_two);
        userName = findViewById(R.id.user_name);
        business_name = findViewById(R.id.businessname);
        person_name = findViewById(R.id.personname);
        mobile_no = findViewById(R.id.personno);
        address_one = findViewById(R.id.address1);
        address_two = findViewById(R.id.address2);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pincode);
        next = findViewById(R.id.next_three);
        String username = new SessionPreference().getStrings(this,SessionPreference.username);
        userName.setText(username);
        progressDialog = new ProgressDialog(signup_two.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Business SetUP...");
        progressDialog.setCancelable(false);
        ScrollView scrollView = findViewById(R.id.scrl);
        userid = new SessionPreference().getStrings(signup_two.this,SessionPreference.userid);
        Snackbar.make(scrollView, "Your User ID is : " + userid, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String businessname = business_name.getText().toString();
                String personname = person_name.getText().toString();
                String mobileno = mobile_no.getText().toString();
                String addressone = address_one.getText().toString();
                String addresstwo = address_two.getText().toString();
                String city_is = city.getText().toString();
                String state_is = state.getText().toString();
                String pincode_is = pincode.getText().toString();
                if (businessname.isEmpty() || personname.isEmpty() || mobileno.isEmpty()){
                    Snackbar snackbar = Snackbar
                            .make(v, "Please! Fill all details.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    SessionPreference.saveString(signup_two.this, SessionPreference.businessname, businessname);
                    SessionPreference.saveString(signup_two.this, SessionPreference.personname, personname);
                    SessionPreference.saveString(signup_two.this, SessionPreference.mobileno, mobileno);
                    registerApproval(v,businessname,personname,mobileno);

                }
               /* if (businessname.isEmpty() || personname.isEmpty() || mobileno.isEmpty() || addressone.isEmpty() || addresstwo.isEmpty()||city_is.isEmpty()||state_is.isEmpty()||pincode_is.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(v, "Please! Fill all details.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    SessionPreference.saveString(signup_two.this, SessionPreference.businessname, businessname);
                    SessionPreference.saveString(signup_two.this, SessionPreference.personname, personname);
                    SessionPreference.saveString(signup_two.this, SessionPreference.mobileno, mobileno);
                    SessionPreference.saveString(signup_two.this, SessionPreference.addressone, addressone);
                    SessionPreference.saveString(signup_two.this, SessionPreference.addresstwo, addresstwo);
                    SessionPreference.saveString(signup_two.this, SessionPreference.cityIs, city_is);
                    SessionPreference.saveString(signup_two.this, SessionPreference.stateIs, state_is);
                    SessionPreference.saveString(signup_two.this, SessionPreference.pincodeis, pincode_is);
                    registerApproval(v,businessname,personname,mobileno,addressone,addresstwo,state_is,city_is,pincode_is);
                }*/
            }
        });
    }

    private void registerApproval(final View v, final String b_name, final String p_name, final String p_contact) {
        try {
            imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = API.businesssetup;
        progressDialog.show();
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int getStatus = Integer.parseInt(jsonObject.getString("status"));
                            String smsg = jsonObject.getString("msg");
                            if (getStatus!=0) {
                                Snackbar.make(v, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                finish();
                                drawer.openDrawer(Gravity.LEFT);
                            } else {
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
                params.put("X-TOKEN", new SessionPreference().getStrings(signup_two.this,SessionPreference.tokenvalue));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("Business Name", b_name);
                data3.put("Contact Person", p_name);
                data3.put("Contact Number", p_contact);
                /*data3.put("sformfield_11", b_address1);
                data3.put("sformfield_12", b_address2);
                data3.put("sformfield_13", b_state);
                data3.put("sformfield_14", b_city);
                data3.put("sformfield_15", b_pincode);*/
                data3.put("id", userid);

                return data3;

            }
        };

        queue.add(strRequest);

    }


   /* @Override
    public void onBackPressed() {
        finish();
        drawer.openDrawer(Gravity.LEFT);
    }*/

}
