package com.trailsoffroad.assessment.screens

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.trailsoffroad.assessment.R
import com.trailsoffroad.assessment.models.UserProfile
import com.trailsoffroad.assessment.ui.theme.Typography
import com.trailsoffroad.assessment.viewmodels.EditProfileViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

@Composable
fun EditProfileScreen() {
    val editProfileViewModel: EditProfileViewModel = hiltViewModel()
    val userProfile: State<UserProfile> = editProfileViewModel.userProfile.collectAsState() // we can fill this fetched data in the form

    var fullName by remember { mutableStateOf("") }
    var birthday by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var checkedState by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            editProfileViewModel.getProfile()
        }
    }


    val context = LocalContext.current

    Column(modifier = Modifier
        .padding(22.dp)
        .fillMaxSize(), verticalArrangement = Arrangement.SpaceEvenly) {
        Text(modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(0.dp, 0.dp, 0.dp, 20.dp), text = stringResource(R.string.edit_profile), fontWeight = FontWeight.Bold)
        Image(modifier = Modifier
            .size(130.dp)
            .clip(CircleShape)
            .align(Alignment.CenterHorizontally)
            .scale(1.2f),
            painter = painterResource(id = R.drawable.pic), contentDescription = "profilePic",
            contentScale = ContentScale.Crop, alignment = Alignment.Center)
        Text(modifier = Modifier.padding(0.dp, 18.dp), text = stringResource(R.string.about_me), fontSize = 25.sp, fontWeight = FontWeight.Bold)
        CustomTextField(stringResource(R.string.full_name), fullName, {fullName = it})
        CustomTextField(stringResource(R.string.birthday), birthday, {birthday = it}, Icons.Filled.CalendarMonth, {
            val calendar = Calendar.getInstance(TimeZone.getDefault())
            DatePickerDialog(context,
                { p0, year, month, day ->
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH,month)
                    calendar.set(Calendar.DAY_OF_MONTH,day)

                    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.UK)
                    birthday = sdf.format(calendar.time)
                }, calendar[Calendar.YEAR], calendar[Calendar.MONTH], calendar[Calendar.DAY_OF_MONTH]).show()
        })
        CustomTextField(stringResource(R.string.e_mail), email, {email = it})
        CustomTextField(stringResource(R.string.phone), phone, {phone = it}, Icons.Filled.KeyboardArrowDown)
        Row(modifier = Modifier
            .fillMaxWidth()
            .clickable { checkedState = !checkedState },
            verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = checkedState, onCheckedChange = { checkedState = it },
                colors = CheckboxDefaults.colors(checkedColor = Color.Blue))
            Text(text = stringResource(R.string.i_want_to_subscribe_to_a_newsletter))
        }

        var inProgress by remember { mutableStateOf(false) }
        Button(modifier = Modifier.fillMaxWidth(), onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                inProgress = true
                editProfileViewModel.setProfile(UserProfile(fullName, birthday, email, phone, subscribeToNewsLetter = true)){
                    inProgress = false
                    Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                }
            }
        }, shape = RoundedCornerShape(10),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White)) {
            Text(text = if (inProgress) stringResource(R.string.saving)  else stringResource(R.string.save_changes), color = Color.White)
        }
    }
}

@Composable fun CustomLabel(text: String) {
    Text(text = text, style = Typography.labelLarge)
}

@Composable fun CustomTextField(label: String, text: String, onValueChange: (String) -> Unit, trailingImage: ImageVector? = null,
                                onClick: (() -> Unit)? = null) {

    val trailingIcon: @Composable (() -> Unit)? = if (trailingImage != null) {
        { Icon(imageVector = trailingImage, contentDescription = "") }
    } else null
    Column {
        CustomLabel(label)
        TextField(modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 5.dp)
            .background(Color.Transparent)
            .onFocusChanged {
                if (it.isFocused) onClick?.invoke()
            }, value = text, onValueChange = onValueChange, shape = RoundedCornerShape(10), trailingIcon = trailingIcon,
            colors = TextFieldDefaults.colors(unfocusedIndicatorColor = Color.White, unfocusedTrailingIconColor = Color.Blue))
    }
}

@Preview(showBackground = true, showSystemUi = true) @Composable
fun MainPreview() {
    EditProfileScreen()
}