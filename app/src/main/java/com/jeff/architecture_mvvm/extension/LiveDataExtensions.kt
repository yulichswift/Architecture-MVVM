package com.jeff.architecture_mvvm.extension

import androidx.lifecycle.MutableLiveData

fun MutableLiveData<Boolean>.setNot() {
    this.value = this.value?.not()
}