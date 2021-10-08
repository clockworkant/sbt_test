package com.example.sbtechincaltest.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.nullValue
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

class LoginViewModelTest {
    private val passwordError = "Password must not be blank."
    private val usernameError = "Username must not be blank."

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    lateinit var sut: LoginViewModel

    @Mock
    private lateinit var observer: Observer<in LoginModel>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun loginWithNoDetails_showPasswordError_showUsernameError(){
        //Given
        sut = LoginViewModel()
        sut.loginViewState.observeForever(observer)

        //When
        sut.login()

        //Then
        val model = sut.loginViewState.value!!

        assertThat(model.loginSuccessful, nullValue())
        assertThat(model.passwordError, equalTo(passwordError))
        assertThat(model.usernameError, equalTo(usernameError))
    }

    @Test
    fun loginWithOnlyUsername_showPasswordError(){
        //Given
        sut = LoginViewModel()
        sut.loginViewState.observeForever(observer)
        sut.username = "spongebob"

        //When
        sut.login()

        //Then
        val model = sut.loginViewState.value!!

        assertThat(model.loginSuccessful, nullValue())
        assertThat(model.passwordError, equalTo(passwordError))
        assertThat(model.usernameError, nullValue())
    }

    @Test
    fun loginWithOnlyPassword_showUsernameError(){
        //Given
        sut = LoginViewModel()
        sut.loginViewState.observeForever(observer)
        sut.password = "password123"

        //When
        sut.login()

        //Then
        val model = sut.loginViewState.value!!

        assertThat(model.loginSuccessful, nullValue())
        assertThat(model.passwordError, nullValue())
        assertThat(model.usernameError, equalTo(usernameError))
    }

    @Test
    fun loginWithPasswordAndUsername_showSuccessfulLogin(){
        //Given
        sut = LoginViewModel()
        sut.loginViewState.observeForever(observer)
        sut.username = "spongebob"
        sut.password = "password123"

        //When
        sut.login()

        //Then
        val model = sut.loginViewState.value!!

        assertThat(model.loginSuccessful, equalTo(true))
        assertThat(model.passwordError, nullValue())
        assertThat(model.usernameError, nullValue())
    }
}