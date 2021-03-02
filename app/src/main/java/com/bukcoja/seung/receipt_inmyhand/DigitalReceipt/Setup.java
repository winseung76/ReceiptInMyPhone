package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt;

public class Setup {

    // 문자 인코딩 방법
    public static class CharSet{
        public final static byte EUC_KR=0x00;
        public final static byte UTF8=0x01;
    }
    // 화폐 단위
    public static class Currency{
        public final static byte WON=0x00;
        public final static byte DOLLAR=0x01;
        public final static byte YUAN=0x02;
        public final static byte EURO=0x03;
        public final static byte EN=0x04;
    }
    // 영수증 버전
    public static class Version{
        public final static byte STANDARD10=0x10;
    }
}
