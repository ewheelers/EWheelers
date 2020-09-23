package com.ewheelers.ewheelers.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.ewheelers.ewheelers.BuildConfig;
import com.ewheelers.ewheelers.Fragments.HomeFragment;
import com.ewheelers.ewheelers.Fragments.WalletFragment;
import com.ewheelers.ewheelers.R;
import com.ewheelers.ewheelers.Utils.SessionPreference;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.nineoldandroids.animation.AnimatorInflater;
import com.nineoldandroids.animation.AnimatorSet;

import static java.lang.System.exit;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnSuccessListener<AppUpdateInfo> {
    NavigationView navigationView;
    View mHeaderView;
    public static DrawerLayout drawer;
    RadioGroup radioGroup;
    RadioButton radioButton, radioButto;
    ImageView imageView_logout, menu_icon;
    TextView user_name, user_Is, view_account;
    Button scan_qr;
    private AppUpdateManager appUpdateManager;
    private boolean mNeedsFlexibleUpdate;
    public static final int REQUEST_CODE = 1234;
    private int RC_APP_UPDATE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String username = new SessionPreference().getStrings(Home.this, SessionPreference.username);
        user_name = findViewById(R.id.username);
        user_name.setText(username);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_drawer);
        navigationView.setNavigationItemSelectedListener(this);
        mHeaderView = navigationView.getHeaderView(0);

        user_Is = mHeaderView.findViewById(R.id.userIs);
        view_account = mHeaderView.findViewById(R.id.viewAccount);
        user_Is.setText(username);
        scan_qr = findViewById(R.id.scanQR);
        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(getApplicationContext(),ScanQRCode.class);
                startActivity(i);
                //finish();
            }
        });
        menu_icon = findViewById(R.id.menuicon);
        radioGroup = findViewById(R.id.bottomLayout);
        imageView_logout = findViewById(R.id.logout);
        int id = radioGroup.getCheckedRadioButtonId();
        radioButto = findViewById(id);
        if (radioButto.isChecked()) {
            FragmentTras();
        }

        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(this);
        mNeedsFlexibleUpdate = false;
        /*AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(), R.anim.property_animator);
        set.setTarget(scan_qr);
        set.start();*/

        view_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Home.this, "Shows account activity", Toast.LENGTH_SHORT).show();
            }
        });

        menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        imageView_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionPreference.clearString(Home.this, SessionPreference.userid);
                SessionPreference.clearString(Home.this, SessionPreference.tokenvalue);
                Intent i = new Intent(getApplicationContext(), LoginScreenActivity.class);
                startActivity(i);
                finish();
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int id = group.getCheckedRadioButtonId();
                radioButton = findViewById(id);
                switch (checkedId) {
                    case R.id.home_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
                        break;
                    case R.id.wallet_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new WalletFragment()).commit();
                        break;
                    /*case R.id.scan_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new WalletFragment()).commit();
                        break;*/
                    case R.id.messages_radio:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new WalletFragment()).commit();
                        break;
                }
            }
        });
    }

    private void FragmentTras() {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
      /*  if (id == R.id.bussinessettings) {
            Intent i = new Intent(getApplicationContext(), signup_two.class);
            startActivity(i);
        }*/
        if (id == R.id.accountsettings) {
            Intent i = new Intent(getApplicationContext(), SetupAccount.class);
            startActivity(i);
        }
        if (id == R.id.banksettings) {
            Intent i = new Intent(Home.this, BankAccountDetails.class);
            startActivity(i);
        }
        if (id == R.id.estoresettings) {
            SessionPreference.clearString(Home.this,SessionPreference.shopaddress);
            SessionPreference.clearString(Home.this,SessionPreference.zipcode);
            SessionPreference.clearString(Home.this,SessionPreference.latitude);
            SessionPreference.clearString(Home.this,SessionPreference.logitude);
            Intent i = new Intent(Home.this, eStoreSettings.class);
            startActivity(i);
        }
        if(id == R.id.share){
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String shareMessage = "\nLargest eBike Digital Store\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        }
        if (id == R.id.parkingaddress) {

        }
        if (id == R.id.chargingaddress) {

        }
        if(id == R.id.logout){
            SessionPreference.clearString(Home.this, SessionPreference.shopid);
            SessionPreference.clearString(Home.this, SessionPreference.userid);
            SessionPreference.clearString(Home.this, SessionPreference.tokenvalue);
            Intent i = new Intent(getApplicationContext(), LoginScreenActivity.class);
            startActivity(i);
            finish();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(this);
    }

    @Override
    public void onResume() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(this);
        super.onResume();
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        appUpdateManager = AppUpdateManagerFactory.create(context);
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e("AppUpdate", "onActivityResult: app download failed");
            }
        }
    }

    @Override
    public void onSuccess(AppUpdateInfo appUpdateInfo) {
        Log.e("abc", "abc");


        if (appUpdateInfo.updateAvailability()
                == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
            // If an in-app update is already running, resume the update.
            startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
            Log.e("abc1", "abc1");

        } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
            // If the update is downloaded but not installed,
            // notify the user to complete the update.
            popupSnackbarForCompleteUpdate();
            Log.e("abc2", "abc2");

        } else if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
            if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdate(appUpdateInfo, AppUpdateType.IMMEDIATE);
                Log.e("abc3", "abc3");

            } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                mNeedsFlexibleUpdate = true;
                showFlexibleUpdateNotification();
                Log.e("abc4", "abc4");

            }
        }

    }


    private void startUpdate(final AppUpdateInfo appUpdateInfo, final int appUpdateType) {
        final Activity activity = this;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo,
                            appUpdateType,
                            activity,
                            REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /* Displays the snackbar notification and call to action. */
    private void popupSnackbarForCompleteUpdate() {
        Snackbar snackbar =
                Snackbar.make(findViewById(R.id.frame5),
                        "An update has just been downloaded.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appUpdateManager.completeUpdate();
            }
        });
        snackbar.setActionTextColor(getResources().getColor(R.color.colorPrimary));
        snackbar.show();
    }

    private void showFlexibleUpdateNotification() {
        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.frame5),
                        "An update is available and accessible in More.",
                        Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}
