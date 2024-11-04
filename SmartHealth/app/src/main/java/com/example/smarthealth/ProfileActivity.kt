package com.example.smarthealth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProfileActivity : AppCompatActivity() {

    private lateinit var achievementsRecyclerView: RecyclerView
    private lateinit var achievementsAdapter: AchievementsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize RecyclerView
        achievementsRecyclerView = findViewById(R.id.achievements_recycler_view)
        achievementsRecyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data
        val achievements = listOf("Completed 5 Workouts", "First Milestone Reached", "Top Achiever")

        achievementsAdapter = AchievementsAdapter(achievements)
        achievementsRecyclerView.adapter = achievementsAdapter
    }
}
