package com.eseo.getmyspot.view.main.adapter

import android.graphics.BitmapFactory
import android.hardware.SensorManager
import android.location.Geocoder
import android.location.Location
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.eseo.getmyspot.R
import com.eseo.getmyspot.data.models.SpotModel
import java.util.*

class HomeSpotsAdapter(
    private val spots: List<SpotModel>,
    private val onClick: (spotModel: SpotModel) -> Unit
) :
    RecyclerView.Adapter<HomeSpotsAdapter.ViewHolder>() {

    // Comment s'affiche ma vue
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun showItem(spot: SpotModel, onClick: (spotModel: SpotModel) -> Unit) {

            itemView.findViewById<TextView>(R.id.pseudo).text = spot.pseudo

            if (spot.image != null) {
                val decodedByte = Base64.decode(spot.image, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
                itemView.findViewById<ImageView>(R.id.profile_picture).setImageBitmap(bitmap);
            }

            if (spot.image_spot != null) {
                val decodedByte = Base64.decode(spot.image_spot, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
                itemView.findViewById<ImageView>(R.id.image_spot).setImageBitmap(bitmap);
            }

            itemView.findViewById<TextView>(R.id.txt_battery).text = spot.battery + "%"

            itemView.findViewById<TextView>(R.id.time).text = spot.time

            itemView.findViewById<TextView>(R.id.txt_address).text = geoCode(spot.position)

            itemView.findViewById<TextView>(R.id.txt_altitude).text = SensorManager.getAltitude(
                SensorManager.PRESSURE_STANDARD_ATMOSPHERE, spot.pressure.toFloat()
            ).toString() + " " + itemView.context.getString(R.string.metter)

            itemView.findViewById<TextView>(R.id.txt_pressure).text =
                spot.pressure + " " + itemView.context.getString(R.string.mbar)

            itemView.findViewById<TextView>(R.id.txt_light).text = spot.brightness + " lux"

            itemView.findViewById<Button>(R.id.goToSpot).setOnClickListener {
                onClick(spot)
            }
        }

        private fun geoCode(location: Location?): String {
            if (location != null) {
                val geocoder = Geocoder(itemView.context, Locale.getDefault())
                val results = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                return results[0].getAddressLine(0)
            }
            return ""
        }
    }

    // Retourne une « vue » / « layout » pour chaque élément de la liste
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_spot_item, parent, false)
        return ViewHolder(
            view
        )
    }

    // Connect la vue ET la données
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.showItem(spots[position], onClick)
    }

    override fun getItemCount(): Int {
        return spots.size
    }

}