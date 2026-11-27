package com.experto.seguridad.util;

public final class IdentificacionEcuadorValidator {

    private IdentificacionEcuadorValidator() {
        // Constructor privado para evitar instanciación
    }

    public static void validar(String tipo, String numero) {
        if (numero == null || numero.isBlank()) {
            throw new IllegalArgumentException("El número de identificación es obligatorio.");
        }

        switch (tipo) {
            case "C" -> {
                if (!esCedulaValida(numero)) {
                    throw new IllegalArgumentException("El número de cédula ecuatoriana no es válido.");
                }
            }
            case "R" -> {
                if (!esRucValido(numero)) {
                    throw new IllegalArgumentException("El número de RUC ecuatoriano no es válido.");
                }
            }
            case "P" -> {
                if (!numero.matches("^[A-Za-z0-9]{5,15}$")) {
                    throw new IllegalArgumentException("El pasaporte debe tener entre 5 y 15 caracteres alfanuméricos.");
                }
            }
            default -> throw new IllegalArgumentException("Tipo de identificación no soportado.");
        }
    }

    public static boolean esCedulaValida(String cedula) {
        if (cedula == null || !cedula.matches("^\\d{10}$")) {
            return false;
        }
        int provincia = Integer.parseInt(cedula.substring(0, 2));
        if ((provincia < 1 || provincia > 24) && provincia != 30) {
            return false;
        }
        int tercerDigito = Character.getNumericValue(cedula.charAt(2));
        if (tercerDigito >= 6) {
            return false;
        }

        int[] coeficientes = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;
        for (int i = 0; i < 9; i++) {
            int valor = Character.getNumericValue(cedula.charAt(i)) * coeficientes[i];
            suma += (valor >= 10) ? (valor - 9) : valor;
        }
        int digitoVerificador = Character.getNumericValue(cedula.charAt(9));
        int decenaSuperior = ((suma + 9) / 10) * 10;
        int resultado = decenaSuperior - suma;
        if (resultado == 10) resultado = 0;

        return resultado == digitoVerificador;
    }

    public static boolean esRucValido(String ruc) {
        if (ruc == null || !ruc.matches("^\\d{13}$") || !ruc.endsWith("001")) {
            return false;
        }
        int tercerDigito = Character.getNumericValue(ruc.charAt(2));
        // Persona natural: los primeros 10 dígitos son una cédula válida
        if (tercerDigito < 6) {
            return esCedulaValida(ruc.substring(0, 10));
        }
        // Sociedades privadas (tercer dígito = 9) o públicas (tercer dígito = 6)
        return tercerDigito == 6 || tercerDigito == 9;
    }
}
