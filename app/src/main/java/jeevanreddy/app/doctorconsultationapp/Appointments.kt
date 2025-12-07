package jeevanreddy.app.doctorconsultationapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@Composable
fun Appointments()
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.SlateBlue))
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Image(painter = painterResource(id = R.drawable.outline_arrow_back_24),
                contentDescription = "Doctor App",
                modifier = Modifier
                    .size(34.dp)
                    .clickable {
//                        val intent = Intent(context, ::class.java)
//                        context.startActivity(intent)
                    }

            )

            Spacer(modifier= Modifier.width(12.dp))

            Text(
                text = "Appointment",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier. height(12.dp))

        DatePicker()

        Spacer(modifier = Modifier. height(12.dp))

        PatientDetailsCard(R.drawable.doctor_profile,"Rakesh","Male","25")
        PatientDetailsCard(R.drawable.doctor_profile,"Lovely","Female","20")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker() {
    // Whether the date picker dialog is visible
    var showDatePicker by remember { mutableStateOf(false) }

    // Store the selected date
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }

    // State for the Material 3 DatePicker
    val datePickerState = rememberDatePickerState()

    // Automatically update when user selects a date
    LaunchedEffect(datePickerState.selectedDateMillis) {
        datePickerState.selectedDateMillis?.let { millis ->
            selectedDate = Instant.ofEpochMilli(millis)
                .atZone(ZoneId.systemDefault())
                .toLocalDate()
            showDatePicker = false // auto-close after selecting
        }
    }

    OutlinedTextField(
        value = selectedDate?.format(DateTimeFormatter.ofPattern("dd MMM yyyy")) ?: "",
        onValueChange = {},
        placeholder = { Text("Select Date") },
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Pick Date",
                tint = Color.Gray,
                modifier = Modifier.clickable { showDatePicker = true }
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            cursorColor = Color.Gray

            ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),

        )

    // Date Picker Dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {}, // no confirm button
            dismissButton = {}
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


@Composable
fun PatientDetailsCard(
    patientImage: Int,
    patientName: String,
    patientGender: String,
    patientAge: String,
    onViewClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = patientImage),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .size(62.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .shadow(6.dp, CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(3f)
            ) {
                Text(
                    text = patientName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Row() {
                    Text(
                        text = "Gender : ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        text = patientGender,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )

                }

                Row() {
                    Text(
                        text = "Age : ",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                    Text(
                        text = patientAge,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Black
                    )

                }


            }

            Text(
                text = "View",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f)
                    .background(
                        color = Color(0xFF6C63FF),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}



























@Preview(showBackground = true)
@Composable
fun AppointmentsPreview() {
    Appointments()
}