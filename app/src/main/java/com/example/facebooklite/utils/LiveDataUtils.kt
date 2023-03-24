package com.example.facebooklite.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

object LiveDataUtils {
    fun <T> observeOnce(liveData: LiveData<T>, observer: Observer<T>) {
        liveData.observeForever(object : Observer<T> {
            override fun onChanged(t: T) {
                liveData.removeObserver(this)
                observer.onChanged(t)
            }
        })
    }
}