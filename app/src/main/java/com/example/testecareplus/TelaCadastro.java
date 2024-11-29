package com.example.testecareplus;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class TelaCadastro extends AppCompatActivity {

    private TextView requisito8Caracteres, requisitoMaiuscula, requisitoNumero, requisitoSimbolo;
    private Button btCadastrar;
    private boolean requisitosCumpridos = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_cadastro);

        // Referências aos campos e TextViews
        EditText campoEmail = findViewById(R.id.campoEmailCadastro);
        EditText campoUsuario = findViewById(R.id.campoUsuario);
        EditText campoSenha = findViewById(R.id.campoSenhaCadastro);
        btCadastrar = findViewById(R.id.btCadastrarCadastro);
        requisito8Caracteres = findViewById(R.id.requisito8Caracteres);
        requisitoMaiuscula = findViewById(R.id.requisitoMaiuscula);
        requisitoNumero = findViewById(R.id.requisitoNumero);
        requisitoSimbolo = findViewById(R.id.requisitoSimbolo);

        btCadastrar.setEnabled(false);  // Desabilita o botão de cadastro até que todos os requisitos sejam atendidos

        campoSenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                // Verifica os requisitos da senha
                validarSenha(campoSenha.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        btCadastrar.setOnClickListener(view -> {
            String email = campoEmail.getText().toString().trim();
            String senha = campoSenha.getText().toString().trim();
            String usuario = campoUsuario.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty() || usuario.isEmpty()) {
                Toast.makeText(TelaCadastro.this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Salva os dados usando SharedPreferences
            SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("email", email);
            editor.putString("senha", senha);
            editor.putString("usuario", usuario);
            editor.apply();

            campoUsuario.setText("");
            campoEmail.setText("");
            campoSenha.setText("");
            Intent intent = new Intent(TelaCadastro.this, TelaEscolha.class);
            startActivity(intent);
        });
    }

    private void validarSenha(String senha) {
        boolean hasMinLength = senha.length() >= 8;
        boolean hasUpperCase = !senha.equals(senha.toLowerCase());
        boolean hasNumber = senha.matches(".*\\d.*");
        boolean hasSymbol = senha.matches(".*[!@#$%^&*(),.?\":{}|<>].*");

        // Atualiza os requisitos com base na senha
        if (hasMinLength) {
            requisito8Caracteres.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            requisito8Caracteres.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }

        if (hasUpperCase) {
            requisitoMaiuscula.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            requisitoMaiuscula.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }

        if (hasNumber) {
            requisitoNumero.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            requisitoNumero.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }

        if (hasSymbol) {
            requisitoSimbolo.setTextColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            requisitoSimbolo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }

        // Habilita o botão de cadastrar se todos os requisitos forem cumpridos
        requisitosCumpridos = hasMinLength && hasUpperCase && hasNumber && hasSymbol;
        btCadastrar.setEnabled(requisitosCumpridos);
    }
}
