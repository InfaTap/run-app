package com.example.auth.presentation.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.auth.domain.AuthRepository
import com.example.auth.domain.UserDataValidator
import com.example.core.domain.util.DataError
import com.example.core.domain.util.Result
import com.example.core.presentation.ui.UiText
import com.example.core.presentation.ui.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authRepository: AuthRepository,
    private val userDataValidator: UserDataValidator

    ): ViewModel() {

    var state by mutableStateOf(LoginState())
        private set

    private val eventChanel = Channel<LoginEvent>()
    val events = eventChanel.receiveAsFlow()

    init {
        combine(
            snapshotFlow {  state.email.text},
            snapshotFlow {  state.password.text}
        ) { email, password ->
            userDataValidator.isEmailValid(email.toString().trim()) && password.isNotEmpty()
        }.onEach { canLogin ->
            state = state.copy(canLogin = canLogin)
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnTogglePasswordVisibility -> {
                state = state.copy(isPasswordVisible = !state.isPasswordVisible)
            }
            is LoginAction.OnRegisterClick -> {
                // Handle register click
            }
            is LoginAction.OnLoginClick -> login()
        }
    }

    private fun login() {
        viewModelScope.launch{
            state = state.copy(
                isLogging =  true
            )
            val result = authRepository.login(state.email.text.toString(), password = state.password.text.toString())

            state = state.copy(isLogging = false)

            when(result) {
                is Result.Error -> {
                    if(result.error == DataError.Network.UNAUTHORIZED) {
                        eventChanel.send(LoginEvent.Error(UiText.DynamicString("Incorrect email or password")))
                    } else {
                        eventChanel.send(LoginEvent.Error(result.error.asUiText()))
                    }
                }
                is Result.Success -> eventChanel.send(LoginEvent.LoginSuccess)
            }
        }
    }

}