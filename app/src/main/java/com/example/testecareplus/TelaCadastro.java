package com.example.testecareplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class TelaCadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);


        //Funcionalidade dos botões
        EditText email = findViewById(R.id.campoEmailCadastro);
        EditText usuario = findViewById(R.id.campoUsuario);
        EditText senha = findViewById(R.id.campoSenhaCadastro);
        Button cadastrar = findViewById(R.id.btCadastrarCadastro);

        cadastrar.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(TelaCadastro.this, HomeFragment.class);

                startActivity(intent);

            }

        });



    }
}