package com.eseo.getmyspot.data.models

import android.location.Location
import java.time.LocalDateTime

data class SpotModel(var pseudo: String, var image: String?, var battery: String, var time: LocalDateTime,
                     var position: Location, var pressure: String, var brightness: String,
                     var image_spot: String?)
