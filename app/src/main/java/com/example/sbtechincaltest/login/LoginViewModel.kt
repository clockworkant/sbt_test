package com.example.sbtechincaltest.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {

    private val _loginViewState = MutableLiveData<LoginViewState>()
    val loginViewState = _loginViewState as LiveData<LoginViewState>

    var username: String? = null
    var password: String? = null

    fun login(){

        if(username.isNullOrEmpty() || password.isNullOrEmpty()){
            _loginViewState.value = LoginViewState(
                usernameError = getUsernameError(),
                passwordError = getPasswordError(),
            )
        } else {
            doLogin()
        }
    }

    private fun doLogin() {
        _loginViewState.value = LoginViewState(loginSuccessful = true)
        _loginViewState.value = LoginViewState() //reset after reporting login successful
    }

    private fun getPasswordError(): String? =
        if(password.isNullOrEmpty()) "Password must not be blank." else null


    private fun getUsernameError(): String? =
        if(username.isNullOrEmpty()) "Username must not be blank." else null

}

data class LoginViewState(
    val usernameError: String? = null,
    val passwordError: String? = null,
    val loginSuccessful: Boolean? = null
)