package jeevanreddy.app.doctorconsultationapp.patient


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchDoctorScreen(
    onBack: () -> Unit = {}
) {

    val allDoctors = remember { mutableStateListOf<Doctor>() }
    var searchText by remember { mutableStateOf("") }
    var selectedDoctor by remember { mutableStateOf<Doctor?>(null) }

    // NEW: specialization filter
    var selectedSpecialization by remember { mutableStateOf("All") }
    var specializationList by remember { mutableStateOf(listOf("All")) }

    // Load doctors from Firebase
    LaunchedEffect(true) {
        FirebaseDatabase.getInstance().getReference("doctors")
            .addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val tempList = mutableListOf<String>()
                    allDoctors.clear()

                    for (child in snapshot.children) {
                        val doc = child.getValue(Doctor::class.java)
                        if (doc != null) {
                            allDoctors.add(doc)
                            tempList.add(doc.specialization)
                        }
                    }

                    specializationList = listOf("All") + tempList.distinct()
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    // APPLY FILTERS
    val filteredDoctors = allDoctors.filter { doc ->

        val matchesSearch =
            doc.name.lowercase().contains(searchText.lowercase()) ||
                    doc.specialization.lowercase().contains(searchText.lowercase())

        val matchesSpeciality =
            selectedSpecialization == "All" || doc.specialization == selectedSpecialization

        matchesSearch && matchesSpeciality
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Search Doctor", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            // -------------------------------------------------
            // 🔍 SEARCH INPUT
            // -------------------------------------------------
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search by name or specialization") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            // -------------------------------------------------
            // ⭐ SPECIALIZATION FILTER CHIPS
            // -------------------------------------------------
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(vertical = 8.dp)
            ) {
                specializationList.forEach { speciality ->

                    FilterChipItem(
                        text = speciality,
                        isSelected = speciality == selectedSpecialization,
                        onSelect = { selectedSpecialization = speciality }
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // -------------------------------------------------
            // 📋 DOCTOR LIST
            // -------------------------------------------------
            if (filteredDoctors.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No doctors found", color = Color.Gray)
                }
            } else {
                LazyColumn {
                    items(filteredDoctors) { doctor ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable { selectedDoctor = doctor },
                            shape = RoundedCornerShape(16.dp),
                            elevation = CardDefaults.cardElevation(8.dp)
                        ) {

                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {

                                Image(
                                    painter = rememberAsyncImagePainter(doctor.imageUrl),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .size(70.dp)
                                        .padding(end = 12.dp)
                                        .clip(RoundedCornerShape(50))
                                )

                                Column {
                                    Text(doctor.name, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                    Text(doctor.specialization, color = Color.Gray)
                                    Text("${doctor.experience} yrs experience", fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // -------------------------------------------------------
    // 👨‍⚕️ DOCTOR PROFILE DIALOG
    // -------------------------------------------------------
    if (selectedDoctor != null) {
        DoctorProfileDialog(selectedDoctor!!) {
            selectedDoctor = null
        }
    }
}


// ===========================================================
// ⭐ FILTER CHIP COMPONENT
// ===========================================================
@Composable
fun FilterChipItem(text: String, isSelected: Boolean, onSelect: () -> Unit) {

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) Color(0xFF1976D2) else Color(0xFFE0E0E0))
            .clickable { onSelect() }
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color.Black,
            fontWeight = FontWeight.Medium
        )
    }
}

// ===========================================================
// 👨‍⚕️ DOCTOR PROFILE DIALOG
// ===========================================================
@Composable
fun DoctorProfileDialog(doctor: Doctor, onClose: () -> Unit) {

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        },
        text = {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Image(
                    painter = rememberAsyncImagePainter(doctor.imageUrl),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(60.dp))
                )

                Spacer(Modifier.height(12.dp))

                Text("Specialization: ${doctor.specialization}")
                Text("Experience: ${doctor.experience} years")

                Spacer(Modifier.height(6.dp))

                Text("About:", fontWeight = FontWeight.Bold)
                Text(doctor.about, fontSize = 14.sp)

                Spacer(Modifier.height(10.dp))

                Text("Available Days: ${doctor.availableDays.joinToString()}")
                Text("Slots: ${doctor.availableSlots.joinToString()}")
            }
        },
        confirmButton = {
            TextButton(onClick = onClose) {
                Text("Close")
            }
        }
    )
}


// ===========================================================
// 📌 DOCTOR MODEL
// ===========================================================
data class Doctor(
    val id: String = "",
    val name: String = "",
    val specialization: String = "",
    val experience: Int = 0,
    val about: String = "",
    val imageUrl: String = "",
    val location: String = "",
    val availableDays: List<String> = emptyList(),
    val availableSlots: List<String> = emptyList()
)



class SearchDoctorActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            SearchDoctorScreen(
                onBack = {
                    finish()
                }
            )

        }
    }
}