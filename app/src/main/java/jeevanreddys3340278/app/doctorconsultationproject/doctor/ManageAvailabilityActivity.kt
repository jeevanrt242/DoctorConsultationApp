package jeevanreddys3340278.app.doctorconsultationproject.doctor

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import jeevanreddys3340278.app.doctorconsultationproject.LocalUserData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun DoctorAvailabilityContainer(doctorId: String, onBack: () -> Unit) {

    var availableDays by remember { mutableStateOf<List<String>>(emptyList()) }
    var advanceDays by remember { mutableStateOf(7) }
    var leaves by remember { mutableStateOf<Set<String>>(emptySet()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        FirebaseDatabase.getInstance()
            .getReference("doctors")
            .child(doctorId)
            .get()
            .addOnSuccessListener { snapshot ->

                availableDays = snapshot.child("availableDays")
                    .children.mapNotNull { it.getValue(String::class.java) }

                advanceDays = snapshot
                    .child("availabilityConfig")
                    .child("advanceBookingDays")
                    .getValue(Int::class.java) ?: 7

                leaves = snapshot.child("leaves")
                    .children.mapNotNull { it.key }
                    .toSet()

                isLoading = false
            }
    }

    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier)
    } else {
        DoctorAvailabilityScreen(
            doctorId = doctorId,
            availableDays = availableDays,
            initialAdvanceDays = advanceDays,
            existingLeaves = leaves,
            onBack = onBack
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorAvailabilityScreen(
    doctorId: String,
    availableDays: List<String>,
    initialAdvanceDays: Int,
    existingLeaves: Set<String>,
    onBack: () -> Unit
) {

    val context = LocalContext.current


    var selectedDays by remember { mutableStateOf(availableDays.toMutableSet()) }
    var advanceDays by remember { mutableStateOf(initialAdvanceDays) }

    val leaves = remember {
        mutableStateListOf<String>().apply {
            addAll(existingLeaves)
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Doctor Availability",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {

            Button(
                onClick = {

                    val doctorRef = FirebaseDatabase.getInstance()
                        .getReference("doctors")
                        .child(doctorId)

                    doctorRef
                        .child("advanceBookingDays")
                        .setValue(advanceDays)

                    doctorRef.child("availableDays")
                        .setValue(selectedDays.toList())

                    Toast.makeText(
                        context,
                        "Availability updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(
                    "Save Changes",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Card(
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {

                    Text(
                        "Advance Booking Window",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )

                    Spacer(Modifier.height(12.dp))

                    Slider(
                        value = advanceDays.toFloat(),
                        onValueChange = { advanceDays = it.toInt() },
                        valueRange = 1f..30f,
                        steps = 28
                    )

                    Text(
                        "Patients can book up to $advanceDays days in advance",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Text(
                "Leaves",
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )

            Spacer(Modifier.height(8.dp))

            OutlinedButton(
                onClick = {
                    showDatePickerWithDisabledDates(
                        context = context,
                        disabledDates = leaves,
                        onDateSelected = { date ->
                            toggleLeave(doctorId, date)
                            leaves.add(date)
                        }
                    )

                }
            ) {
                Text("Add Leave Date")
            }



            Spacer(Modifier.height(12.dp))

            if (leaves.isNotEmpty()) {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    leaves.forEach { date ->

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color.Gray),
                            color = Color.White
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {

                                Text(
                                    text = date,
                                    fontSize = 14.sp
                                )

                                Spacer(Modifier.width(8.dp))

                                IconButton(
                                    onClick = {
                                        FirebaseDatabase.getInstance()
                                            .getReference("doctors")
                                            .child(doctorId)
                                            .child("leaves")
                                            .child(date)
                                            .removeValue()

                                        leaves.remove(date)
                                    },
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Leave",
                                        tint = Color.Gray,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}



fun showDatePickerWithDisabledDates(
    context: android.content.Context,
    disabledDates: List<String>,
    onDateSelected: (String) -> Unit
) {
    val calendar = Calendar.getInstance()
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    val dialog = DatePickerDialog(
        context,
        { _, year, month, day ->
            val selectedCal = Calendar.getInstance().apply {
                set(year, month, day)
            }
            val date = formatter.format(selectedCal.time)

            if (disabledDates.contains(date)) {
                Toast.makeText(
                    context,
                    "This date is already marked as leave",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                onDateSelected(date)
            }
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    dialog.datePicker.minDate = calendar.timeInMillis
    dialog.show()
}


fun toggleLeave(
    doctorId: String,
    date: String
) {
    val ref = FirebaseDatabase.getInstance()
        .getReference("doctors")
        .child(doctorId)
        .child("leaves")
        .child(date)

    ref.get().addOnSuccessListener {
        if (it.exists()) ref.removeValue()
        else ref.setValue(true)
    }
}


class ManageAvailabilityActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val doctorId = LocalUserData.getID(this)

            DoctorAvailabilityContainer(doctorId, onBack = {
                finish()
            })
        }
    }
}


