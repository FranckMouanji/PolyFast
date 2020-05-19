package com.example.polyfast.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.polyfast.R;

import java.io.File;
import java.util.ArrayList;

public class DownloadedFile extends AppCompatActivity {

    ListView list_file_downloded;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    TextView contenu_vide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloaded_file);

        list_file_downloded = findViewById(R.id.list_file);
        contenu_vide = findViewById(R.id.contenu_vide);




        list_file_downloded.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    openFile("/sdcard/PolyFast/Download/", position);
            }
        });



        if (ContextCompat.checkSelfPermission(DownloadedFile.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(DownloadedFile.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {


            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(DownloadedFile.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.


            }
        } else {

            ArrayList<String> filesinfolder = GetFiles("/sdcard/PolyFast/Download/");
            ArrayAdapter<String> adapter
                    = new ArrayAdapter<String>(this,
                    android.R.layout.simple_list_item_1,
                    filesinfolder);

            if (filesinfolder != null){
                list_file_downloded.setAdapter(adapter);
                list_file_downloded.setVisibility(View.VISIBLE);
                contenu_vide.setVisibility(View.GONE);
            }else {
                list_file_downloded.setVisibility(View.GONE);
                contenu_vide.setVisibility(View.VISIBLE);
            }




        }
    }

    private void openFile(String path_name, int position) {
        String filename = getPartuclarFile(path_name, position);
        String path = path_name + "/" + filename;
        File file = new File(path);

        Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
        pdfViewIntent.setDataAndType(Uri.fromFile(file),"application/pdf");
        pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(pdfViewIntent, "Open File");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            // Instruct the user to install a PDF reader here, or something
        }
    }

    public String getPartuclarFile(String directorypath, int position){
        String Myfiles;
        File f = new File(directorypath);
        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0) {
            return null;
        } else {
            Myfiles = files[position].getName();
        }
        return Myfiles;
    }


    public ArrayList<String> GetFiles(String directorypath) {
        ArrayList<String> Myfiles = new ArrayList<String>();
        File f = new File(directorypath);
        f.mkdirs();
        File[] files = f.listFiles();
        if (files.length == 0) {
            return null;
        } else {
            for (int i = 0; i < files.length; i++)
                Myfiles.add(files[i].getName());
        }
        return Myfiles;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    ArrayList<String> filesinfolder = GetFiles("/sdcard/PolyFast/Download/");
                    ArrayAdapter<String> adapter
                            = new ArrayAdapter<String>(this,
                            android.R.layout.simple_list_item_1,
                            filesinfolder);

                    if (filesinfolder != null){
                        list_file_downloded.setAdapter(adapter);
                        list_file_downloded.setVisibility(View.VISIBLE);
                        contenu_vide.setVisibility(View.GONE);
                    }else {
                        list_file_downloded.setVisibility(View.GONE);
                        contenu_vide.setVisibility(View.VISIBLE);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
