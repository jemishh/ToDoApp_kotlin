package com.example.todo.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.databinding.ItemToDoBinding
import com.example.todo.models.ToDoModel

class rvAdapterToDos(private val todoList: ArrayList<ToDoModel>) : RecyclerView.Adapter<rvAdapterToDos.ViewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    inner class ViewHolder(val binding: ItemToDoBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(todoList[position]){
                binding.tvTitle.text = this.title
                binding.tvDescription.text = this.todo

            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
   /* class myViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    }
*/
}