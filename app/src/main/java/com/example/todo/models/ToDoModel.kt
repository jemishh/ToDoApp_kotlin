package com.example.todo.models

import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator

class ToDoModel : Parcelable {
    var title: String? = null
    var todo: String? = null


    constructor() {}
    constructor(title: String?, todo: String?) {
        this.title = title
        this.todo = todo
    }

    protected constructor(`in`: Parcel) {
        title = `in`.readString()
        todo = `in`.readString()

    }

    override fun toString(): String {
        return "UserModel{" +
                "title='" + title + '\'' +
                ", todo='" + todo +
                '}'
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeString(title)
        parcel.writeString(todo)
    }

    companion object CREATOR : Creator<ToDoModel> {
        override fun createFromParcel(parcel: Parcel): ToDoModel {
            return ToDoModel(parcel)
        }

        override fun newArray(size: Int): Array<ToDoModel?> {
            return arrayOfNulls(size)
        }
    }


}