package com.callcenter;

import com.callcenter.model.TipoEmpleados;
import javafx.util.Pair;
import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

public class CallCenterStrategy {
    private static final Logger logger = LoggerFactory.getLogger(CallCenterStrategy.class);

    public String asignarEmpleado(ConcurrentLinkedDeque<Pair<String, String>> empleadosDisponibles) {
        Validate.notNull(empleadosDisponibles);
        Optional<Pair<String, String>> operador = empleadosDisponibles.stream().filter(e -> e.getKey() == TipoEmpleados.Operador.name()).findAny();

        return operador.map((t) -> t.getValue()).orElseGet(() -> {
                   logger.info("No hay OPERADORES disponibles");
                   return empleadosDisponibles.stream().filter(e -> e.getKey() == TipoEmpleados.Supervisor.name()).findAny().map((t) -> t.getValue())
                           .orElseGet(() -> {
                               logger.info("No hay SUPERVISORES disponibles");
                               return empleadosDisponibles.stream().filter(e -> e.getKey() == TipoEmpleados.Director.name()).findAny().map((t) -> t.getValue()).orElse(null);
                            }
                       );
                }
        );

    }

}
