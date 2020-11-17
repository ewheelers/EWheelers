package com.ewheelers.ewheelers.Fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import com.ewheelers.ewheelers.Activities.ImagePickerActivity;
import com.ewheelers.ewheelers.Activities.UserRegistrationActivity;
import com.ewheelers.ewheelers.ActivityModels.Stateslist;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.Network.VolleyMultipartRequest;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

public class eStoreBannerLogoFragment extends Fragment{
    Spinner language, displayfor;
    String tokenValue, lang_banner, lang_logo, displayForBanner;
    ScrollView scrollView;
    ArrayList<String> languages = new ArrayList<>();
    ArrayList<Stateslist> displayforno = new ArrayList<>();
    Button buttonUploadanner;
    ProgressDialog progressDialog;
//    private int REQUEST_CAMERA = 0;
    private int REQUEST_CAMERA = 100;

    private String userChoosenTask;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;
    String fileSelctedPath;
    ImageView bannerImageIs;
    public eStoreBannerLogoFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_e_store_banner_logo, container, false);
        tokenValue = new SessionPreference().getStrings(Objects.requireNonNull(getActivity()), SessionPreference.tokenvalue);
        language = v.findViewById(R.id.lang_spinner);
        displayfor = v.findViewById(R.id.display_for);
        scrollView = v.findViewById(R.id.scrol_view);
        buttonUploadanner = v.findViewById(R.id.uploadbanner);
        bannerImageIs = v.findViewById(R.id.banner_image);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Updating Banner ....");
        progressDialog.setCancelable(false);
        //language.setOnItemSelectedListener(this);
        ImagePickerActivity.clearCache(getActivity());
        getmedia();
        buttonUploadanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //selectImage();
                Dexter.withActivity(getActivity())
                        .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    showImagePickerOptions();
                                }

                                if (report.isAnyPermissionPermanentlyDenied()) {
                                    showSettingsDialog();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();

            }
        });
        return v;
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
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

    private void showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(getActivity(), new ImagePickerActivity.PickerOptionListener() {
            @Override
            public void onTakeCameraSelected() {
                cameraIntent();
            }

            @Override
            public void onChooseGallerySelected() {
                showFileChooser();
            }
        });
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
        builder.show();

    }
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", Objects.requireNonNull(getActivity()).getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    private void showFileChooser() {
        /*Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);*/
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);
        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void cameraIntent() {
        /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        startActivityForResult(intent, REQUEST_CAMERA);*/
        Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_CAMERA);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                Uri filePath = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    uploadBanner(bitmap);
                    bannerImageIs.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
       /* if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            //fileSelctedPath = getPath(filePath);
            //if (fileSelctedPath != null) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    uploadBanner(bitmap);
                    bannerImageIs.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
           *//* } else {
                Toast.makeText(getActivity(), "no image selected", Toast.LENGTH_LONG).show();
            }*//*

        }

        if (resultCode == RESULT_OK && requestCode == REQUEST_CAMERA) {

            //onCaptureImageResult(data);
            bitmap = (Bitmap) data.getExtras().get("data");
            uploadBanner(bitmap);
            bannerImageIs.setImageBitmap(bitmap);

        }*/
    }

    public String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void getmedia() {
        displayforno.clear();
        languages.clear();
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        String serverurl = API.getShopmedia;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String shopid = jsonObject1.getString("shopId");
                        JSONArray jsonArray = jsonObject1.getJSONArray("bannerTypeArr");
                        JSONObject jsonObjectscreen = jsonObject1.getJSONObject("screenArr");
                        JSONObject jsonObjectfileType = jsonObject1.getJSONObject("fileTypeArr");
                        Iterator iterator = jsonObjectscreen.keys();
                        while (iterator.hasNext()) {
                            String key = (String) iterator.next();
                            String value = String.valueOf(jsonObjectscreen.getString(key));
                            Stateslist stateslist = new Stateslist(key, value);
                            displayforno.add(stateslist);
                        }

                        displayfor.setAdapter(new ArrayAdapter<Stateslist>(getActivity(), android.R.layout.simple_spinner_dropdown_item, displayforno));
                        displayfor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                displayForBanner = displayforno.get(position).getStateid();
                                //Toast.makeText(getActivity(), "displayis: " + displayForBanner, Toast.LENGTH_SHORT).show();
                                getImages(lang_banner, displayForBanner);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                        for (int i = 0; i < jsonArray.length(); i++) {
                            languages.add(jsonArray.getString(i));
                        }
                        language.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, languages));
                        language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                lang_banner = String.valueOf(language.getItemIdAtPosition(position));
                                //Toast.makeText(getActivity(), "lang: " + lang_banner, Toast.LENGTH_SHORT).show();
                                getImages(lang_banner, displayForBanner);

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

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

    private void getImages(String lang_banner, String displayForBanner) {
        progressDialog.show();
        final RequestQueue queue = Volley.newRequestQueue(getActivity());
        String serverurl = API.getImages + "banner" + "/" + lang_banner + "/" + displayForBanner;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        progressDialog.dismiss();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONArray imageurl = jsonObject1.getJSONArray("imageUrl");
                        if(imageurl.length()!=0) {
                            String imgurl = imageurl.getString(0);
                            Picasso.get().load(imgurl).fit().memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE).into(bannerImageIs);
                        }
                    } else {
                        progressDialog.dismiss();
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
        queue.add(stringRequest);

    }

   /* @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }*/

    private void uploadBanner(final Bitmap bitmap) {
        progressDialog.show();
        String url = API.setupShopmedia;
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        String resultResponse = new String(response.data);

                        try {
                            JSONObject jsonObject = new JSONObject(resultResponse);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if(status.equals("1")){
                                progressDialog.dismiss();
                                Snackbar.make(scrollView, message, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();

                            }else{
                                progressDialog.dismiss();
                                Snackbar.make(scrollView, message, Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError", "" + error.getMessage());
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("X-TOKEN", tokenValue);
                return params;
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lang_id", lang_banner);
                params.put("slide_screen", displayForBanner);
                params.put("file_type", "5");
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                Log.e("imagepart", String.valueOf(new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap))));
                params.put("file", new DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        //adding the request to volley
        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        Volley.newRequestQueue(getActivity()).add(volleyMultipartRequest);
    }

}

