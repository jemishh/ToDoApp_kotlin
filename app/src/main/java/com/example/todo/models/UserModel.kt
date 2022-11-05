package com.example.todo.models

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator

class UserModel : Parcelable {
    var userId: String? = null
    var email: String? = null
    var name: String? = null
    var dob: String? = null
    var age: String? = null

    constructor() {}
    constructor(user_id: String?, email: String?, name: String?,dob: String?,age: String?) {
        userId = user_id
        this.email = email
        this.name = name
        this.dob = dob
        this.age = age
    }

    protected constructor(`in`: Parcel) {
        userId = `in`.readString()
        email = `in`.readString()
        name = `in`.readString()
        dob = `in`.readString()
        age = `in`.readString()
    }

    override fun toString(): String {
        return "UserModel{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", dob='" + dob + '\'' +
                ", age=" + age +
                '}'
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(userId)
        parcel.writeString(email)
        parcel.writeString(name)
        parcel.writeString(dob)
        parcel.writeString(age)
    }

    companion object CREATOR : Creator<UserModel> {
        override fun createFromParcel(parcel: Parcel): UserModel {
            return UserModel(parcel)
        }

        override fun newArray(size: Int): Array<UserModel?> {
            return arrayOfNulls(size)
        }
    }


}