package com.alept.exampletwo.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.alept.exampletwo.R
import com.alept.exampletwo.activities.AppBlockingActivity

// adapter used to show installed apps in the device
class AllAppsAdapter(private val context:Context,private val names:ArrayList<String>,private val drawable:ArrayList<Drawable>,private val packages:ArrayList<String>):RecyclerView.Adapter<AllAppsAdapter.AAViewHolder>() {

    inner class AAViewHolder(view:View):RecyclerView.ViewHolder(view){
        val name : TextView = view.findViewById(R.id.aaName)
        val appImg : ImageView = view.findViewById(R.id.aaImg)
        val container : CardView = view.findViewById(R.id.appContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AAViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.all_app_row,parent,false)
        return AAViewHolder(view)
    }

    override fun onBindViewHolder(holder: AAViewHolder, position: Int) {
        holder.name.text = names[holder.adapterPosition]
        holder.appImg.setImageDrawable(drawable[holder.adapterPosition])
        holder.container.setOnClickListener {
            val i = Intent(context,AppBlockingActivity::class.java)
            i.putExtra("name",names[holder.adapterPosition])
            i.putExtra("package",packages[holder.adapterPosition])
            i.putExtra("icon",getBitmapFromDrawable(drawable[holder.adapterPosition]))
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap {
        val bmp = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bmp)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bmp
    }
}