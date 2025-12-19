package jeevanreddy.app.doctorconsultationapp.patient


import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.FirebaseDatabase
import jeevanreddy.app.doctorconsultationapp.UserPrefs
import jeevanreddy.app.doctorconsultationapp.data.DoctorViewModel
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.UUID


class BookAppointmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val viewModel: DoctorViewModel = viewModel()
            val doctors = viewModel.doctors

            // Show UI only when data is loaded
            if (doctors.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                BookAppointmentScreen(doctors, onBack = {
                    finish()
                })
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookAppointmentScreen(
    doctors: List<Doctor>,
    onBack: () -> Unit = {}
) {

    var selectedDoctor by remember { mutableStateOf<Doctor?>(null) }

    // Dialog controls
    var showDateDialog by remember { mutableStateOf(false) }
    var showPatientDialog by remember { mutableStateOf(false) }

    // Appointment data
    var selectedDate by remember { mutableStateOf("") }
    var patientName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Book Appointment",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // Doctor selection title
            item {
                Text("Select Doctor", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(10.dp))
            }

            // Doctor list
            items(doctors) { doctor ->

                DoctorCard(
                    doctor = doctor,
                    onSelect = {
                        selectedDoctor = doctor
                        showDateDialog = true
                    }
                )

            }
        }

        // ---------------------------
        // DATE SELECTION DIALOG
        // ---------------------------
        if (showDateDialog) {
            AlertDialog(
                onDismissRequest = { showDateDialog = false },
                title = {
                    Text(
                        "Select Appointment Date",
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {

                    val calendar = Calendar.getInstance()
                    val maxCalendar = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_MONTH, selectedDoctor!!.advanceBookingDays)
                    }

                    AndroidView(
                        factory = { context ->
                            DatePicker(context).apply {

                                // ✅ Today onwards
                                minDate = calendar.timeInMillis

                                // ✅ Advance booking limit
                                maxDate = maxCalendar.timeInMillis

                                Log.e("Test","$selectedDoctor - ABD : ${selectedDoctor!!.advanceBookingDays}")

                                init(
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                ) { _, y, m, d ->

                                    val pickedDate = formatDate(y, m, d)

                                    // 🚫 Leave date check
                                    if (selectedDoctor!!.leaves.containsKey(pickedDate)) {
                                        Toast.makeText(
                                            context,
                                            "Doctor is on leave on this date",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        selectedDate = ""
                                    } else {
                                        selectedDate = pickedDate
                                    }
                                }
                            }
                        }
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (selectedDate.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Please select a valid date",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                showDateDialog = false
                                showPatientDialog = true
                            }
                        }
                    ) {
                        Text("Next")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDateDialog = false }) {
                        Text("Cancel")
                    }
                }
            )

        }

        // ---------------------------
        // PATIENT DETAILS DIALOG
        // ---------------------------
        if (showPatientDialog) {

            AlertDialog(
                onDismissRequest = { showPatientDialog = false },
                title = { Text("Patient Details", fontWeight = FontWeight.Bold) },
                text = {

                    Column {

                        // NAME INPUT
                        OutlinedTextField(
                            value = patientName,
                            onValueChange = { patientName = it },
                            label = { Text("Patient Name") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(12.dp))

                        // GENDER SELECTOR (Chips)
                        Text("Gender", fontWeight = FontWeight.Medium)
                        Spacer(Modifier.height(6.dp))

                        Row {
                            GenderChip("Male", gender) { gender = it }
                            Spacer(Modifier.width(8.dp))
                            GenderChip("Female", gender) { gender = it }
                            Spacer(Modifier.width(8.dp))
                            GenderChip("Other", gender) { gender = it }
                        }

                        Spacer(Modifier.height(12.dp))

                        // AGE INPUT
                        OutlinedTextField(
                            value = age,
                            onValueChange = { age = it },
                            label = { Text("Age") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            val tokenId = UUID.randomUUID().toString()
                            val userEmailRaw = UserPrefs.getEmail(context)
                            val userEmail =
                                userEmailRaw.replace(".", ",") // sanitize for Firebase keys

                            // create a push key under the user's node
                            val ref = FirebaseDatabase.getInstance()
                                .getReference("appointments")
                                .child(userEmail)
                                .push()

                            val appointmentId = ref.key ?: UUID.randomUUID().toString()

                            val appointment = mapOf(
                                "id" to appointmentId,
                                "doctorId" to selectedDoctor!!.id,
                                "doctorName" to selectedDoctor!!.name,
                                "date" to selectedDate,
                                "patientName" to patientName,
                                "gender" to gender,
                                "age" to age,
                                "token" to tokenId,
                                "timestamp" to System.currentTimeMillis()
                            )

                            ref.setValue(appointment)
                                .addOnSuccessListener {
                                    showPatientDialog = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Appointment booked successfully!")
                                    }
                                }
                                .addOnFailureListener { ex ->
                                    ex.printStackTrace()
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Failed to book appointment: ${ex.message}")
                                    }
                                }
                        }

                    ) { Text("Book") }
                },
                dismissButton = {
                    TextButton(onClick = { showPatientDialog = false }) { Text("Cancel") }
                }
            )
        }
    }
}

fun formatDate(y: Int, m: Int, d: Int): String {
    return String.format("%04d-%02d-%02d", y, m + 1, d)
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorCard(
    doctor: Doctor,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // ===== Doctor Image =====
            Image(
                painter = rememberAsyncImagePainter(doctor.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        shape = CircleShape
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(Modifier.width(14.dp))

            // ===== Doctor Info =====
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = doctor.name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = doctor.specialization,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Text(
                    text = "${doctor.experience} yrs experience",
                    fontSize = 13.sp,
                    color = Color.DarkGray
                )
            }

            // ===== Select Button =====
            Button(
                onClick = onSelect,
                shape = RoundedCornerShape(14.dp),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text(
                    text = "Select",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}


@Composable
fun GenderChip(text: String, selectedGender: String, onSelect: (String) -> Unit) {
    val isSelected = text == selectedGender

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0xFF1976D2) else Color(0xFFE0E0E0))
            .clickable { onSelect(text) }
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}


@Composable
fun CustomInput(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF2F2F2),
            unfocusedContainerColor = Color(0xFFF2F2F2),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

