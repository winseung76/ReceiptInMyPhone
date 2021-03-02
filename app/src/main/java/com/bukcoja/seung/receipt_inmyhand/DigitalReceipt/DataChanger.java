package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class DataChanger {

    public static byte[] hexStringToVar(String hexString){

        byte[] value=new BigInteger(hexString, 16).toByteArray();
        byte[] length=intToByteArray(value.length);
        byte[] vardata=new byte[16+value.length];

        System.arraycopy(length,0,vardata,0,16);
        System.arraycopy(value,0,vardata,16,value.length);

        return vardata;
    }
    public static byte[] hexStringTobyteArray(String hexString){

        return new BigInteger(hexString, 16).toByteArray();
    }
    public static String byteArrayToHexString(byte[] bytes){

        StringBuilder sb = new StringBuilder();

        for(byte b : bytes){
            sb.append(String.format("%02X", b&0xff));
        }

        return sb.toString();
    }
    public  static byte[] intToByteArray(int value) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte)(value >> 24);
        byteArray[1] = (byte)(value >> 16);
        byteArray[2] = (byte)(value >> 8);
        byteArray[3] = (byte)(value);

        return byteArray;
    }
    public static int byteArrayToInt(byte bytes[]) {
        return ((((int)bytes[0] & 0xff) << 24) |
                (((int)bytes[1] & 0xff) << 16) |
                (((int)bytes[2] & 0xff) << 8) |
                (((int)bytes[3] & 0xff)));
    }
    public static short byteArrayToShort(byte[] bytes) {
        short newValue = 0;
        //newValue |= (((int)bytes[0])<<8)&0xFF00;
        //newValue |= (((int)bytes[1]))&0xFF;

        newValue=(short)(((bytes[0] & 0xFF) << 8) | (bytes[1] & 0xFF));

        return newValue;
    }
    public static byte[] shortToByteArray(short a) {

        byte[] shortToByte = new byte[2];
        shortToByte[0] |= (byte)((a & 0xFF00) >>> 8);
        shortToByte[1] |= (byte)(a & 0xFF & 0xff);

        return shortToByte;
    }
    public static String byteToBinaryString(byte b){
        StringBuilder binaryStringBuilder = new StringBuilder();
        for(int i = 0; i < 8; i++)
            binaryStringBuilder.append(((0x80 >>> i) & b) == 0? '0':'1');
        return binaryStringBuilder.toString();
    }
    public static String byteArrayToBinaryString(byte[] b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < b.length; ++i) {
            sb.append(byteToBinaryString(b[i]));
        }
        return sb.toString();
    }

    public static byte[] byteCompress(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);

        ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
        deflater.finish();

        byte[] buffer = new byte[1024];

        while(!deflater.finished()) {
            int count = deflater.deflate(buffer);
            baos.write(buffer, 0, count);
        }

        byte[] b = baos.toByteArray();

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return b;
    }
    public static byte[] byteDecompress(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        byte[] buffer = new byte[1024];

        ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length);
        while(!inflater.finished()) {
            try {
                int count = inflater.inflate(buffer);
                baos.write(buffer, 0, count);
            } catch (DataFormatException e) {
                e.printStackTrace();
            }
        }

        byte[] b = baos.toByteArray();

        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return b;
    }




}
