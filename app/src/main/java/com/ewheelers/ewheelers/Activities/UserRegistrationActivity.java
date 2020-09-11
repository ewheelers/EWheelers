package com.ewheelers.ewheelers.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    TextView select_image, id_pass;
    EditText reg_full_name, reg_user_name, reg_email, reg_mob, reg_pswd, reg_conf_pswd, reg_id;
    String sname, suserName, smob, semail, spass, scpass, simage, sid;
    String schec, snews;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        String userid = new SessionPreference().getStrings(this,SessionPreference.userid);

        if(userid!=null){
            Intent i = new Intent(this,signup_two.class);
            startActivity(i);
            finish();
        }

        progressDialog = new ProgressDialog(UserRegistrationActivity.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Registration...");
        progressDialog.setCancelable(false);

        reg_full_name = findViewById(R.id.Reg_full_name);
        // reg_full_name.setText("Ramesh");
        reg_user_name = findViewById(R.id.Reg_user_name);
        //reg_user_name.setText("Ramesh Poshala");
        reg_email = findViewById(R.id.Reg_email);
        // reg_email.setText("ramesh@gamil.com");
        reg_mob = findViewById(R.id.Reg_mobile);
        // reg_mob.setText("9876786778");
        reg_pswd = findViewById(R.id.Reg_password);
        //reg_pswd.setText("ramesh@123");
        reg_conf_pswd = findViewById(R.id.Reg_conf_password);
        //reg_conf_pswd.setText("ramesh@123");
        reg_id = findViewById(R.id.Reg_id);
        select_image = findViewById(R.id.Reg_select_image);
        reg = findViewById(R.id.register_but);
        id_pass = findViewById(R.id.id_pass);
        news = findViewById(R.id.news);
        checkBox1 = findViewById(R.id.agree_conditions);
        reg.setVisibility(View.GONE);
        sid = reg_id.getText().toString();
        id_pass.setText(sid);


        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == select_image) {
                    selectImage();
                }
            }
        });

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
                    }
                    else {
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
                custom(v);
            }
        });

    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(UserRegistrationActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = true;

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        showFileChooser();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                //user_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAMERA) {

            //onCaptureImageResult(data);
            bitmap = (Bitmap) data.getExtras().get("data");
            //storeImage(bm);
            //user_image.setImageBitmap(bitmap);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    private void custom(final View v) {
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
                            if (getStatus.equals("1")) {
                                int user_id = jsonObject.getInt("user_id");
                                SessionPreference.saveString(UserRegistrationActivity.this,SessionPreference.userid, String.valueOf(user_id));
                                Intent i = new Intent(getApplicationContext(),signup_two.class);
                                startActivity(i);
                                finish();
                                /*String smsg = jsonObject.getString("msg");
                                new Alertdialogs().showSuccessAlert(UserRegistrationActivity.this,smsg);*/
                            } else {
                                String smsg = jsonObject.getString("msg");
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
                sid = reg_id.getText().toString();


                Map<String, String> data3 = new HashMap<String, String>();
                data3.put("user_email", semail);
                data3.put("user_name", sname);
                data3.put("user_username", suserName);
                //data3.put("user_phone", smob);
                data3.put("user_password", spass);
                data3.put("password1", scpass);
                data3.put("agree", schec);
                data3.put("user_id", sid);
                //data3.put("user_newsletter_signup", snews);
                return data3;

            }
        };

        queue.add(strRequest);
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imagebytes = baos.toByteArray();
        String encodeImage = Base64.encodeToString(imagebytes, Base64.DEFAULT);
        return encodeImage;
    }

}
