package com.demo.example

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.demo.example.room.Note

class NoteRVAdapter(val context: Context,val noteClickInterface: NoteClickInterface)
    : RecyclerView.Adapter<NoteRVAdapter.ViewHolder>() {

    interface NoteClickInterface {
        fun onDeleteIconClick(note: Note)
        fun onNoteClick(note: Note)
    }


    private val allNotes = ArrayList<Note>()


  inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
      val noteTV = itemView.findViewById<TextView>(R.id.idTVNote)
      val dateTV = itemView.findViewById<TextView>(R.id.idTVDate)
      val deleteIV = itemView.findViewById<ImageView>(R.id.idIVDelete)
    }

    override fun onCreateViewHolder(perent: ViewGroup, p1: Int): ViewHolder {
        val view  = LayoutInflater.from(perent.context).inflate(R.layout.note_rv_item,perent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
      return  allNotes.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.noteTV.setText(allNotes.get(position).noteTitle)
        holder.dateTV.setText("Last Updated : " + allNotes.get(position).timeStamp)
        holder.deleteIV.setOnClickListener {
            noteClickInterface.onDeleteIconClick(allNotes.get(position))
        }
        holder.itemView.setOnClickListener {
            noteClickInterface.onNoteClick(allNotes.get(position))
        }
    }

    fun updateList(newList: List<Note>) {
        allNotes.clear()
        allNotes.addAll(newList)
        notifyDataSetChanged()
    }
}


