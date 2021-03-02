package com.bukcoja.seung.receipt_inmyhand;

import com.bukcoja.seung.receipt_inmyhand.VO.CategoryListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartDetailVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ChartListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.DateVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupMemberListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.GroupVO;
import com.bukcoja.seung.receipt_inmyhand.VO.LoginVO;
import com.bukcoja.seung.receipt_inmyhand.VO.PointListVO;
import com.bukcoja.seung.receipt_inmyhand.VO.PointVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ReceiptVO;
import com.bukcoja.seung.receipt_inmyhand.VO.ServiceVO;
import com.bukcoja.seung.receipt_inmyhand.VO.UpperLimitVO;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitAPI {

    @FormUrlEncoded
    @POST("/Login/checkLogin.php")
    Call<LoginVO> getUserInfo(@FieldMap HashMap<String, Object> param);

    @POST("/Category/getCategory.php")
    Call<CategoryListVO> getCategory();

    @FormUrlEncoded
    @POST("/Receipt/getReceipt.php")
    Call<ReceiptVO> getReceipt(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Receipt/deleteReceipt.php")
    Call<String> deleteReceipt(@FieldMap HashMap<String, Object> param,@Field("receiptlist[]") List<Integer> receiptlist);

    /*
    @FormUrlEncoded
    @POST("/Receipt/saveReceipt5.php")
    Call<String> saveReceipt(@FieldMap HashMap<String, Object> param,@Field("prodnames[]") List<String> prodnames,
                             @Field("unitprices[]") List<Integer> unitprices,@Field("counts[]") List<Integer> nums,
                             @Field("ctgs[]") List<Integer> ctgs);

     */
    @FormUrlEncoded
    @POST("/Receipt/saveReceipt5.php")
    Call<String> saveReceipt(@FieldMap HashMap<String, Object> param,@Field("codes[]") List<Integer> codes ,@Field("counts[]") List<Integer> nums);

    @FormUrlEncoded
    @POST("/Receipt/shareReceipt.php")
    Call<ResponseBody> shareReceipt(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Chart/getProdWithCtg.php")
    Call<ChartListVO> getChartInfo(@FieldMap HashMap<String, Object> param, @Field("categories[]") List<Integer> categories);

    @FormUrlEncoded
    @POST("/Chart/getDetail.php")
    Call<ChartDetailVO> getChartDetailInfo(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Chart/getUpperLimit.php")
    Call<UpperLimitVO> getUpperLimit(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Chart/saveUpperLimit.php")
    Call<String> saveUpperLimit(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Group/registerGroup.php")
    Call<String> registerGroup(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Group/getGroup.php")
    Call<GroupListVO> getGroupList(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Group/checkGroupExist.php")
    Call<GroupVO> getGroup(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Group/removeGroup.php")
    Call<String> removeGroup(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Group/getGroupMembers.php")
    Call<GroupMemberListVO> getGroupMembers(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Point/getUserPoint.php")
    Call<PointVO> getUserPoint(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Point/updateUserPoint.php")
    Call<PointVO> updateUserPoint(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Point/getPointHistory.php")
    Call<PointListVO> getPointHistory(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/User/registerUser.php")
    Call<ResponseBody> registerUser(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/User/checkIDOverlay.php")
    Call<String> checkIDOverlay(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/User/removeUser.php")
    Call<String> removeUser(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/User/updateToken.php")
    Call<String> updateToken(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/RcmdService/getStoreInfo.php")
    Call<ServiceVO> getStoreInfo(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/RcmdService/getUpdateDate.php")
    Call<DateVO> getUpdateDate(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Upload/uploadImage.php")
    Call<String> uploadFile(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Upload/uploadProfile.php")
    Call<String> uploadProfile(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Upload/getProfile.php")
    Call<ResponseBody> getProfile(@FieldMap HashMap<String, Object> param);

    @FormUrlEncoded
    @POST("/Upload/setDefaultProfile.php")
    Call<ResponseBody> setDefaultProfile(@FieldMap HashMap<String, Object> param);
}
