package com.example.auth.presentation.login

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.presentation.register.RegisterAction
import com.example.core.presentation.designsystem.CheckIcon
import com.example.core.presentation.designsystem.EmailIcon
import com.example.core.presentation.designsystem.Poppins
import com.example.core.presentation.designsystem.R
import com.example.core.presentation.designsystem.RunGray
import com.example.core.presentation.designsystem.RunTheme
import com.example.core.presentation.designsystem.components.GradientBackground
import com.example.core.presentation.designsystem.components.RunActionButton
import com.example.core.presentation.designsystem.components.RunPasswordTextField
import com.example.core.presentation.designsystem.components.RunTextField
import com.example.core.presentation.ui.ObserveAsEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun LoginScreenRoot(
    onLoginSuccess: () -> Unit,
    onSignUpClick: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current

    ObserveAsEvent(viewModel.events) { event ->
        when(event) {
            is LoginEvent.LoginSuccess -> {
                keyboardController?.hide()
                Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                onLoginSuccess()
            }
            is LoginEvent.Error -> {
                keyboardController?.hide()
                Toast.makeText(context, "Login failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    LoginScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {

                is LoginAction.OnRegisterClick -> onSignUpClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )

}

@Composable
private fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit
) {
    GradientBackground() {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(vertical = 32.dp)
            .padding(top = 16.dp)
        ) {
            Text(text = stringResource(R.string.hi_there),
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(text = stringResource(R.string.welcome_text),
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.padding(48.dp))

            RunTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = CheckIcon,
                hint = "test@example.com",
                title = stringResource(com.example.auth.presentation.R.string.email),
                modifier = Modifier.fillMaxWidth(),

            )
            Spacer(modifier = Modifier.padding(8.dp))
            RunPasswordTextField(
                state = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisible = {
                    onAction(LoginAction.OnTogglePasswordVisibility)
                },
                hint = "********",
                title = stringResource(com.example.auth.presentation.R.string.password),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(32.dp))
            RunActionButton(
                text = stringResource(com.example.auth.presentation.R.string.login),
                isLoading = state.isLogging,
                enabled = state.canLogin && !state.isLogging,
                onClick = {
                    onAction(LoginAction.OnLoginClick)
                }
            )
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontFamily = Poppins, color = RunGray)
                ){
                    append(stringResource(com.example.auth.presentation.R.string.dont_have_an_account) +" ")
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "signup",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    fontFamily = Poppins,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ),
                            linkInteractionListener = { onAction(LoginAction.OnRegisterClick) }
                        )
                    ){
                        append(stringResource(com.example.auth.presentation.R.string.login))
                    }
                }
            }
            Box(
                modifier = Modifier.align(Alignment.CenterHorizontally).weight(1f),
                contentAlignment = Alignment.BottomCenter
            ) {
                Text(text = annotatedString)
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {

    RunTheme {
        LoginScreen(
            state = LoginState(),
            onAction = {}
        )

    }

}