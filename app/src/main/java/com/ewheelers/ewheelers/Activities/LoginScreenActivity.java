package com.ewheelers.ewheelers.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsMessage;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.ewheelers.ewheelers.Activities.SmsIntegration.SmsBroadcastReceiver;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.Network.VolleySingleton;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.Constants;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreenActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQ_USER_CONSENT = 999;
    EditText username,phoneNumber;
    TextInputEditText password;
    Button login,sendOtp;
    TextView create_accout;
    TextView forget_password;
    String susername, spass;
    SharedPreferences sharedPreferences;
    String token, userName, userImage, reponseMessage,tokenno;
    String getStatus;
    String userId;
    ProgressDialog progressDialog;
    private static final int AUTO_SCROLL_THRESHOLD_IN_MILLI = 3000;
    //TabLayout tabs;
    //AutoScrollViewPager viewPager;
    InputMethodManager imm;
    private String[] permissions = {"android.permission.FLASHLIGHT" , "com.google.android.providers.gsf.permission.READ_GSERVICES","android.permission.CAMERA","android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION","android.permission.CALL_PHONE","com.example.mapdemo.permission.MAPS_RECEIVE","android.permission.ACCESS_NETWORK_STATE","android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE","android.permission.INTERNET"};
    int requestCode = 200;
    TextView tremsCond;
    LinearLayout passwordLayout,otpLayout;
    TextView passwordLoginTV,otpLoginTV;
    private int first = -100, second = -100, third = -100, fourth = -100;
    EditText et1, et2, et3, et4;
    AlertDialog dismissDailog;
    View listenerView;
    SmsBroadcastReceiver smsBroadcastReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        progressDialog = new ProgressDialog(LoginScreenActivity.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Login...");
        progressDialog.setCancelable(false);
        tremsCond = findViewById(R.id.terms);
        tremsCond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),WebViewActivity.class);
                i.putExtra("urlIs","https://ewheelers.in/terms-conditions");
                startActivity(i);
            }
        });
        username = findViewById(R.id.username1);
        password = findViewById(R.id.password1);
        login = findViewById(R.id.login1);
        create_accout = findViewById(R.id.createNewAccout);
        forget_password = findViewById(R.id.forget_password);

        otpLoginTV = findViewById(R.id.otpLoginTV);
        passwordLoginTV = findViewById(R.id.passwordLoginTV);
        passwordLayout = findViewById(R.id.passwordLayout);
        otpLayout = findViewById(R.id.otpLayout);
        sendOtp = findViewById(R.id.sendOTP);
        phoneNumber = findViewById(R.id.phoneNumber);
        startSmsUserConsent();
        sharedPreferences = getApplicationContext().getSharedPreferences("pref", MODE_PRIVATE);

        tokenno = new SessionPreference().getStrings(this,SessionPreference.tokenvalue);



       /* AutoScrollPagerAdapter autoScrollPagerAdapter = new AutoScrollPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(autoScrollPagerAdapter);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        viewPager.startAutoScroll();
        viewPager.setInterval(AUTO_SCROLL_THRESHOLD_IN_MILLI);
        viewPager.setCycle(true);
        viewPager.setAutoScrollDurationFactor(5.00);
        viewPager.setBorderAnimation(false);*/

        login.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        create_accout.setOnClickListener(this);
        otpLoginTV.setOnClickListener(this);
        passwordLoginTV.setOnClickListener(this);
        sendOtp.setOnClickListener(this);

        /*  KeyboardUtils.addKeyboardToggleListener(this, new KeyboardUtils.SoftKeyboardToggleListener() {
            @Override
            public void onToggleSoftKeyboard(boolean isVisible) {
                if (!isVisible) {
                    tabs.setVisibility(View.VISIBLE);
                    viewPager.setVisibility(View.VISIBLE);
                } else {
                    tabs.setVisibility(View.GONE);
                    viewPager.setVisibility(View.GONE);
                }
            }
        });
*/
    }
    private void registerBroadcastReceiver() {
        smsBroadcastReceiver = new SmsBroadcastReceiver();
        smsBroadcastReceiver.smsBroadcastReceiverListener =
                new SmsBroadcastReceiver.SmsBroadcastReceiverListener() {
                    @Override
                    public void onSuccess(Intent intent) {
                        startActivityForResult(intent, REQ_USER_CONSENT);
                    }
                    @Override
                    public void onFailure() {
                    }
                };
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(smsBroadcastReceiver, intentFilter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_USER_CONSENT) {
            if ((resultCode == RESULT_OK) && (data != null)) {
                //That gives all message to us.
                // We need to get the code from inside with regex
                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                getOtpFromMessage(message);
            }
        }
    }
    private void startSmsUserConsent() {
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    private void getOtpFromMessage(String message) {
        try{
        boolean b = message.contains("eWheelers.in");
        String messageText = message.replaceAll("[^0-9]", "");   // contains otp
        if (b == true) {
            Log.d("isFrom...", "OTPSignin");
            if ( messageText.length() == 4) {
                et1.setText(String.valueOf(messageText.charAt(0)));
                et2.setText(String.valueOf(messageText.charAt(1)));
                et3.setText(String.valueOf(messageText.charAt(2)));
                et4.setText(String.valueOf(messageText.charAt(3)));
                loginJson(listenerView,true,messageText);                    }
        }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login1:
                String userName = username.getText().toString();
                String passWord = password.getText().toString();
                if (userName.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(v, "Enter User Name", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else if (passWord.isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(v, "Enter Password", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    //password.setError("Enter Password");
                }
                else {
                    loginJson(v,false,"");
                }
                break;
            case R.id.forget_password:
                try {
                    imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                Intent forget_act = new Intent(getApplicationContext(), ForgetPasswordActivity.class);
                startActivity(forget_act);
                break;
            case R.id.createNewAccout:
                /*try {
                    imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                    // TODO: handle exception
                }*/
                Intent j = new Intent(getApplicationContext(), UserRegistrationActivity.class);
                startActivity(j);
                break;
            case R.id.passwordLoginTV:
                passwordLoginTV.setTextColor(getResources().getColor(R.color.colorPrimary));
                otpLoginTV.setTextColor(getResources().getColor(R.color.gray_btn_bg_color));
                passwordLayout.setVisibility(View.VISIBLE);
                otpLayout.setVisibility(View.GONE);
                break;
            case R.id.otpLoginTV:
                otpLoginTV.setTextColor(getResources().getColor(R.color.colorPrimary));
                passwordLoginTV.setTextColor(getResources().getColor(R.color.gray_btn_bg_color));
                otpLayout.setVisibility(View.VISIBLE);
                passwordLayout.setVisibility(View.GONE);
                break;
            case R.id.sendOTP:
                if (phoneNumber.getText().toString().trim().isEmpty()) {
                    Snackbar snackbar = Snackbar
                            .make(v, "Enter User Name", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if(phoneNumber.getText().toString().trim().length() < 10){
                    Snackbar snackbar = Snackbar
                            .make(v, "Enter valid phone number", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else{
                    singWithLoginOTP(v,false);
                }
                break;
        }
    }
    private void singWithLoginOTP(final View v,boolean isFromResendOtp) {
        progressDialog.show();
//        sign_in.setBackgroundColor(Color.GRAY);
//        sign_in.setEnabled(false);
        String Login_url = API.sendloginOtp;

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Constants.isFromOTPLogin = true;
                            Constants.isFromSignUp  = false;
                            JSONObject jsonObject = new JSONObject(response);
                            int getStatus = jsonObject.getInt("status");
                            String message = jsonObject.getString("msg");

                            if (getStatus == 1) {
                                progressDialog.dismiss();
                                if(!isFromResendOtp) {
                                    otpDialog(v, message);
                                }else{
                                    try{
                                        first = -100;
                                        second = -100;
                                        third = -100;
                                        fourth = -100;
                                        et1.setText("");
                                        et2.setText("");
                                        et3.setText("");
                                        et4.setText("");
                                        et1.requestFocus();
                                    }catch (Exception e){

                                    }

                                }
                                //pDialog.dismiss();
                                /*sendRegistrationToServer(token, firetoken);
                                SessionStorage.saveString(getActivity(), SessionStorage.user_id, userId);
                                SessionStorage.saveString(getActivity(), SessionStorage.tokenvalue, token);*/
                                /*Intent intent = new Intent(getActivity(), NavAppBarActivity.class);
                                startActivity(intent);
                                getActivity().finish();*/

                            } else {
                                progressDialog.dismiss();
                                Log.d("error...",message);
                                showfailedDialog(LoginScreenActivity.this, v, message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
                /* sign_in.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.button_bg));
                sign_in.setEnabled(true);
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());*/
                Toast.makeText(LoginScreenActivity.this, "" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("username", phoneNumber.getText().toString().trim());
                data3.put("user_dial_code", "91");
                data3.put("user_country_iso", "in");
                return data3;

            }
        };
        strRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        Log.d("Request...",strRequest.getBodyContentType());
        VolleySingleton.getInstance(LoginScreenActivity.this).addToRequestQueue(strRequest);
    }
    private void otpDialog(View v,String msg){
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(LoginScreenActivity.this).inflate(R.layout.success_layout, viewGroup, false);
        TextView textView = dialogView.findViewById(R.id.message);
        TextView otpMsg = dialogView.findViewById(R.id.otpmsg);
        et1 = dialogView.findViewById(R.id.otpstr1);
        et2 = dialogView.findViewById(R.id.otpstr2);
        et3 = dialogView.findViewById(R.id.otpstr3);
        et4 = dialogView.findViewById(R.id.otpstr4);
        TextView countDown = dialogView.findViewById(R.id.countOtpSec);
        Button button = dialogView.findViewById(R.id.buttonOk);
        textView.setText(msg);
        first = -100;
        second = -100;
        third = -100;
        fourth = -100;
        et1.setText("");
        et2.setText("");
        et3.setText("");
        et4.setText("");
        et1.requestFocus();
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginScreenActivity.this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.setCancelable(false);
        dismissDailog = alertDialog;
        listenerView = v;
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                countDown.setText("Please wait for " + millisUntilFinished / 1000 + " seconds to Resend-OTP");
            }

            public void onFinish() {
                countDown.setText("Resend-OTP");
                countDown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        resendOTP(v,countDown,usedId);
                        singWithLoginOTP(v,true);
                    }
                });
            }

        }.start();
/*
        View.OnKeyListener key=new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(!((EditText) v).toString().isEmpty())
                    v.focusSearch(View.FOCUS_RIGHT).requestFocus();

                return false;
            }
        };
        otpstr1.setOnKeyListener(key);
        otpstr2.setOnKeyListener(key);
        otpstr3.setOnKeyListener(key);
*/
        et1.addTextChangedListener(new GenericTextWatcher(et1));
        et2.addTextChangedListener(new GenericTextWatcher(et2));
        et3.addTextChangedListener(new GenericTextWatcher(et3));
        et4.addTextChangedListener(new GenericTextWatcher(et4));

        otpMsg.setText("OTP sent to the Phone - Verify the OTP by entering 4 digit number");


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first != -100 && second != -100 && third != -100 && fourth != -100){
                    String resultingOtp = String.valueOf(first) + String.valueOf(second) + String.valueOf(third) + String.valueOf(fourth);

                    loginJson(v,true,resultingOtp);
                }else{
                    showfailedDialog(LoginScreenActivity.this, v, "Not entered 4 digit OTP code");
                }


                                      /*  Intent intent = new Intent(getActivity(), HomeActivity.class);
                                        startActivity(intent);
                                        getActivity().finish();*/
            }
        });

    }
    public void onBackPressed() {
        AlertDialog.Builder builder
                = new AlertDialog
                .Builder(LoginScreenActivity.this);

        builder.setMessage("Do you want to exit ?");

        builder.setCancelable(false);

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void loginJson(final View v,boolean isFromOtpLogin,String optCode) {
        try {
            imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            // TODO: handle exception
        }
        String Login_url = API.Login_post;
        progressDialog.show();

        StringRequest strRequest = new StringRequest(Request.Method.POST, Login_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            getStatus = jsonObject.getString("status");

                            if (getStatus.equals("1")) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                token = jsonObject1.getString("token");
                                userName = jsonObject1.getString("user_name");
                                userId = jsonObject1.getString("user_id");
                                userImage = jsonObject1.getString("user_image");
                                Log.i("TokenId", token);
                                SessionPreference.saveString(LoginScreenActivity.this,SessionPreference.userid,userId);
                                SessionPreference.saveString(LoginScreenActivity.this,SessionPreference.tokenvalue,token);
                                SessionPreference.saveString(LoginScreenActivity.this,SessionPreference.username,userName);
                                Intent i = new Intent(getApplicationContext(), Home.class);
                                startActivity(i);
                                finish();
                            } else {
                                reponseMessage = jsonObject.getString("msg");
                                username.setText("");
                                password.setText("");
                                Snackbar.make(v, reponseMessage, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                VolleyLog.d("Main", "Error :" + error.getMessage());
                Log.d("Main", "" + error.getMessage() + "," + error.toString());
            }
        }) {

            @Override
            public Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> data3 = new HashMap<String, String>();
                if(isFromOtpLogin){
                    susername = phoneNumber.getText().toString().trim();
                    spass = optCode;
                    data3.put("username", susername);
                    data3.put("password", spass);
                    data3.put("userType", String.valueOf(1));
                    data3.put("user_dial_code", "+91");
                }else {
                    susername = username.getText().toString();
                    spass = password.getText().toString();
                    data3.put("username", susername);
                    data3.put("password", spass);
                    data3.put("userType", "2");
                }
                return data3;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(strRequest);
    }

    private void showPrivacyPolicies(String url) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        WebView webView = new WebView(this);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        dialog.setView(webView);
        dialog.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //viewPager.startAutoScroll(AUTO_SCROLL_THRESHOLD_IN_MILLI);
    }

    @Override
    protected void onPause() {
        //viewPager.stopAutoScroll();
        super.onPause();
    }
    @Override
    public void onStart() {
        super.onStart();
        registerBroadcastReceiver();

    }

    @Override
    public void onStop() {
        super.onStop();
        unregisterReceiver(smsBroadcastReceiver);

    }
    public static void showfailedDialog(Context context, View v, String message) {
        ViewGroup viewGroup = v.findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.failed_layout, viewGroup, false);
        TextView textView = dialogView.findViewById(R.id.message);
        Button button = dialogView.findViewById(R.id.buttonOk);
        textView.setText(message);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {

                case R.id.otpstr1:
                    if (text.length() == 1) {
                        Log.d("first", text);
                        first = Integer.parseInt(text);
                        et2.requestFocus();
                    } else if (text.equals("") || text.length() == 0) {
                        first = -100;
                    }
                    break;
                case R.id.otpstr2:
                    if (text.length() == 1) {
                        Log.d("second", text);
                        second = Integer.parseInt(text);
                        et3.requestFocus();
                    } else if (text.equals("") || text.length() == 0) {
                        second = -100;
                    }
                    break;
                case R.id.otpstr3:
                    if (text.length() == 1) {
                        Log.d("third", text);
                        third = Integer.parseInt(text);
                        et4.requestFocus();
                    } else if (text.equals("") || text.length() == 0) {
                        third = -100;
                    }
                    break;
                case R.id.otpstr4:
                    if (text.length() == 1) {
                        Log.d("fourth", text);
                        fourth = Integer.parseInt(text);
                    } else if (text.equals("") || text.length() == 0) {
                        fourth = -100;
                        hideSoftKeyboard();
                    }
                    break;

            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }
    public void hideSoftKeyboard() {
        View view = LoginScreenActivity.this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) LoginScreenActivity.this.getSystemService(LoginScreenActivity.this.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
