package com.gimble.seekpot.feature.enquiry.presentation

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gimble.seekpot.R
import com.gimble.seekpot.feature.enquiry.domain.model.FoundItemData
import com.squareup.picasso.Picasso


class EnquiryAdapter(private val itemlist : List<FoundItemData>,private val context: Context) : RecyclerView.Adapter<EnquiryAdapter.MyViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemview = LayoutInflater.from(parent.context).inflate(R.layout.card_linkdata,parent,false)
        return MyViewHolder(itemview)
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentitem = itemlist[position]
        holder.names.text=currentitem.itemname
        holder.desc.text=currentitem.itemtype
        Picasso.get().load(currentitem.itempicture).into(holder.image)
        holder.time.text = currentitem.time
        holder.callbtn.setOnClickListener{makePhoneCall(context,"7483002017")}
    }

    //extra methods
    fun makePhoneCall(context: Context, number: String) {
        val intent = Intent(ACTION_CALL, Uri.parse("tel:" + number))
        context.startActivity(intent)
    }
    class MyViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {
        val names : TextView = itemview.findViewById(R.id.nameolink)
        val desc : TextView = itemview.findViewById(R.id.timeolink)
        val image : ImageView =itemview.findViewById(R.id.linkpicture)
        val status : TextView =itemview.findViewById(R.id.clickss)
        val contact : TextView =itemview.findViewById(R.id.linktextview)
        val callbtn : ImageView =itemview.findViewById(R.id.imageView4)
        val time : TextView = itemview.findViewById(R.id.textView7)

    }
}