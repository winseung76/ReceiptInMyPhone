package com.bukcoja.seung.receipt_inmyhand;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.DigitalReceipt;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.Header;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.Payload;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.BasicInfo;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Tag0x21;
import com.bukcoja.seung.receipt_inmyhand.Presenter.MainPresenter;

import java.util.Random;

public class ReceiptDecoder {

    private DBHelper dbHelper;
    private UserInfo userInfo;
    private MainPresenter presenter;
    private Context context;

    public ReceiptDecoder(Context context,DBHelper dbHelper, UserInfo userInfo, MainPresenter presenter){
        this.dbHelper=dbHelper;
        this.userInfo=userInfo;
        this.presenter=presenter;
        this.context=context;
    }

    public void decode(NdefMessage ndefMessage){
        // NdefMessage로부터 첫번째 NdefRecord 추출
        NdefRecord record=ndefMessage.getRecords()[0];

        // NdefRecord로부터 페이로드 추출
        byte[] bytes=record.getPayload();

        //byte[] bytes= DataChanger.byteDecompress(record.getPayload());
        //Log.e("ndef record length",String.valueOf(record.getTnf()));
        Log.e("ReceiptDecoder","NDEF message length : "+String.valueOf(ndefMessage.getByteArrayLength()));
        Log.e("ReceiptDecoder","전자 영수증 전체 length : "+String.valueOf(bytes.length));

        //System.out.println("바이트 크기 : "+bytes.length);
        //byte[] processed=new byte[bytes.length-2];
        //System.arraycopy(bytes,2,processed,0,bytes.length-2);

        DigitalReceipt dr=new DigitalReceipt(bytes);  // ndefrecord로 부터 전자 영수증 객체 얻어옴

        Header header=dr.getHeader();     // 전자 영수증 헤더 추출
        Payload payload=dr.getPayload();  // 전자 영수증 페이로드 추출

        Random random = new Random(System.currentTimeMillis());
        int max=99999999;
        int min=11111111;
        int receiptid=random.nextInt(max - min + 1) + min;

        /*
        ReceiptItem item=payload.getItem((byte)0x01);
        //((BasicInfo)item).setAddress("");
        // 전자 영수증 발급 날짜
        String date=((BasicInfo)item).getDate();
        //dbHelper.insertItem(receiptid,date,userInfo.getGroup(),item);


        ArrayList<Byte> validTags=header.getValidTags(); // 헤더로부터 포함된 태그들 추출

        for(int i=0;i<validTags.size();i++){
            byte tag=validTags.get(i);
            item=payload.getItem(tag);
            dbHelper.insertItem(receiptid,date,userInfo.getGroup(),item);
        }
        */

        // 전자 영수증 페이로드로부터 기본 정보 추출
        BasicInfo item1=(BasicInfo)payload.getItem((byte)0x01);

        // 전자 영수증 페이로드로부터 상품 정보 추출
        Tag0x21 item2=(Tag0x21)payload.getItem((byte)0x21);

        String date=item1.getDate();

        // 전자 영수증 정보를 중앙 DB로 전달
        presenter.saveReceipt(context,receiptid,userInfo.getId(),userInfo.getGroup(),item1,item2);

        presenter.updateUserPoint(context,receiptid,userInfo.getGroup(), userInfo.getId(),date,0);

    }
}
