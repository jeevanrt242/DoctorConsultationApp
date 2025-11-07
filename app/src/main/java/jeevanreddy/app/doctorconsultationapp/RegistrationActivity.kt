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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults.contentPadding
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


class DoctorRegistrationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            UserRegistrationActivityScreen()
        }
    }
}


@Composable
fun UserRegistrationActivityScreen() {
    var patientFN by remember { mutableStateOf("") }
    var patientAge by remember { mutableStateOf("") }
    var patientEmail by remember { mutableStateOf("") }
    var patientPass by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }

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
                text = "Create New Account",
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
                    text = "Enter FullName"
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = patientFN,
                    onValueChange = { patientFN = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "Enter Age"
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = patientAge,
                    onValueChange = { patientAge = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )


                Spacer(modifier = Modifier.height(8.dp))


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

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.padding(start = 12.dp),
                    text = "Enter Password"
                )

                Spacer(modifier = Modifier.height(6.dp))

                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    value = patientPass,
                    onValueChange = { patientPass = it },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = {
                        when {
                            patientFN.isEmpty() -> {
                                Toast.makeText(context, " Please Enter Name", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            patientAge.isEmpty() -> {
                                Toast.makeText(context, " Please Enter Age", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            patientEmail.isEmpty() -> {
                                Toast.makeText(context, " Please Enter Email", Toast.LENGTH_SHORT)
                                    .show()
                            }

                            patientPass.isEmpty() -> {
                                Toast.makeText(
                                    context,
                                    " Please Enter Password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            else -> {
                                val facultyDetails = PatientDetails(
                                    patientFN,
                                    patientEmail,
                                    patientAge,
                                    patientPass
                                )
                                registerPatient(facultyDetails, context);
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
                        text = "Register",
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "I'm an old user !", fontSize = 14.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Login",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.white), // Blue text color for "Sign Up"
                        modifier = Modifier.clickable {
                            context.startActivity(Intent(context, LoginActivity::class.java))
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
fun UserRegistrationActivityPreview() {
    UserRegistrationActivityScreen()
}

fun registerPatient(patientDetails: PatientDetails, context: Context) {

    val firebaseDatabase = FirebaseDatabase.getInstance()
    val databaseReference = firebaseDatabase.getReference("PatientDetails")

    databaseReference.child(patientDetails.emailid.replace(".", ","))
        .setValue(patientDetails)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, "You Registered Successfully", Toast.LENGTH_SHORT)
                    .show()

            } else {
                Toast.makeText(
                    context,
                    "Registration Failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        .addOnFailureListener { _ ->
            Toast.makeText(
                context,
                "Something went wrong",
                Toast.LENGTH_SHORT
            ).show()
        }
}

data class PatientDetails(
    var name: String = "",
    var emailid: String = "",
    var age: String = "",
    var password: String = ""
)