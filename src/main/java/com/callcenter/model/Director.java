package com.callcenter.model;

import akka.actor.Props;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Director extends Empleado{

    public static Props props() {
        return Props.create(Director.class, () -> new Director());
    }

    private static final Logger logger = LoggerFactory.getLogger(Director.class);
    ExecutorService executor = Executors.newFixedThreadPool(1);


    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Llamada.class, l -> {
                    logger.info("el Director {} esta atendiendo llamada de {} minutos", getSelf().path(), l.tiempo);
                    this.estado = Estado.OCUPADO;

                    Callable<Estado> task = () -> {
                        try {
                            TimeUnit.SECONDS.sleep(l.tiempo);
                            this.estado = Estado.DISPONIBLE;
                            logger.info("Ahora estoy disponible");
                            sender().tell(new Libre(getSelf().path().name()), getSelf());
                            return Estado.DISPONIBLE;
                        }
                        catch (InterruptedException e) {
                            throw new IllegalStateException("task interrupted", e);
                        }
                    };

                    this.estado = executor.submit(task).get();
                })
                .build();
    }

}
