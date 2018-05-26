package com.diegomalone.brg.service;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.diegomalone.brg.BuildConfig;
import com.diegomalone.brg.model.Book;
import com.diegomalone.brg.network.BookProgressUpdateRestClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.functions.Consumer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import timber.log.Timber;

public class UpdateProgressIntentService extends IntentService {

    public static final String BROADCAST_ACTION = "com.diegomalone.brg.intent.action.SEND_UPDATE_PROGRESS_REQUEST_STATUS";

    public static final String INTENT_DATA_BOOK = "book";
    public static final String REQUEST_STATUS = "requestStatus";

    public static final int REQUEST_STATUS_SUCCESS = 1;
    public static final int REQUEST_STATUS_INTERNET_ERROR = 2;
    public static final int REQUEST_STATUS_ERROR = 3;
    public static final int REQUEST_STATUS_INVALID_PARAM = 4;

    private BookProgressUpdateRestClient restClient;

    public UpdateProgressIntentService() {
        super("UpdateProgressIntentService");

        OkHttpClient okHttpClient = createOkHttpClient();
        Gson gson = createGson();

        Retrofit retrofit = createRetrofit(okHttpClient, gson, BuildConfig.API_URL);

        restClient = retrofit.create(BookProgressUpdateRestClient.class);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onHandleIntent(Intent workIntent) {

        Book book = workIntent.getParcelableExtra(INTENT_DATA_BOOK);

        if (book == null) {
            sendResponse(REQUEST_STATUS_INVALID_PARAM);
            onDestroy();
            return;
        }

        restClient.postBookUpdate(book)
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) {
                        if (s != null && !s.isEmpty()) {
                            sendResponse(REQUEST_STATUS_SUCCESS);
                        } else {
                            sendResponse(REQUEST_STATUS_ERROR);
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Timber.e(throwable, "Error sending data to the server");

                        sendResponse(REQUEST_STATUS_INTERNET_ERROR);
                    }
                });
    }

    private void sendResponse(int response) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BROADCAST_ACTION);
        broadcastIntent.putExtra(REQUEST_STATUS, response);
        sendBroadcast(broadcastIntent);
    }

    @NonNull
    private Retrofit createRetrofit(OkHttpClient okHttpClient, Gson gson, String apiUrl) {
        return new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(apiUrl)
                .client(okHttpClient)
                .build();
    }

    @NonNull
    private Gson createGson() {
        return new GsonBuilder()
                .setLenient()
                .create();
    }

    @NonNull
    private OkHttpClient createOkHttpClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

        if (BuildConfig.DEBUG) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        } else {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
    }
}
