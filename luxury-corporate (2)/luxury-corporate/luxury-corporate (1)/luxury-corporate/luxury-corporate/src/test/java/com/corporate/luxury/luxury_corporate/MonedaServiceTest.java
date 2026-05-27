package com.corporate.luxury.luxury_corporate.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class MonedaServiceTest {

    private MonedaService monedaService;

    @BeforeEach
    public void setUp() {
        monedaService = new MonedaService();
    }

    @Test
    public void testConvertirSolesADolares() {
        BigDecimal montoOrigen = new BigDecimal("37.50");
        BigDecimal resultadoEsperado = new BigDecimal("10.00");

        BigDecimal resultadoReal = monedaService.convertir(montoOrigen, "PEN", "USD");

        assertEquals(resultadoEsperado, resultadoReal, "La conversion de Soles a Dolares fallo");
    }

    @Test
    public void testConvertirDolaresAEuros() {
        BigDecimal montoOrigen = new BigDecimal("10.00");
        BigDecimal resultadoEsperado = new BigDecimal("9.26");

        BigDecimal resultadoReal = monedaService.convertir(montoOrigen, "USD", "EUR");

        assertEquals(resultadoEsperado, resultadoReal, "La conversión cruzada de Dólares a Euros no es exacta");
    }

    @Test
    public void testMonedaNoSoportadaLanzaExcepcion() {
        BigDecimal monto = new BigDecimal("100.00");

        // se lanzan una excepción si mandan una moneda que no existe 
        assertThrows(IllegalArgumentException.class, () -> {
            monedaService.convertir(monto, "PEN", "COP");
        }, "Se esperaba una excepción por moneda no soportada");
    }
}