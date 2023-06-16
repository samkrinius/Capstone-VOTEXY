package com.example.votexy.initialApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.votexy.appPage.HomeActivity
import com.example.votexy.R
import com.example.votexy.database.db_helper

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val buttonLgn = findViewById<Button>(R.id.btnLogin)
        val username = findViewById<EditText>(R.id.username)
        val nim = findViewById<EditText>(R.id.nim)
        val pw = findViewById<EditText>(R.id.password)
        val db = db_helper(this)
        val user = "VotexFams"
//        val pass = 'admin123'
        buttonLgn.setOnClickListener {
            if (username.text.toString().isNotEmpty()  and pw.text.toString().isNotEmpty()) {
                val idUser = db.checkUser(username.text.toString(), pw.text.toString())
//                Toast.makeText(applicationContext,idUser, Toast.LENGTH_SHORT).show()
                if (username.text.toString() in user ){
                    val bundle = Bundle()
                    val intent = Intent(this, HomeActivity::class.java)
                    bundle.putString("iduser", idUser.toString())
                    intent.putExtras(bundle)
                    startActivity(intent)
                }else {
                    Toast.makeText(applicationContext, "Anda belum terdaftar", Toast.LENGTH_SHORT).show()
                }
            }
            else {
                Toast.makeText(applicationContext, "Lengkapi terlebih dahulu data anda", Toast.LENGTH_SHORT).show()
            }
        }

        val button2 = findViewById<Button>(R.id.btnsigup)
        button2.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}