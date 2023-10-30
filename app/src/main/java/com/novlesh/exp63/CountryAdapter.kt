package com.novlesh.exp63
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

class CountryAdapter(
    private val context: Context,
    private val countryList: List<CountryInfo>,
    private val independenceDates: List<String>
) : ArrayAdapter<CountryInfo>(context, R.layout.item_country, countryList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.item_country, parent, false)

        val countryInfo = countryList[position]

        val flagImageView: ImageView = view.findViewById(R.id.flag_image)
        Glide.with(context)
            .load(countryInfo.flagUrl)
            .into(flagImageView)

        val countryNameTextView: TextView = view.findViewById(R.id.country_name)
        countryNameTextView.text = countryInfo.name

        // Set click listener to display independence date
        view.setOnClickListener {
            displayIndependenceDate(countryInfo.flagUrl, countryInfo.name, independenceDates[position])
        }

        return view
    }

    private fun displayIndependenceDate(flagUrl: String, countryName: String, independenceDate: String) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_independence_date)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val flagImageViewDialog: ImageView = dialog.findViewById(R.id.flag_image_dialog)
        val countryNameTextView: TextView = dialog.findViewById(R.id.country_name)
        val independenceDateTextView: TextView = dialog.findViewById(R.id.independence_date)

        // Load flag image using Glide
        Glide.with(context)
            .load(flagUrl)
            .into(flagImageViewDialog)

        countryNameTextView.text = countryName
        independenceDateTextView.text = "Independence Day: $independenceDate"

        dialog.window?.attributes?.gravity = Gravity.CENTER
        dialog.setCanceledOnTouchOutside(true)

        dialog.show()
    }
}
