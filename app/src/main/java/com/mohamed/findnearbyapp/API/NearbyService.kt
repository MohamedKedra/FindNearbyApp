package com.mohamed.findnearbyapp.API

import com.mohamed.findnearbyapp.Models.PlacesResponse
import com.mohamed.findnearbyapp.Models.VenuePhotoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NearbyService {

    @GET("explore")
    fun getAllVenues(
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("ll") latLng: String,
        @Query("v") version: String

    ) : Call<PlacesResponse>

    @GET("{venueId}/photos")
    fun getVenuePhoto(
        @Path("venueId") venueId : String,
        @Query("client_id") clientId: String,
        @Query("client_secret") clientSecret: String,
        @Query("v") version: String

    ): Call<VenuePhotoResponse>

}