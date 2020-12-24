package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewAccount extends AppCompatActivity {
    TextView vrify,name,username,ph,emailId,regDate,activ,dob,adver,suppl,privacy,faqIs;
    CircularImageView circularImageView;
    ImageView imageView;
    String upripol,ufaq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_account);
        imageView = findViewById(R.id.ver_img);
        vrify = findViewById(R.id.verified);
        name = findViewById(R.id.nameIs);
        circularImageView = findViewById(R.id.prof_img);
        activ = findViewById(R.id.active);
        emailId = findViewById(R.id.email);
        ph = findViewById(R.id.phno);
        dob = findViewById(R.id.dobIs);
        adver = findViewById(R.id.advertiserIs);
        suppl = findViewById(R.id.supplierIs);
        privacy = findViewById(R.id.privacyPolicies);
        faqIs = findViewById(R.id.faq);
        regDate = findViewById(R.id.regDate);
        username = findViewById(R.id.username);
        getProfile();
        privacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(upripol!=null){
                    Intent i = new Intent(getApplicationContext(),WebViewActivity.class);
                    i.putExtra("urlIs",upripol);
                    startActivity(i);
                }
            }
        });
        faqIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ufaq!=null){
                    Intent i = new Intent(getApplicationContext(),WebViewActivity.class);
                    i.putExtra("urlIs",ufaq);
                    startActivity(i);
                }

            }
        });

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
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("personalInfo");
                        if (jsonObject2.length() == 0) {

                        } else {
                            String uname = jsonObject2.getString("user_name");
                            String uphone = jsonObject2.getString("user_phone");
                            String ureg = jsonObject2.getString("user_regdate");
                            String uusername = jsonObject2.getString("credential_username");
                            String uemail = jsonObject2.getString("credential_email");
                            String uactive = jsonObject2.getString("credential_active");
                            String uverify = jsonObject2.getString("credential_verified");
                            String udob = jsonObject2.getString("user_dob");
                            String usup = jsonObject2.getString("user_is_supplier");
                            String uadv = jsonObject2.getString("user_is_advertiser");
                            String uimg = jsonObject2.getString("userImage");
                            upripol = jsonObject1.getString("privacyPolicyLink");
                            ufaq = jsonObject1.getString("faqLink");
                            if(uverify.equals("1")) {
                                vrify.setText("Verified");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    vrify.setTextColor(getResources().getColor(R.color.colorGreen));
                                }
                                imageView.setImageResource(R.drawable.ic_verified);
                            }else {
                                vrify.setText("Not verified");
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    vrify.setTextColor(getResources().getColor(R.color.colorGrey));
                                }
                                imageView.setImageResource(R.drawable.ic_cancel_black_24dp);
                            }
                            name.setText(uusername);
                            Picasso.get().load(uimg).into(circularImageView);
                            if(uactive.equals("1")) {
                                activ.setText("Yes");
                            }else {
                                activ.setText("No");
                            }
                            emailId.setText(uemail);
                            ph.setText(uphone);
                            dob.setText(udob);
                            if(uadv.equals("1")) {
                                adver.setText("Yes");
                            }else {
                                adver.setText("No");
                            }
                            if(usup.equals("1")){
                                suppl.setText("Yes");
                            }else {
                                suppl.setText("No");
                            }
                            regDate.setText(ureg);
                            username.setText(uname);
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
                params.put("X-TOKEN", new SessionPreference().getStrings(ViewAccount.this, SessionPreference.tokenvalue));
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
