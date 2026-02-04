package ru.rmdm.waifupics.di;


import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.rmdm.waifupics.network.RetroWaifuService;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    Interceptor onlineInterCeptor = chain -> {
        Response response = chain.proceed(chain.request());
        int maxAge = 60;
        return response.newBuilder().header("Cache-Control", "public, max-age=" + maxAge).build();
    };

    @Singleton
    @Provides
    public RetroWaifuService getRetroServiceInterface(Retrofit retrofit){
        return retrofit.create(RetroWaifuService.class);
    }

    @Singleton
    @Provides
    public Retrofit getRetroInstance(OkHttpClient client){
        return  new Retrofit.Builder()
                .baseUrl("https://api.waifu.im/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)  // Увеличение таймаута подключения
                .readTimeout(60, TimeUnit.SECONDS)     // Увеличение таймаута чтения
                .writeTimeout(60, TimeUnit.SECONDS)    // Увеличение таймаута записи
                .retryOnConnectionFailure(true)                // Повтор при ошибках соединения
                .addNetworkInterceptor(onlineInterCeptor)
                .build();
    }
}
