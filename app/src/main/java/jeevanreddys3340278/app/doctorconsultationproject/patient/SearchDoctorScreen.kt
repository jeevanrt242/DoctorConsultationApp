package jeevanreddys3340278.app.doctorconsultationproject.patient


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
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

            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search by name or specialization") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

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

    if (selectedDoctor != null) {
        DoctorProfileDialog(selectedDoctor!!) {
            selectedDoctor = null
        }
    }
}


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileDialog(
    doctor: Doctor,
    onClose: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        shape = RoundedCornerShape(20.dp),
        containerColor = Color.White,
        title = {
            Text(
                text = "Doctor Profile",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape
                        )
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(doctor.imageUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = doctor.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = doctor.specialization,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {

                        InfoRow("Experience", "${doctor.experience} years")
                        InfoRow(
                            "Available Days",
                            doctor.availableDays.joinToString()
                        )
                        InfoRow(
                            "Time Slots",
                            doctor.availableSlots.joinToString()
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "About Doctor",
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = doctor.about,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onClose) {
                Text(
                    "Close",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}


data class Doctor(
    val id: String = "",
    val name: String = "",
    val specialization: String = "",
    val experience: Int = 0,
    val about: String = "",
    val imageUrl: String = "",
    val location: String = "",
    val availableDays: List<String> = emptyList(),
    val availableSlots: List<String> = emptyList(),

    val advanceBookingDays: Int = 7,
    val leaves: Map<String, Boolean> = emptyMap()
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