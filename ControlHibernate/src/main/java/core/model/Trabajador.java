package core.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trabajador")
public class Trabajador {

    public enum Rol { ADMIN, EMPLEADO }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombre;
    private String dni;

    @Enumerated(EnumType.STRING)
    private Rol rol;   // nuevo campo para el rol

    public Trabajador() {
    }

    public Trabajador(String nombre, String dni, Rol rol) {
        this.nombre = nombre;
        this.dni = dni;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return nombre;
    }
}

