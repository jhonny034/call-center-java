package com.callcenter;

import com.callcenter.model.Empleado;
import com.callcenter.model.TipoEmpleados;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.junit.Assert.assertEquals;

public class CallCenterStrategyTest {

    @Test
    public void testOperadorDisponible() {
        ConcurrentLinkedDeque empleados = new ConcurrentLinkedDeque(
                Arrays.asList(Empleado.crearTupla(TipoEmpleados.Operador.name(), "operador1"),
                        Empleado.crearTupla(TipoEmpleados.Operador.name(), "operador3"),
                        Empleado.crearTupla(TipoEmpleados.Supervisor.name(), "supervisor1"),
                        Empleado.crearTupla(TipoEmpleados.Director.name(), "director")));

        CallCenterStrategy strategy = new CallCenterStrategy();
        String operador = strategy.asignarEmpleado(empleados);

        assertEquals(operador, "operador1");
    }

    @Test
    public void testSupervisorDisponible() {
        ConcurrentLinkedDeque empleados = new ConcurrentLinkedDeque(
                Arrays.asList(
                        Empleado.crearTupla(TipoEmpleados.Director.name(), "director"),
                        Empleado.crearTupla(TipoEmpleados.Supervisor.name(), "supervisor1")
                        ));

        CallCenterStrategy strategy = new CallCenterStrategy();
        String supervisor = strategy.asignarEmpleado(empleados);

        assertEquals(supervisor, "supervisor1");
    }

    @Test
    public void testDirectorDisponible() {
        ConcurrentLinkedDeque empleados = new ConcurrentLinkedDeque(
                Arrays.asList(
                        Empleado.crearTupla(TipoEmpleados.Director.name(), "director")
                ));

        CallCenterStrategy strategy = new CallCenterStrategy();
        String director = strategy.asignarEmpleado(empleados);

        assertEquals(director, "director");
    }
}
