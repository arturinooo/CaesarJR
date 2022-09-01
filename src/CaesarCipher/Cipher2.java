package CaesarCipher;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;

public class Cipher2 {

    static final String alphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ" + "абвгдеёжзийклмнопрстуфхцчшщъыьэюя" + ".,\":-!? ";

    public static void main(String[] args) {
        Scanner console = new Scanner(System.in);
        System.out.println("Выберите режим работы:\n" +
                "[Введите 1 - Зашифровка файла] [Введите 2 - Расшифровка файла] [Введите 3 - BruteForce]");
        int choice = console.nextInt();
        if (choice == 1) {
            readFromFile();
        } else if (choice == 2) {
            writeToFile();
        } else if (choice == 3) {
            bruteForce();
        } else System.out.println("Введите пожалуйста единицу, двойку или три, а не " + choice);
    }

    public static void readFromFile() {     // читаю текст из файла и записываю в массив outputBuffer
        System.out.println("Введите путь к файлу: ");
        try (Scanner scanner = new Scanner(System.in);
             BufferedReader input = new BufferedReader(new FileReader(scanner.nextLine(), StandardCharsets.UTF_8));
             BufferedWriter output = new BufferedWriter(new FileWriter("d:\\cipher.txt", StandardCharsets.UTF_8))) {
            System.out.println("Введите количество смещений: ");
            int offset = scanner.nextInt();
            char[] buffer = new char[65536];
            while (input.ready()) {
                char[] outputBuffer = new char[input.read(buffer)];
                for (int i = 0; i < outputBuffer.length; i++) {     // в массиве outputBuffer будет чистый текст без null
                    outputBuffer[i] = buffer[i];
                }
                output.write(toCipher(outputBuffer, offset));

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("На вашем диске D был создан зашифрованный файл cipher.txt");
    }

    public static void writeToFile() {
        System.out.println("Введите путь к зашифрованному файлу: ");
        try (Scanner scanner = new Scanner(System.in);
             BufferedReader input = new BufferedReader(new FileReader(scanner.nextLine(), StandardCharsets.UTF_8));
             BufferedWriter output = new BufferedWriter(new FileWriter("d:\\decipher.txt", StandardCharsets.UTF_8))) {
            System.out.println("Введите количество смещений: ");
            int offset = scanner.nextInt();
            char[] buffer = new char[65536];
            while (input.ready()) {
                char[] outputBuffer = new char[input.read(buffer)];
                for (int i = 0; i < outputBuffer.length; i++) {     // в массиве outputBuffer будет чистый текст без null
                    outputBuffer[i] = buffer[i];
                }
                output.write(deCipher(outputBuffer, offset));
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("На вашем диске D был создан расшифрованный файл decipher.txt");
    }

    public static char[] toCipher(char[] buffer, int offset) { // метод шифровки
        for (int i = 0; i < buffer.length; i++) {
            if (alphabet.contains(String.valueOf(buffer[i]))) { // проверка входящего символа на наличие его в нашем криптоалфавите
                buffer[i] = alphabet.charAt((alphabet.indexOf(buffer[i]) + offset) % alphabet.length()); // присваивание индексу массива buffer зашифрованного символа, смещенного по нашему алфавиту

            } else buffer[i] = buffer[i]; // если входяшего символа в нашем алфавите нет, то мы его не шифруем
        }
        return buffer;
    }

    public static char[] deCipher(char[] buffer, int offset) {
        return toCipher(buffer, alphabet.length() - (offset % alphabet.length()));
    }

    public static void bruteForce() {
        System.out.println("Введите путь к зашифрованному файлу: ");
        Map<Integer, String> map = new HashMap<>();
        try (Scanner scanner = new Scanner(System.in);
             BufferedReader input = new BufferedReader(new FileReader(scanner.nextLine(), StandardCharsets.UTF_8));
             BufferedWriter output = new BufferedWriter(new FileWriter("d:\\bruteForceMod.txt", StandardCharsets.UTF_8))) {
            char[] buffer = new char[65536];
            while (input.ready()) {
                char[] outputBuffer = new char[input.read(buffer)];
                for (int i = 0; i < outputBuffer.length; i++) {
                    outputBuffer[i] = buffer[i];
                }
                for (int i = 0; i < alphabet.length(); i++) {
                    String str = new String((toCipher(outputBuffer, 1)));
                    map.put(i, str);
                }
            }
            int key = 0;
            int maxSpace = 0;
            for (Map.Entry<Integer, String> m : map.entrySet()) {
                int space = 0;
                String lineFromMap = m.getValue();
                for (int i = 0; i < lineFromMap.length(); i++) {
                    if (lineFromMap.charAt(i) == ' ') {
                        space++;
                    }
                }
                if (space > maxSpace) {
                    maxSpace = space;
                    key = m.getKey();
                }
            }
            System.out.println("На вашем диске D был создан файл bruteForceMod.txt, который содержит расшифровку.");
            output.write(map.get(key) + "\n-----------------\nКЛЮЧ: " + key);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
}
