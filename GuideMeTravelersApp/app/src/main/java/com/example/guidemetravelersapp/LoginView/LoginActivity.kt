package com.example.guidemetravelersapp.LoginView

import android.app.Activity
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.example.guidemetravelersapp.ExperienceDetailsView.ExperienceDetailsActivity
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.models.authentication.Auth
import com.example.guidemetravelersapp.models.authentication.AuthManager
import com.example.guidemetravelersapp.models.authentication.SharedPreferencesRepository
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.ui.theme.Pink200
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.CodeVerifierUtil

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Username(this)
        }
    }
}

//region Login logic
fun login(context: Context) {
    val authManager: AuthManager = AuthManager.getInstance(context)
    val authService: AuthorizationService = authManager.getAuthService()
    val auth: Auth = authManager.getAuth()

    val authRequestBuilder = AuthorizationRequest.Builder(
        authManager.getAuthConfig(),
        auth.clientId!!,
        auth.responseType!!,
        Uri.parse(auth.redirectUri)
    ).setScope(auth.scope)

    var codeVerifierUtil: String = CodeVerifierUtil.generateRandomCodeVerifier()
    val sharedPreferencesRepository: SharedPreferencesRepository = SharedPreferencesRepository(context)
    sharedPreferencesRepository.saveCodeVerifier(codeVerifierUtil)

    authRequestBuilder.setCodeVerifier(codeVerifierUtil)
    val authRequest: AuthorizationRequest = authRequestBuilder.build()

    var authIntent = Intent(context, ExperienceDetailsActivity::class.java)
    var pendingIntent = TaskStackBuilder.create(context).run {
        addNextIntentWithParentStack(authIntent)
        getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    try {
        authService.performAuthorizationRequest(
            authRequest,
            pendingIntent
        )
    } catch (e : Exception) {
        e.printStackTrace()
    }
}
//endregion

//region Composable methods
@Composable
fun Username(context: Context? = null) {
    val typography = MaterialTheme.typography
    var username = remember { mutableStateOf(TextFieldValue())}
    var password = remember { mutableStateOf(TextFieldValue())}

    GuideMeTravelersAppTheme {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Box(modifier = Modifier.size(220.dp), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.transparent_logo_drwable),
                        contentDescription = "Guide Me Logo",
                        colorFilter = ColorFilter.tint(Pink200)
                    )
                }
            }
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text(text = stringResource(id = R.string.username_label)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onPrimary),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary)
            )
            Spacer(modifier = Modifier.height(40.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = stringResource(id = R.string.password_label)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onPrimary)
            )
            LoginButton(context)
        }
    }
}

@Composable
fun LoginButton(
    context: Context? = null
) {
    Spacer(modifier = Modifier.height(50.dp))
    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        Button(
            onClick = {
                login(context!!)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(stringResource(id = R.string.login_button), color = Color.White)
        }
    }
}

@Composable
fun DisplayResult(response: String) {
    Text(text = response, color = MaterialTheme.colors.onPrimary)
}
//endregion

private class PreviewParams : PreviewParameterProvider<Context>{
    override val values: Sequence<Context>
        get() = sequenceOf()
}
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    Username()
}