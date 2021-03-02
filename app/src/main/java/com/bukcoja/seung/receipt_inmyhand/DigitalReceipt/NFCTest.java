package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt;

import android.nfc.NdefMessage;

import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.BasicInfo;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Product;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Tag0x21;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NFCTest {

    // ndefmessage 인코딩
    public byte[] encoding(){
        NdefMessage ndefMessage=null;

        // 각각의 항목별로 임시 데이터 세팅하기
        // [포스기] 에서 할 일
        BasicInfo basicInfo=new BasicInfo();

        basicInfo.setToi((byte)0x00);
        //basicInfo.setCrn("0123456789");
        //basicInfo.setCn("rimp문구점");
        //basicInfo.setName("이승연");
        //basicInfo.setAddress("서울 도봉구 삼양로 144길 33");
        //basicInfo.setTel("01029771939");
        basicInfo.setTob((byte)0);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        basicInfo.setDate(mFormat.format(new Date(System.currentTimeMillis())));
        //basicInfo.setDate("2019-09-05 15:10:17");
        basicInfo.setPrice(65000);
        basicInfo.setVat((short)2500);
        //basicInfo.setMop((byte)0x21);
        //basicInfo.setMobs((byte)0x03);
        basicInfo.setItem_num((byte)0x01);

        /*
        Tag0x11 tag0x11=new Tag0x11();
        tag0x11.setMembershipNum("1234567812345678");
        //tag0x11.setKind("rimpship");
        tag0x11.setUsable((short)3500);
        tag0x11.setTotal((short)3500);
        tag0x11.setNowUsed((short)1000);
        tag0x11.setNowSaved((short)100);
        //tag0x11.setAcceptMsg("00");
        */

        Tag0x21 tag0x21=new Tag0x21();
        //tag0x21.addProduct(new Product("",(byte)5,(short)10000,(byte)1));
        //tag0x21.addProduct(new Product("",(byte)2,(short)10000,(byte)1));

        tag0x21.addProduct(new Product(2,(byte)3));
        tag0x21.addProduct(new Product(11,(byte)1));
        //tag0x21.addProduct(new Product(12,(byte)1));
        //tag0x21.addProduct(new Product(13,(byte)1));
        //tag0x21.addProduct(new Product(15,(byte)1));
        //tag0x21.addProduct(new Product(16,(byte)1));
        //tag0x21.addProduct(new Product(19,(byte)1));
        //tag0x21.addProduct(new Product(20,(byte)1));
        //tag0x21.addProduct(new Product(23,(byte)1));
        //tag0x21.addProduct(new Product(25,(byte)1));
        //tag0x21.addProduct(new Product(3,(byte)2));
        //tag0x21.addProduct(new Product(4,(byte)4));

        // 페이로드 구성
        Payload payload=new Payload();
        payload.addItem(basicInfo);
        //payload.addItem(tag0x11);
        payload.addItem(tag0x21);

        //payload.setBasicInfo(basicInfo);
        //payload.setTag0x11(tag0x11);
        //payload.setTag0x21(tag0x21);

        // 헤더 구성
        Header header=new Header(Setup.Version.STANDARD10,payload.getCompositeInfo(), Setup.CharSet.EUC_KR,Setup.Currency.WON);

        // 페이로드+헤더
        DigitalReceipt digitalReceipt=new DigitalReceipt(header,payload);

        // NDEF 메시지로 인코딩
        //String mimeType="application/x-receipt";
        //NdefRecord ndefRecord=new NdefRecord(NdefRecord.TNF_WELL_KNOWN,mimeType.getBytes(Charset.forName("utf-8")),new byte[0],digitalReceipt.getBytes());
        //ndefMessage=new NdefMessage(ndefRecord);

        return digitalReceipt.getBytes();
    }

    /*
    public ReceiptItems decoding(NdefMessage ndefMessage){

        ReceiptItems items;

        // 포스기로부터 받은 NdefMessage를 디코딩하는 과정
        NdefRecord nr=ndefMessage.getRecords()[0];
        byte[] input_payload=nr.getPayload();

        DigitalReceipt dr=new DigitalReceipt(input_payload);
        Payload payload=dr.getPayload();
        Header h=dr.getHeader();

        byte[] input=payload.getVALUE(BasicInfo.TAG);
        byte[] input2=payload.getVALUE(Tag0x11.TAG);
        byte[] input3=payload.getVALUE(Tag0x21.TAG);

        BasicInfo b=new BasicInfo(input);
        Tag0x11 t=new Tag0x11(input2);
        Tag0x21 t2=new Tag0x21(input3);

        items=new ReceiptItems(b,t,t2);


        return items;
    }
    */
}
