package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAdd.setOnClickListener { view ->
            addTodo(view)
        }
        setupListOfDataIntoRecycview()

    }
    fun addTodo(view: View){
        val todoText = etTextTODO.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if(!todoText.isEmpty()){
            val status = databaseHandler.addTodo(
                TODOClass(0,todoText))
            if(status > -1){
                Toast.makeText(applicationContext, "TODO Saved", Toast.LENGTH_LONG).show()
                etTextTODO.text?.clear()
                setupListOfDataIntoRecycview()
            } else {
                Toast.makeText(applicationContext, "TODO cannot be blank", Toast.LENGTH_LONG).show()
            }
        }
    }
    private fun setupListOfDataIntoRecycview(){
        if(getItemsList().size > 0){
            rvTodoList.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            rvTodoList.layoutManager = LinearLayoutManager(this)
            val itemAdapter = TodoAdapter(this,getItemsList())
            rvTodoList.adapter = itemAdapter
        } else {
            rvTodoList.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        }
    }
    private fun getItemsList(): ArrayList<TODOClass>{
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val todoList: ArrayList<TODOClass> = databaseHandler.viewTodo()
        return todoList
    }
}