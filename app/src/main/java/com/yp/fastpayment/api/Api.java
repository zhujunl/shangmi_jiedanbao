package com.yp.fastpayment.api;


import com.yp.fastpayment.api.request.InitRequest;
import com.yp.fastpayment.api.request.OrderDelRequest;
import com.yp.fastpayment.api.request.OrderListRequest;
import com.yp.fastpayment.api.request.PeriodRequest;
import com.yp.fastpayment.api.request.WriteOffRequest;
import com.yp.fastpayment.api.response.InitResponse;
import com.yp.fastpayment.api.response.OrderDelResponse;
import com.yp.fastpayment.api.response.OrderListResponse;
import com.yp.fastpayment.api.response.PeriodResponse;
import com.yp.fastpayment.api.response.WriteOffResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 */

public interface Api {

    @POST("zjypg/shangmishouchiInit")
    Call<InitResponse> shangmishouchiInit(@Body InitRequest entity);


    @POST("zjypg/shangmishouchiOrderList")
    Call<OrderListResponse> shangmishouchiOrderList(@Body OrderListRequest entity);



        @POST("zjypg/shangmishouchiGetNewOrder")
    Call<OrderListResponse> shangmishouchiGetNewOrder(@Body OrderListRequest entity);

    @POST("api/wxapp/reserve/getBranchMealPeriod")
    Call<PeriodResponse> shangmishouchigetBranchMealPeriod(@Body PeriodRequest entity);


//    @GET("api/wxapp/reserve/getBranchMealPeriod")
//    Call<PeriodResponse> shangmishouchigetBranchMealPeriod(@Query("shopId") int shopId, @Query("branchId") int branchId);


    @POST("api/wxapp/openapi/iSqlquery")
    Call<OrderDelResponse> GetDeleteOrder(@Body OrderDelRequest entity);

    @POST("api/wxapp/reserveOrder/modifyMealStatus")
    Call<WriteOffResponse> SetMealStatus(@Body WriteOffRequest entity);
}