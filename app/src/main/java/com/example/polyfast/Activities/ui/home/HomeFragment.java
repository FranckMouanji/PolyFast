package com.example.polyfast.Activities.ui.home;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.polyfast.R;
import com.example.polyfast.data_class.File_uploaded;
import com.example.polyfast.fireBase_fonction.Controller;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ListView list_pdf_total;
    TextView vide;
    public List<File_uploaded> file_uploadeds = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        list_pdf_total = root.findViewById(R.id.list_pdf_total);
        vide = root.findViewById(R.id.vide);
        Controller.recup_and_charge_data(getContext(), list_pdf_total, "home", vide, file_uploadeds);
        list_pdf_total.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File_uploaded file_uploaded = file_uploadeds.get(position);

                Controller.Download(getContext(), file_uploaded.getFileName(), ".pdf", file_uploaded.getFilePathInFirebase());
            }
        });

        return root;
    }
}
