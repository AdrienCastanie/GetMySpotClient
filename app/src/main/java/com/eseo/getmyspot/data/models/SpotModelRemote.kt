package com.eseo.getmyspot.data.models


data class SpotModelRemote(var pseudo: String, var image: String?, var battery: String, var time: String,
                           var position_latitude: Double, var position_longitude: Double, var pressure: String,
                           var brightness: String, var image_spot: String?)
