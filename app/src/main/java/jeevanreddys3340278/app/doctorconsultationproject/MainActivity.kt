package jeevanreddys3340278.app.doctorconsultationproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jeevanreddys3340278.app.doctorconsultationproject.doctor.DoctorHomeActivity
import jeevanreddys3340278.app.doctorconsultationproject.ui.theme.DoctorConsultationAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DoctorConsultationAppTheme {
                EntryScreenMA()
            }
        }
    }
}


@Composable
fun EntryScreenMA() {
    val context = LocalContext.current as Activity
    var splashValue by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        delay(3000)
        splashValue=false

    }

    if (splashValue) {
        EntryScreen()
    } else {

        if (LocalUserData.checkLoginStatus(context = context)) {
            val role = LocalUserData.getRole(context)

            if (role == "doctor") {
                gotoDoctorHome(context)
            } else {
                gotoPatientHome(context)
            }
        } else {
            gotoLogin(context)
        }
    }
}

fun gotoLogin(context: Activity) {
    context.startActivity(Intent(context, LoginActivity::class.java))
    context.finish()
}

fun gotoDoctorHome(context: Activity) {
    context.startActivity(Intent(context, DoctorHomeActivity::class.java))
    context.finish()
}

fun gotoPatientHome(context: Activity) {
    context.startActivity(Intent(context, HomeActivity::class.java))
    context.finish()
}


@Composable
fun EntryScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.SlateBlue)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.weight(1f))


            Image(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                painter = painterResource(id = R.drawable.ic_doctor),
                contentDescription = "Doctor Consultation App by Jeevan Reddy",
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Welcome To",
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.white), // Green color similar to the design
                fontSize = 26.sp,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Doctor Consultation App\nby Jeevan Reddy",
                color = colorResource(id = R.color.white), // Green color similar to the design
                style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))


        }
    }

}


@Preview(showBackground = true)
@Composable
fun EntryScreenPreview() {
    EntryScreen()
}