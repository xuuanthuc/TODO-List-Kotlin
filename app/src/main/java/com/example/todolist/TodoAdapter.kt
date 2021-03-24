package com.example.todolist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_todo.view.*

class TodoAdapter(val context: Context, val items: ArrayList<TODOClass>): RecyclerView.Adapter<TodoAdapter.ViewHolder>() {
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val llMain = view.llMain
        val tvTodo = view.tvTodo
        val ivUpdate = view.ivUpdateTodo
        val ivDelete = view.ivDeleteTodo
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_todo,parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var items = items.get(position)
        holder.tvTodo.text = items.todo
        holder.ivUpdate.setOnClickListener {
            if(context is MainActivity){
                context.updateTODO(items)
            }
        }
        holder.ivDelete.setOnClickListener {
            if(context is MainActivity){
                context.deleteTODO(items)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}