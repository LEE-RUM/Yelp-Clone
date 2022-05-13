package com.example.hw5_yelpclone

import android.annotation.SuppressLint
import android.content.Intent
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class YelpAdapter(val context: Context, private val restaurants: ArrayList<Restaurant>) : RecyclerView.Adapter<YelpAdapter.MyViewHolder>() {

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    inner class MyViewHolder (itemView: View): RecyclerView.ViewHolder (itemView){
        init {
            itemView.setOnClickListener {

                val mapLocation = restaurants[adapterPosition].location

                var address = mapLocation.address

                val businessName = restaurants[adapterPosition].name.take(15)
                    .replace("&", "and")

                val url =
                    "http://maps.google.co.in/maps?q=$businessName$address"
                val location = Uri.parse(url)
                val mapIntent = Intent(Intent.ACTION_VIEW, location)
                context.startActivity(mapIntent)
            }
        }
        // This class will represent a single row in our recyclerView list
        // This class also allows caching views and reuse them
        // Each MyViewHolder object keeps a reference to 3 view items in our row_item.xml file


        val name = itemView.findViewById<TextView>(R.id.restaraunt_name)
        val review_count = itemView.findViewById<TextView>(R.id.num_reviews)
        val distance = itemView.findViewById<TextView>(R.id.distance)
        val image_url = itemView.findViewById<ImageView>(R.id.restaraunt_img)
        val ratingBar = itemView.findViewById<RatingBar>(R.id.rtg_bar)
        val price = itemView.findViewById<TextView>(R.id.price)
        val location = itemView.findViewById<TextView>(R.id.restaraunt_location)
        val category = itemView.findViewById<TextView>(R.id.restaraunt_category)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // Inflate a layout from our XML (row_item.XML) and return the holder
        // create a new view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_item, parent, false)
        return MyViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "PrivateResource")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        val currentItem = restaurants[position]
        holder.distance.text = currentItem.convertDistance()
        holder.review_count.text = "${currentItem.review_count} Reviews"
        holder.name.text = currentItem.name
        holder.ratingBar.rating = currentItem.rating.toFloat()
        holder.price.text = currentItem.price
        holder.location.text = currentItem.location.address
        holder.category.text = currentItem.categories[0].title

        // Get the context for glide
        val context = holder.itemView.context

        // Load the image from the url using Glide library
        Glide.with(context)
            .load(currentItem.image_url)
            .placeholder(com.google.android.material.R.drawable.ic_clock_black_24dp) // In case the image is not loaded show this placeholder image // optional - Circle image with rounded corners
            .into(holder.image_url)

    }

    override fun getItemCount(): Int {
        // Return the size of your dataset (invoked by the layout manager)
        return restaurants.size
    }

}