package com.bukcoja.seung.receipt_inmyhand.DigitalReceipt.ReceiptPayloadItem;

public interface ReceiptItem {

    byte[] encode();
    void decode(byte[] data);

    int getLength();
    byte getTag();
}
