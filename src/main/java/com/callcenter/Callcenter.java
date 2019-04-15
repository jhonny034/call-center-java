package com.callcenter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import com.callcenter.model.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.IntStream;

public class Callcenter {

    public static void main(String[] args) {

        final ActorSystem system = ActorSystem.create("callcenter-system");

        try {
            final ActorRef operador1 = system.actorOf(Operador.props(), "operador1");
            final ActorRef operador2 = system.actorOf(Operador.props(), "operador2");
            final ActorRef operador3 = system.actorOf(Operador.props(), "operador3");
            final ActorRef supervisor1 = system.actorOf(Supervisor.props(), "supervisor1");
            final ActorRef supervisor2 = system.actorOf(Supervisor.props(), "supervisor2");
            final ActorRef director = system.actorOf(Director.props(), "director1");


            ConcurrentLinkedDeque listaEmpleados = new ConcurrentLinkedDeque(
                    Arrays.asList(Empleado.crearTupla(TipoEmpleados.Operador.name(), operador1.path().toString()),
                            Empleado.crearTupla(TipoEmpleados.Operador.name(), operador2.path().toString()),
                            Empleado.crearTupla(TipoEmpleados.Operador.name(), operador3.path().toString()),
                            Empleado.crearTupla(TipoEmpleados.Supervisor.name(), supervisor1.path().toString()),
                            Empleado.crearTupla(TipoEmpleados.Supervisor.name(), supervisor2.path().toString()),
                            Empleado.crearTupla(TipoEmpleados.Director.name(), director.path().toString())));

            final ActorRef dispatcher = system.actorOf(Dispatcher.props(listaEmpleados), "dispatcher");

            IntStream.range(1, 16).forEach(duracion -> {
                    dispatcher.tell(new Empleado.Llamada(), ActorRef.noSender());
                }
            );

            System.out.println(">>> Press ENTER to exit <<<");
            System.in.read();
        } catch (IOException ioe) {
        } finally {
            system.terminate();
        }
    }
}
