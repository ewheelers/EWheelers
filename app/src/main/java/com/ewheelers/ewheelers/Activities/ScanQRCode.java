package com.ewheelers.ewheelers.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ewheelers.ewheelers.Network.API;
import com.ewheelers.ewheelers.Network.VolleySingleton;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ScanQRCode extends AppCompatActivity implements View.OnClickListener {
    TextView textView;

    ImageView flashOff, scan_image;
    BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private SurfaceView cameraView;
    boolean flashmode = false;
    private Camera camera = null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);
        flashOff = findViewById(R.id.flash_off);
        cameraView = findViewById(R.id.surfaceView);
        scan_image = findViewById(R.id.scanByImage);
        textView = findViewById(R.id.scanbyText);
        flashOff.setOnClickListener(this);
        progressDialog = new ProgressDialog(ScanQRCode.this);
        progressDialog.setTitle("Ewheelers");
        progressDialog.setMessage("Bank Details Updating ....");
        progressDialog.setCancelable(false);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flash_off:
                flashOnButton();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    private void initialiseDetectorsAndSources() {
        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        cameraView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(cameraView.getHolder());

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {
                    textView.post(new Runnable() {
                        @Override
                        public void run() {
                            //textView.setText(barcodes.valueAt(0).displayValue);
                            //textView.setTextColor(getResources().getColor(R.color.colorPrimary));
                            getOrder(barcodes.valueAt(0).displayValue);
                        }
                    });
                }
            }
        });
    }

    private void getOrder(String displayValue) {
        progressDialog.show();
        final RequestQueue queue = Volley.newRequestQueue(ScanQRCode.this);
        String serverurl = API.getOrders + displayValue;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, serverurl, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(ScanQRCode.this);
                        final View dialogView = getLayoutInflater().inflate(R.layout.custom, null);
                        TextView orderid = dialogView.findViewById(R.id.orderid);
                        TextView invoice = dialogView.findViewById(R.id.invoice);
                        TextView station = dialogView.findViewById(R.id.stationDetails);
                        TextView statusDetails = dialogView.findViewById(R.id.status);
                        TextView productname = dialogView.findViewById(R.id.prodname);
                        TextView typeofVeh = dialogView.findViewById(R.id.options);
                        TextView vehNo = dialogView.findViewById(R.id.vehno);
                        TextView vehModel = dialogView.findViewById(R.id.vehmodel);
                        TextView timings = dialogView.findViewById(R.id.timings);
                        TextView shopName = dialogView.findViewById(R.id.shopname);
                        TextView sellerName = dialogView.findViewById(R.id.sellername);
                        TextView addedDate = dialogView.findViewById(R.id.addeddate);
                        TextView amount = dialogView.findViewById(R.id.amount);
                        NetworkImageView imageView = dialogView.findViewById(R.id.prod_img);
                        Button yesbutton = dialogView.findViewById(R.id.dialogButtonOK);
                        builder.setView(dialogView);
                        // create and show the alert dialog
                        final AlertDialog dialog = builder.create();
                        dialog.show();
                        dialog.setCancelable(true);
                        yesbutton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                                dialog.dismiss();
                                dialog.setCancelable(false);
                            }
                        });

                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        JSONObject jsonObject2 = jsonObject1.getJSONObject("orderDetail");
                        JSONObject totalamount = jsonObject2.getJSONObject("totalAmount");
                        String value = totalamount.getString("value");
                        amount.setText(value);
                        JSONObject station_details = jsonObject2.getJSONObject("opd_station_details");
                        JSONObject jsonObjectaddress = station_details.getJSONObject("product_address");
                        String address = jsonObjectaddress.getString("address");
                        String cityname = jsonObjectaddress.getString("city_name");
                        String statename = jsonObjectaddress.getString("state_name");
                        String countryname = jsonObjectaddress.getString("country_name");
                        String zipcode = jsonObjectaddress.getString("ua_zip");
                        String uaname = jsonObjectaddress.getString("ua_name");
                        station.setText(uaname+"\n"+address + "," + cityname + ","+ countryname+" - "+zipcode);
                        String productImgurl = jsonObject2.getString("product_image_url");
                        JSONArray jsonArray = jsonObject2.getJSONArray("comments");
                        if (jsonArray.length() != 0) {
                            //for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject3 = jsonArray.getJSONObject(0);
                            String status_name = jsonObject3.getString("orderstatus_name");
                            String product_name = jsonObject3.getString("op_product_name");
                            String prod_options = jsonObject3.getString("op_selprod_options");
                            String shop_name = jsonObject3.getString("op_shop_name");
                            String seller_name = jsonObject3.getString("seller_name");
                            String date_added = jsonObject3.getString("oshistory_date_added");
                            String op_Id = jsonObject3.getString("op_id");
                            String op_oreder_Id = jsonObject3.getString("op_order_id");
                            String invoice_no = jsonObject3.getString("op_invoice_number");
                            String selprod_id = jsonObject3.getString("op_selprod_id");
                            String selprod_code = jsonObject3.getString("op_selprod_code");
                            String opQty = jsonObject3.getString("op_qty");
                            String opProductModel = jsonObject3.getString("op_product_model");
                            String opd_station_details = jsonObject3.getString("opd_station_details");
                            orderid.setText(op_oreder_Id);
                            invoice.setText(invoice_no);
                            statusDetails.setText(status_name);
                            productname.setText(product_name);
                            typeofVeh.setText(prod_options);
                            shopName.setText(shop_name);
                            sellerName.setText(seller_name);
                            addedDate.setText(date_added);
                            if(!productImgurl.isEmpty()){
                                ImageLoader imageLoader = VolleySingleton.getInstance(ScanQRCode.this).getImageLoader();
                                imageLoader.get(productImgurl, ImageLoader.getImageListener(imageView, R.drawable.logo_partner, android.R.drawable.ic_dialog_alert));
                                imageView.setImageUrl(productImgurl, imageLoader);
                            }
                            getExcelList(op_oreder_Id,vehNo,vehModel,timings);

                            //}
                        }


                    } else {
                        progressDialog.dismiss();
                        textView.setText(msg);
                        textView.setTextSize(18f);
                        textView.setTextColor(Color.RED);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                textView.setText("Scan customer Parking Pass");
                                textView.setTextSize(16f);
                                textView.setTextColor(Color.BLACK);
                            }
                        },1500);

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
                params.put("X-TOKEN", new SessionPreference().getStrings(ScanQRCode.this, SessionPreference.tokenvalue));
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

    private void getExcelList(final String order_id, final TextView vehNo, final TextView vehModel, final TextView timings) {
        String url_link = "https://script.google.com/macros/s/AKfycbxxYKFslwo8bfMOFDNTgo3DMCKpjrcecu5Dyl4lRJ1E7I2MugM/exec?action=getItems";
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url_link, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.startsWith("<")) {

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObjectdata = jsonArray.getJSONObject(i);
                            String orderid = jsonObjectdata.getString("orderid");
                            if (orderid.equals(order_id)) {
                                String vehno = jsonObjectdata.getString("vehno");
                                String vehmodel = jsonObjectdata.getString("vehmodel");
                                String timing = jsonObjectdata.getString("timing");
                                //String address = jsonObjectdata.getString("address");
                                vehNo.setText(vehno);
                                vehModel.setText(vehmodel);
                                timings.setText(timing);
                                progressDialog.dismiss();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                params.put("X-TOKEN", new SessionPreference().getStrings(ScanQRCode.this, SessionPreference.tokenvalue));
                return params;
            }

            @Override
            public Map<String, String> getParams() {
                Map<String, String> data3 = new HashMap<String, String>();
                return data3;
            }

        };
        // Add the realibility on the connection.
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000, 1, 1.0f));
        queue.add(stringRequest);
    }


    private static Camera getCamera(@NonNull CameraSource cameraSource) {
        Field[] declaredFields = CameraSource.class.getDeclaredFields();

        for (Field field : declaredFields) {
            if (field.getType() == Camera.class) {
                field.setAccessible(true);
                try {
                    Camera camera = (Camera) field.get(cameraSource);
                    if (camera != null) {
                        return camera;
                    }
                    return null;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void flashOnButton() {
        camera = getCamera(cameraSource);
        if (camera != null) {
            try {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode ? Camera.Parameters.FLASH_MODE_TORCH : Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
                if (flashmode) {
                    //showToast("Flash Switched ON");
                    flashOff.setBackground(getResources().getDrawable(R.drawable.round_red_strike));
                } else {
                    //showToast("Flash Switched Off");
                    flashOff.setBackground(getResources().getDrawable(R.drawable.round_button));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
