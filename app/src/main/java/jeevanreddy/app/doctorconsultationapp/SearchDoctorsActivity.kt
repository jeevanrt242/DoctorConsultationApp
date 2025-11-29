package jeevanreddy.app.doctorconsultationapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())

    )
    {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.SlateBlue))
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {

            Image(painter = painterResource(id = R.drawable.outline_arrow_back_24),
                contentDescription = "Doctor App",
                modifier = Modifier
                    .size(34.dp)
                    .clickable {
//                        val intent = Intent(context, ::class.java)
//                        context.startActivity(intent)
                    }

            )

            Spacer(modifier= Modifier.width(12.dp))

            Text(
                text = "Select Doctor",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

        }

        SearchBar(
            query = query,
            onQueryChange = { query = it }
        )

        DoctorCard(R.drawable.doctor_profile,"Dr.Amira Yuasha","MBBS,MD(Neurology)","Experience : Worked as Senior Doctor in MGM")
        DoctorCard(R.drawable.doctor_profile,"Dr.Rajesh","MBBS,MD(Cardiao)","Experience : Worked as Senior Doctor in MGM ")


    }

}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray
            )
        },
        placeholder = { Text("Search...") },
        shape = RoundedCornerShape(30.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.Gray,
            unfocusedBorderColor = Color.LightGray,
            cursorColor = Color.Gray
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .height(54.dp)
    )
}


@Composable
fun DoctorCard(
    doctorImage: Int,
    doctorName: String,
    doctorSpecification: String,
    doctorExp: String,
    onSelectClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(id = doctorImage),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
                    .size(62.dp)
                    .clip(CircleShape)
                    .border(2.dp, Color.White, CircleShape)
                    .shadow(6.dp, CircleShape)
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(10.dp))

            Column(
                modifier = Modifier
                    .weight(3f)
            ) {
                Text(
                    text = doctorName,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = doctorSpecification,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = doctorExp,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black
                )
            }

            Text(
                text = "Select",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .weight(1f) 
                    .background(
                        color = Color(0xFF6C63FF),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                textAlign = TextAlign.Center
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
fun CardOfDoctorDetailsPreview() {
    CardOfDoctorDetails()
}