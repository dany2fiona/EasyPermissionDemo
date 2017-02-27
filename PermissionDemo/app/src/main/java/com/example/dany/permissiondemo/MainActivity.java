package com.example.dany.permissiondemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import android.Manifest;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,View.OnClickListener{
    private static final String TAG = "dan.y";
    private static final int RC_CAMERA_PERM = 123;
    private static final int RC_LOCATION_CONTACTS_PERM = 124;
    String[] perms = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.READ_CONTACTS};
    private Button cameraBtn;
    private Button loconBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cameraBtn = (Button) findViewById(R.id.btn_camera);
        loconBtn = (Button) findViewById(R.id.btn_locon);
        cameraBtn.setOnClickListener(this);
        loconBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_camera:
                cameraTask();
                break;
            case R.id.btn_locon:
                loconTask();
                break;
        }
    }

    @AfterPermissionGranted(RC_CAMERA_PERM)
    private void cameraTask(){
        if(EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA)){
            //TODO:Have permission,do the thing!
            Toast.makeText(this, "TODO: Camera things", Toast.LENGTH_LONG).show();
        }else{
            //Ask for one permission
            EasyPermissions.requestPermissions(this,getString(R.string.rationale_camera),RC_CAMERA_PERM,Manifest.permission.CAMERA);
        }
    }

    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    private void loconTask(){
        for (int i=0;i<perms.length;i++){
            Log.d("dan.o",perms[i]);
        }
        if(EasyPermissions.hasPermissions(this,perms)){
            //TODO:Have permissions,do the thing!
            Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
        }else{//没什么必要将信息显示的这么明细化..
            if(perms.length == 2){
                //Ask for both permissions
                EasyPermissions.requestPermissions(this,getString(R.string.rationale_location_contacts),RC_LOCATION_CONTACTS_PERM,perms);
            }else if(perms.length == 1){
                if(perms[0].equals(Manifest.permission.ACCESS_FINE_LOCATION)){
                    EasyPermissions.requestPermissions(this,getString(R.string.rationale_location),RC_LOCATION_CONTACTS_PERM,perms);
                }else if(perms[0].equals(Manifest.permission.READ_CONTACTS)){
                    EasyPermissions.requestPermissions(this,getString(R.string.rationale_contacts),RC_LOCATION_CONTACTS_PERM,perms);
                }
            }else{}
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //Forward results to EasyPermissions..
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d(TAG,"onPermissionsGranted:"+requestCode+":"+perms.size());
        for (String perm:perms){
            Log.d(TAG,"onPermissionsGranted:"+requestCode+":"+perm);
        }
        switch (requestCode){
            case RC_CAMERA_PERM:
                break;
            case RC_LOCATION_CONTACTS_PERM:
                if(perms.size()<=0){
                    break;
                }
                ArrayList<String> temp = new ArrayList<>();
                for (int i = 0;i<this.perms.length;i++){
                    for (String p:perms){
                        if(!(this.perms[i].equals(p))){
                            Log.d(TAG,p);
                            temp.add(this.perms[i]);
                        }
                    }
                }
                this.perms = new String[temp.size()];
                for (int i = 0;i<temp.size();i++){
                    this.perms[i] = temp.get(i);
                }

                break;

        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG,"onPermissionsDenied:"+requestCode+":"+perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            new AppSettingsDialog.Builder(this).build().show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, R.string.returned_from_app_settings_to_activity, Toast.LENGTH_SHORT).show();
        }
    }

}
