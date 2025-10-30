package jeevanreddy.app.doctorconsultationapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import jeevanreddy.app.doctorconsultationapp.ui.theme.DoctorConsultationAppTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DoctorConsultationAppTheme {
                MainScreen(::checkLoginStatusAndGo)
            }
        }
    }

    private fun checkLoginStatusAndGo(value: Int) {

        when (value) {
            1 -> {
                //TODO Add Home Screen
            }

            2 -> {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }

            3 -> {
//                startActivity(Intent(this, SignUpActivity::class.java))
//                finish()
            }
        }
    }
}

@Composable
fun MainScreen(onLoginClick: (value: Int) -> Unit) {
    var showSplash by remember { mutableStateOf(true) }

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(3000) // 3 seconds delay
        showSplash = false
    }

    if (showSplash) {
        SplashScreen()
    } else {
        if (AccountData.getLoginStatus(context)) {
            onLoginClick.invoke(1)
        } else {
            onLoginClick.invoke(2)
        }
    }
}

@Composable
fun SplashScreen() {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.p1)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Spacer(modifier = Modifier.weight(1f))


            Image(
                painter = painterResource(id = R.drawable.doctor_consultation_icon),
                contentDescription = "Doctor Consultation App",
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "Doctor Consultation App",
                color = colorResource(id = R.color.p2),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 6.dp)
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = "By Jeevan Reddy",
                color = colorResource(id = R.color.p2),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 18.dp)
                    .align(Alignment.CenterHorizontally)
            )


        }
    }

}


@Preview(showBackground = true)
@Composable
fun MainActivityPreview() {
    SplashScreen()
}