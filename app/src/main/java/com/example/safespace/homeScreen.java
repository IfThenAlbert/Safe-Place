package com.example.safespace;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class homeScreen extends AppCompatActivity {

    private ParseUser current;

    private boolean isEverythingOk;
    private String lastKnownLocation;
    private FusedLocationProviderClient fusedClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // get and check if there is a current user signed in
        isEverythingOk = false;
        current = ParseUser.getCurrentUser();
        lastKnownLocation = "";

        if (current == null) {
            Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goHome);
            return;
        } else {

            TextView tvUser = (TextView) findViewById(R.id.txtUser);
            tvUser.setText("Hello " + current.get("firstname").toString());
        };

        fusedClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    isEverythingOk = true;
                }else{
                    Toast.makeText(getApplicationContext(),"Please enable your GPS/Location", Toast.LENGTH_LONG).show();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, 280);
            };
        };

    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 280) {
            if ((grantResults[0] == PackageManager.PERMISSION_GRANTED) && (grantResults[1] == PackageManager.PERMISSION_GRANTED) &&
                    (grantResults[2] == PackageManager.PERMISSION_GRANTED)) {
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    isEverythingOk = true;
                }else{
                    Toast.makeText(getApplicationContext(),"Please enable your GPS/Location", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "can not use the app without letting the app access to your location and sms", Toast.LENGTH_LONG).show();
            };
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        };
    }

    public void checkMyInfo(View v) {
        // when user wishes to view account
        Intent goInfo = new Intent(getApplicationContext(), myInfoScreen.class);
        startActivity(goInfo);
    };

    public void sendEmergencyAlert(View v) {
        // when user is in danger and wants to alert friends

        if (isEverythingOk) {
            alertEveryone();
        } else {
            Toast.makeText(getApplicationContext(), "Please let the app access location and sms", Toast.LENGTH_LONG).show();
        };
    };

    public void friendsButton(View v) {
        // open up the friends screen to view and or add friends
        Intent goFriend = new Intent(getApplicationContext(), friendsScreen.class);
        startActivity(goFriend);

    };

    public void logMeOut(View v) {
        // when user wants to logout
        ParseUser.logOut();
        Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(goHome);
    };


    public void alertEveryone() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Geocoder possitionConverter = new Geocoder(getApplicationContext(), Locale.getDefault());
                try {
                    List<Address> possibleAdds = possitionConverter.getFromLocation(location.getLatitude(),location.getLongitude(),3);
                    if(possibleAdds.size() > 1 ) {
                        if(possibleAdds.get(0).getAddressLine(0) !=null) {
                            lastKnownLocation = possibleAdds.get(0).getAddressLine(0);
                        }else{
                            lastKnownLocation = "LAT: " + location.getLatitude() + "\nLONG: " + location.getLongitude();
                        }
                    }else{
                        lastKnownLocation = "LAT: " + location.getLatitude() + "\nLONG: " + location.getLongitude();
                    };
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(),"Message: " + e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
        ParseQuery<ParseObject> alertingQ = ParseQuery.getQuery("safeplacefriends");
        alertingQ.whereEqualTo("friendsof", current.getObjectId());
        alertingQ.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    String message = "";
                    if (objects.size() != 0) {
                        for (ParseObject pob : objects) {
                            String phone = "+63 " + pob.getString("phone");
                            message = current.getString("firstname") + "\nlast known location\n"+lastKnownLocation;
                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(phone, null,""+message , null, null);
                        };


                    } else {
                        Toast.makeText(getApplicationContext(), "please add friends to notify !  ! !", Toast.LENGTH_SHORT).show();
                    };
                };
            };
        });
    };

    @Override
    protected void onRestart() {
        super.onRestart();

        current = ParseUser.getCurrentUser();
        lastKnownLocation = "";

        if (current == null) {
            Intent goHome = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(goHome);
            return;
        } else {
            TextView tvUser = (TextView) findViewById(R.id.txtUser);
            tvUser.setText("Hello " + current.get("firstname").toString());
        };
    }

    @Override
    public void onBackPressed() {
        // prevent user backing
    }



}