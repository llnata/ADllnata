package core;

import core.model.Trabajador;

public class Sesion {

    private static Trabajador usuarioActual;

    public static Trabajador getUsuarioActual() {
        return usuarioActual;
    }

    public static void setUsuarioActual(Trabajador usuarioActual) {
        Sesion.usuarioActual = usuarioActual;
    }

    public static boolean hayUsuario() {
        return usuarioActual != null;
    }

    public static void cerrarSesion() {
        usuarioActual = null;
    }
}


