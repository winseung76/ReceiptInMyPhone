package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Header {

    private byte version;  // 영수증 버전
    private byte composite_info; // 데이터 구성 정보
    private byte encoding;  // 문자 인코딩 방법
    private byte currency;  // 화폐 단위
    private int payload_length;
    private byte[] headerBytes;
    public static final int length=8;
    private final Map<Byte,Byte> map=new HashMap<Byte, Byte>() {
        {
            put((byte)1, (byte)0x02);
            put((byte)2, (byte)0x03);
            put((byte)4,(byte)0x11);
            put((byte)8, (byte)0x16);
            put((byte)16, (byte)0x21);
            put((byte)32, (byte)0x31);
        }
    };

    public Header(byte version,byte composite_info,byte encoding,byte currency){
        this.version=version;
        this.composite_info=composite_info;
        this.encoding=encoding;
        this.currency=currency;

    }
    public void setPayloadLeg(int length) {
        this.payload_length=length;
    }

    public Header(byte[] headerBytes){

        this.headerBytes=headerBytes;
        byteArrayToHeader();
    }

    public void byteArrayToHeader(){

        version=headerBytes[0];
        composite_info=headerBytes[1];
        encoding=headerBytes[2];
        currency=headerBytes[3];
        byte[] temp=new byte[4];
        System.arraycopy(headerBytes,4,temp,0,4);
        payload_length=DataChanger.byteArrayToInt(temp);

    }
    public byte[] encoding(){

        byte[] header=new byte[Header.length];

        header[0]=version;
        header[1]=composite_info;
        header[2]=encoding;
        header[3]=currency;
        System.arraycopy(DataChanger.intToByteArray(payload_length),0,header,4,4);

        return header;
    }

    public byte getComposite_info(){
        return composite_info;
    }

    public ArrayList<Byte> getValidTags(){
        byte mask=1;
        byte tag;
        ArrayList<Byte> tags=new ArrayList<>();

        for(int i=0;i<8;i++){
            if((composite_info&mask)==mask) {
                tag = map.get(mask);
                tags.add(tag);
            }
            mask*=2;
        }
        return tags;
    }
}
