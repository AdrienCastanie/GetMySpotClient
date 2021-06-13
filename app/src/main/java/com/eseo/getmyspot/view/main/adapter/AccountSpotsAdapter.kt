package com.eseo.getmyspot.view.main.adapter

import android.graphics.BitmapFactory
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
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

class AccountSpotsAdapter(private val spots: List<SpotModel>, private val onClick: (spotModel: SpotModel) -> Unit) :
    RecyclerView.Adapter<AccountSpotsAdapter.ViewHolder>() {

    // Comment s'affiche ma vue
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun showItem(spot: SpotModel, onClick: (spotModel: SpotModel) -> Unit) {

            if (spot.image_spot != null) {
                val decodedByte = Base64.decode(spot.image_spot, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.size)
                itemView.findViewById<ImageView>(R.id.image_spot).setImageBitmap(bitmap);
            }

            itemView.findViewById<TextView>(R.id.battery).text = spot.battery

            val timeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            val timeFormatted = spot.time.format(timeFormatter)
            itemView.findViewById<TextView>(R.id.time).text = timeFormatted

            itemView.findViewById<TextView>(R.id.position).text = geoCode(spot.position)

            itemView.findViewById<TextView>(R.id.pressure).text = spot.pressure

            itemView.findViewById<TextView>(R.id.brightness).text = spot.brightness

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
            LayoutInflater.from(parent.context).inflate(R.layout.account_spot_item, parent, false)
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