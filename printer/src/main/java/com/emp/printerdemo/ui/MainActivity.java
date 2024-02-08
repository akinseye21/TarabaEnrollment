package com.emp.printerdemo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.emp.printerdemo.R;
import com.emp.printerdemo.utils.PermissionUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
    }


    private void initPermission() {
        String[] permissionArray = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        String[] noCheckPermissionArray = PermissionUtil.checkPermissions(this, permissionArray);
        if (noCheckPermissionArray != null && noCheckPermissionArray.length > 0) {
            PermissionUtil.applyPermission(this, noCheckPermissionArray);
        }
    }

    public void onPrint(View view) {
        startActivity(new Intent(this,PrintActivity.class));
    }
}