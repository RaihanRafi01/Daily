package com.example.daily

class UserLoginModel {
    var name: String?
    var number: String?
    var email: String?
    var password : String? = null

    constructor(name: String?, number: String?, email: String?, password : String?) {
        this.name = name
        this.number = number
        this.email = email
        this.password = password
    }
    constructor(name: String?, number: String?, email: String?) {
        this.name = name
        this.number = number
        this.email = email
    }

}