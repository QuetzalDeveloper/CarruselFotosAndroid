package com.mexico.quetzal.carruseljuntos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private int numImage, time;
    private ImageView imagen;
    private String ruta = "/mnt/usb_storage/Juntos/", rutF;
    private boolean flag = true, edo = true;
    private Button boton, pausa, izq, der;
    Hilo h = new Hilo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecuperarPreferencias();
        boton = findViewById(R.id.boton);
        pausa = findViewById(R.id.pausa);
        izq = findViewById(R.id.izquierda);
        der = findViewById(R.id.derecha);

        h.start();

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Ajustes.class);
                startActivity(i);
            }
        });

        pausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pausar();
            }
        });


    }

    private void Pausar() throws InterruptedException {
        if(flag) {
            flag = false;
            h.join();
            izq.setVisibility(View.VISIBLE);
            der.setVisibility(View.VISIBLE);
        }
        else{
            flag = true;
            h.run();
            izq.setVisibility(View.INVISIBLE);
            der.setVisibility(View.INVISIBLE);
        }
    }

        private void RecuperarPreferencias() {
            SharedPreferences preferences = getSharedPreferences("galeria", Context.MODE_MULTI_PROCESS);
            numImage = preferences.getInt("fotos", 2);
            time = preferences.getInt("tiempo", 30);
        }


        private Bitmap decodeImgen(File f){
            try {
                //Decode image size
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(f),null,o);
                final int REQUIRED_SIZE=600;
                int scale=1;
                while(o.outWidth/scale/2>=REQUIRED_SIZE && o.outHeight/scale/2>=REQUIRED_SIZE)
                    scale*=2;
                BitmapFactory.Options o2 = new BitmapFactory.Options();
                o2.inSampleSize=scale;
                return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return null;
    }


    class Hilo extends Thread{
        int c = 1;
        @Override
        public void run() {
            while (flag) {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(c == 1) {
                                RecuperarPreferencias();
                                Random aleatorio = new Random(System.currentTimeMillis());
                                int intAletorio = aleatorio.nextInt(numImage);
                                aleatorio.setSeed(System.currentTimeMillis());
                                aleatorio.setSeed(aleatorio.nextLong());
                                if (intAletorio <= 0) {
                                    intAletorio = 1;
                                }
                                rutF = ruta + "" + intAletorio + ".jpg";
                                imagen = findViewById(R.id.imagen);
                                imagen.setImageBitmap(decodeImgen(new File(rutF)));
                                c=time;
                            }
                            else{
                                c--;
                            }

                        }
                    });
                    Thread.sleep( 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}