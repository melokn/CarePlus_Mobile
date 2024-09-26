package com.example.testecareplus;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        EditText campoEmail = findViewById(R.id.campoEmailLogin);
        EditText camposenha = findViewById(R.id.campoSenhaLogin);
        Button login = findViewById(R.id.btLogin);
        Button btcadastrar = findViewById(R.id.btCadastro);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginFragment.this, HomeFragment.class);

                startActivity(intent);
            }
        });

    }

}