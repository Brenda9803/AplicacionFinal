package modelos;
//se pone el nombre regularmente  de el objeto al que se hace referencia
public class Usuario {
    //declaracion de propiedades de la clase
    private int id;
    private String nickname;
    private String nombre;
    private String password;
    private String correo;
    private String direccion;
    //uno vacio por que en ocaciones no se conocen todos los datos
    public Usuario() {
    }
    //con todos los parametros para hacer consultas
    public Usuario(int id, String nickname, String nombre, String password, String correo, String direccion) {
        this.id = id;
        this.nickname = nickname;
        this.nombre = nombre;
        this.password = password;
        this.correo = correo;
        this.direccion = direccion;
    }
    //sin id para hacer registro
    public Usuario(String nickname, String nombre, String password, String correo, String direccion) {
        this.nickname = nickname;
        this.nombre = nombre;
        this.password = password;
        this.correo = correo;
        this.direccion = direccion;
    }
    //generar los accesos a los datos
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
