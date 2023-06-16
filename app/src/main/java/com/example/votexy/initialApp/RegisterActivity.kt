package com.example.votexy.initialApp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.votexy.R
import com.example.votexy.database.User
import com.example.votexy.database.db_helper

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val nama : EditText = findViewById(R.id.et_usernameregis)
        val nim : EditText = findViewById(R.id.et_nimregis)
        val email : EditText = findViewById(R.id.et_emailregis)
        val pw : EditText = findViewById(R.id.et_pwregis)
        val button1 = findViewById<Button>(R.id.btnregis)
        button1.setOnClickListener {
            if (nama.text.toString().isNotEmpty() and
                nim.text.toString().isNotEmpty() and
                email.text.toString().isNotEmpty() and
                pw.text.toString().isNotEmpty() )
            {
                val user = User(nama.text.toString(), nim.text.toString(), email.text.toString(), pw.text.toString())
                val db = db_helper(this)
                db.insertUser(user)

            }
            else{
                val toast = Toast.makeText(applicationContext, "Lengkapi data anda!", Toast.LENGTH_SHORT)
                toast.show()
            }
        }

        val button = findViewById<Button>(R.id.btnFoto)
        button.setOnClickListener {
            val intent = Intent(this, KameraActivity::class.java)
            startActivity(intent)
        }
    }
}