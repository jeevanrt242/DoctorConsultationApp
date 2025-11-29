package jeevanreddy.app.doctorconsultationapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun EnterPatientDetails()
{
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
                text = "Enter Patient Details",
                style = MaterialTheme.typography.titleLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
        BookingFormScreen()


    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingFormScreen() {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedGender by remember { mutableStateOf("") }

    val genderOptions = listOf("Male", "Female", "Other")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {


//        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Full Name",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text("Enter your name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        //  Gender Field
        Text(
            text = "Gender",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            genderOptions.forEach { gender ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    RadioButton(
                        selected = selectedGender == gender,
                        onClick = { selectedGender = gender }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = gender,
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Age Field
        Text(
            text = "Age",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = age,
            onValueChange = { name = it },
            placeholder = { Text("Enter your age") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Book Now Button
        Button(
            onClick = {
                // TODO: handle booking action here
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = MaterialTheme.shapes.medium,
            enabled = name.isNotEmpty() && age.isNotEmpty() && selectedGender.isNotEmpty()
        ) {
            Text(
                text = "Book Now",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun EnterPatientDetailsPreview() {
    EnterPatientDetails()
}