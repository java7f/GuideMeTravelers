package com.example.guidemetravelersapp.views.profileView

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.skydoves.landscapist.glide.GlideImage
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.format.DateTimeFormatter

class EditProfile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuideMeTravelersAppTheme {
                EditProfileContent()
            }
        }
    }
}

@Composable
fun EditProfileContent() {
    val name = remember { mutableStateOf(TextFieldValue()) }
    val lastname = remember { mutableStateOf(TextFieldValue()) }
    val username = remember { mutableStateOf(TextFieldValue()) }
    val email = remember { mutableStateOf(TextFieldValue()) }
    val phone = remember { mutableStateOf(TextFieldValue()) }
    val birthday = remember { mutableStateOf(TextFieldValue()) }
    val description = remember { mutableStateOf(TextFieldValue()) }
    var profileUri by remember { mutableStateOf<Uri?>(null) }

    val focusManager = LocalFocusManager.current
    val selectImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            uri: Uri? -> profileUri = uri
    }

    LazyColumn(modifier = Modifier
        .padding(20.dp)
        .fillMaxSize()) {
        item {
            /* Image selection */
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                content = {
                    Box(
                        content = {
                            if (profileUri == null) {
                                Image(
                                    painter = painterResource(R.drawable.dummy_avatar),
                                    contentDescription = "Temporal dummy avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.clip(CircleShape).size(160.dp)
                                )
                            } else {
                                profileUri?.let {
                                    GlideImage(
                                        imageModel = it,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.clip(CircleShape).size(160.dp)
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier.matchParentSize(),
                                contentAlignment = Alignment.TopEnd,
                                content = {
                                    Image(
                                        painter = painterResource(R.drawable.edit_circle),
                                        contentDescription = "Edit Image",
                                        colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
                                        modifier = Modifier
                                            .height(40.dp)
                                            .clickable { selectImageLauncher.launch("image/*") },
                                    )
                                }
                            )
                        }
                    )
                }
            )
            /* ------------- */
            Row(modifier = Modifier.padding(top = 20.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { value -> name.value = value },
                        label = { Text(text = stringResource(id = R.string.name_label)) },
                        textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = lastname.value,
                        onValueChange = { value -> lastname.value = value },
                        label = { Text(text = stringResource(id = R.string.lastname_label)) },
                        textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                        singleLine = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = username.value,
                onValueChange = { value -> username.value = value },
                label = { Text(text = stringResource(id = R.string.username_label)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = email.value,
                onValueChange = { value -> email.value = value },
                label = { Text(text = stringResource(id = R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = phone.value,
                onValueChange = { value -> phone.value = value },
                label = { Text(text = stringResource(id = R.string.phone_label)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            DateField(birthday)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = description.value,
                onValueChange = { value -> description.value = value },
                label = { Text(text = stringResource(id = R.string.description)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(10.dp))
            CustomDropDown()
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {/* TODO */},
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                            Text(stringResource(id = R.string.sign_up), color = Color.White)
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    GuideMeTravelersAppTheme {
        EditProfileContent()
    }
}

@Composable
fun DateField(birthday: MutableState<TextFieldValue>) {
    val dialog = MaterialDialog()

    dialog.build(
        buttons = {
            positiveButton(text = stringResource(id = R.string.ok))
            negativeButton(text = stringResource(id = R.string.cancel))
        },
        content = {
            datepicker { date ->
                val formattedDate = date.format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
                birthday.value = TextFieldValue(formattedDate)
            }
        }
    )
    Column(
        content = {
            ReadonlyTextField(
                value = birthday.value,
                onValueChange = { birthday.value = it },
                modifier = Modifier.fillMaxWidth(),
                onClick = { dialog.show() },
                label = { Text(text = stringResource(id = R.string.birthday)) }
            )
        }
    )
}

@Composable
fun CustomDropDown() {
    var expanded by remember { mutableStateOf(false) }
    val suggestions = listOf("Dominican Republic","United States","France")
    var selectedCountry by remember { mutableStateOf("") }
    var dropDownWidth by remember { mutableStateOf(0) }

    val icon =
        if (expanded)
            Icons.Default.ArrowDropUp
        else
            Icons.Default.ArrowDropDown

    Column(
        content = {
            Box(
                content = {
                    OutlinedTextField(
                        value = selectedCountry,
                        onValueChange = { selectedCountry = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onSizeChanged { dropDownWidth = it.width },
                        label = {Text(text = stringResource(id = R.string.country))},
                        trailingIcon = { Icon(icon,"country") }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .alpha(0f)
                            .clickable(onClick = { expanded = !expanded })
                    )
                }
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.width(with(LocalDensity.current){dropDownWidth.toDp()})
            ) {
                suggestions.forEach { label ->
                    DropdownMenuItem(
                        onClick = { selectedCountry = label },
                        content = { Text(text = label) },
                    )
                }
            }
        }
    )
}

@Composable
fun ReadonlyTextField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, modifier: Modifier = Modifier,
                      onClick: () -> Unit, label: @Composable () -> Unit) {
    Box(
        content = {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = modifier,
                label = label,
                leadingIcon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar") }
            )
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .alpha(0f)
                    .clickable(onClick = onClick)
            )
        }
    )
}