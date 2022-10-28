package com.example.registarlijekova


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.Locale

class MyAdapter(var listOfMedicaments: List<Info?>, clickListener: ClickListener) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>(), Filterable {

    private var clickListener: ClickListener = clickListener
    var medicamentFilterList: List<Info?> = listOf()

    init {
        medicamentFilterList = listOfMedicaments
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name: TextView = itemView.findViewById(R.id.name)
        val title: TextView = itemView.findViewById(R.id.atc)
        val color: View = itemView.findViewById(R.id.view)
        val category: TextView = itemView.findViewById(R.id.category)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val list = medicamentFilterList?.get(position)
        println("Nova filtritana: " + medicamentFilterList)
        holder.category.text = list?.categoryNameInfo
        holder.name.text = list?.nameInfo
        holder.title.text = list?.atcInfo + "  -"
        holder.color.setBackgroundColor(Color.parseColor(list?.colorInfo))

        holder.itemView.setOnClickListener {
            clickListener.clickeditem(list)
        }
    }

    override fun getItemCount(): Int {
        return medicamentFilterList.size
    }

    interface ClickListener {
        fun clickeditem(userModel: Info?)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    medicamentFilterList = listOfMedicaments as ArrayList<Info?>
                } else {
                    val resultList = ArrayList<Info?>()
                    for (row in listOfMedicaments) {
                        if (row?.nameInfo?.lowercase(Locale.ROOT)!!
                                .contains(charSearch.lowercase(Locale.ROOT))
                        ) {
                            resultList.add(row)
                        }
                    }
                    medicamentFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = medicamentFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                medicamentFilterList = results?.values as ArrayList<Info?>
                notifyDataSetChanged()
            }

        }
    }
}