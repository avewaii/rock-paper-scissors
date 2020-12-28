package com.company;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Scanner;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Main {

    private static class HMAC {
        private HMAC() {
        }

        public static byte[] hmac256(String secretKey, String message) {
            try {
                return hmac256(secretKey.getBytes("UTF-8"), message.getBytes("UTF-8"));
            } catch (Exception var3) {
                throw new RuntimeException("Failed to generate HMACSHA256 encrypt", var3);
            }
        }

        public static byte[] hmac256(byte[] secretKey, byte[] message) {
            Object var2 = null;

            try {
                Mac mac = Mac.getInstance("HmacSHA256");
                SecretKeySpec sks = new SecretKeySpec(secretKey, "HmacSHA256");
                mac.init(sks);
                byte[] hmac256 = mac.doFinal(message);
                return hmac256;
            } catch (Exception var5) {
                throw new RuntimeException("Failed to generate HMACSHA256 encrypt ");
            }
        }
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        String[] array = args;
        for (int i = 0; i < array.length; i++){
            for (int j = i+1; j < array.length; j++){
                if (array[i].equals(array[j])){
                    System.out.println("Error, you entered the same words.");
                    return;} } }

        if (array.length >= 3 && array.length % 2 != 0) {
            Scanner console = new Scanner(System.in);
            SecureRandom random = SecureRandom.getInstanceStrong();
            byte[] values = new byte[16];
            random.nextBytes(values);
            StringBuilder sb = new StringBuilder();

            for(int i = 0; i < values.length; ++i) {
                byte b = values[i];
                sb.append(String.format("%01X", b));
            }
            int computerMove = (int)(Math.random() * (double)array.length);
            byte[] hmacSha256 = Main.HMAC.hmac256(sb.toString(), array[computerMove]);
            System.out.println(String.format("HMAC: %032X", new BigInteger(1, hmacSha256)));

            boolean flag = true;
            int moveSelection = 0;

            while(flag) {
                System.out.println("Available moves:");
                for(int i = 0; i < array.length; ++i) {
                    System.out.println(i + 1 + " - " + array[i]);
                }
                System.out.println("0 - exit");
                System.out.print("Select your move: ");

                moveSelection = console.nextInt();
                if (moveSelection <= array.length) {
                    flag = false;
                    if (moveSelection == 0) {
                        return;
                    }
                }
            }

            System.out.println("Yor move: " + array[moveSelection - 1]);
            System.out.println("Computer move: " + array[computerMove]);
            if (moveSelection - 1 == computerMove) {
                System.out.println("Draw!");
                System.out.println("HMAC key:" + sb.toString());
                return;
            }
                for(int j = moveSelection, i = 1; i <= (array.length - 1) / 2; ++j, ++i) {
                    if (j >= array.length) {
                        j = 0;
                    }
                    if (j == computerMove) {
                        System.out.println("You lose!");
                        System.out.println("HMAC key:" + sb.toString());
                        return;
                    }
                }
                System.out.println("You win!");
                System.out.println("HMAC key:" + sb.toString());
        }
        else {
            System.out.println("Error,please enter 3 or more odd number of words, for example:" +
                    " rock paper scissors lizard Spock");
        }
    }
}
