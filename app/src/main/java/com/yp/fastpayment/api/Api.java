package com.yp.fastpayment.api;


import com.yp.fastpayment.api.request.InitRequest;
import com.yp.fastpayment.api.request.OrderListRequest;
import com.yp.fastpayment.api.response.InitResponse;
import com.yp.fastpayment.api.response.OrderListResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
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


}