package com.example.testecareplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TelaCadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);


        EditText campoEmail = findViewById(R.id.campoEmailCadastro);
        EditText campoUsuario = findViewById(R.id.campoUsuario);
        EditText campoSenha = findViewById(R.id.campoSenhaCadastro);
        Button btCadastro = findViewById(R.id.btCadastrarCadastro);

        btCadastro.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                String email = campoEmail.getText().toString().trim();
                String senha = campoSenha.getText().toString().trim();
                String usuario = campoUsuario.getText().toString().trim();


                if (email.isEmpty() || senha.isEmpty() || usuario.isEmpty()) {

                    Toast.makeText(TelaCadastro.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email", email);
                editor.putString("senha", senha);
                editor.putString("usuario", usuario);
                editor.apply();


                Intent intent = new Intent(TelaCadastro.this, TelaEscolha.class);
                startActivity(intent);

            }

        });



    }
}