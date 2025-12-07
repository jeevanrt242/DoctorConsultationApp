package jeevanreddy.app.doctorconsultationapp.doctor


import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import jeevanreddy.app.doctorconsultationapp.UserPrefs
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpcomingAppointmentsScreen(
    onBack: () -> Unit = {}
) {

    val appointments = remember { mutableStateListOf<Appointment>() }

    val context = LocalActivity.current as Activity

    // Load data from Firebase
    LaunchedEffect(true) {

        val userEmailRaw = UserPrefs.getEmail(context)
        val userEmail = userEmailRaw.replace(".", ",")

        FirebaseDatabase.getInstance()
            .getReference("appointments")
            .child(userEmail)
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    appointments.clear()

                    for (child in snapshot.children) {
                        val appt = child.getValue(Appointment::class.java)

                        if (appt != null && !isPastDate(appt.date)) {
                            appointments.add(appt)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upcoming Appointments", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        if (appointments.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No upcoming appointments", fontSize = 18.sp, color = androidx.compose.ui.graphics.Color.Gray)
            }
        } else {

            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
            ) {
                items(appointments) { appt ->
                    AppointmentCard(appt)
                }
            }
        }
    }
}

// --------------------------
// Appointment Card UI
// --------------------------

@Composable
fun AppointmentCard(appt: Appointment) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB))
    ) {

        Column(modifier = Modifier.padding(18.dp)) {

            // ----------------------------------
            // Header - Doctor Info
            // ----------------------------------
            Row(verticalAlignment = Alignment.CenterVertically) {

                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(28.dp)
                )

                Spacer(Modifier.width(10.dp))

                Column {
                    Text(
                        appt.doctorName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Upcoming Appointment",
                        color = Color(0xFF1976D2),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // ----------------------------------
            // Appointment Details
            // ----------------------------------
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(6.dp))
                Text("Date: ${appt.date}")
            }

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(6.dp))
                Text("Token Number: ${appt.token.take(8)}")
            }

            Spacer(Modifier.height(12.dp))
            Divider()
            Spacer(Modifier.height(12.dp))

            // ----------------------------------
            // Patient Details
            // ----------------------------------
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Person, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(6.dp))
                Text("Patient: ${appt.patientName}")
            }

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Man, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(6.dp))
                Text("Gender: ${appt.gender}")
            }

            Spacer(Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Cake, contentDescription = null, tint = Color.Gray)
                Spacer(Modifier.width(6.dp))
                Text("Age: ${appt.age}")
            }
        }
    }
}


fun isPastDate(dateString: String): Boolean {
    return try {
        val sdf = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
        val selectedDate = sdf.parse(dateString)
        val today = sdf.parse(sdf.format(Date()))
        selectedDate.before(today)
    } catch (e: Exception) {
        false
    }
}




class UpcomingAppointmentActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            UpcomingAppointmentsScreen(
                onBack = {
                    finish()
                }
            )
        }
    }
}