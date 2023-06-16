package com.example.votexy.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class db_helper (var context: Context) : SQLiteOpenHelper(context, "voting1", null, 1) {

    private val db = this.writableDatabase
    override fun onCreate(db: SQLiteDatabase?) {
        val createTableUser = "CREATE TABLE user ( " +
                "id_user INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nama VARCHAR(50), " +
                "nim VARCHAR(50), " +
                "email VARCHAR(50), " +
                "password VARCHAR(50)); "

        val insertDataUser = "INSERT INTO user (nama, nim, email, password)VALUES ('Admin', '1234567890', 'admin', 'admin');"

        db?.execSQL(createTableUser)
        db?.execSQL(insertDataUser)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }

    fun insertUser (user: User){
        val cv =ContentValues()
        cv.put("nama", user.nama)
        cv.put("nim", user.nim)
        cv.put("email", user.email)
        cv.put("password", user.password)

        val result = db.insert("user", null, cv)
        if (result == (-1).toLong())
            Toast.makeText(context, "Gagal membuat akun", Toast.LENGTH_SHORT).show()
        else
            Toast.makeText(context, "Berhasil menambahkan akun", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("Range")
    fun checkUser(nama: String, password: String): Int{
        val query = "SELECT * FROM user WHERE nama='$nama=' AND password='$password'"
        val rs = db.rawQuery(query, null)
        if (rs.moveToFirst()) {
            val idUser = rs.getInt(rs.getColumnIndex("id_user"))
//            Toast.makeText(context,idUser, Toast.LENGTH_SHORT).show()
            rs.close()
            return idUser
        }
        return -1
    }

    fun getUsername (id : String) : String {
        val query = "SELECT nama FROM user WHERE id_user = $id"
        val rs = db.rawQuery(query, null)
        if (rs.moveToFirst()) {
            val idUser = rs.getString(0)
            rs.close()
            return idUser
        }
        return ""
    }

}