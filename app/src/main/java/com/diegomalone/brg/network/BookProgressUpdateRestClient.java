package com.diegomalone.brg.network;

import com.diegomalone.brg.model.Book;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BookProgressUpdateRestClient {

    @POST("post")
    Observable<String> postBookUpdate(@Body Book book);
}
