package com.example.guidemetravelersapp.views.registerview

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.*
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
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.User
import com.example.guidemetravelersapp.helpers.models.ScreenStateEnum
import com.example.guidemetravelersapp.services.AuthenticationService
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.ui.theme.Pink200
import com.example.guidemetravelersapp.views.loginView.LoginActivity
import kotlinx.coroutines.launch


class RegisterView : ComponentActivity() {
    private var uiState = mutableStateOf(ScreenStateEnum.DEFAULT)
    private lateinit var authenticationService: AuthenticationService
    override fun onCreate(savedInstanceState: Bundle?) {
        authenticationService = AuthenticationService(this)
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreenContent()
        }
    }

    suspend fun registerUser(user: User, password: String) {
        uiState.value = ScreenStateEnum.IN_PROGRESS
        val result = authenticationService.registerUser(user, password)
        if (result) {
            uiState.value = ScreenStateEnum.SUCCESS
            goToLogin()
        }
        else {
            uiState.value = ScreenStateEnum.ERROR
        }
    }

    private fun goToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    //region Composable
    @Composable
    fun RegisterScreenContent() {
        GuideMeTravelersAppTheme(){
            val newUser = remember { mutableStateOf(User()) }
            val username = remember { mutableStateOf(TextFieldValue()) }
            val password = remember { mutableStateOf(TextFieldValue()) }
            val name = remember { mutableStateOf(TextFieldValue()) }
            val lastname = remember { mutableStateOf(TextFieldValue()) }
            val email = remember { mutableStateOf(TextFieldValue()) }
            val coroutineScope = rememberCoroutineScope()
            Column(modifier = Modifier.padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())){
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(150.dp), contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(R.drawable.transparent_logo_drwable),
                            contentDescription = "Guide Me Logo",
                            colorFilter = ColorFilter.tint(Pink200)
                        )
                    }
                }
                Row() {
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = name.value,
                            onValueChange = { name.value = it
                                            newUser.value.firstName = it.text},
                            label = { Text(text = stringResource(id = R.string.name_label)) },
                            textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary)
                        )
                    }
                    Spacer(modifier = Modifier.weight(0.1f))
                    Column(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = lastname.value,
                            onValueChange = { lastname.value = it
                                newUser.value.lastName = it.text },
                            label = { Text(text = stringResource(id = R.string.lastname_label)) },
                            textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                            colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = username.value,
                    onValueChange = { username.value = it
                        newUser.value.username = it.text },
                    label = { Text(text = stringResource(id = R.string.username_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary)
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(

                    value = email.value,
                    onValueChange = { email.value = it
                        newUser.value.email = it.text },
                    label = { Text(text = stringResource(id = R.string.email_label)) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary)
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    value = password.value,
                    onValueChange = { password.value = it },
                    label = { Text(text = stringResource(id = R.string.password_label)) },
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary)
                )
                Spacer(modifier = Modifier.height(20.dp))
                Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                    Button(
                        onClick = {
                            coroutineScope.launch {
                                registerUser(newUser.value, password.value.text)
                            }
                        },
                        enabled = uiState.value != ScreenStateEnum.IN_PROGRESS,
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                            Text(stringResource(id = R.string.sign_up), color = Color.White)
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
                Spacer(modifier = Modifier.height(20.dp))
                Divider(color = MaterialTheme.colors.onPrimary, thickness = 1.dp)
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    Text(
                        text = stringResource(id = R.string.or),
                        style = typography.h6,
                        color = MaterialTheme.colors.onPrimary,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    IconButton(
                        onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.google),
                            contentDescription = "Google",
                            tint = Color.Unspecified
                        )
                    }
                    IconButton(
                        onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.facebook),
                            contentDescription = "Facebook",
                            tint = Color.Unspecified
                        )
                    }
                    IconButton(
                        onClick = { /*TODO*/ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.twitter),
                            contentDescription = "Twitter",
                            tint = Color.Unspecified
                        )
                    }
                }
            }
        }
    }
    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun RegisterPreview() {
        RegisterScreenContent()
    }
    //endregion
}

