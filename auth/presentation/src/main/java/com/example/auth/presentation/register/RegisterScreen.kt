package com.example.auth.presentation.register

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.auth.domain.PasswordValidationState
import com.example.auth.domain.UserDataValidator
import com.example.auth.presentation.R
import com.example.core.presentation.designsystem.CheckIcon
import com.example.core.presentation.designsystem.CrossIcon
import com.example.core.presentation.designsystem.EmailIcon
import com.example.core.presentation.designsystem.Poppins
import com.example.core.presentation.designsystem.RunDarkRed
import com.example.core.presentation.designsystem.RunGray
import com.example.core.presentation.designsystem.RunGreen
import com.example.core.presentation.designsystem.RunTheme
import com.example.core.presentation.designsystem.components.GradientBackground
import com.example.core.presentation.designsystem.components.RunActionButton
import com.example.core.presentation.designsystem.components.RunPasswordTextField
import com.example.core.presentation.designsystem.components.RunTextField
import com.example.core.presentation.ui.ObserveAsEvent
import org.koin.androidx.compose.koinViewModel

@Composable
fun RegisterScreenRoot(
    onSignInClick: () -> Unit,
    onSuccessfulRegistration: () -> Unit,
    viewModel: RegisterViewModel = koinViewModel()
) {
    val context = LocalContext.current
    val keyBoardController = LocalSoftwareKeyboardController.current
    ObserveAsEvent(viewModel.events) { event ->
        when (event) {
            is RegisterEvent.Error -> {
                keyBoardController?.hide()
                Toast.makeText(context, event.error.asString(context), Toast.LENGTH_LONG).show()
            }
            RegisterEvent.RegistrationSuccess ->{
                keyBoardController?.hide()
                Toast.makeText(context, "Huury", Toast.LENGTH_LONG).show()
                onSuccessfulRegistration()
            }
        }
    }


    RegisterScreen(
        state = viewModel.state,
        onAction = { action ->
            if (action is RegisterAction.OnLoginClick) {
                onSignInClick()
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
private fun RegisterScreen(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit
) {
    GradientBackground {
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(vertical = 32.dp)
            .padding(top = 16.dp)

            ) {
            Text(
                color = MaterialTheme.colorScheme.onBackground,
                text = stringResource(R.string.create_account),
                style =MaterialTheme.typography.headlineLarge,
                )
            val annotatedString = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(fontFamily = Poppins, color = RunGray)
                ){
                    append(stringResource(R.string.already_have_an_account) +" ")
                    withLink(
                        LinkAnnotation.Clickable(
                            tag = "login",
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    fontFamily = Poppins,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            ),
                            linkInteractionListener = { onAction(RegisterAction.OnLoginClick) }
                        )
                    ){
                        append(stringResource(R.string.login))
                    }
                }
            }
            Text(text = annotatedString)
            Spacer(modifier = Modifier.height(45.dp))

            RunTextField(
                state = state.email,
                startIcon = EmailIcon,
                endIcon = if (state.isEmailValid) null else null,
                hint = stringResource(R.string.example_email),
                title = stringResource(R.string.email),
                modifier = Modifier.fillMaxWidth(),
                additionalInfo = stringResource(R.string.invalid_email),
                keyBoardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(16.dp))
            RunPasswordTextField(
                state = state.password,
                isPasswordVisible = state.isPasswordVisible,
                onTogglePasswordVisible = { onAction(RegisterAction.OnTogglePasswordVisibilityClick) },
                hint = stringResource(R.string.password),
                title = stringResource(R.string.password),
                modifier = Modifier.fillMaxWidth()
            )

            PasswordRequirements(
                text = stringResource(R.string.password_requirement_length, UserDataValidator.MIN_PASSWORD_LENGTH),
                isValid = state.passwordValidationState.hasMinLength,
                modifier = Modifier.padding(top = 16.dp)
            )


            PasswordRequirements(
                text = stringResource(R.string.at_least_one_number, UserDataValidator.MIN_PASSWORD_LENGTH),
                isValid = state.passwordValidationState.hasNumber,
                modifier = Modifier.padding(top = 8.dp)
            )

            PasswordRequirements(
                text = stringResource(R.string.contains_lower_case, UserDataValidator.MIN_PASSWORD_LENGTH),
                isValid = state.passwordValidationState.hasLowerCase,
                modifier = Modifier.padding(top = 8.dp)
            )

            PasswordRequirements(
                text = stringResource(R.string.contains_upper_case, UserDataValidator.MIN_PASSWORD_LENGTH),
                isValid = state.passwordValidationState.hasUppercase,
                modifier = Modifier.padding(top = 8.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            RunActionButton(
                text = stringResource(R.string.register),
                onClick = { onAction(RegisterAction.OnRegisterClick) },
                enabled = state.canRegister,
                modifier = Modifier.fillMaxWidth(),
                isLoading = state.isRegistering,
            )


        }
    }
}

@Composable
fun PasswordRequirements(
    text: String,
    isValid: Boolean,
    modifier: Modifier = Modifier) {

    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = if (!isValid) CrossIcon else CheckIcon,
            contentDescription = null,
            tint = if (isValid) RunGreen else RunDarkRed,
            modifier = Modifier.padding(end = 8.dp))

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 14.sp)


    }

}

@Preview
@Composable
private fun PreviewRegisterScreen() {
    RunTheme {
        RegisterScreen(
            state = RegisterState(
                passwordValidationState = PasswordValidationState(
                    hasMinLength = true,
                    hasNumber = true,
                    hasLowerCase = true,
                    hasUppercase = true
                ),
            ),
            onAction = {}
        )
    }
}
