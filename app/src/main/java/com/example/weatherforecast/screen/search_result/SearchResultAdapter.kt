package com.example.weatherforecast.screen.search_result

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherforecast.R
import com.example.weatherforecast.util.DateUtil
import kotlinx.android.synthetic.main.item_search_result.view.*

/**
 * Created by viet on 1/20/21.
 */
class SearchResultAdapter(private var dataList: List<ForecastData>) : RecyclerView.Adapter<SearchResultAdapter.ViewHolder>() {

    data class ForecastData(
        val date: Long? = null,
        val averageTemperature: Float? = null,
        val pressure: Int? = null,
        val humidity: Int? = null,
        val description: String? = null
    )

    fun setData(dataList: List<ForecastData>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = dataList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_search_result, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: SearchResultAdapter.ViewHolder, position: Int) {
        holder.bindData(dataList[position], position)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindData(data: ForecastData, position: Int) {
            itemView.run {
                isr_date_tv.text = DateUtil.parseTimestampToString(data.date)
                isr_average_temp_tv.text = context.getString(R.string.celsius, data.averageTemperature)
                isr_pressure_tv.text = data.pressure?.toString()
                isr_humidity_tv.text = "${data.humidity}%"
                isr_description_tv.text = data.description

                isr_divider.visibility = if (position == dataList.size - 1) {
                    View.INVISIBLE
                } else {
                    View.VISIBLE
                }
            }
        }
    }
}