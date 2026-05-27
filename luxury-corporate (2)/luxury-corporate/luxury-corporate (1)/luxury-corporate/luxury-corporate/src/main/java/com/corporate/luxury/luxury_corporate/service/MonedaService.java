package com.corporate.luxury.luxury_corporate.service;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
public class MonedaService {

    private static final Map<String, BigDecimal> TASAS = new HashMap<>();

    static {
        TASAS.put("PEN", BigDecimal.ONE);
        TASAS.put("USD", new BigDecimal("3.75")); // 1 Dolar = 3.75 Soles
        TASAS.put("EUR", new BigDecimal("4.05")); // 1 Euro = 4.05 Soles y 1 dólar = 3.75 Soles
    }


    public BigDecimal convertir(BigDecimal monto, String monedaOrigen, String monedaDestino) {
        if (monto == null || !TASAS.containsKey(monedaOrigen) || !TASAS.containsKey(monedaDestino)) {
            throw new IllegalArgumentException("Moneda no soportada o monto inválido");
        }

        // convirtiendo el monto a Soles (moneda base)
        BigDecimal montoEnSoles = monto.multiply(TASAS.get(monedaOrigen));

        // convertir de soles a la moneda de destino
        return montoEnSoles.divide(TASAS.get(monedaDestino), 2, RoundingMode.HALF_UP);
    }
}