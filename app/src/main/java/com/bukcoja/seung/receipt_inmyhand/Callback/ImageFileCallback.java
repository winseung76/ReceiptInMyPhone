package com.bukcoja.seung.receipt_inmyhand.Callback;

public interface ImageFileCallback {

    void callbackImageUploadResult(int result,int flag);
    void callbackGetProfileImage(String imageUri);
    void callbackDefaultProfile(int result);

}
