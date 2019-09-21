package com.example.bgom.aplicacionfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity {

   // Button btnRegistrar,btnLogin; ya no se inicializan por que se asigno el onclick desde el layout
    EditText txtUser,txtPass;
    String user, password;

    Button btnIr, btnMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUser=findViewById(R.id.txtUserMain);
        txtPass=findViewById(R.id.txtPasswordMain);

        btnMaps=findViewById(R.id.btnMaps);
        btnIr=findViewById(R.id.btnIr);
        btnIr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(intent);
            }
        });
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(),MapsActivity.class);
                startActivity(intent);
            }
        });


    }
//llamar el metodo a travez del boton
    public void login(View view) {
        user=txtUser.getText().toString();
        password=txtPass.getText().toString();
        //validacion de datos de la interfaza (usuario y password)
        //regresa un booleano
        if(TextUtils.isEmpty(user)){
            txtUser.setError("Usuario vacio");
            txtUser.setFocusable(true);
            //para que se detenga
            return;

        }
        if(TextUtils.isEmpty(password)){
            txtPass.setError("Password vacio");
            txtPass.setFocusable(true);
            //para que se detenga
            return;

        }
        //ejecuta el metodo
        new  LoginRest().execute(user,password);

    }
    public  void add(View view){
        Intent intent=new Intent(getApplicationContext(),RegistroActivity.class);
        startActivity(intent);

    }

    //clase asincrona(AsyncTask  es como abrir un hilo para la comunicacion asincrona
    class LoginRest extends AsyncTask<String,Integer,String>{

        //variable de peticion de conexión
        URLConnection connection=null;
        //variable para el resultado
        String result="0";

        //hace las tareas en segundo plano
        @Override
        ///... SIGNIFICA que es un arreglo de una dimencion
        protected String doInBackground(String... strings) {

            //hacer peticion
            try {
                //abrir conexion
                connection=new URL("http://172.18.26.67/cursoAndroid/vista/Usuario/iniciarSesion.php?usuario="+strings[0]+"&password="+strings[1]).openConnection();

//trae el set de datos para analizarlos depues
                InputStream inputStream=(InputStream) connection.getContent();
                //tamaño del set de datos
                byte[]buffer=new byte[10000];
                //inputStream.read(buffer);//100000000000000000
                //para inducar cuantos son realmente datos dentro de la cadena de respuestas
                int size=inputStream.read(buffer);
                //dar el resultado
                result=new String(buffer,0,size);
                //imprimir en consola el resultado
                Log.i("result ",result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }
//hace el tratamieto de los datos
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //cuando se comparan cadenas
            if(s.equals("1")){
                Intent intent=new Intent(MainActivity.this,MenuActivity.class);
                startActivity(intent);
            }else{
                Toast.makeText(MainActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();


            }

        }
    }
}
