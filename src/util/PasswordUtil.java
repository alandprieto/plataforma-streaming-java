package util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Utilidades para el hashing y verificación de contraseñas.
 * Usa PBKDF2 con HMAC-SHA256, salt aleatorio y 100.000 iteraciones.
 * Formato almacenado: iteraciones$salt(base64)$hash(base64)
 */
public final class PasswordUtil {

    private static final int ITERATIONS = 100_000;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT_LENGTH_BYTES = 16;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final String SEPARATOR = "$";

    private PasswordUtil() {
    }

    /**
     * Genera un hash seguro de la contraseña con salt aleatorio.
     */
    public static String hash(String contrasena) {
        if (contrasena == null || contrasena.isEmpty()) {
            throw new IllegalArgumentException("La contraseña no puede ser vacía.");
        }
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        new SecureRandom().nextBytes(salt);
        byte[] hash = derivar(contrasena, salt, ITERATIONS);
        return ITERATIONS + SEPARATOR
                + Base64.getEncoder().encodeToString(salt) + SEPARATOR
                + Base64.getEncoder().encodeToString(hash);
    }

    /**
     * Verifica una contraseña contra un valor almacenado (hash o legado).
     */
    public static boolean verificar(String contrasena, String almacenada) {
        if (contrasena == null || almacenada == null) {
            return false;
        }
        try {
            String[] partes = almacenada.split("\\$");
            if (partes.length != 3) {
                return false;
            }
            int iteraciones = Integer.parseInt(partes[0]);
            byte[] salt = Base64.getDecoder().decode(partes[1]);
            byte[] hash = Base64.getDecoder().decode(partes[2]);
            byte[] calculado = derivar(contrasena, salt, iteraciones);
            return comparacionSegura(hash, calculado);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Indica si un valor almacenado tiene formato de hash (true) o es
     * una contraseña en texto plano de migración (false).
     */
    public static boolean esHash(String almacenada) {
        return almacenada != null && almacenada.split("\\$").length == 3;
    }

    private static byte[] derivar(String contrasena, byte[] salt, int iteraciones) {
        try {
            PBEKeySpec spec = new PBEKeySpec(contrasena.toCharArray(), salt, iteraciones, KEY_LENGTH_BITS);
            SecretKeyFactory factory = SecretKeyFactory.getInstance(ALGORITHM);
            return factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new IllegalStateException("No se pudo derivar el hash de la contraseña.", e);
        }
    }

    private static boolean comparacionSegura(byte[] a, byte[] b) {
        if (a == null || b == null || a.length != b.length) {
            return false;
        }
        int result = 0;
        for (int i = 0; i < a.length; i++) {
            result |= a[i] ^ b[i];
        }
        return result == 0;
    }
}