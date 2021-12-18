package com.example.webchatclient.ui.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.webchatclient.ui.data.net.ApiMapperImpl
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class LoginViewModel:ViewModel() {
    private val apiMapper = ApiMapperImpl()


    private val resultOfRegistrationLiveData:MutableLiveData<String> = MutableLiveData()
    private val resultOfLoginLiveData:MutableLiveData<Pair<String, String?>> = MutableLiveData()


    fun registerUser(login: String, password: String){
        apiMapper.registerNewUser(login, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    resultOfRegistrationLiveData.value=it
                },
                {
                    resultOfLoginLiveData.value=Pair("Server is not responding", null)
                })
    }

    fun loginUser(login: String, password: String){
        apiMapper.loginUser(login, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {
                    resultOfLoginLiveData.value=it
                },
                {
                    resultOfLoginLiveData.value=Pair("Server is not responding", null)
                })
    }

    fun getResultOfRegistrationLiveData():LiveData<String>{
        return resultOfRegistrationLiveData
    }

    fun getResultOfLoginLiveData():LiveData<Pair<String, String?>>{
        return resultOfLoginLiveData
    }

}