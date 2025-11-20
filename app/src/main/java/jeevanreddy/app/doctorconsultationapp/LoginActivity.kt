package jeevanreddy.app.doctorconsultationapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.database.FirebaseDatabase
import kotlin.jvm.java


class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AccessActivityScreen()
        }
    }
}


@Composable
fun AccessActivityScreen() {
    var patientEmail by remember { mutableStateOf("") }
    var patientPassword by remember { mutableStateOf("") }

    val context = LocalContext.current as Activity

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.SlateBlue))
    ) {

        Spacer(modifier = Modifier.height(32.dp))

        Image(
            painter = painterResource(id = R.drawable.ic_doctor), // Replace with your drawable resource
            contentDescription = "App Logo",
            modifier = Modifier
                .size(108.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(32.dp))



        Card(
            modifier = Modifier
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = 36.dp,
                        topEnd = 36.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    )
                ) // Round only top corners
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Login to your account",
                color = colorResource(id = R.color.SlateBlue),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(
                        RoundedCornerShape(
                            topStart = 36.dp,
                            topEnd = 36.dp,
                            bottomStart = 0.dp,
                            bottomEnd = 0.dp
                        )
                    ),
                colors = CardDefaults.cardColors(containerColor = colorResource(id = R.color.SoftBlue))
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "Enter Email"
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = patientEmail,
                    onValueChange = { patientEmail = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "Enter Password"
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = patientPassword,
                    onValueChange = { patientPassword = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        when {
                            patientEmail.isEmpty() -> {
                                Toast.makeText(context, " Please Enter Mail", Toast.LENGTH_SHORT).show()
                            }

                            patientPassword.isEmpty() -> {
                                Toast.makeText(context, " Please Enter Password", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            else -> {
                                val facultyDetails = PatientDetails(
                                    "",
                                    patientEmail,
                                    "",
                                    patientPassword
                                )

                                loginUser(facultyDetails,context)
                            }

                        }
                    },
                    modifier = Modifier
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally),
                    contentPadding = PaddingValues(vertical = 12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.SlateBlue),
                        contentColor = colorResource(
                            id = R.color.white
                        )
                    )
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "I'm new to this app !", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Register",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.white), // Blue text color for "Sign Up"
                        modifier = Modifier.clickable {
                            context.startActivity(
                                Intent(
                                    context,
                                    DoctorRegistrationActivity::class.java
                                )
                            )
                            context.finish()
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun AccessActivityPreview() {
    AccessActivityScreen()
}

fun loginUser(facultyDetails: PatientDetails, context: Context) {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("PatientDetails").child(facultyDetails.emailid.replace(".", ","))

    databaseReference.get().addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val donorData = task.result?.getValue(PatientDetails::class.java)
            if (donorData != null) {
                if (donorData.password == facultyDetails.password) {

                    Toast.makeText(context, "Login Sucessfully", Toast.LENGTH_SHORT).show()
                    context.startActivity(Intent(context, HomeActivity::class.java))
                    (context as Activity).finish()

                } else {
                    Toast.makeText(context, "Seems Incorrect Credentials", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Your account not found", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(
                context,
                "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()
        }

    }
}