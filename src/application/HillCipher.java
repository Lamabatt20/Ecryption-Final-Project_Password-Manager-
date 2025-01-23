package application;

public class HillCipher {
    // Include all characters you want to support
    private static final String CHARSET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 !@#$%^&*()_+[]{}|;:',.<>?/`~\"\\=";
    private static final int CHARSET_SIZE = CHARSET.length();

    private static final int[][] KEY_MATRIX = {{2, 3}, {1, 3}}; // Example key matrix

    private static int modInverse(int a, int m) {
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1) {
                return x;
            }
        }
        return -1;
    }

    private static int determinant(int[][] matrix) {
        return (matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]) % CHARSET_SIZE;
    }

    private static int[][] getInverseKeyMatrix(int[][] matrix) {
        int det = determinant(matrix);
        int modInv = modInverse(det, CHARSET_SIZE);
        if (modInv == -1) {
            throw new IllegalArgumentException("Matrix determinant has no modular inverse.");
        }

        int[][] inverse = new int[2][2];
        inverse[0][0] = matrix[1][1] * modInv % CHARSET_SIZE;
        inverse[0][1] = -matrix[0][1] * modInv % CHARSET_SIZE;
        inverse[1][0] = -matrix[1][0] * modInv % CHARSET_SIZE;
        inverse[1][1] = matrix[0][0] * modInv % CHARSET_SIZE;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                inverse[i][j] = (inverse[i][j] + CHARSET_SIZE) % CHARSET_SIZE;
            }
        }

        return inverse;
    }

    public static String encrypt(String plaintext) {
        // Ensure plaintext length is even
        if (plaintext.length() % 2 != 0) {
            plaintext += plaintext.charAt(plaintext.length() - 1); // Repeat the last character
        }

        StringBuilder ciphertext = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += 2) {
            int[] plaintextVector = new int[2];
            for (int j = 0; j < 2; j++) {
                plaintextVector[j] = CHARSET.indexOf(plaintext.charAt(i + j));
                if (plaintextVector[j] == -1) {
                    throw new IllegalArgumentException("Character not in CHARSET: " + plaintext.charAt(i + j));
                }
            }

            int[] cipherVector = new int[2];
            for (int row = 0; row < 2; row++) {
                cipherVector[row] = (KEY_MATRIX[row][0] * plaintextVector[0] + KEY_MATRIX[row][1] * plaintextVector[1]) % CHARSET_SIZE;
            }

            for (int j = 0; j < 2; j++) {
                ciphertext.append(CHARSET.charAt(cipherVector[j]));
            }
        }

        return ciphertext.toString();
    }

    public static String decrypt(String ciphertext) {
        int[][] inverseKeyMatrix = getInverseKeyMatrix(KEY_MATRIX);

        StringBuilder plaintext = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += 2) {
            int[] cipherVector = new int[2];
            for (int j = 0; j < 2; j++) {
                cipherVector[j] = CHARSET.indexOf(ciphertext.charAt(i + j));
                if (cipherVector[j] == -1) {
                    throw new IllegalArgumentException("Character not in CHARSET: " + ciphertext.charAt(i + j));
                }
            }

            int[] plaintextVector = new int[2];
            for (int row = 0; row < 2; row++) {
                plaintextVector[row] = (inverseKeyMatrix[row][0] * cipherVector[0] + inverseKeyMatrix[row][1] * cipherVector[1]) % CHARSET_SIZE;
            }

            for (int j = 0; j < 2; j++) {
                plaintext.append(CHARSET.charAt(plaintextVector[j]));
            }
        }
        return plaintext.toString();
    }

    public static void main(String[] args) {
        // Test cases
        String plaintext = "password_sama_linkedin";
        System.out.println("Original Plaintext: " + plaintext);

        // Encrypt
        String encrypted = encrypt(plaintext);
        System.out.println("Encrypted Text: " + encrypted);

        // Decrypt
        String decrypted = decrypt(encrypted);
        System.out.println("Decrypted Text: " + decrypted);

        // Check if decryption matches the original plaintext
        System.out.println("Decryption Successful: " + plaintext.equals(decrypted));
    }
}
