package com.mohamed.findnearbyapp.Repositories

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mohamed.findnearbyapp.API.Client
import com.mohamed.findnearbyapp.API.NearbyService
import com.mohamed.findnearbyapp.Constant
import com.mohamed.findnearbyapp.Models.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository(context: Context) {

    var service: NearbyService = Client.getInstance(context)

    fun getAllVenues(lat: Double, lng: Double): LiveData<List<Item>>? {

        var items = MutableLiveData<List<Item>>()
        service.getAllVenues(Constant.ClientID, Constant.ClientSecret, "$lat,$lng", "20191223")
            .enqueue(
                object : Callback<PlacesResponse> {
                    override fun onFailure(call: Call<PlacesResponse>, t: Throwable) {
                        items.value = null
                    }

                    override fun onResponse(call: Call<PlacesResponse>, response: Response<PlacesResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            items.value = response.body()?.response?.groups?.get(0)?.items
                        }else{
                            items.value = emptyList()
                        }
                    }

                })
        return items
    }

    fun getVenuePhoto(id: String): LiveData<List<PhotoItem>>? {
        var photos = MutableLiveData<List<PhotoItem>>()
        service.getVenuePhoto(id, Constant.ClientID, Constant.ClientSecret, "20191223")
            .enqueue(
                object : Callback<VenuePhotoResponse> {
                override fun onResponse(call: Call<VenuePhotoResponse>, response: Response<VenuePhotoResponse>) {
                    photos.value = response.body()?.response?.photos?.items
                }

                override fun onFailure(call: Call<VenuePhotoResponse>, t: Throwable) {
                    photos.value = null
                }
            })
        return photos
    }

}