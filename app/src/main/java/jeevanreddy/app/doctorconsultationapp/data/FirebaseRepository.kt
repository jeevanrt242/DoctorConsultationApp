package jeevanreddy.app.doctorconsultationapp.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import jeevanreddy.app.doctorconsultationapp.patient.Doctor

class DoctorRepository {

    private val db = FirebaseDatabase.getInstance().getReference("doctors")

    fun getDoctors(onResult: (List<Doctor>) -> Unit) {
        db.get().addOnSuccessListener { snapshot ->
            val list = snapshot.children.mapNotNull { it.getValue(Doctor::class.java) }
            onResult(list)
        }
    }
}


class DoctorViewModel : ViewModel() {

    private val repo = DoctorRepository()

    var doctors by mutableStateOf<List<Doctor>>(emptyList())
        private set

    init {
        loadDoctors()
    }

    private fun loadDoctors() {
        repo.getDoctors {
            doctors = it
        }
    }
}
