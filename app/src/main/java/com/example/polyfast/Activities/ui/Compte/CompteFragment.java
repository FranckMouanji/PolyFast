package com.example.polyfast.Activities.ui.Compte;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.polyfast.Activities.Sing_in_sind_up;
import com.example.polyfast.R;
import com.example.polyfast.data_class.Temp_information_user;
import com.example.polyfast.fireBase_fonction.Controller;

public class CompteFragment extends Fragment {

    public static CompteFragment newInstance() {
        return new CompteFragment();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.compte_fragment, container, false);

        //initialisation
        TextView nom = view.findViewById(R.id.nom);
        TextView login = view.findViewById(R.id.login);
        Button deconnexion = view.findViewById(R.id.deconnexion);

        Temp_information_user temp_information_user = Controller.Trans_to_Temp_information_user(view.getContext());

        nom.setText(temp_information_user.getUser_name());
        login.setText(temp_information_user.getLogin());

        //fonction de deconnexion
        deconnexion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Controller.delete_file(getContext());
                Intent intent = new Intent(getContext(), Sing_in_sind_up.class);
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}
