package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt;

import android.util.Log;

public class DigitalReceipt {

    byte[] header;
    byte[] payload;
    byte[] ndefPayload;

    public DigitalReceipt(Header header, Payload payload) {
        this.payload = payload.encoding();
        header.setPayloadLeg(this.payload.length);
        this.header = header.encoding();

        Log.e("전자 영수증 헤더 length",String.valueOf(this.header.length));
        Log.e("전자 영수증 페이로드 length",String.valueOf(this.payload.length));
    }
    public DigitalReceipt(byte[] ndefPayload){
        this.ndefPayload=ndefPayload;
    }

    public Payload getPayload(){

        byte[] payloadBytes=new byte[ndefPayload.length-Header.length];

        System.arraycopy(ndefPayload,Header.length,payloadBytes,0,ndefPayload.length-Header.length);

        return new Payload(payloadBytes);
    }

    public Header getHeader(){
        byte[] headerBytes=new byte[Header.length];

        System.arraycopy(ndefPayload,0,headerBytes,0,Header.length);

        return new Header(headerBytes);
    }

    public byte[] getBytes() {
        byte[] record = new byte[header.length + payload.length];

        System.arraycopy(header, 0, record, 0, header.length);
        System.arraycopy(payload, 0, record, header.length, payload.length);

        return record;
    }
}

