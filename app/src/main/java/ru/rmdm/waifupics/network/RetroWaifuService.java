package ru.rmdm.waifupics.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.rmdm.waifupics.model.WaifuResponse;

public interface RetroWaifuService {

    @GET("search")
    Call<WaifuResponse> getImages(
            @Query("is_nsfw") String isNsfw,
            @Query("limit") Integer limit);
}
