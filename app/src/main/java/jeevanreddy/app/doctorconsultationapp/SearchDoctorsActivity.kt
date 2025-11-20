package jeevanreddy.app.doctorconsultationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SearchDoctorsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CardOfDoctorDetails()
        }
    }
}

@Composable
fun CardOfDoctorDetails()
{
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

    )
    {

        DoctorCard(R.drawable.doctor_profile,"Dr.Amira Yuasha","MBBS,MD(Neurology)","Experience : Worked as Senior Doctor in MGM")
        DoctorCard(R.drawable.doctor_profile,"Dr.Rajesh","MBBS,MD(Cardiao)","Experience : Worked as Senior Doctor in MGH")



    }

}


@Composable
fun DoctorCard(doctorImage :Int, doctorName: String, doctorSpecification: String, doctorExp: String)
{
    Card(
        modifier = Modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
    )
    {
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .background(color = colorResource(id = R.color.Violet))
                .padding(vertical = 6.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            Image(
                painter = painterResource(id = doctorImage),
                contentDescription = null,
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)          // circle
                    .border(2.dp, Color.White, CircleShape) // border
                    .shadow(6.dp, CircleShape)
                    .background(Color.LightGray) // fallback bg
                ,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(6.dp))

            Column()
            {

                Text(
                    text = doctorName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = doctorSpecification,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,

                    )

                Text(
                    text = doctorExp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,

                    )

            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun CardOfDoctorDetailsPreview() {
    CardOfDoctorDetails()
}