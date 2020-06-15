package ir.aminer.potadoshack.core.utils;

import java.io.*;

public class SerializationUtils {

    public static byte[] serialize(Object o) {
        ByteArrayOutputStream byteOutputStream = null;
        try {
            byteOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteOutputStream);
            outputStream.writeObject(o);
            outputStream.flush();
            byteOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return byteOutputStream.toByteArray();
    }

    public static <T> T deserialize(byte[] data) {
        Object o = null;
        try {
            ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
            ObjectInputStream inputStream = new ObjectInputStream(byteInputStream);
            o = inputStream.readObject();
            inputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return (T) o;
    }
}
