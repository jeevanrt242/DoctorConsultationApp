package jeevanreddys3340278.app.doctorconsultationproject

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import jeevanreddys3340278.app.doctorconsultationproject.patient.BookAppointmentActivity
import jeevanreddys3340278.app.doctorconsultationproject.patient.SearchDoctorActivity
import jeevanreddys3340278.app.doctorconsultationproject.patient.UpcomingAppointmentActivity
import jeevanreddys3340278.app.doctorconsultationproject.patient.PastAppointmentActivity
import jeevanreddys3340278.app.doctorconsultationproject.patient.PatientProfileActivity
import jeevanreddys3340278.app.doctorconsultationproject.ui.theme.P1
import kotlinx.coroutines.delay

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    val context = LocalContext.current as Activity

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Doctor Consultation App",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = P1
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        )
        {


            Spacer(modifier = Modifier.height(8.dp))


            val bannerImages = listOf(
                "https://www.eggoz.com/cdn/shop/articles/Tips_for_a_healthy_lifestyle.jpg?v=1734009785&width=1100",
                "https://m.media-amazon.com/images/I/71HRjCZTtES._AC_UF894,1000_QL80_.jpg",
                "https://www.ccohs.ca/images/products/infographics/lightbox/mh-workplace_prod@3x.png",
                "https://osgpc.com/wp-content/uploads/2015/08/Untitled-design-21-1.png"
            )

            AutoSliderBanner(
                imageUrls = bannerImages,
                modifier = Modifier.padding(8.dp)
            )



            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                Column(
                    modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(
                                context,
                                BookAppointmentActivity::class.java
                            )
                        )

                    },
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {

                    Image(
                        painter = painterResource(id = R.drawable.book_appointment),
                        contentDescription = "Doctor App",
                        modifier = Modifier
                            .size(62.dp)
                    )
                    Text(
                        text = "Book \nAppointment",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center

                    )


                }

                Column(
                    modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(
                                context,
                                UpcomingAppointmentActivity::class.java
                            )
                        )

                    },
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {

                    Image(
                        painter = painterResource(id = R.drawable.upcoming_appointment),
                        contentDescription = "Doctor App",
                        modifier = Modifier
                            .size(62.dp)
                    )
                    Text(
                        text = "Upcoming \nAppointment",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center

                    )

                }
                Column(
                    modifier = Modifier.clickable {
                        context.startActivity(
                            Intent(
                                context,
                                PastAppointmentActivity::class.java
                            )
                        )

                    },
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {

                    Image(
                        painter = painterResource(id = R.drawable.past_appointment),
                        contentDescription = "Doctor App",
                        modifier = Modifier
                            .size(62.dp)
                    )
                    Text(
                        text = "Past \nAppointment",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center

                    )


                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            )
            {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            context.startActivity(
                                Intent(
                                    context,
                                    SearchDoctorActivity::class.java
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
                        painter = painterResource(id = R.drawable.search_doctor),
                        contentDescription = "Doctor App",
                        modifier = Modifier
                            .size(62.dp)
                    )
                    Text(
                        text = "Search Doctor",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center

                    )
                }

                Spacer(modifier = Modifier.width(6.dp))

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
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
                        .padding(16.dp)

                )
                {

                    Image(
                        painter = painterResource(id = R.drawable.profile),
                        contentDescription = "Doctor App",
                        modifier = Modifier
                            .size(62.dp)
                    )
                    Text(
                        text = "My Profile",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center

                    )


                }


            }

            AboutAndContactSection()


        }

    }

}

@Composable
fun AboutAndContactSection() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF8F9FA)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "About App",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Online Doctor Consultation App is created to provide the doctors with the consultations that are quick and convenient online. The application enables individuals to connect with the doctors online, book appointments and receive medical consultation anywhere, anytime.",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(12.dp))

                Divider()

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Developed By",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Student Name: Jeewan",
                    fontSize = 14.sp
                )

                Text(
                    text = "Student Number: S3340278",
                    fontSize = 14.sp
                )

                Text(
                    text = "Email: jeevanreddythummala9@gmail.com",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFFFFFF)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Contact Us",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Questions, remarks and assistance, please contact:",
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "jeevanreddythummala9@gmail.com",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}



@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AutoSliderBanner(
    imageUrls: List<String>,
    modifier: Modifier = Modifier,
    autoScrollDelay: Long = 5000L
) {
    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { imageUrls.size }
    )

    LaunchedEffect(Unit) {
        while (true) {
            delay(autoScrollDelay)
            val nextPage = (pagerState.currentPage + 1) % imageUrls.size
            pagerState.animateScrollToPage(nextPage)
        }
    }

    Box(modifier = modifier) {

        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(Color.Black)
        ) { page ->

            Image(
                painter = rememberAsyncImagePainter(imageUrls[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(),
                contentScale = ContentScale.FillBounds
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(imageUrls.size) { index ->
                val selected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .size(if (selected) 8.dp else 6.dp)
                        .clip(CircleShape)
                        .background(
                            if (selected)
                                MaterialTheme.colorScheme.primary
                            else
                                Color.White.copy(alpha = 0.6f)
                        )
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
