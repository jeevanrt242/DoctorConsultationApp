package jeevanreddy.app.doctorconsultationapp.doctor

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jeevanreddy.app.doctorconsultationapp.R
import jeevanreddy.app.doctorconsultationapp.UserPrefs
import jeevanreddy.app.doctorconsultationapp.patient.PatientProfileActivity
import jeevanreddy.app.doctorconsultationapp.ui.theme.P1
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DoctorHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DoctorHomeScreen(
                doctorId = UserPrefs.getID(this),
                onBack = {
                    startActivity(Intent(this, PatientProfileActivity::class.java))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorHomeScreen(
    doctorId: String,
    onBack: () -> Unit = {}
) {

    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(getTodayDate()) }
    val appointments = remember { mutableStateListOf<Appointment>() }

    val snackBar = remember { SnackbarHostState() }
    val coroutine = rememberCoroutineScope()

    // Load appointments for this doctor on selected date
    LaunchedEffect(selectedDate) {

        FirebaseDatabase.getInstance()
            .getReference("appointments")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    appointments.clear()

                    for (userNode in snapshot.children) {
                        for (apptNode in userNode.children) {

                            val appt = apptNode.getValue(Appointment::class.java)

                            if (appt != null && appt.doctorId == doctorId && appt.date == selectedDate) {
                                appt.firebasePath = apptNode.ref // store reference
                                appointments.add(appt)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackBar) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Doctor Dashboard",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = P1
                ),
                actions = {
                    IconButton(onClick = {
                        onBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            )
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        context.startActivity(
                            Intent(
                                context,
                                ManageAvailabilityActivity::class.java
                            )
                        )
                    }
                    .background(
                        color = Color(0xFFEFEFEF),   // your background color
                        shape = RoundedCornerShape(12.dp) // optional rounded corners
                    )
                    .padding(16.dp) // inner padding so content doesn't touch edges
            )
            {

                Image(
                    painter = painterResource(id = R.drawable.ic_manage_appointment),
                    contentDescription = "Doctor App",
                    modifier = Modifier
                        .size(62.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = "Manage Availability",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center

                )

                Spacer(modifier = Modifier.weight(1f))


                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Arrow",
                    tint = Color.Black
                )

            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFFEFEFEF),   // your background color
                        shape = RoundedCornerShape(12.dp) // optional rounded corners
                    )
                    .clickable {
                        context.startActivity(
                            Intent(
                                context,
                                PatientProfileActivity::class.java
                            )
                        )
                    }
                    .padding(16.dp) // inner padding so content doesn't touch edges

            )
            {

                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Doctor App",
                    modifier = Modifier
                        .size(62.dp)
                )

                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "My Profile",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center

                )

                Spacer(modifier = Modifier.weight(1f))


                Icon(
                    imageVector = Icons.Default.ArrowForwardIos,
                    contentDescription = "Arrow",
                    tint = Color.Black
                )


            }


            // DATE SELECTOR
            Text("Select Date", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
            Spacer(Modifier.height(8.dp))

            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(
                context,
                { _: DatePicker, y: Int, m: Int, d: Int ->
                    selectedDate = "$d/${m + 1}/$y"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF2F2F2))
                    .clickable { datePicker.show() }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
                Spacer(Modifier.width(10.dp))
                Text(selectedDate, fontSize = 16.sp)
            }

            Spacer(Modifier.height(20.dp))

            // CANCEL ALL BUTTON
            if (appointments.isNotEmpty()) {
                Button(
                    onClick = {

                        val dbRef = FirebaseDatabase.getInstance().getReference("appointments")

                        dbRef.get().addOnSuccessListener { snapshot ->

                            for (userNode in snapshot.children) {
                                for (apptNode in userNode.children) {

                                    val appt = apptNode.getValue(Appointment::class.java)

                                    if (appt != null &&
                                        appt.doctorId == doctorId &&
                                        appt.date == selectedDate
                                    ) {
                                        apptNode.ref.removeValue()
                                    }
                                }
                            }

                            coroutine.launch {
                                snackBar.showSnackbar("All appointments cancelled successfully!")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD32F2F)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null)
                    Spacer(Modifier.width(6.dp))
                    Text("Cancel All Appointments", fontSize = 17.sp, color = Color.White)
                }

                Spacer(Modifier.height(16.dp))
            }

            // APPOINTMENT LIST
            if (appointments.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No appointments found on this date", color = Color.Gray)
                }
            } else {
                LazyColumn {
                    items(appointments) { appt ->
                        AppointmentCardForDoctor(appt, snackBar)
                    }
                }
            }
        }
    }
}


// -----------------------------------------------------------
// UPDATED APPOINTMENT CARD WITH PRESCRIPTION BUTTON
// -----------------------------------------------------------
@Composable
fun AppointmentCardForDoctor(appt: Appointment, snackBar: SnackbarHostState) {

    var showPrescriptionDialog by remember { mutableStateOf(false) }
    var prescriptionText by remember { mutableStateOf(appt.prescription ?: "") }

    val coroutine = rememberCoroutineScope()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(modifier = Modifier.padding(18.dp)) {

            // -------------------------------
            // Patient Name Header
            // -------------------------------
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(26.dp)
                )

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        appt.patientName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Token: ${appt.token.take(8)}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

            Spacer(Modifier.height(15.dp))

            // -------------------------------
            // Patient Info Section
            // -------------------------------
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(6.dp))
                Text("Gender: ${appt.gender}")
            }

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(6.dp))
                Text("Date: ${appt.date}")
            }

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(6.dp))
                Text("Age: ${appt.age}")
            }

            // -------------------------------
            // Existing Prescription Badge
            // -------------------------------
            if (!appt.prescription.isNullOrEmpty()) {
                Spacer(Modifier.height(14.dp))
                Box(
                    modifier = Modifier
                        .background(Color(0xFF1976D2).copy(alpha = 0.1f), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column {
                        Text(
                            "Prescription",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D47A1)
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(appt.prescription!!, color = Color(0xFF1A237E))
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // -------------------------------
            // Add Prescription Button
            // -------------------------------
            Button(
                onClick = { showPrescriptionDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.Edit, contentDescription = null, tint = Color.White)
                Spacer(Modifier.width(8.dp))
                Text("Add / Update Prescription", color = Color.White)
            }
        }
    }

    // ------------------------------------------------------
    // PREMIUM PRESCRIPTION INPUT DIALOG (Bottom Sheet Style)
    // ------------------------------------------------------
    if (showPrescriptionDialog) {
        AlertDialog(
            onDismissRequest = { showPrescriptionDialog = false },
            title = {
                Text(
                    "Add Prescription",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column {

                    Text(
                        "Write medical instructions or medication list.",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = prescriptionText,
                        onValueChange = { prescriptionText = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp),
                        placeholder = { Text("Enter prescription details...") },
                        maxLines = 8,
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {

                        val ref = appt.firebasePath
                        ref?.child("prescription")
                            ?.setValue(prescriptionText.trim())
                            ?.addOnSuccessListener {

                                coroutine.launch {
                                    snackBar.showSnackbar("Prescription saved successfully!")
                                }
                                showPrescriptionDialog = false
                            }

                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
                ) {
                    Text("Save", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showPrescriptionDialog = false }) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(20.dp)
        )
    }
}


// -----------------------------------------------------------
// Appointment Model (UPDATED)
// -----------------------------------------------------------
data class Appointment(
    val id: String = "",
    val doctorId: String = "",
    val doctorName: String = "",
    val date: String = "",
    val patientName: String = "",
    val gender: String = "",
    val age: String = "",
    val token: String = "",
    val timestamp: Long = 0,
    val prescription: String? = null,

    @Transient
    var firebasePath: DatabaseReference? = null
)


// -----------------------------------------------------------
// Utility - Today Date
// -----------------------------------------------------------
fun getTodayDate(): String {
    val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
    return sdf.format(Date())
}
