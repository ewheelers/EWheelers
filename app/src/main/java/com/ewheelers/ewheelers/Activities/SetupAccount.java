package com.ewheelers.ewheelers.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.view.View.GONE;

public class SetupAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText fulname, mobileno, dob, organization, profile, city_text;
    Button nextthree;
    Spinner country, state, city;
    ArrayList<String> countrieslist = new ArrayList<>();
    ArrayList<String> statelist = new ArrayList<>();
    ArrayList<String> citieslist = new ArrayList<>();
    String tokenValue, countryString, stateString, cityString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);
        city_text = findViewById(R.id.citytext);
        fulname = findViewById(R.id.fullname);
        mobileno = findViewById(R.id.phoneno);
        dob = findViewById(R.id.dob);
        organization = findViewById(R.id.organization);
        profile = findViewById(R.id.briefprofile);
        nextthree = findViewById(R.id.next_three);
        country = findViewById(R.id.contry);
        state = findViewById(R.id.state);
        city = findViewById(R.id.city);
        country.setOnItemSelectedListener(this);
        state.setOnItemSelectedListener(this);

        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "ddmmyyyy";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) {
                        clean = clean + ddmmyyyy.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE)) ? cal.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s",
                            clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dob.setText(current);
                    dob.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        dob.addTextChangedListener(tw);

        //city.setOnItemSelectedListener(this);
        tokenValue = new SessionPreference().getStrings(this, SessionPreference.tokenvalue);
        getCountries();
        getProfile();
        nextthree.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                city_text = findViewById(R.id.citytext);
                fulname = findViewById(R.id.fullname);
                mobileno = findViewById(R.id.phoneno);
                dob = findViewById(R.id.dob);
                organization = findViewById(R.id.organization);
                profile = findViewById(R.id.briefprofile);
                String name = fulname.getText().toString();
                String mobile_no = mobileno.getText().toString();
                String dobs = dob.getText().toString();
                String organise = organization.getText().toString();
                String profileIs = profile.getText().toString();
                if (name.isEmpty() || mobile_no.isEmpty() || dobs.isEmpty() || organise.isEmpty() || profileIs.isEmpty()) {
                    Snackbar.make(v, "Please! Fill all details to update and set account", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.name, name);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.mobile_no, mobile_no);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.dobs, dobs);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.organise, organise);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.profileIs, profileIs);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.contryid, splitString(countryString));
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.stateid, splitString(stateString));
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.cityname, city_text.getText().toString());
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.cityid, splitString(cityString));
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.accountsetup, "yes");
                    Intent i = new Intent(SetupAccount.this, UpdateAttributes.class);
                    startActivity(i);
                    finish();
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
                        JSONObject jsonObject3 = jsonObject1.getJSONObject("personalInfo");
                        String ful_name = jsonObject3.getString("user_name");
                        fulname.setText(ful_name);
                        String phone = jsonObject3.getString("user_phone");
                        mobileno.setText(phone);
                        /*String dob_is = jsonObject3.getString("user_dob");
                        dob.setText(dob_is);*/
                        String organiz = jsonObject3.getString("user_company");
                        organization.setText(organiz);
                        String briefpof = jsonObject3.getString("user_profile_info");
                        profile.setText(briefpof);
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String splitString(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }
        return String.valueOf(num);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String splitStringAlpha(String str) {
        StringBuffer alpha = new StringBuffer(),
                num = new StringBuffer(), special = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i)))
                num.append(str.charAt(i));
            else if (Character.isAlphabetic(str.charAt(i)))
                alpha.append(str.charAt(i));
            else
                special.append(str.charAt(i));
        }
        return String.valueOf(alpha);
    }

    private void getCountries() {
        String url_link = API.countries;
        final RequestQueue queue = Volley.newRequestQueue(SetupAccount.this);
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
                        countrieslist.add(countryid + " - " + countryname);
                    }

                    country.setAdapter(new ArrayAdapter<String>(SetupAccount.this, android.R.layout.simple_spinner_dropdown_item, countrieslist));


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
        countryString = country.getSelectedItem().toString();
        getStatesNames(splitString(countryString));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getStatesNames(String splitString) {
        statelist.clear();
        String url_link = API.states + splitString;
        final RequestQueue queue = Volley.newRequestQueue(SetupAccount.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("states");

                    for (int i = 0; i < jsonArrayCountries.length(); i++) {
                        JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                        String countryid = jsonObjectcountry.getString("id");
                        JSONObject countryname = jsonObjectcountry.getJSONObject("name");
                        String stateid = countryname.getString("state_id");
                        String state_code = countryname.getString("state_code");
                        String state_name = countryname.getString("state_name");

                        statelist.add(stateid + " - " + state_name);
                    }

                    state.setAdapter(new ArrayAdapter<String>(SetupAccount.this, android.R.layout.simple_spinner_dropdown_item, statelist));
                    state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            stateString = state.getSelectedItem().toString();
                            //Toast.makeText(SetupBillingAddressActivity.this, splitString(stateString), Toast.LENGTH_SHORT).show();
                            getCityNames(splitString(countryString), splitString(stateString));

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

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

    private void getCityNames(String countryid, String stateid) {
        citieslist.clear();
        String url_link = API.cities + countryid + "/" + stateid;
        Log.i("cityurl:", url_link);
        final RequestQueue queue = Volley.newRequestQueue(SetupAccount.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                    String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                    JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("cities");
                    if (jsonArrayCountries != null) {
                        for (int i = 0; i < jsonArrayCountries.length(); i++) {
                            JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                            String cityid = jsonObjectcountry.getString("id");
                            String cityname = jsonObjectcountry.getString("name");

                            citieslist.add(cityid + " - " + cityname);
                        }
                        city.setAdapter(new ArrayAdapter<String>(SetupAccount.this, android.R.layout.simple_spinner_dropdown_item, citieslist));
                        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                cityString = city.getSelectedItem().toString();
                                //Toast.makeText(SetupAccount.this, cityString, Toast.LENGTH_SHORT).show();
                                if (cityString.equalsIgnoreCase("-1 - Other")) {
                                    city_text.setVisibility(View.VISIBLE);
                                } else {
                                    city_text.setVisibility(GONE);
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    } else {
                        Toast.makeText(SetupAccount.this, "No cities are added to show", Toast.LENGTH_SHORT).show();
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

}
