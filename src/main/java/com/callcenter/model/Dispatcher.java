package com.callcenter.model;

import akka.actor.AbstractActor;
import akka.actor.Props;
import com.callcenter.CallCenterStrategy;
import javafx.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class Dispatcher extends AbstractActor {

    private static final Logger logger = LoggerFactory.getLogger(Dispatcher.class);
    private ConcurrentLinkedDeque<Pair<String, String>> empleadosDisponibles;
    private CallCenterStrategy estategiaSeleccion;

    static public Props props(ConcurrentLinkedDeque<Pair<String, String>> empleadosDisponibles) {
        return Props.create(Dispatcher.class, () -> new Dispatcher(empleadosDisponibles));
    }


    public Dispatcher(ConcurrentLinkedDeque<Pair<String, String>> empleadosdisponibles) {
        this.empleadosDisponibles = empleadosdisponibles;
        estategiaSeleccion = new CallCenterStrategy();
    }


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Empleado.Libre.class, l -> {
                    empleadosDisponibles.add(new Pair(Empleado.Libre.class, l.nombreActor));
                    logger.info("Actor {} ahora disponible para atender llamadas", l.nombreActor);
                })
                .match(Empleado.Llamada.class, l -> {
                    // TimeUnit.SECONDS.sleep(5);
                    String empleado = estategiaSeleccion.asignarEmpleado(empleadosDisponibles);
                    Optional<String> empleadoDesignado = Optional.ofNullable(empleado);

                    if(empleadoDesignado.isPresent()) {
                        empleadosDisponibles = new ConcurrentLinkedDeque(empleadosDisponibles.stream().filter((e) -> e.getValue() != empleadoDesignado.get()).collect(Collectors.toList()));
                        logger.info("enviando llamada a el Operador {}", empleadoDesignado.get());
                        getContext().getSystem().actorSelection(empleadoDesignado.get()).tell(l, self());
                    } else {
                        getContext().getSystem().deadLetters().tell(l, self());
                    }
                })
                .build();
    }
}
