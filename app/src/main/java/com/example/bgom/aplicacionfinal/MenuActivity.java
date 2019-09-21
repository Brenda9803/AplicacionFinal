package com.example.bgom.aplicacionfinal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import modelos.Tienda;

public class MenuActivity extends AppCompatActivity {
    ListView lst;
    MyAdapterStore myAdapterStore;
    ArrayList<Tienda>arrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        lst=findViewById(R.id.lstTiendas);
        //definir array list
       /*/ arrayList=new ArrayList<Tienda>();
        //agregar registros a mano
        arrayList.add(new Tienda("tiendaUno","descrip1"));
        arrayList.add(new Tienda("tiendaDos","descrip2"));
        myAdapterStore=new MyAdapterStore(this,arrayList);
        lst.setAdapter(myAdapterStore);/*/
       //llamar el metodo asincrono

        //registra el elemento que tenga el menu contextual
        registerForContextMenu(lst);
       new ConsultarTiendas().execute();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_context,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();

//en el arraylisti hay objetos le decimos que traer
        switch (item.getItemId()){
            case R.id.itemContextUpdate:
                Toast.makeText(getApplicationContext(),"Actualizar: "+arrayList.get(info.position).getId(),Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),FormularioTienda.class);
                //para mandar todos los objetos
                Tienda tienda=new Tienda();
                tienda.setId(arrayList.get(info.position).getId());
                tienda.setNombre(arrayList.get(info.position).getNombre());
                tienda.setDireccion(arrayList.get(info.position).getDireccion());
                tienda.setDescripcion(arrayList.get(info.position).getDescripcion());
                tienda.setLatitud(arrayList.get(info.position).getLatitud());
                tienda.setLongitud(arrayList.get(info.position).getLongitud());
                intent.putExtra("myObj",tienda);
                startActivity(intent);
                return  true;


            case R.id.itemContextDelete:
                Toast.makeText(getApplicationContext(),"Eliminar",Toast.LENGTH_SHORT).show();
                Tienda store1=new Tienda();
                store1.setId(arrayList.get(info.position).getId());
                //instancia de la clase asincriona
                new DeleteStore().execute(store1);

                return true;

            case R.id.itemContextNotify:
                //notificacion

                int nNotificationId=1;
                String channelID="my_Channel_01";
                //contructor de notificacion
                NotificationCompat.Builder noti= new NotificationCompat.Builder(getApplicationContext(),null); //o channelID
//gestionar como y cuando va a aparecer la notificacion
                NotificationManager nm=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                Intent intent1=new Intent(getApplicationContext(),MenuActivity.class);
                //sirve para c uando la actividad no esta ejecutando si no que lo madas llamar ded¿se otro lado
                PendingIntent pendingIntent=PendingIntent.getActivity(
                        MenuActivity.this,0,intent1,0
                );

                //configurar notificaciones para versiones igual o superiores a android oreo
                //SDK REGRESA LA VERCION DE SO
                if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
                    CharSequence name="myName";
                    String descripcion="MyDescrip";
                    int importance=NotificationManager.IMPORTANCE_HIGH;
                    NotificationChannel nChannel=new NotificationChannel(channelID,name,importance);
                    nChannel.setDescription(descripcion);
                    nChannel.enableLights(true);
                    nChannel.setLightColor(Color.BLUE);
                    nChannel.enableVibration(true);
                    nChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300});
                    //agarre el canal
                    nm.createNotificationChannel(nChannel);
                    noti=new NotificationCompat.Builder(getApplicationContext(),channelID);

                }
                noti.setSmallIcon(R.drawable.ic_launcher_background).setContentTitle("myTitle").setContentText("myText").setContentIntent(pendingIntent);

                noti.setChannelId(channelID);

                nm.notify(nNotificationId,noti.build());


                return true;

                default:
                    return super.onContextItemSelected(item);
        }

    }







    //parametro de la consulta
    //progreso de la tarea
    // el resultado de los datos

    class ConsultarTiendas extends AsyncTask<Void,Integer, JSONArray>{

        @Override
        protected JSONArray doInBackground(Void... voids) {

            URLConnection connection=null;
            //variable para el resultado

            JSONArray jsonArray=null;
            //abrir conexion

            try {
                connection=new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/obtenerTiendas.php").openConnection();
                InputStream inputStream=(InputStream) connection.getContent();
                //numero de cabidades del arreglo para guardar datos
                byte[] buffer=new byte[10000];

                //para inducar cuantos son realmente datos dentro de la cadena de respuestas
                //va a leer la cantidad que trae el input stream
                int size=inputStream.read(buffer);
                jsonArray=new JSONArray((new String(buffer,0,size)));

                //tambien se puede englobar en Exeption e
                //hay errores y ecepciones los errores no se solucionan
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonArray;
        }
        //procesar info


        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            //el json tiene arreglos [] --- adentro {} objetos
            //para sacar los objetos del json
            Tienda myTienda;
           arrayList=new ArrayList<Tienda>();


            for (int i=0;i<jsonArray.length();i++){
                try {
                    JSONObject jsonObjetos=jsonArray.getJSONObject(i);
                    //cada valor de i es un objeto
                    myTienda=new Tienda(jsonObjetos.getInt("idtienda"),
                            jsonObjetos.getString("nombre"),
                            jsonObjetos.getString("direccion"),
                            jsonObjetos.getDouble("latitud"),
                            jsonObjetos.getDouble("longitud"),
                            jsonObjetos.getString("descripcion"));
                    arrayList.add(myTienda);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            myAdapterStore=new MyAdapterStore(MenuActivity.this,arrayList);
            //llenar el list view
            lst.setAdapter(myAdapterStore);

        }
    }




    class DeleteStore extends AsyncTask<Tienda,Integer,Boolean>{

        @Override
        protected Boolean doInBackground(Tienda... tiendas) {


            String params="idtienda="+tiendas[0].getId();

            try {
                URL url=new URL("http://172.18.26.67/cursoAndroid/vista/Tienda/eliminarTienda.php");

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
                Toast.makeText(MenuActivity.this,"Tienda eliminada con exito",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(MenuActivity.this,"No se pudo eliminar, intenta nuevamente",Toast.LENGTH_SHORT).show();
            }
        }
    }

}
