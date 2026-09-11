package ru.rmdm.waifupics.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import ru.rmdm.waifupics.model.WaifuImage;
import ru.rmdm.waifupics.network.RetroRepository;
import ru.rmdm.waifupics.network.RetroWaifuService;

@HiltViewModel
public class WaifuViewModel  extends ViewModel {
    MutableLiveData<List<WaifuImage>> liveData;

    @Inject
    RetroWaifuService retroWaifuService;


    @Inject
    public WaifuViewModel() {
        this.liveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<WaifuImage>> getLiveData() {
        return liveData;
    }

    public void makeApiCall(){
        RetroRepository retroRepository = new RetroRepository(retroWaifuService);
        retroRepository.makeNsfwApiCall("false",20,liveData);
    }
    public void makeApiSwitch(String nsfw_flag){
        RetroRepository retroRepository = new RetroRepository(retroWaifuService);
        retroRepository.makeNsfwApiCall(nsfw_flag,20,liveData);
    }
}
