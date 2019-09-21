package com.example.bgom.aplicacionfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

import modelos.Tienda;

public class FormularioTienda extends AppCompatActivity implements View.OnClickListener {

    EditText txtId, txtNombre,txtDescripcion,txtDireccion,txtLatitud,txtLongitud;
    Button btnSaveForm,btnCancelForm;
    Tienda storeIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario_tienda);

        txtId=findViewById(R.id.txtIdForTienda);
        txtNombre=findViewById(R.id.txtNombreForTienda);
        txtDescripcion=findViewById(R.id.txtDescripcionFormTienda);
        txtDireccion=findViewById(R.id.txtDireccionForTienda);
        txtLatitud=findViewById(R.id.txtLatitudFormTienda);
        txtLongitud=findViewById(R.id.txtLongitudFormTienda);



        btnCancelForm=findViewById(R.id.btnCancel);
        btnSaveForm=findViewById(R.id.btnSave);

        btnSaveForm.setOnClickListener(this);
        btnCancelForm.setOnClickListener(this);
//para que no se escriba en el id
        txtId.setEnabled(false);



        storeIntent=(Tienda)getIntent().getExtras().getSerializable("myObj");


        //las comillas convierten un entero a un string
        txtId.setText(storeIntent.getId()+"");
        txtNombre.setText(storeIntent.getNombre());
        txtDescripcion.setText(storeIntent.getDescripcion());
        txtDireccion.setText(storeIntent.getDireccion());
        txtLatitud.setText(storeIntent.getLatitud()+"");
        txtLongitud.setText(storeIntent.getLongitud()+"");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSave:
                //crear ojeto que traiga los dato para mandarlo a la asincrona

                Tienda myStore=new Tienda();
                myStore.setId(Integer.parseInt(
                        txtId.getText().toString().trim()
                ));
                myStore.setNombre(txtNombre.getText().toString().trim());
                myStore.setDireccion(txtDireccion.getText().toString().trim());
                myStore.setDescripcion(txtDescripcion.getText().toString().trim());
                myStore.setLatitud(
                        Double.parseDouble(
                                txtLatitud.getText().toString().trim()
                        ));
                myStore.setLongitud(
                        Double.parseDouble(
                                txtLongitud.getText().toString().trim()
                        ));
                new UpdateStore().execute(myStore);
                Intent intent=new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(intent);

                finish();
                break;
            case R.id.btnCancel:
                Intent intent2=new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(intent2);

                finish();
                break;
        }
    }

    //definir subclase asincrona

    class UpdateStore extends AsyncTask<Tienda,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Tienda... tiendas) {
            String params="nombre="+tiendas[0].getNombre()+"&"+
                    "direccion="+tiendas[0].getDireccion()+"&"+
                    "latitud="+tiendas[0].getLatitud()+"&"+
                    "longitud="+tiendas[0].getLongitud()+"&"+
                    "descripcion="+tiendas[0].getDescripcion()+"&"+
                    "idtienda="+tiendas[0].getId();

            try {
                URL url=new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/modificarTienda.php");

                HttpURLConnection connection=(HttpURLConnection)url.openConnection();

                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                OutputStream outputStream=connection.getOutputStream();

                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                writer.write(params);
                writer.flush();
                writer.close();
                outputStream.close();
                connection.connect();
                int responseCode=connection.getResponseCode();
                if(responseCode==HttpURLConnection.HTTP_OK){

                    return true;
                }else {
                    return false;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(FormularioTienda.this,"Tienda actualizada con exito",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(FormularioTienda.this,"No se pudo actualizar, intenta nuevamente",Toast.LENGTH_SHORT).show();
            }
        }
    }
















}
