package com.example.bgom.aplicacionfinal;

        import androidx.appcompat.app.AppCompatActivity;

        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.util.Log;
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

        import modelos.Usuario;

public class RegistroActivity extends AppCompatActivity {

    EditText txtUser, txtName,txtMail,txtAddress, txtPassword;
    Button btnAdd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        txtUser=findViewById(R.id.txtUsuario);
        txtName=findViewById(R.id.txtNombre);
        txtMail=findViewById(R.id.txtCorreo);
        txtAddress=findViewById(R.id.txtDireccion);
        txtPassword=findViewById(R.id.txtPassword);

        btnAdd=findViewById(R.id.btnAddRegistro);
        //validar campos y
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mandar llamar la validacion de datos
                if(
                        ValidarDatos(txtUser.getText().toString().trim(),
                                txtName.getText().toString().trim(),
                                txtMail.getText().toString().trim(),
                                txtAddress.getText().toString().trim(),
                                txtPassword.getText().toString())
                ){
                    //crear el objeto de tipo usuario y llenarlo con los datos
                    Usuario usuario=new Usuario();
                    usuario.setNickname(txtUser.getText().toString().trim());
                    usuario.setNombre(txtName.getText().toString().trim());
                    usuario.setCorreo(txtMail.getText().toString().trim());
                    usuario.setDireccion(txtAddress.getText().toString().trim());
                    usuario.setPassword(txtPassword.getText().toString());

                    //crear la instancia de la subclase asyncTask para realizar cONEXION
                    new AddUser().execute(usuario);


                }

            }
        });
    }

    public Boolean ValidarDatos(String nickname,String nombre,String correo, String direccion, String password){
        //validar que los datos no esten vacios
        if(nombre.isEmpty()){
            txtName.setError("Campo vacio");
            txtName.setFocusable(true);
            return false;
        }
        if(nickname.isEmpty()){
            txtUser.setError("Campo vacio");
            txtUser.setFocusable(true);
            return false;
        }
        if(correo.isEmpty()){
            txtMail.setError("Campo vacio");
            txtMail.setFocusable(true);
            return false;
        }
        if(direccion.isEmpty()){
            txtAddress.setError("Campo vacio");
            txtAddress.setFocusable(true);
            return false;
        }
        if(password.isEmpty()){
            txtPassword.setError("Campo vacio");
            txtPassword.setFocusable(true);
            return false;
        }
        //crear objeto con los valores
        return true;

    }


    //se crea un hilo mediante una subclase para  realizar la comunicacion
    //crea un proceso en un hilo distinto al principal (estas asynTask en caso de que colapsen no cierran aplicacion)
    class  AddUser extends AsyncTask<Usuario,Integer,Boolean>{
        //implementacion del metodo que ejecuta las operaciones
        @Override
        protected Boolean doInBackground(Usuario... usuarios) {
            //preparar los datos de la insersión
            //almacena la cadena completa de los datos
            String params="";
            params="user="+usuarios[0].getNickname()+"&"+
                    "nombre="+usuarios[0].getNombre()+"&"+
                    "correo="+usuarios[0].getCorreo()+"&"+
                    "direccion="+usuarios[0].getDireccion()+"&"+
                    "password="+usuarios[0].getPassword();

            //preparar conexion
            try {
                URL url=new URL("http://172.18.26.67/cursoAndroid/vista/Usuario/crearUsuario.php");
                //abrir conexion
                //se puede hacer cateo xq el padre puede heredar propiedades
                HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                //cambiar la forma de  llevar la info por el metodo POST y no GET
                connection.setRequestMethod("POST");
                //para que le indique a la URL que puede llevar datos de entrada o salida
                connection.setDoInput(true);
                connection.setDoOutput(true);
                //va a llevar el valor de los elementos que queremos escribir
                OutputStream outputStream=connection.getOutputStream();
                BufferedWriter writer=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));


                //escribe los parametros
                writer.write(params);
                //limpia los datos
                writer.flush();
                //cierra conexion
                writer.close();
                outputStream.close();
                //usar la conexion
                connection.connect();
                int responseCode= connection.getResponseCode();
                //validar
                if (responseCode==HttpURLConnection.HTTP_OK){

                    Log.i("AddUser","usuario agregado con éxito");
                    return  true;
                }else{
                    return  false;
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return false;
        }
        //CONEXION ENTRE UN HILO PRINCIPAL Y ASINCROnO aqui si se puede enviar mensajes
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if(aBoolean){
                Toast.makeText(RegistroActivity.this,"Usuario agregado con exito",Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(RegistroActivity.this,"Usuario no agregado, intenta nuevamente!",Toast.LENGTH_SHORT).show();

            }
        }
    }



}
