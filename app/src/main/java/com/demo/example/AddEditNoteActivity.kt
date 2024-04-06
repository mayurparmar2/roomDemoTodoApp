package com.demo.example

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.demo.example.mvvm.NoteViewModal
import kotlinx.android.synthetic.main.activity_add_edit_note.btnSelectDate
import kotlinx.android.synthetic.main.activity_add_edit_note.noteEdt
import kotlinx.android.synthetic.main.activity_add_edit_note.noteTitleEdt
import kotlinx.android.synthetic.main.activity_add_edit_note.saveBtn
import kotlinx.android.synthetic.main.activity_add_edit_note.spinner
import kotlinx.android.synthetic.main.activity_add_edit_note.txtDateTime
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date


class AddEditNoteActivity : AppCompatActivity() {
    lateinit var  mContext: Context
    lateinit var viewModal: NoteViewModal
    var noteID = -1;

    val categories = listOf("Home","Office","Market")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_note)


        val adapter = ArrayAdapter(this, R.layout.spinner_item_layout, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter


        mContext = this
        viewModal =
            ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(application)).get(
                NoteViewModal::class.java
            )

        val noteType = intent.getStringExtra("noteType")
        if (noteType.equals("Edit")) {
            // on below line we are setting data to edit text.
            val noteTitle = intent.getStringExtra("noteTitle")
            val noteDescription = intent.getStringExtra("noteDescription")
            noteID = intent.getIntExtra("noteId", -1)
            saveBtn.setText("Update Note")
            noteTitleEdt.setText(noteTitle)
            noteEdt.setText(noteDescription)
        } else {
            saveBtn.setText("Save Note")
        }

        btnSelectDate.setOnClickListener {
            getDateTime()

//            val materialTimePicker: MaterialTimePicker = MaterialTimePicker.Builder()
//                // set the title for the alert dialog
//                .setTitleText("SELECT YOUR TIMING")
//                // set the default hour for the
//                // dialog when the dialog opens
//                .setHour(12)
//                // set the default minute for the
//                // dialog when the dialog opens
//                .setMinute(10)
//                // set the time format
//                // according to the region
//                .setTimeFormat(TimeFormat.CLOCK_12H)
//                .build()
//
//            materialTimePicker.show(supportFragmentManager, "MainActivity")


        }

        saveBtn.setOnClickListener(object : OnClickListener {
            override fun onClick(v: View?) {
                val noteTitle = noteTitleEdt.text.toString()
                val noteDescription = noteEdt.text.toString()

                if (noteType.equals("Edit")) {
                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                        val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                        val currentDateAndTime: String = sdf.format(Date())
                        val updatedNote = com.demo.example.room.Note(
                            noteTitle,
                            noteDescription,
                            currentDateAndTime,0,spinner.selectedItemPosition)
                        updatedNote.id = noteID
                        viewModal.updateNote(updatedNote)
                        Toast.makeText(applicationContext, "Note Updated..", Toast.LENGTH_LONG).show()
                    }
                } else {
                    if (noteTitle.isNotEmpty() && noteDescription.isNotEmpty()) {
                        val sdf = SimpleDateFormat("dd MMM, yyyy - HH:mm")
                        val currentDateAndTime: String = sdf.format(Date())
                        viewModal.addNote(
                            com.demo.example.room.Note(
                                noteTitle,
                                noteDescription,
                                currentDateAndTime,0,spinner.selectedItemPosition
                            )
                        )
                        Toast.makeText(applicationContext, "$noteTitle Added", Toast.LENGTH_LONG).show()
                    }
                }

                val notificationTime = System.currentTimeMillis() + 10 * 1000 // 10 seconds from now
                scheduleNotification(notificationTime)

                startActivity(Intent(applicationContext, MainActivity::class.java))
                finish()
            }
        })
    }


    var mDateTime: String? = null

    fun getDateTime() {
        val c: Calendar = Calendar.getInstance()
        val currentYear: Int = c.get(Calendar.YEAR)
        val currentMonth: Int = c.get(Calendar.MONTH)
        val currentDay: Int = c.get(Calendar.DAY_OF_MONTH)
        val currentHour: Int = c.get(Calendar.HOUR_OF_DAY)
        val currentMinute: Int = c.get(Calendar.MINUTE)

        mDateTime = ""
        val datePickerDialog = DatePickerDialog(
            mContext, { view, year, month, dayOfMonth ->
                // Validate if selected date is after the current date
                if (year > currentYear ||
                    (year == currentYear && month > currentMonth) ||
                    (year == currentYear && month == currentMonth )
                ) {
                    mDateTime = "$year-$month-$dayOfMonth"
                    Handler().postDelayed(Runnable {
                        TimePickerDialog(
                            mContext, { view, hourOfDay, minute ->
                                // Validate if selected time is after the current time
                                if (year > currentYear ||
                                    (year == currentYear && month > currentMonth) ||
                                    (year == currentYear && month == currentMonth ) ||
                                    (year == currentYear && month == currentMonth  && hourOfDay > currentHour) ||
                                    (year == currentYear && month == currentMonth  && hourOfDay == currentHour && minute > currentMinute)
                                ) {
                                    mDateTime += " $hourOfDay:$minute"
                                    txtDateTime.setText(mDateTime)

                                    // Convert selected date and time to milliseconds
                                    val calendar = Calendar.getInstance()
                                    calendar.set(year, month, dayOfMonth, hourOfDay, minute)
                                    // Schedule notification
                                    scheduleNotification(calendar.timeInMillis)
                                } else {
                                    // Selected time is not valid
                                    // You can display an error message or handle it as you wish
                                    // For example:
                                    Toast.makeText(mContext, "Please select a time after the current time.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            currentHour, currentMinute, false
                        ).show()
                    }, 500)
                } else {
                    // Selected date is not valid
                    // You can display an error message or handle it as you wish
                    // For example:
                    Toast.makeText(mContext, "Please select a date after the current date.", Toast.LENGTH_SHORT).show()
                }
            },
            currentYear,
            currentMonth,
            currentDay
        )

        // Set minimum date as current date to disable past dates
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }



//    var mDateTime: String? = null
//    fun getDateTime() {
//        val c: Calendar = Calendar.getInstance()
//        val year: Int = c.get(Calendar.YEAR)
//        val month: Int = c.get(Calendar.MONTH)
//        val day: Int = c.get(Calendar.DAY_OF_MONTH)
//        val hour: Int = c.get(Calendar.HOUR_OF_DAY)
//        val minute: Int = c.get(Calendar.MINUTE)
//        mDateTime = ""
//        DatePickerDialog(
//            mContext, { view, year, month, dayOfMonth ->
//                mDateTime = "$year-$month-$dayOfMonth"
//                Handler().postDelayed(Runnable {
//                    TimePickerDialog(mContext, { view, hourOfDay, minute ->
//                        mDateTime += " $hourOfDay:$minute"
//                        txtDateTime.setText(mDateTime)
//                        // Convert selected date and time to milliseconds
//                        val calendar = Calendar.getInstance()
//                        calendar.set(year, month, dayOfMonth, hourOfDay, minute)
//                        val timeInMillis = calendar.timeInMillis
//                        // Schedule notification
//                        scheduleNotification(timeInMillis)
//
//                        }, hour, minute, false).show()
//                }, 500)
//            }, year, month, day
//        ).show()
//    }


    private fun scheduleNotification(timeInMillis: Long) {
        val intent = Intent(this, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager?.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
    }

}