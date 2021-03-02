package com.bukcoja.seung.receipt_inmyhand;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.BasicInfo;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Product;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.ReceiptItem;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Tag0x11;
import com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem.Tag0x21;

public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // db 새로 생성시에 호출되는 메소드드
    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL("DROP TABLE IF EXISTS USER_INFO");
        db.execSQL("DROP TABLE IF EXISTS RECEIPT_INFO");
        db.execSQL("DROP TABLE IF EXISTS ITEM");
        db.execSQL("DROP TABLE IF EXISTS MEMBERSHIP");
        db.execSQL("DROP TABLE IF EXISTS POINT_HISTORY");
        db.execSQL("DROP TABLE IF EXISTS PRODUCT");
        db.execSQL("DROP TABLE IF EXISTS CATEGORY");
        db.execSQL("DROP TABLE IF EXISTS LOGIN");

        db.execSQL("CREATE TABLE IF NOT EXISTS USER_INFO (" +
                "id TEXT PRIMARY KEY, "+
                "pwd TEXT, "+
                "name TEXT, "+
                "phone TEXT, "+
                "mypoint INTEGER);");

        db.execSQL("CREATE TABLE IF NOT EXISTS RECEIPT_INFO (" +
                "_id INTEGER PRIMARY KEY, " +
                "groupid INTEGER, " +
                "toi INTEGER, "+       // type of issue 발행 종류
                "crn TEXT, " +         // company register number : 사업자 등록 번호
                "cn VARCHAR, "+        // company name : 매장명
                "name TEXT, "+         // 사업자 이름
                "address TEXT, "+      // 주소
                "tel TEXT, "+          // 연락처
                "tob INTEGER, "+       // type of business : 업종
                "date DATETIME, " +        // 전자 영수증 발행 일시
                "price INTEGER, "+     // 거래 총액
                "vat INTEGER, "+       // 부가가치세
                "mop INTEGER, "+       // method of payment  : 결제 수단
                "mods INTEGER, "+       // method of discount,saving : 할인/적립 수단
                "item_num INTEGER);");  // 상품/서비스 항목 수

        db.execSQL("CREATE TABLE IF NOT EXISTS ITEM("+
                "_id INTEGER, " +
                "name TEXT, "+
                "category INTEGER, "+
                "price INTEGER, "+
                "count INTEGER, "+
                "total INTEGER);");

        db.execSQL("CREATE TABLE IF NOT EXISTS MEMBERSHIP("+
                "_id INTEGER, "+
                "date DATETIME, "+        // 구매 날짜
                "usable INTEGER, "+      //  가용 포인트
                "total INTEGER, "+       // 누적 포인트
                "nowused INTEGER, "+     // 현재 사용한 포인트
                "nowsaved INTEGER);");   // 현재 적립한 포인트

        db.execSQL("CREATE TABLE IF NOT EXISTS POINT_HISTORY("+
                "_id INTEGER, "+
                "date DATETIME, "+        // 구매 날짜
                "type INTEGER, "+     // 적립(1) or 사용(0)
                "point INTEGER);");   // 적립한 포인트 or 사용한 포인트

        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUCT("+
                "_id INTEGER, "+
                "date DATETIME, "+         // 구매 날짜
                "pname TEXT, "+            //  제품 이름
                "pinfo INTEGER, "+         //  제품 정보
                "unitprice INTEGER, "+     // 제품 단가
                "num INTEGER, "+           //  제품 개수
                "ctg INTEGER);");          // 제품 카테고리

        db.execSQL("CREATE TABLE IF NOT EXISTS CATEGORY("+
                "categoryid INTEGER, "+
                "category TEXT);");          // 제품 카테고리

        db.execSQL("CREATE TABLE IF NOT EXISTS LOGIN(" +
                "id TEXT, "+
                "state INTEGER);");

    }

    public void createCtgTable(SQLiteDatabase db){
        db.execSQL("CREATE TABLE IF NOT EXISTS LOGIN(" +
                "id TEXT, "+
                "state INTEGER);");

    }
    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    /*
    public void insertBasicInfoData(int id,String date,BasicInfo b){
        String query="INSERT INTO RECEIPT_INFO VALUES("+id+", "+b.getToi()+", '"+b.getCrn()+"', '"+b.getCn()+"', '"+b.getName()+"', '";
        query+=b.getAddress()+"', '"+b.getTel()+"', "+b.getTob()+", '"+date+"', "+b.getPrice()+", "+b.getVat()+", ";
        query+=b.getMop()+", "+b.getMobs()+", "+b.getItem_num()+");";
        insert(query);
    }
    */
    public boolean insertItem(int id, String date, int groupid, ReceiptItem item){

        String query;

        switch(item.getTag()){
            case 0x11:
                Tag0x11 t=(Tag0x11)item;
                query="INSERT INTO MEMBERSHIP VALUES("+id+", '"+date+"', "+t.getUsable()+", "+t.getTotal()+", "+t.getNowUsed()+", "+t.getNowSaved()+");";
                insert(query);
                Log.e("0x11 inserted",query);
                return true;
            case 0x21:
                Tag0x21 t2=(Tag0x21)item;
                Log.d("[ItemFragment]prodNum",String.valueOf(t2.getProdNum()));
                for(int i=0;i<t2.getProdNum();i++) {
                    Product p=t2.getProduct(i);
                    query = "INSERT INTO PRODUCT VALUES(" + id + ", '" + date + "', '" + p.getName() + "', " +
                            0 + ", " + p.getUnitPrice() + ", " + p.getNum() +", "+p.getInfo()+ ");";
                    insert(query);
                    Log.e("0x21 inserted",query);
                }
                return true;
            case 0x01:
                BasicInfo b=(BasicInfo)item;
                query="INSERT INTO RECEIPT_INFO VALUES("+id+", "+groupid+", "+b.getToi()+", '"+b.getCrn()+"', '"+b.getCn()+"', '"+b.getName()+"', '";
                query+=b.getAddress()+"', '', "+b.getTob()+", '"+date+"', "+b.getPrice()+", "+b.getVat()+", ";
                query+=0+", "+0+", "+b.getItem_num()+");";
                insert(query);
                Log.e("0x01 inserted",query);
                return true;
        }
        return false;

    }
    public void createTable(){
        SQLiteDatabase db = getWritableDatabase();

        //db.execSQL("DROP TABLE RECEIPT_INFO");

        db.execSQL("CREATE TABLE IF NOT EXISTS USER_INFO (" +
                "id TEXT PRIMARY KEY, "+
                "pwd TEXT, "+
                "name TEXT, "+
                "phone TEXT, "+
                "mypoint INTEGER);");

        db.execSQL("CREATE TABLE IF NOT EXISTS RECEIPT_INFO (" +
                "_id INTEGER PRIMARY KEY, " +
                "groupid INTEGER, " +
                "toi INTEGER, "+       // type of issue 발행 종류
                "crn TEXT, " +         // company register number : 사업자 등록 번호
                "cn VARCHAR, "+        // company name : 매장명
                "name TEXT, "+         // 사업자 이름
                "address TEXT, "+      // 주소
                "tel TEXT, "+          // 연락처
                "tob INTEGER, "+       // type of business : 업종
                "date DATETIME, " +        // 전자 영수증 발행 일시
                "price INTEGER, "+     // 거래 총액
                "vat INTEGER, "+       // 부가가치세
                "mop INTEGER, "+       // method of payment  : 결제 수단
                "mods INTEGER, "+       // method of discount,saving : 할인/적립 수단
                "item_num INTEGER);");  // 상품/서비스 항목 수

        db.execSQL("CREATE TABLE IF NOT EXISTS ITEM("+
                "_id INTEGER, " +
                "name TEXT, "+
                "category INTEGER, "+
                "price INTEGER, "+
                "count INTEGER, "+
                "total INTEGER);");

        db.execSQL("CREATE TABLE IF NOT EXISTS MEMBERSHIP("+
                "_id INTEGER, "+
                "date DATETIME, "+        // 구매 날짜
                "usable INTEGER, "+      //  가용 포인트
                "total INTEGER, "+       // 누적 포인트
                "nowused INTEGER, "+     // 현재 사용한 포인트
                "nowsaved INTEGER);");   // 현재 적립한 포인트

        db.execSQL("CREATE TABLE IF NOT EXISTS POINT_HISTORY("+
                "_id INTEGER, "+
                "date DATETIME, "+        // 구매 날짜
                "type INTEGER, "+     // 적립(1) or 사용(0)
                "point INTEGER);");   // 적립한 포인트 or 사용한 포인트

        db.execSQL("CREATE TABLE IF NOT EXISTS PRODUCT("+
                "_id INTEGER, "+
                "date DATETIME, "+         // 구매 날짜
                "pname TEXT, "+            //  제품 이름
                "pinfo INTEGER, "+         //  제품 정보
                "unitprice INTEGER, "+     // 제품 단가
                "num INTEGER, "+           //  제품 개수
                "ctg INTEGER);");          // 제품 카테고리

        db.execSQL("CREATE TABLE IF NOT EXISTS CATEGORY("+
                "categoryid INTEGER, "+
                "category TEXT);");          // 제품 카테고리

        db.execSQL("CREATE TABLE IF NOT EXISTS LOGIN(" +
                "id TEXT, "+
                "state INTEGER);");

    }
    public void insert(String query){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        // DB에 입력한 값으로 행 추가
        db.execSQL(query);
        db.close();

    }
    public void delete(int id){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        // DB에 입력한 값으로 행 추가
        db.execSQL("DELETE FROM RECEIPT_INFO WHERE _id="+id);
        db.execSQL("DELETE FROM MEMBERSHIP WHERE _id="+id);
        db.execSQL("DELETE FROM PRODUCT WHERE _id="+id);
        db.close();
    }
    public void deleteAll(){
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();

        // DB에 입력한 값으로 행 추가
        db.execSQL("DELETE FROM RECEIPT_INFO");
        db.execSQL("DELETE FROM MEMBERSHIP");
        db.execSQL("DELETE FROM PRODUCT");
        db.close();
    }

    public Cursor getResult(String query) {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }
}