package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.ewheelers.ewheelers.Activities.Home.drawer;

public class UserRegistrationActivity extends AppCompatActivity {

    private int REQUEST_CAMERA = 0;
    private String userChoosenTask;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    //ImageView user_image;
    CheckBox checkBox1, news;

    String[] content = {"select" + " an image "};

    public static final int PICK_IMAGE = 1;
    Button reg;
    TextView id_pass,terms;
    EditText reg_full_name, reg_user_name, reg_email, reg_mob, reg_id;
    TextInputEditText reg_pswd,reg_conf_pswd;
    String sname, suserName, smob, semail, spass, scpass, sid;
    String schec, snews;

    EditText business_name, person_name, mobile_no, address_one, address_two, city, state, pincode;
    String userid;

    ProgressDialog progressDialog;
    private InputMethodManager imm;

    //String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        //userid = new SessionPreference().getStrings(this,SessionPreference.userid);

        /*if(userid!=null){
            Intent i = new Intent(this,signup_two.class);
            startActivity(i);
        }*/

        progressDialog = new ProgressDialog(UserRegistrationActivity.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Registration...");
        progressDialog.setCancelable(false);
        terms = findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),WebViewActivity.class);
                i.putExtra("urlIs","https://ewheelers.in/terms-conditions");
                startActivity(i);
            }
        });
        reg_full_name = findViewById(R.id.Reg_full_name);
        reg_user_name = findViewById(R.id.Reg_user_name);
        reg_email = findViewById(R.id.Reg_email);
        reg_mob = findViewById(R.id.Reg_mobile);
        reg_pswd = findViewById(R.id.Reg_password);
        reg_conf_pswd = findViewById(R.id.Reg_conf_password);
        reg = findViewById(R.id.register_but);
        id_pass = findViewById(R.id.id_pass);
        news = findViewById(R.id.news);
        checkBox1 = findViewById(R.id.agree_conditions);
        reg.setVisibility(View.GONE);

        business_name = findViewById(R.id.businessname);
        person_name = findViewById(R.id.personname);
        mobile_no = findViewById(R.id.personno);
        address_one = findViewById(R.id.address1);
        address_two = findViewById(R.id.address2);
        city = findViewById(R.id.city);
        state = findViewById(R.id.state);
        pincode = findViewById(R.id.pincode);


        checkBox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String full_name = reg_full_name.getText().toString().trim();
                    String user_name = reg_user_name.getText().toString().trim();
                    String emailid = reg_email.getText().toString().trim();
                    String mobileno = reg_mob.getText().toString().trim();
                    String password = reg_pswd.getText().toString().trim();
                    String confirmPassword = reg_conf_pswd.getText().toString().trim();
                    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                    if (full_name.isEmpty() || user_name.isEmpty() || emailid.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                        Snackbar snackbar = Snackbar
                                .make(buttonView, "Leaved Empty Field.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                        //Toast.makeText(UserRegistrationActivity.this, "Leaved Empty Field", Toast.LENGTH_SHORT).show();
                        checkBox1.setChecked(false);
                    }
                    /*else if(mobileno.length()<10){
                        reg_mob.setError("Enter 10 digit mobile number");
                        checkBox1.setChecked(false);
                    }*/
                    else if (!emailid.matches(emailPattern)) {
                        reg_email.setError("Enter valid Email Id");
                        checkBox1.setChecked(false);
                    } else {
                        checkBox1.setChecked(true);
                        reg.setVisibility(View.VISIBLE);
                    }
                } else {
                    reg.setVisibility(View.GONE);
                }
            }
        });
        reg.setOnClickListener(new View.OnClickListener() {
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
                if (businessname.isEmpty() || personname.isEmpty() || mobileno.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(v, "Please! Fill all details.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    custom(v,businessname,personname,mobileno);
                }               /* if (businessname.isEmpty() || personname.isEmpty() || mobileno.isEmpty() || addressone.isEmpty() || addresstwo.isEmpty() || city_is.isEmpty() || state_is.isEmpty() || pincode_is.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(v, "Please! Fill all details.", Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    custom(v,businessname,personname,mobileno,addressone,addresstwo,city_is,state_is,pincode_is);
                }*/
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void custom(final View v, final String businessname, final String personname, final String mobileno) {
        try {
            imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = API.register2;
        progressDialog.show();
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String getStatus = jsonObject.getString("status");
                            String smsg = jsonObject.getString("msg");
                            if (getStatus.equals("1")) {
                                int user_id = jsonObject.getInt("user_id");
                                SessionPreference.saveString(UserRegistrationActivity.this, SessionPreference.userid, String.valueOf(user_id));
                               /* Intent i = new Intent(getApplicationContext(),signup_two.class);
                                startActivity(i);
                                finish();*/
                               /* Intent i = new Intent(getApplicationContext(),LoginScreenActivity.class);
                                startActivity(i);
                                finish();*/
                                //registerApproval(v,user_id,businessname,personname,mobileno,addressone,addresstwo,state_is,city_is,pincode_is);
                                registerApproval(v,user_id,businessname,personname,mobileno);

                            } else {
                                Snackbar.make(v, smsg, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                //new Alertdialogs().showFailedAlert(UserRegistrationActivity.this,smsg);
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

           /* @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-USER-TYPE", reg_user_name.getText().toString());
                return params;
            }*/

            @Override
            protected Map<String, String> getParams() {

                sname = reg_full_name.getText().toString();
                suserName = reg_user_name.getText().toString();
                semail = reg_email.getText().toString();
                spass = reg_pswd.getText().toString();
                // simage = select_image.getResources().toString();
                scpass = reg_conf_pswd.getText().toString();
                //smob = reg_mob.getText().toString();
                schec = String.valueOf(checkBox1.isChecked());
                snews = String.valueOf(news.isChecked());


                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("user_email", semail);
                data3.put("user_name", sname);
                data3.put("user_username", suserName);
                //data3.put("user_phone", smob);
                data3.put("user_password", spass);
                data3.put("password1", scpass);
                data3.put("agree", schec);
                data3.put("user_id", "0");
                //data3.put("user_newsletter_signup", snews);
                return data3;

            }
        };

        queue.add(strRequest);
    }

    private void registerApproval(final View v, final int user_id, final String b_name, final String p_name, final String p_contact) {
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
                                final AlertDialog.Builder builder = new AlertDialog.Builder(UserRegistrationActivity.this);
                                builder.create();
                                builder.setIcon(R.drawable.partnerlogo);
                                builder.setTitle("Successfully Registered");
                                builder.setMessage("Please Sign In");
                                builder.setCancelable(false);
                                builder.setPositiveButton("Sign In", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent i = new Intent(getApplicationContext(),LoginScreenActivity.class);
                                        startActivity(i);
                                        finish();
                                        dialog.cancel();
                                    }
                                });
                                builder.show();
                                //Snackbar.make(v, smsg, Snackbar.LENGTH_LONG).setAction("Action", null).show();
                               /* finish();
                                drawer.openDrawer(Gravity.LEFT);*/
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
                params.put("X-TOKEN", new SessionPreference().getStrings(UserRegistrationActivity.this,SessionPreference.tokenvalue));
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("sformfield_1", b_name);
                data3.put("sformfield_2", p_name);
                data3.put("sformfield_3", p_contact);
               /* data3.put("sformfield_11", b_address1);
                data3.put("sformfield_12", b_address2);
                data3.put("sformfield_13", b_state);
                data3.put("sformfield_14", b_city);
                data3.put("sformfield_15", b_pincode);*/
                data3.put("id", String.valueOf(user_id));

                return data3;

            }
        };

        queue.add(strRequest);

    }


}
