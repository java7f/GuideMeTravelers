package com.example.guidemetravelersapp.views.profileView

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
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
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.guidemetravelersapp.R
import com.example.guidemetravelersapp.dataModels.User
import com.example.guidemetravelersapp.helpers.commonComposables.AutoCompleteTextView
import com.example.guidemetravelersapp.ui.theme.GuideMeTravelersAppTheme
import com.example.guidemetravelersapp.ui.theme.MilitaryGreen200
import com.example.guidemetravelersapp.viewModels.ProfileViewModel
import com.skydoves.landscapist.coil.CoilImage
import com.skydoves.landscapist.glide.GlideImage
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

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
fun EditProfileContent(profileViewModel: ProfileViewModel = viewModel()) {
    val name = remember { mutableStateOf(TextFieldValue(text = profileViewModel.editableUser.firstName)) }
    val lastname = remember { mutableStateOf(TextFieldValue(text = profileViewModel.editableUser.lastName)) }
    val phone = remember { mutableStateOf(TextFieldValue(text = profileViewModel.editableUser.phone)) }
    val birthday = remember { mutableStateOf(TextFieldValue(Instant.ofEpochMilli(profileViewModel.editableUser.birthdate.time).atZone(ZoneId.systemDefault()).toLocalDate().toString())) }
    val description = remember { mutableStateOf(TextFieldValue(text = profileViewModel.editableUser.aboutUser)) }
    var profileUri by remember { mutableStateOf<Uri?>(null) }
    val languages = remember { mutableStateOf(profileViewModel.editableUser.languages) }

    val textState = remember { mutableStateOf(TextFieldValue("")) }

    name.value = TextFieldValue(profileViewModel.editableUser.firstName)
    lastname.value = TextFieldValue(profileViewModel.editableUser.lastName)
    phone.value = TextFieldValue(profileViewModel.editableUser.phone)
    description.value = TextFieldValue(profileViewModel.editableUser.aboutUser)
    languages.value = profileViewModel.editableUser.languages
    birthday.value = TextFieldValue(Instant.ofEpochMilli(profileViewModel.editableUser.birthdate.time).atZone(ZoneId.systemDefault()).toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))

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
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                content = {
                    Box(
                        content = {
                            if (profileUri == null && profileViewModel.editableUser.profilePhotoUrl.isEmpty()) {
                                Image(
                                    painter = painterResource(R.drawable.dummy_avatar),
                                    contentDescription = "Temporal dummy avatar",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .clip(CircleShape)
                                        .size(160.dp)
                                )
                            } else if (profileViewModel.editableUser.profilePhotoUrl.isNotEmpty() && profileUri == null) {
                                Box(modifier = Modifier
                                    .size(160.dp)
                                    .border(2.dp, MilitaryGreen200, CircleShape)) {
                                    CoilImage(
                                        imageModel = profileViewModel.editableUser.profilePhotoUrl,
                                        contentDescription = "User profile photo",
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.clip(CircleShape)
                                    )
                                }
                            } else {
                                profileUri?.let {
                                    GlideImage(
                                        imageModel = it,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(160.dp),
                                        placeHolder = painterResource(R.drawable.dummy_avatar)
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
                        onValueChange = { value ->
                            name.value = value
                            profileViewModel.editableUser.firstName = value.text
                        },
                        label = { Text(text = stringResource(id = R.string.name_label)) },
                        textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Right) }),
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.weight(0.1f))
                Column(modifier = Modifier.weight(1f)) {
                    OutlinedTextField(
                        value = lastname.value,
                        onValueChange = { value ->
                            lastname.value = value
                            profileViewModel.editableUser.lastName = value.text
                        },
                        label = { Text(text = stringResource(id = R.string.lastname_label)) },
                        textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                        colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                        singleLine = true
                    )
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = profileViewModel.editableUser.email,
                onValueChange = {  },
                label = { Text(text = stringResource(id = R.string.email_label)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                singleLine = true,
                enabled = false
            )
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = phone.value,
                onValueChange = { value ->
                    phone.value = value
                    profileViewModel.editableUser.phone = value.text
                },
                label = { Text(text = stringResource(id = R.string.phone_label)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus()}),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(10.dp))
            DateField(birthday, profileViewModel.editableUser)
            Spacer(modifier = Modifier.height(10.dp))
            OutlinedTextField(
                value = description.value,
                onValueChange = { value ->
                    description.value = value
                    profileViewModel.editableUser.aboutUser = value.text
                },
                label = { Text(text = stringResource(id = R.string.description)) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = MaterialTheme.colors.onSecondary),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = MaterialTheme.colors.secondary),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
                maxLines = 5
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp)){
                if(profileViewModel.editableUser.languages != null) {
                    Text(
                        text = stringResource(id = R.string.languages) + ": ",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    for (lang in languages.value) {
                        Text(
                            text = "$lang, ",
                            style = MaterialTheme.typography.body1,
                            color = MaterialTheme.colors.onSecondary,
                        )
                    }
                }
            }
            Languages(profileViewModel = profileViewModel, languages)
            Spacer(modifier = Modifier.height(30.dp))
            Row(horizontalArrangement = Arrangement.Start, modifier = Modifier.fillMaxWidth()){
                if(profileViewModel.editableUser.address != null) {
                    Text(
                        text = stringResource(id = R.string.country) + ": ",
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                    Text(
                        text = profileViewModel.editableUser.address.country,
                        style = MaterialTheme.typography.body1,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onSecondary,
                        modifier = Modifier.padding(start = 15.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            SearchView(
                textState,
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                profileViewModel = profileViewModel
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(
                    onClick = {profileViewModel.saveProfileChange(profileUri)},
                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                    modifier = Modifier.fillMaxWidth(),
                    content = {
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
                            Text(stringResource(id = R.string.Save), color = Color.White)
                            if (profileViewModel.updateProfileResult.inProgress) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier
                                        .padding(horizontal = 10.dp)
                                        .size(25.dp)
                                )
                            }
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
fun DateField(birthday: MutableState<TextFieldValue>, user: User) {
    val dialog = MaterialDialog()

    dialog.build(
        buttons = {
            positiveButton(text = stringResource(id = R.string.ok))
            negativeButton(text = stringResource(id = R.string.cancel))
        },
        content = {
            datepicker(
                colors = DatePickerDefaults.colors(
                    headerTextColor = MaterialTheme.colors.onSecondary,
                    activeTextColor = Color.White
                )
            ) { date ->
                val formattedDate = date.format(
                    DateTimeFormatter.ofPattern("dd/MM/yyyy")
                )
                birthday.value = TextFieldValue(formattedDate)
                user.birthdate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
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
fun Languages(profileViewModel: ProfileViewModel, languages: MutableState<MutableList<String>>) {
    var expanded by remember { mutableStateOf(false) }
    val suggestions = mutableMapOf("English" to "EN","Español" to "ES","Français" to "FR", "Italiano" to "IT")
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
                        label = {Text(text = stringResource(id = R.string.languages))},
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
                        onClick = {
                            selectedCountry = label.key
                            if(label.key !in profileViewModel.editableUser.languages
                                && label.key !in languages.value) {
                                languages.value.add(label.value)
                                profileViewModel.editableUser.languages.add(label.value)
                            }
                            expanded = false
                        },
                        content = { Text(text = label.key) },
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
                leadingIcon = { Icon(imageVector = Icons.Default.DateRange, contentDescription = "Calendar") },
                textStyle = TextStyle(color = MaterialTheme.colors.onSecondary)
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

@Composable
fun SearchView(textState: MutableState<TextFieldValue>, modifier: Modifier, profileViewModel: ProfileViewModel) {

    AutoCompleteTextView(
        modifier = modifier,
        query = textState.value.text,
        queryLabel = stringResource(id = R.string.search_by_location),
        onQueryChanged = { updateAddress ->
            textState.value = TextFieldValue(updateAddress)
            profileViewModel.onQueryChanged(updateAddress)
        },
        predictions = profileViewModel.predictions,
        onClearClick = {
            textState.value = TextFieldValue("")
            profileViewModel.locationSearchValue = ""
            profileViewModel.predictions = mutableListOf()
        },
        onItemClick = { place ->
            textState.value = TextFieldValue(place.getPrimaryText(null).toString())
            profileViewModel.onPlaceItemSelected(place)
        }
    ) {
        Text("${it.getFullText(null)}", style = MaterialTheme.typography.caption)
    }
}