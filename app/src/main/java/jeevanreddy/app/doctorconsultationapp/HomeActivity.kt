package jeevanreddy.app.doctorconsultationapp

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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import coil.compose.rememberAsyncImagePainter
import jeevanreddy.app.doctorconsultationapp.patient.BookAppointmentActivity
import jeevanreddy.app.doctorconsultationapp.patient.SearchDoctorActivity
import jeevanreddy.app.doctorconsultationapp.doctor.UpcomingAppointmentActivity
import jeevanreddy.app.doctorconsultationapp.patient.PastAppointmentActivity
import jeevanreddy.app.doctorconsultationapp.patient.PatientProfileActivity
import jeevanreddy.app.doctorconsultationapp.ui.theme.P1
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
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(color = colorResource(id = R.color.SlateBlue))
//                    .padding(vertical = 6.dp, horizontal = 16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            )
//            {
//
//                Text(
//                    text = "Doctor App",
//                    style = MaterialTheme.typography.titleLarge,
//                    color = Color.White,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.weight(1f))
//
//                Image(
//                    painter = painterResource(id = R.drawable.profile),
//                    contentDescription = "Doctor App",
//                    modifier = Modifier
//                        .size(44.dp)
//                        .padding(start = 8.dp)
//                        .clickable {
//
//                        }
//
//                )
//
//            }



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
                        .padding(16.dp) // inner padding so content doesn't touch edges

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

        // ===== Indicator Dots =====
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
