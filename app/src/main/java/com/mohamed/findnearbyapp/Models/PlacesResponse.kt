package com.mohamed.findnearbyapp.Models

data class PlacesResponse(
   val meta : Meta,
   val response: Response
)

data class Response(
   val suggestedRadius: Int,
   val totalResults: Int,
   val groups: List<Group>
)

data class Group(
   val type : String,
   val items : List<Item>
)

data class Item(
   val venue: Venue
)

data class Venue(
   val id: String,
   val name: String,
   val location: Location,
   val categories : List<Category>
)

data class Category (
   val id : String,
   val icon : Icon
)

data class Icon(
   val prefix : String,
   val suffix : String
)

data class Location(
   val address: String,
   val lat: Double,
   val lng: Double,
   val distance: String,
   val city: String,
   val state: String,
   val country: String
)