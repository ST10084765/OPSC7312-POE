package com.example.smarthealth

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AchievementsAdapter(private val achievements: List<String>) :
    RecyclerView.Adapter<AchievementsAdapter.AchievementViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        // Inflate the layout for each item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.achievement_item, parent, false)
        return AchievementViewHolder(view)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        // Bind the data to the views
        val achievement = achievements[position]
        holder.achievementTextView.text = achievement
    }

    override fun getItemCount(): Int {
        return achievements.size
    }

    class AchievementViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val achievementTextView: TextView = itemView.findViewById(R.id.achievement_name)
    }
}
