package com.example.guidemetravelersapp.LoginView

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guidemetravelersapp.Logic.Authentication.MSGraphRequestWrapper
import com.example.guidemetravelersapp.Logic.Authentication.UserAuthentication
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.ui.theme.Pink200
import com.microsoft.identity.client.*
import com.microsoft.identity.client.exception.MsalException
import org.json.JSONObject

class LoginActivity : ComponentActivity() {
    private val TAG: String = LoginActivity::class.java.simpleName

    /* Azure AD Variables */
    private var mSingleAccountApp: ISingleAccountPublicClientApplication? = null
    private lateinit var mAccount: IAccount
    var graphApiResult = mutableStateOf("")
    val activity: Activity = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        UserAuthentication.CreatePublicClientApplication(this, onPublicClientApplication, onAccountLoaded)
        setContent {
            GuideMeTravelersAppTheme(){
                Column {
                    Username()
                    LoginButton(
                        activity = activity,
                        onSuccessfulAuthentication = { onSuccessfulAuthentication },
                        onGraphApiSuccess = { onGraphApiSuccess })
                    DisplayResult(response = graphApiResult.value)
                }
            }
        }
    }

    val onPublicClientApplication : (ISingleAccountPublicClientApplication) -> Unit = { application ->
        mSingleAccountApp = application
    }

    val onAccountLoaded : (IAccount?) -> Unit = { account ->
        if (account != null) {
            mAccount = account
        }
    }

    val onSuccessfulAuthentication : (IAccount) -> Unit = { account ->
        mAccount = account
    }

    val onGraphApiSuccess : (String) -> Unit = { response ->
        graphApiResult.value = response
    }

    //region Composable methods
    @Composable
    fun Username() {
        val typography = MaterialTheme.typography
        var username = remember { mutableStateOf(TextFieldValue())}
        var password = remember { mutableStateOf(TextFieldValue())}

        Column(modifier = Modifier.padding(10.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Box(modifier = Modifier.size(220.dp), contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(R.drawable.transparent_logo_drawable),
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

        }
    }

    @Composable
    fun LoginButton(
        activity: Activity,
        onSuccessfulAuthentication: (IAccount) -> Unit,
        onGraphApiSuccess: (String) -> Unit
    ) {
        Spacer(modifier = Modifier.height(50.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    UserAuthentication.SignIn(activity, activity, mSingleAccountApp, onSuccessfulAuthentication, onGraphApiSuccess)
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

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun DefaultPreview2() {
        Username()
    }
//endregion
}