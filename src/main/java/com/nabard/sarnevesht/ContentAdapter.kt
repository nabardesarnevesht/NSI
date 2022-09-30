package com.google.firebase.example.messaging.kotlin.com.nabard.sarnevesht

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.nabard.sarnevesht.R
import com.squareup.picasso.Picasso
import com.nabard.sarnevesht.ContentDetails
import com.nabard.sarnevesht.ui.NotificationDetails


class RecyclerViewAdapter(recyclerDataArrayList: ArrayList<Content>, mcontext: Context) :
    RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>() {
    // creating a variable for our array list and context.
    private val contentDataArrayList: ArrayList<Content>
    private val mcontext: Context

    // creating a constructor class.
    init {
        contentDataArrayList = recyclerDataArrayList
        this.mcontext = mcontext
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        // Inflate Layout
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.content_item, parent, false)
        return RecyclerViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        // Set the data to textview from our modal class.
        val modal: Content = contentDataArrayList[position]
        holder.title.text=modal.title
        Picasso.get().load(modal.picture).into(holder.picture);

        holder.itemView.setOnClickListener {
            val intent = Intent(mcontext, ContentDetails::class.java)
            intent.putExtra("title", modal.title)
            intent.putExtra("desc", modal.description)
            intent.putExtra("picture", modal.picture)
            mcontext.startActivity(intent)

        }
    }

    override fun getItemCount(): Int {
        // this method returns the size of recyclerview
        return contentDataArrayList.size
    }

    // View Holder Class to handle Recycler View.
    inner class RecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // creating variables for our views.
        public val title: TextView
        public val picture: ImageView


        init {
            // initializing our views with their ids.
            title = itemView.findViewById(R.id.title)
            picture = itemView.findViewById(R.id.picture)

        }
    }
}
