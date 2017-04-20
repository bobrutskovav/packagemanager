package com.example.user23.packagemanager;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends Activity {
    private ListView packagesListView;
    private PackageAdapter packageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packagesListView = (ListView) findViewById(R.id.list_packages);


       List<PackageInfo> packages = getPackageManager().getInstalledPackages(0);
        packageAdapter =new PackageAdapter(packages);
        packagesListView.setAdapter(packageAdapter);

        packagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PackageInfo info = packageAdapter.getItem(position);
                Intent intent = getPackageManager().getLaunchIntentForPackage(info.packageName);
                if (intent != null){
                    startActivity(intent);
                }else {
                    Toast.makeText(MainActivity.this,getString(R.string.cant_start,info.packageName),Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
