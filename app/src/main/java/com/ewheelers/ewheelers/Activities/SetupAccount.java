package com.ewheelers.ewheelers.Activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
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
import com.ewheelers.ewheelers.ActivityModels.Stateslist;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static android.view.View.GONE;
import static com.ewheelers.ewheelers.Activities.Home.drawer;

public class SetupAccount extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText fulname, mobileno, organization, profile, city_text;
    static TextView dob;
    Button nextthree;
    Spinner country, state, city;
    ArrayList<Stateslist> countrieslist = new ArrayList<>();
    ArrayList<Stateslist> statelist = new ArrayList<>();
    ArrayList<Stateslist> citieslist = new ArrayList<>();
    String tokenValue, countryString, countryid, stateString, stateid, cityString, cityid;
    private ProgressDialog progressDialog;
    Switch edit_mode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_account);
        edit_mode = findViewById(R.id.editMode);
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
        progressDialog = new ProgressDialog(SetupAccount.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("General SetUp ....");
        progressDialog.setCancelable(false);
        country.setOnItemSelectedListener(this);
        state.setOnItemSelectedListener(this);
        country.setEnabled(false);
        state.setEnabled(false);
        city.setEnabled(false);
        edit_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    city_text.setEnabled(true);
                    city_text.setTextColor(getResources().getColor(R.color.colorBlack));
                    fulname.setEnabled(true);
                    fulname.setTextColor(getResources().getColor(R.color.colorBlack));
                    mobileno.setEnabled(true);
                    mobileno.setTextColor(getResources().getColor(R.color.colorBlack));
                    dob.setEnabled(true);
                    dob.setTextColor(getResources().getColor(R.color.colorBlack));
                    organization.setEnabled(true);
                    organization.setTextColor(getResources().getColor(R.color.colorBlack));
                    profile.setEnabled(true);
                    profile.setTextColor(getResources().getColor(R.color.colorBlack));
                    country.setEnabled(true);
                    state.setEnabled(true);
                    city.setEnabled(true);
                }else {
                    city_text.setEnabled(false);
                    city_text.setTextColor(getResources().getColor(R.color.colorNavy));
                    fulname.setEnabled(false);
                    fulname.setTextColor(getResources().getColor(R.color.colorNavy));
                    mobileno.setEnabled(false);
                    mobileno.setTextColor(getResources().getColor(R.color.colorNavy));
                    dob.setEnabled(false);
                    dob.setTextColor(getResources().getColor(R.color.colorNavy));
                    organization.setEnabled(false);
                    organization.setTextColor(getResources().getColor(R.color.colorNavy));
                    profile.setEnabled(false);
                    profile.setTextColor(getResources().getColor(R.color.colorNavy));
                    country.setEnabled(false);
                    state.setEnabled(false);
                    city.setEnabled(false);
                }
            }
        });

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTruitonDatePickerDialog(v);
            }
        });

        //city.setOnItemSelectedListener(this);
        tokenValue = new SessionPreference().getStrings(this, SessionPreference.tokenvalue);
        getCountries();
        //getProfile();
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
                    //Toast.makeText(SetupAccount.this, "ids : "+countryid+stateid+cityid, Toast.LENGTH_SHORT).show();
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.name, name);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.mobile_no, mobile_no);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.dobs, dobs);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.organise, organise);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.profileIs, profileIs);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.contryid, countryid);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.stateid, stateid);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.cityname, city_text.getText().toString());
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.cityid, cityid);
                    SessionPreference.saveString(SetupAccount.this, SessionPreference.accountsetup, "yes");
                    Intent i = new Intent(SetupAccount.this, UpdateAttributes.class);
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    private void showTruitonDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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
                    if(status.equals("1")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                        //String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                        JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("countries");
                        for (int i = 0; i < jsonArrayCountries.length(); i++) {
                            JSONObject jsonObjectcountry = jsonArrayCountries.getJSONObject(i);
                            String countryid = jsonObjectcountry.getString("id");
                            String countryname = jsonObjectcountry.getString("name");
                            Stateslist stateslist = new Stateslist(countryid, countryname);
                            //countrieslist.add(countryid + " - " + countryname);
                            countrieslist.add(stateslist);
                        }

                        country.setAdapter(new ArrayAdapter<Stateslist>(SetupAccount.this, android.R.layout.simple_spinner_dropdown_item, countrieslist));
                    }else {
                        Toast.makeText(SetupAccount.this, msg, Toast.LENGTH_SHORT).show();
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Stateslist stateslist = (Stateslist) country.getSelectedItem();
        countryString = stateslist.getStatename();
        //countryString = country.getSelectedItem().toString();
        countryid = stateslist.getStateid();
        //getStatesNames(stateslist.getStateid());
        getProfile(countryid);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void getProfile(final String countryid) {
        progressDialog.show();
        final RequestQueue queue = Volley.newRequestQueue(this);
        String serverurl = API.getProfileinfo;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.d("Response...",response.toString());
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
                        String dob_is = jsonObject3.getString("user_dob");
                        dob.setText(dob_is);
                        String organiz = jsonObject3.getString("user_company");
                        organization.setText(organiz);
                        String briefpof = jsonObject3.getString("user_profile_info");
                        profile.setText(briefpof);
                        String contry_id = jsonObject3.getString("user_country_id");
                        String state_id = jsonObject3.getString("user_state_id");
                        String city_id = jsonObject3.getString("user_city_id");
                        if(state_id.equals("0")){
                            //progressDialog.dismiss();
                            getStatesNames(countryid, "","");

                        }else {
                            //progressDialog.dismiss();
                            getStatesNames(contry_id, state_id,city_id);
                        }

                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(SetupAccount.this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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
        Log.e("Request...",stringRequest.toString());
        queue.add(stringRequest);

    }

    private void getStatesNames(final String splitString, final String state_id, final String city_id) {
        //progressDialog.show();
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
                    if(status.equals("1")) {
                        JSONObject jsonObjectData = jsonObject.getJSONObject("data");
                       // String cartItemsCount = jsonObjectData.getString("cartItemsCount");

                        JSONArray jsonArrayCountries = jsonObjectData.getJSONArray("states");

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
                        if (statelist != null) {
                            state.setAdapter(new ArrayAdapter<Stateslist>(SetupAccount.this, android.R.layout.simple_spinner_dropdown_item, statelist));
                            if (!state_id.isEmpty()) {
                                for (int j = 0; j < statelist.size(); j++) {
                                    if (statelist.get(j).getStateid().equals(state_id)) {
                                        state.setSelection(j);
                                    }
                                }
                            }
                        }
                        state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Stateslist stateslist = (Stateslist) state.getSelectedItem();
                                stateString = stateslist.getStatename();
                                stateid = stateslist.getStateid();
                                //stateString = state.getSelectedItem().toString();
                                //Toast.makeText(SetupBillingAddressActivity.this, splitString(stateString), Toast.LENGTH_SHORT).show();
                                getCityNames(splitString, stateslist.getStateid(), city_id);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        progressDialog.dismiss();
                    }else {
                        //progressDialog.dismiss();
                        Toast.makeText(SetupAccount.this, msg, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
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

    private void getCityNames(String countryid, String stateid, final String city_id) {
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
                    if(status.equals("1")) {
                        //progressDialog.dismiss();
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
                            if (citieslist != null) {
                                city.setAdapter(new ArrayAdapter<Stateslist>(SetupAccount.this, android.R.layout.simple_spinner_dropdown_item, citieslist));
                                if (!city_id.isEmpty()) {
                                    for (int j = 0; j < citieslist.size(); j++) {
                                        if (citieslist.get(j).getStateid().equals(city_id))
                                            city.setSelection(j);
                                    }
                                }
                            }
                            city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    Stateslist stateslist = (Stateslist) city.getSelectedItem();
                                    cityString = stateslist.getStatename();
                                    cityid = stateslist.getStateid();
                                    //cityString = city.getSelectedItem().toString();
                                    //Toast.makeText(SetupAccount.this, cityString, Toast.LENGTH_SHORT).show();
                                    if (cityString.equalsIgnoreCase("Other")) {
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
                            //progressDialog.dismiss();
                            Toast.makeText(SetupAccount.this, "No cities are added to show", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        //progressDialog.dismiss();
                        Toast.makeText(SetupAccount.this, msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
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



    @Override
    public void onBackPressed(){
        finish();
        drawer.openDrawer(Gravity.LEFT);
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            Date d=new Date();
            Date d_min=new Date();

            Calendar cal=Calendar.getInstance();
            cal.set(2000, 11, 31, 0, 0);
            d.setTime(cal.getTimeInMillis());
            Calendar min_cal=Calendar.getInstance();
            min_cal.set(1950, 1, 1, 0, 0);
            d_min.setTime(min_cal.getTimeInMillis());
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDate = new DatePickerDialog(getActivity(), this, year, month, day);
            mDate.getDatePicker().setMinDate(d_min.getTime());
            mDate.getDatePicker().setMaxDate(d.getTime());

            // Create a new instance of DatePickerDialog and return it
            return mDate;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            view.setMinDate(System.currentTimeMillis() - 1000);
            // Do something with the date chosen by the user
            dob.setText(year + "-" + (month + 1) + "-" + day);
        }
    }
}
