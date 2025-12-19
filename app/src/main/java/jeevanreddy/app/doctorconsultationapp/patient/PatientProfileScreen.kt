package jeevanreddy.app.doctorconsultationapp.patient

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase
import jeevanreddy.app.doctorconsultationapp.LoginActivity
import jeevanreddy.app.doctorconsultationapp.UserPrefs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientProfileScreen(
    onBack: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf(UserPrefs.getName(context)) }
    val email = UserPrefs.getEmail(context)
    var age by remember { mutableStateOf(UserPrefs.getAge(context)) }
    val role = UserPrefs.getRole(context)

    var showEditDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Profile",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (role == "patient") {
                        IconButton(onClick = { showEditDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Profile")
                        }
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 20.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(Modifier.height(24.dp))

            // ===== Profile Header =====
            Card(
                shape = RoundedCornerShape(24.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    Text(
                        text = if (role == "patient")
                            "Patient Profile Details"
                        else
                            "Doctor Profile Details",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(28.dp))

            ProfileInfoCard(label = "Full Name", value = name)
            ProfileInfoCard(label = "Email Address", value = email)
            ProfileInfoCard(label = "Age", value = "$age years")

            Spacer(Modifier.height(32.dp))

            OutlinedButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
            ) {
                Text(
                    "Logout",
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    // ================= Edit Profile Dialog =================
    if (showEditDialog) {
        EditPatientProfileDialog(
            currentName = name,
            currentAge = age,
            email = email,
            onDismiss = { showEditDialog = false },
            onSave = { newName, newAge ->

                updatePatientProfile(
                    context = context,
                    email = email,
                    name = newName,
                    age = newAge,
                    onSuccess = {
                        name = newName
                        age = newAge
                        showEditDialog = false
                    }
                )
            }
        )
    }
}

@Composable
fun ProfileInfoCard(
    label: String,
    value: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF8F9FA)
        )
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(
                text = label,
                fontSize = 13.sp,
                color = Color.Gray
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun EditPatientProfileDialog(
    currentName: String,
    currentAge: String,
    email: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentName) }
    var age by remember { mutableStateOf(currentAge) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Edit Profile", fontWeight = FontWeight.Bold)
        },
        text = {
            Column {

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    singleLine = true
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = {},
                    label = { Text("Email") },
                    enabled = false
                )

                Spacer(Modifier.height(12.dp))

                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    singleLine = true
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (name.isBlank() || age.isBlank()) return@TextButton
                    onSave(name, age)
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

// ================= Firebase Update =================
fun updatePatientProfile(
    context: Context,
    email: String,
    name: String,
    age: String,
    onSuccess: () -> Unit
) {
    val ref = FirebaseDatabase.getInstance()
        .getReference("PatientDetails")
        .child(email.replace(".", ","))

    ref.child("name").setValue(name)
    ref.child("age").setValue(age)
        .addOnSuccessListener {

            UserPrefs.saveName(context, name)
            UserPrefs.saveAge(context, age)

            Toast.makeText(
                context,
                "Profile updated successfully",
                Toast.LENGTH_SHORT
            ).show()

            onSuccess()
        }
        .addOnFailureListener {
            Toast.makeText(
                context,
                "Failed to update profile",
                Toast.LENGTH_SHORT
            ).show()
        }
}

class PatientProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            PatientProfileScreen(
                onBack = {
                    finish()
                },
                onLogout = {
                    UserPrefs.markLoginStatus(this, false)

                    // Open Login Activity & clear all previous screens
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)

                    finish() // finish profile screen itself
                }
            )
        }
    }
}