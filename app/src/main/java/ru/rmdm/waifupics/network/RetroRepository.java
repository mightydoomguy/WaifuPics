package ru.rmdm.waifupics.network;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.rmdm.waifupics.model.WaifuImage;
import ru.rmdm.waifupics.model.WaifuResponse;

public class RetroRepository {
    private RetroWaifuService retroWaifuService;


    public RetroRepository(RetroWaifuService retroWaifuService) {
        this.retroWaifuService = retroWaifuService;
    }

    public void makeNsfwApiCall(String is_nsfw, Integer limit, MutableLiveData<List<WaifuImage>> liveData){
        Call<WaifuResponse> call = retroWaifuService.getImages(is_nsfw,limit);
        call.enqueue(new Callback<WaifuResponse>() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onResponse(Call<WaifuResponse> call, Response<WaifuResponse> response) {
                if (response.isSuccessful() && response.body() !=null){
                    liveData.postValue(response.body().getImages());
                }
                else
                    liveData.postValue(null);
                    Log.e("RetroRepository", "Response not successful. Code: " + response.code());
            }

            @Override
            public void onFailure(Call<WaifuResponse> call, Throwable t) {

                liveData.postValue(null);
            }
        });
    }
}
