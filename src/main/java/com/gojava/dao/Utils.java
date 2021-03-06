package com.gojava.dao;

import com.gojava.dao.impl.DataStorage;

import java.io.*;

/**
 * @author Vancho
 * @author Artur Roze
 * @author firsachi
 */
public final class Utils {

    private Utils() {
    }

    public static void printBorder() {
        System.out.println("-----------------------------------------------");
    }

    /**
     * This method writes static message and reads integer from console.
     * @return integer or null.
     */
    public static Integer provideIntInputStream() {
        return provideIntInputStreamWithMessage("Select choice (confirm Enter): ");
    }

    /**
     * This method writes message which was put in param and reads integer from console.
     * @param message which we show to user.
     * @return integer or null.
     */
    public static Integer provideIntInputStreamWithMessage(String message) {
        System.out.print(message);
        String line;
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            line = br.readLine();
            try {
                return Integer.valueOf(line);
            } catch (NumberFormatException e) {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method writes message which was put in param and reads string from console.
     * @param message which we show to user.
     * @return String
     */
    public static String provideStringInputStream(String message) {
        System.out.print(message);
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(System.in));
            return deleteSpaces(br.readLine());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static File checkFile(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static <T> void writeFile(String fileName, T object) {
        try {
            File file = checkFile(fileName);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T extends DataStorage> T readFile(String fileName) {
        try {
            T result;
            File file = new File(fileName);
            if (file.exists() || file.length() > 1) {

                FileInputStream fis = new FileInputStream(fileName);
                ObjectInputStream oin = new ObjectInputStream(fis);
                result = (T) oin.readObject();
                oin.close();
                return result;
            } else {
                return null;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.getMessage();
        }
        return null;
    }

    /**
     * This method validates given string.
     * @param str
     * @return true if string has passed validation
     * @return false if string has failed validation
     */
    public static boolean isValidString(String str) {
        return !(str == null || str.isEmpty() || str.split(" ").length == 0);
    }

    /**
     * This method deletes extra spaces from given string
     * @param str String with extra spaces
     * @return String without extra spaces
     */
    public static String deleteSpaces(String str) {

        String newString = "";

        final String[] split = str.split(" ");

        for (int i = 0; i < split.length; i++) {
            if (isValidString(split[i])) {
                newString += split[i];
                if (i < split.length - 1)
                    newString += " ";
            }
        }
        return newString;
    }
}
