package com.callcenter.model;

import akka.actor.AbstractActor;
import javafx.util.Pair;

import java.util.concurrent.ThreadLocalRandom;

public abstract class Empleado  extends AbstractActor {

    Estado estado;

    //#mensajes
    static public class Llamada {
        public final int tiempo;
        public Llamada() {
            this.tiempo = ThreadLocalRandom.current().nextInt(5, 10 + 1);
        }
    }

    static public class Libre {
        public final String nombreActor;
        public Libre(String nombreActor) {
            this.nombreActor = nombreActor;
        }
    }

    //#mensajes

    public static Pair<String, String> crearTupla(String TipoEmpleado, String path) {
        return new Pair(TipoEmpleado, path);
    }

}
