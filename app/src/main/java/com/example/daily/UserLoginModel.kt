package com.example.daily

class UserLoginModel {

    var name: String? = null
    var number: String? = null
    var email: String? = null
    var password : String? = null
    var imgUrl: String? = null
    var uid: String? = null


    constructor(name: String?, number: String?, email: String?, password : String?,imgUrl : String,uid : String) {
        this.name = name
        this.number = number
        this.email = email
        this.password = password
        this.imgUrl = imgUrl
        this.uid = uid
    }
   /* constructor(name: String?, number: String?, email: String?, password : String?,imgUrl : String) {
        this.name = name
        this.number = number
        this.email = email
        this.password = password
        this.imgUrl = imgUrl
    }
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
    constructor(name: String?, number: String?) {
        this.name = name
        this.number = number
    }*/

}


