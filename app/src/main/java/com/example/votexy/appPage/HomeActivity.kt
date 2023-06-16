package com.example.votexy.appPage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.votexy.R
import com.example.votexy.database.db_helper
import com.example.votexy.initialApp.HasilVoteActivity
import com.example.votexy.initialApp.KameraActivity
import com.example.votexy.initialApp.MainActivity
import com.google.android.material.navigation.NavigationView

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var mToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        mToggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)

        drawer_layout.addDrawerListener(mToggle)
        mToggle.syncState()

        val menu_nav = findViewById<NavigationView>(R.id.nav_view)
        menu_nav.setNavigationItemSelectedListener(this)

        val button:Button = findViewById(R.id.button_candidat1)
        button.setOnClickListener {
            Toast.makeText(this, "Anda memilih kandidat 1", Toast.LENGTH_LONG).show()
        }

        val button2:Button = findViewById(R.id.button_candidat2)
        button2.setOnClickListener {
            Toast.makeText(this, "Anda memilih kandidat 2", Toast.LENGTH_LONG).show()
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return mToggle.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean{
        when(p0.itemId){
            R.id.nav_candidat -> {
                startActivity(Intent(this, InfoCandidatActivity::class.java))
                true
            }
            R.id.nav_hasil -> {
                startActivity(Intent(this, HasilVoteActivity::class.java))
                true
            }
            R.id.nav_profile -> {
                startActivity(Intent(this, AkunActivity::class.java))
                true
            }
            R.id.nav_exit -> {
                startActivity(Intent(this, MainActivity::class.java))
                true
            }
            R.id.nav_camera -> {
                startActivity(Intent(this, KameraActivity::class.java))
                true
            }
            else -> true
        }
        return true
    }
}