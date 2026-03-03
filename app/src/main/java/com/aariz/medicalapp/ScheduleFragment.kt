package com.aariz.medicalapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar
import com.google.android.material.bottomnavigation.BottomNavigationView

class ScheduleFragment : Fragment(R.layout.activity_schedule) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AppointmentAdapter
    private val appointmentList = mutableListOf<Appointment>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerViewAppointments)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        appointmentList.add(
            Appointment("Emily Anderson", "General Checkup", "Mon, 09:00 AM", "Upcoming")
        )

        appointmentList.add(
            Appointment("James Carter", "Cardiology Consult", "Mon, 11:30 AM", "Completed")
        )

        adapter = AppointmentAdapter(appointmentList)
        recyclerView.adapter = adapter

        val fab = view.findViewById<FloatingActionButton>(R.id.fabAddAppointment)
        fab.setOnClickListener {
            showAddDialog()
        }

        val btnBack = view.findViewById<ImageView>(R.id.btnBack)

        btnBack.setOnClickListener {

            (requireActivity() as MainDoctorActivity)
                .loadFragment(DashboardFragment(), "back")

            requireActivity()
                .findViewById<BottomNavigationView>(R.id.bottom_navigation)
                .selectedItemId = R.id.nav_dashboard
        }
    }

    private fun showAddDialog() {

        val bottomSheetDialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_add_appointment, null)

        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.show()

        val etName = view.findViewById<EditText>(R.id.etPatientName)
        val etType = view.findViewById<EditText>(R.id.etType)
        val etTime = view.findViewById<EditText>(R.id.etTime)
        val etStatus = view.findViewById<EditText>(R.id.etStatus)
        val btnAdd = view.findViewById<Button>(R.id.btnAdd)

        val calendar = Calendar.getInstance()

        etTime.setOnClickListener {

            val datePicker = DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->

                    val timePicker = TimePickerDialog(
                        requireContext(),
                        { _, hour, minute ->
                            val formatted =
                                "$dayOfMonth/${month + 1}/$year  $hour:$minute"
                            etTime.setText(formatted)
                        },
                        calendar.get(Calendar.HOUR_OF_DAY),
                        calendar.get(Calendar.MINUTE),
                        true
                    )
                    timePicker.show()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        btnAdd.setOnClickListener {

            val newAppointment = Appointment(
                etName.text.toString(),
                etType.text.toString(),
                etTime.text.toString(),
                etStatus.text.toString()
            )

            appointmentList.add(newAppointment)
            adapter.notifyItemInserted(appointmentList.size - 1)

            bottomSheetDialog.dismiss()
        }
    }
}