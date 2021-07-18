package com.example.guidemetravelersapp.LoginView

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.guidemetravelersapp.ui.theme.AcceptGreen
import com.example.guidemetravelersapp.ui.theme.CancelRed

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Username()
        }
    }
}

@Composable
fun Username() {
    GuideMeTravelersAppTheme(){
        val typography = MaterialTheme.typography
        var username = remember { mutableStateOf(TextFieldValue())}
        var password = remember { mutableStateOf(TextFieldValue())}
        Column(modifier = Modifier.padding(20.dp)){
            Text(
                text = "GUIDE ME LOGO",
                style = typography.h3, color = MaterialTheme.colors.primary
            )
            Spacer(modifier = Modifier.height(60.dp))
            Text(
                text = "Username",
                style = typography.h6, color = MaterialTheme.colors.onPrimary
            )
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text(text = "Username") },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onPrimary)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = stringResource(id = R.string.password_label),
                style = typography.h6, color = MaterialTheme.colors.onPrimary
            )
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = stringResource(id = R.string.password_label)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onPrimary)
            )
            Spacer(modifier = Modifier.height(50.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.login_button), color = Color.White)
                }
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DefaultPreview2() {
    Username()
}

fun dummy() {

}