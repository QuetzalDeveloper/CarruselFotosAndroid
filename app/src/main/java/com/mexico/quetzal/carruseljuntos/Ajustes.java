package com.mexico.quetzal.carruseljuntos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

public class Ajustes extends AppCompatActivity {

    private EditText valor;
    private RadioButton v10, v30, v60;
    private Button guardar;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes);

        valor = findViewById(R.id.numero);
        v10 = findViewById(R.id.v10);
        v30 = findViewById(R.id.v30);
        v60 = findViewById(R.id.v60);
        guardar = findViewById(R.id.guardar);

        RecuperarPreferencias();

        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int fotos = Integer.parseInt(valor.getText().toString())+1;
                int tiempo;
                if(v10.isChecked()){
                    tiempo = 10;
                }
                else if (v30.isChecked()){
                    tiempo = 30;
                }
                else{
                    tiempo = 60;
                }

                GuardarPreferencias(fotos, tiempo);
                finish();
            }
        });
    }

    private void RecuperarPreferencias(){
        SharedPreferences preferences = getSharedPreferences("galeria", Context.MODE_PRIVATE);
        valor.setText(preferences.getInt("fotos", 2)-1+"");
        time = preferences.getInt("tiempo", 30);
        if(time == 10){
            v10.setChecked(true);
        }
        else if(time == 60){
            v60.setChecked(true);
        }
        else{
            v30.setChecked(true);

        }
    }

    private void GuardarPreferencias(int f, int t){
        SharedPreferences preferences= getSharedPreferences("galeria", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("fotos", f);
        editor.putInt("tiempo", t);
        editor.commit();
    }


}