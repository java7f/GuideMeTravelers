package com.example.guidemetravelersapp.views.LoginView

import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.guidemetravelersapp.views.ExperienceDetailsView.ExperienceDetailsActivity
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.Services.AuthenticationService
import com.example.guidemetravelersapp.helperModels.ScreenStateEnum
import com.example.guidemetravelersapp.ui.theme.Pink200
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {
    private var uiState = mutableStateOf(ScreenStateEnum.SIGNED_OUT)
    private var authenticationService: AuthenticationService = AuthenticationService(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Username(this)
        }
    }

    override fun onStart() {
        super.onStart()
        if(authenticationService.isUserLoggedIn()){
            //uiState.value = ScreenStateEnum.SIGNED_IN
            authenticationService.signOut()
            //goToHomescreen()
        }
    }

    //region Login logic
    private suspend fun login(email: String, password: String) {
        uiState.value = ScreenStateEnum.IN_PROGRESS
        val result = authenticationService.login(email, password)
        if (result?.user != null) {
            uiState.value = ScreenStateEnum.SIGNED_IN
            goToHomescreen()
        }
        else {
            uiState.value = ScreenStateEnum.ERROR
        }
    }

    private fun goToHomescreen() {
        //TODO: CHANGE NAVIGATION TO HOME SCREEN
        startActivity(Intent(this, ExperienceDetailsActivity::class.java))
    }
    //endregion

    //region Composable methods
    @Composable
    fun Username(context: Context? = null) {
        var username = remember { mutableStateOf(TextFieldValue())}
        var password = remember { mutableStateOf(TextFieldValue())}

        com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme {
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
                    textStyle = TextStyle(color = MaterialTheme.colors.onSecondary, fontSize = 15.sp),
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
                    textStyle = TextStyle(color = MaterialTheme.colors.onSecondary, fontSize = 15.sp)
                )
                LoginButton(username.value.text, password.value.text)
            }
        }
    }

    @Composable
    fun LoginButton(email: String, password: String) {
        val coroutineScope = rememberCoroutineScope()
        Spacer(modifier = Modifier.height(50.dp))
        Row(horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = {
                    coroutineScope.launch {
                        login(email, password)
                    }
                },
                //If the login is in progress, disable the button and show spinner
                enabled = uiState.value != ScreenStateEnum.IN_PROGRESS,
                colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                    Text(stringResource(id = R.string.login_button), color = Color.White)
                    if (uiState.value == ScreenStateEnum.IN_PROGRESS) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier
                                .padding(horizontal = 10.dp)
                                .size(25.dp)
                        )
                    }
                }
            }
        }
    }
    //endregion

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun DefaultPreview2() {
        Username()
    }
}