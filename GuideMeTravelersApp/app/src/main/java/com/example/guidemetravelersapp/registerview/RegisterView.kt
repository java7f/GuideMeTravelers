package com.example.guidemetravelersapp.registerview

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme


class RegisterView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RegisterScreenContent()

            /* TODO: add keyboardOptions & keyboardActions to textfield */
        }
    }
}

@Composable
fun RegisterScreenContent() {
    GuideMeTravelersAppTheme(){
        val username = remember { mutableStateOf(TextFieldValue()) }
        val password = remember { mutableStateOf(TextFieldValue()) }
        val name = remember { mutableStateOf(TextFieldValue()) }
        val lastname = remember { mutableStateOf(TextFieldValue()) }
        val email = remember { mutableStateOf(TextFieldValue()) }
        Column(modifier = Modifier.padding(20.dp)){
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Text(
                    text = stringResource(id = R.string.register_header),
                    style = typography.h4,
                    color = MaterialTheme.colors.primaryVariant,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }
            Row(modifier = Modifier.padding(top = 30.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text(text = stringResource(id = R.string.name_label)) },
                        textStyle = TextStyle(color = MaterialTheme.colors.secondary)
                    )
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = lastname.value,
                        onValueChange = { lastname.value = it },
                        label = { Text(text = stringResource(id = R.string.lastname_label)) },
                        textStyle = TextStyle(color = MaterialTheme.colors.secondary)
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text(text = stringResource(id = R.string.username_label)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.secondary)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(

                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(text = stringResource(id = R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.secondary)
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(text = stringResource(id = R.string.password_label)) },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.secondary)
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(id = R.string.sign_in), color = Color.White)
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = MaterialTheme.colors.onPrimary, thickness = 2.dp)
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