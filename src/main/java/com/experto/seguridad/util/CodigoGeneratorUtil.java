package com.experto.seguridad.util;

import java.security.SecureRandom;

public final class CodigoGeneratorUtil {

    private static final SecureRandom RANDOM = new SecureRandom();
    private static final String ALFANUMERICO = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";

    private CodigoGeneratorUtil() {
    }

    public static int generarNumerico(int longitud) {
        if (longitud <= 0 || longitud > 9) {
            throw new IllegalArgumentException("La longitud debe estar entre 1 y 9 dígitos.");
        }
        int min = (int) Math.pow(10, longitud - 1);
        int max = (int) Math.pow(10, longitud) - 1;
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static String generarClaveTemporal(int longitud) {
        StringBuilder sb = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            sb.append(ALFANUMERICO.charAt(RANDOM.nextInt(ALFANUMERICO.length())));
        }
        return sb.toString();
    }
}