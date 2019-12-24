package com.mohamed.findnearbyapp.Models

class Response(
    val suggestedRadius: Int,
    val totalResults: Int,
    val groups: List<Group>
)