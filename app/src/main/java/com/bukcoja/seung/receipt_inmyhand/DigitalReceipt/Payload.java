package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt;

import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.BasicInfo;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.ReceiptItem;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Tag0x11;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Tag0x21;

import java.util.ArrayList;

public class Payload {

    public static final int TAG_SIZE=1;
    public static final int LENGTH_SIZE=2;
    private byte[] payloadBytes;
    private ArrayList<ReceiptItem> items=new ArrayList<>();
    private BasicInfo basicInfo;
    private Tag0x11 tag0x11;
    private Tag0x21 tag0x21;
    private ArrayList<byte[]> list=new ArrayList<>();
    private byte[] payload;
    //private int payload_length=0;
    private byte composite_info;  // 어떤 태그들이 사용되는지를 header에게 전달하기 위한 변수


    public Payload(){

    }
    public Payload(byte[] payloadBytes){

        this.payloadBytes=payloadBytes;
        //this.payload_length=0;
    }
    public byte[] createTLV(byte type,byte[] value){

        // length
        //byte[] reqLength = toBytes(value.length);
        int length=0;

        // 0xff와 & 연산을 하는 이유 : byte에서 맨 앞의 비트가 부호 비트이기 때문에 범위가 127까지밖에 표현되지 못하기 때문에
        // 그 이상의 값은 음수가 나옴 -> 그래서 0xff와 &연산을 한다.
        switch(type&0xff){
            case 0x01:
            case 0x11:
            case 0x16:
            case 0x31:
            case 0x32:
            case 0x51:
            case 0x61:
            case 0x91:
                length=16;
                break;
        }
        // 전체 전송값
        byte[] req = new byte[value.length + 8+16];
        // 0~7까지는 타입을 입력
        System.arraycopy(type, 0, req, 0, 8);
        // 7부터 16비트는 length를 입력
        // 16비트인 이유는 short이기 때문에.
        //System.arraycopy(reqLength, 0, req, 4, 16);
        // 나머지는 value를 입력한다.
        System.arraycopy(value, 0, req, 24, value.length);

        list.add(req);

        return req;

    }

    public byte getCompositeInfo(){

        return (byte)0x10;
    }

    public ArrayList<byte[]> getList(){
        return this.list;
    }

    public byte[] encoding(){

        //payload=new byte[payload_length];
        int index=0;
        int total_index=0;
        byte[][] bytearr=new byte[items.size()][];

        //System.out.println("items.size() : "+items.size());
        for(int i=0;i<items.size();i++){
            ReceiptItem item=items.get(i);
            byte[] data=item.encode();
            //byte[] data=DataChanger.byteCompress(item.encode());
            byte[] temp=new byte[3+data.length];
            index=0;
            // TAG 정보 넣음
            temp[index++]=item.getTag();

            // Length 정보 넣음
            System.arraycopy(DataChanger.shortToByteArray((short)data.length),0,temp,index,LENGTH_SIZE);
            index+=LENGTH_SIZE;

            // Value 정보 넣음
            //byte[] data=item.encode();
            //byte[] compressed= DataChanger.byteCompress(data);
            //System.arraycopy(data,0,payload,index,data.length);
            //System.arraycopy(data,0,payload,index,data.length);
            System.arraycopy(data,0,temp,index,data.length);
            Log.e("item byte size",String.valueOf(3+data.length));
            //index+=item.getLength();
            //index+=data.length;

            //total_index+=3+data.length;
            bytearr[i]=temp;
            //System.arraycopy(temp,0,payload,total_index,temp.length);
        }
        int payloadLeg=0;
        for(int i=0;i<items.size();i++){
            payloadLeg+=bytearr[i].length;
        }
        payload=new byte[payloadLeg];
        total_index=0;
        for(int i=0;i<items.size();i++){
            System.arraycopy(bytearr[i],0,payload,total_index,bytearr[i].length);
            total_index+=bytearr[i].length;
        }

        return payload;
    }

    public void addItem(ReceiptItem item){
        items.add(item);
        //payload_length+=TAG_SIZE+LENGTH_SIZE+item.getLength();
    }

    // 태그를 찾으면 해당 위치를 반환
    // 태그위치서부터 디코딩하기 시작하므로
    public int findStartPos(byte tag){

        int index=0;

        while(index<payloadBytes.length){
            if(payloadBytes[index]==tag)
                return index;

            System.out.println("payloadBytes[index] : "+payloadBytes[index]);
            index++;

            // 그 다음 2byte로부터 페이로드의 길이 탐색
            byte[] lengthBytes=new byte[LENGTH_SIZE];
            System.arraycopy(payloadBytes,index,lengthBytes,0,LENGTH_SIZE);
            index+= LENGTH_SIZE;

            // 페이로드의 길이만큼 index에 증가시켜서 건너뛴다
            int length=(int)DataChanger.byteArrayToShort(lengthBytes);
            System.out.println("length : "+length);

            index+=length;

        }
        return -1;
    }

    // 태그 정보로부터 해당 태그의 value값을 페이로드에서 찾아서 반환하는 메소드
    public byte[] getVALUE(byte tag){
        byte[] result = null;
        int index=findStartPos(tag);
        Log.e("payload 길이/index 위치",payloadBytes.length+"/"+index);
        if(index!=-1) {
            index++;

            byte[] lengthBytes = new byte[LENGTH_SIZE];
            System.arraycopy(payloadBytes, index, lengthBytes, 0, LENGTH_SIZE);
            index += LENGTH_SIZE;

            int length = (int)DataChanger.byteArrayToShort(lengthBytes);
            //int length = (int)DataChanger.byteArrayToInt(lengthBytes);
            Log.e("Payload",tag+"의 길이 : "+length);

            //System.out.println("getvalue length : "+length);
            //System.out.println("index : "+index);

            result = new byte[length];
            System.arraycopy(payloadBytes, index, result, 0, result.length);
            //result=DataChanger.byteDecompress(result);

        }
        else
            Log.e("Payload", "not found tag in payload");

        return result;
    }


    public void setTag0x11(Tag0x11 tag0x11) {
        this.tag0x11 = tag0x11;
        //payload_length+=TAG_SIZE+LENGTH_SIZE+tag0x11.getLength();
    }


    public void setTag0x21(Tag0x21 tag0x21){
        this.tag0x21=tag0x21;
        //payload_length+=TAG_SIZE+LENGTH_SIZE+tag0x21.getLength();
    }

    public void setBasicInfo(BasicInfo basicInfo){
        this.basicInfo=basicInfo;
        //payload_length+=TAG_SIZE+LENGTH_SIZE+basicInfo.getLength();
    }

    public ReceiptItem getItem(byte tag){

        switch(tag){
            case (byte)0x01: // 기본정보 추출
                return new BasicInfo(getVALUE(tag));
            case (byte)0x11:
                return new Tag0x11(getVALUE(tag));
            case (byte)0x21: // 상품정보 추출
                return new Tag0x21(getVALUE(tag));
        }
        return null;
    }
    /*
    public Tag0x11 getTag0x11() {

        tag0x11=new Tag0x11(getVALUE(Tag0x11.TAG));

        return tag0x11;
    }


    public Tag0x21 getTag0x21(){

        tag0x21=new Tag0x21(getVALUE(Tag0x21.TAG));

        return tag0x21;
    }


    public BasicInfo getBasicInfo(){

        basicInfo=new BasicInfo(getVALUE(BasicInfo.TAG));

        return basicInfo;
    }
    */

    /*
    public int getPayloadLength(){
        return payload_length;
    }
    */


    public int getItemSize(){
        return items.size();
    }
}
