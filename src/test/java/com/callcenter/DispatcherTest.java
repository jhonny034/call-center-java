package com.callcenter;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import com.callcenter.model.*;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;


public class DispatcherTest {

    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }

    @Test
    public  void testDispatcher() throws InterruptedException {
        final ActorSystem system = ActorSystem.create("callcenter-system");

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

        TimeUnit.SECONDS.sleep(100);

    }
}
