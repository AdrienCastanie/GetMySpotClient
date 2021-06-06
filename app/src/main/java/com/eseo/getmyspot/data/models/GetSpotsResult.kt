package com.eseo.getmyspot.data.models

data class GetSpotsResult(val error: Int, val message: String, val list_spots: List<SpotModelRemote>)
