package com.example.votexy.database

class User {
    var nama : String = ""
    var nim : String = ""
    var email : String = ""
    var password : String = ""

    constructor(nama : String, nim: String,email: String, password : String ){
        this.nama = nama
        this.nim = nim
        this.email = email
        this.password = password
    }
}
