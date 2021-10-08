package com.example.sbtechincaltest.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.hadilq.liveevent.LiveEvent

class LoginViewModel: ViewModel() {

    private var _loginViewState = LiveEvent<LoginModel>()
    val loginViewState = _loginViewState as LiveData<LoginModel>

    var username: String? = null
    var password: String? = null

    fun login(){

        if(username.isNullOrEmpty() || password.isNullOrEmpty()){
            _loginViewState.value = LoginModel(
                usernameError = getUsernameError(),
                passwordError = getPasswordError(),
            )
        } else {
            doLogin()
        }
    }

    private fun doLogin() {
        _loginViewState.value = LoginModel(loginSuccessful = true)
        resetLoginForm()

    }

    private fun resetLoginForm() {
        username = null
        password = null
    }

    private fun getPasswordError(): String? =
        if(password.isNullOrEmpty()) "Password must not be blank." else null


    private fun getUsernameError(): String? =
        if(username.isNullOrEmpty()) "Username must not be blank." else null

}

data class LoginModel(
    val usernameError: String? = null,
    val passwordError: String? = null,
    val loginSuccessful: Boolean? = null
)