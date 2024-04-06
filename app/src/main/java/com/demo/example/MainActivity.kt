package com.demo.example

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.example.mvvm.NoteViewModal
import com.demo.example.room.Note
import kotlinx.android.synthetic.main.activity_main.idFAB
import kotlinx.android.synthetic.main.activity_main.notesRV
import java.util.jar.Manifest


class MainActivity : AppCompatActivity(), NoteRVAdapter.NoteClickInterface {

    lateinit var viewModal: NoteViewModal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this,android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1);
            }
        }

        createNotificationChannel()
        notesRV.layoutManager = LinearLayoutManager(this)
        val noteRVAdapter = NoteRVAdapter(this, this)
        notesRV.adapter = noteRVAdapter

        viewModal = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(application)
        ).get(NoteViewModal::class.java)

        viewModal.allNotes.observe(this, Observer {
            it?.let {
                noteRVAdapter.updateList(it)
            }
        })


        idFAB.setOnClickListener {
            val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
            startActivity(intent)
            this.finish()
        }


    }

    override fun onDeleteIconClick(note: Note) {
//        val builder: AlertDialog.Builder = AlertDialog.Builder(
//            ContextThemeWrapper(
//                this,
//               R.style.myDialog
//            ))
//        builder.setTitle(title)
//        builder.setMessage("message")
//        builder.setPositiveButton("Yes",
//            DialogInterface.OnClickListener { dialog, which ->
//                viewModal.deleteNote(note)
//                Toast.makeText(this, "${note.noteTitle} Deleted", Toast.LENGTH_LONG).show()
//                dialog.dismiss()
//            })
//        builder.setNegativeButton("No",
//            DialogInterface.OnClickListener { dialog, which ->
//                dialog.dismiss()
//            })
//        val dialog: AlertDialog = builder.create()
//        dialog.show()
    }

    override fun onNoteClick(note: Note) {
        val intent = Intent(this@MainActivity, AddEditNoteActivity::class.java)
        intent.putExtra("noteType", "Edit")
        intent.putExtra("noteTitle", note.noteTitle)
        intent.putExtra("noteDescription", note.noteDescription)
        intent.putExtra("noteId", note.id)
        startActivity(intent)
        this.finish()
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "MyNotificationChannel"
            val description = "Channel description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel_id", name, importance)
            channel.description = description
            val notificationManager = getSystemService(
                NotificationManager::class.java
            )
            notificationManager.createNotificationChannel(channel)
        }
    }


}