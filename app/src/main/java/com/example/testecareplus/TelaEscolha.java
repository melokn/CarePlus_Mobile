package com.example.testecareplus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class TelaEscolha extends AppCompatActivity {

    public void createUser(String funcaoUsuario){
        SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String senha = prefs.getString("senha", null);
        String usuario = prefs.getString("usuario", null);


        String url = "http://192.168.15.10:4060/users";


        JSONObject dadosBody = new JSONObject();
        try{
            dadosBody.put("name", usuario);
            dadosBody.put("email", email);
            dadosBody.put("password", senha);
            dadosBody.put("userFunction", funcaoUsuario);
        }catch(JSONException exc){
            exc.printStackTrace();
        }
        JsonObjectRequest enviarPost = new JsonObjectRequest(
                Request.Method.POST,
                url,
                dadosBody,
                new Response.Listener<JSONObject>() {
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.has("userId")) {
                                String userId = response.getString("userId");
                                SharedPreferences prefs = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = prefs.edit();
                                editor.putString("userId", userId);
                                editor.apply();


                                Toast.makeText(TelaEscolha.this, "Cadastrado", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(TelaEscolha.this, MainActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(TelaEscolha.this, "Erro: resposta inválida", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(TelaEscolha.this, "Erro ao processar a resposta", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Toast.makeText(TelaEscolha.this, "Erro ao enviar", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(TelaEscolha.this);
        queue.add(enviarPost);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_escolha);


        ImageButton btfamilia = findViewById(R.id.btfamilia);
        ImageButton btcuidador = findViewById(R.id.btcuidador);

        btfamilia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String funcaoUsuario = "Familiar";
                createUser(funcaoUsuario);;
            }
        });


        btcuidador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String funcaoUsuario = "Cuidador";
                createUser(funcaoUsuario);
            }
        });
    }
}