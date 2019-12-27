package com.mohamed.findnearbyapp.Models

data class VenuePhotoResponse(
    val meta : Meta,
    val response: PhotoResponse
)

data class PhotoResponse(
    val photos : List<Photo>
)

data class Photo(
    val count : Int,
    val items : List<PhotoItem>
)

data class PhotoItem (
    val id : Int,
    val prefix : String,
    val suffix : String,
    val width : Int,
    val height : Int
)

