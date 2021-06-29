package com.example.guidemetravelersapp.RegisterView

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guidemetravelersapp.LoginView.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.ui.theme.Gray200

class RegisterView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreenContent()
        }
    }
}

@Composable
fun RegisterScreenContent() {
    GuideMeTravelersAppTheme(){
        var username = remember { mutableStateOf(TextFieldValue()) }
        var password = remember { mutableStateOf(TextFieldValue()) }
        var name = remember { mutableStateOf(TextFieldValue()) }
        var lastname = remember { mutableStateOf(TextFieldValue()) }
        var email = remember { mutableStateOf(TextFieldValue()) }
        Column(modifier = Modifier.padding(20.dp)){
            Text(
                text = stringResource(id = R.string.register_header),
                style = typography.h4,
                color = MaterialTheme.colors.primaryVariant
            )
            Row(modifier = Modifier.padding(top = 40.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.name_label),
                        style = typography.h6,
                        color = Gray200,

                    )
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text(text = stringResource(id = R.string.name_label)) },
                        textStyle = TextStyle(color = Gray200)
                    )
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.lastname_label),
                        style = typography.h6,
                        color = Gray200
                    )
                    OutlinedTextField(
                        value = lastname.value,
                        onValueChange = { lastname.value = it },
                        label = { Text(text = stringResource(id = R.string.lastname_label)) },
                        textStyle = TextStyle(color = Gray200)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.username_label),
                style = typography.h6,
                color = Gray200
            )
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text(text = stringResource(id = R.string.username_label)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Gray200)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.email_label),
                style = typography.h6,
                color = Gray200
            )
            OutlinedTextField(

                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = stringResource(id = R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Gray200)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(id = R.string.password_label),
                style = typography.h6, color = Gray200
            )
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = stringResource(id = R.string.password_label)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = Gray200)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.sign_in), color = Color.White)
                }
            }
            // TODO: add google, fb, login
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterPreview() {
    RegisterScreenContent()
}