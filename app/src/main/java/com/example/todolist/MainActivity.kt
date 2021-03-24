package com.example.todolist

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_update.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnAdd.setOnClickListener { view ->
            addTodo(view)
        }
        setupListOfDataIntoRecycview()
    }

    fun addTodo(view: View) {
        val todoText = etTextTODO.text.toString()
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        if (!todoText.isEmpty()) {
            val status = databaseHandler.addTodo(
                TODOClass(0, todoText)
            )
            if (status > -1) {
                Toast.makeText(applicationContext, "TODO Saved", Toast.LENGTH_LONG).show()
                etTextTODO.text?.clear()
                setupListOfDataIntoRecycview()
            }
        } else {
            Toast.makeText(applicationContext, "TODO cannot be blank", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupListOfDataIntoRecycview() {
        if (getItemsList().size > 0) {
            rvTodoList.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE
            rvTodoList.layoutManager = LinearLayoutManager(this)
            val itemAdapter = TodoAdapter(this, getItemsList())
            rvTodoList.adapter = itemAdapter
        } else {
            rvTodoList.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        }
    }

    private fun getItemsList(): ArrayList<TODOClass> {
        val databaseHandler: DatabaseHandler = DatabaseHandler(this)
        val todoList: ArrayList<TODOClass> = databaseHandler.viewTodo()
        return todoList
    }

    fun updateTODO(todoClass: TODOClass){
        val updateDialog = Dialog(this)
        updateDialog.setCancelable(false)
        updateDialog.setContentView(R.layout.dialog_update)
        updateDialog.etUpdateTodo.setText(todoClass.todo)
        updateDialog.tvUpdate.setOnClickListener{
                val todo = updateDialog.etUpdateTodo.text.toString()
                val databaseHandler: DatabaseHandler = DatabaseHandler(this)
                if(todo.isNotEmpty()){
                    val status = databaseHandler.updateTodo(TODOClass(todoClass.id, todo))
                    if(status > -1){
                        Toast.makeText(applicationContext, "TODO Update", Toast.LENGTH_LONG).show()
                        setupListOfDataIntoRecycview()
                        updateDialog.dismiss()
                    }
                } else {
                    Toast.makeText(applicationContext, "TODO cannot be blank", Toast.LENGTH_LONG).show()
                }
            }

        updateDialog.tvCancel.setOnClickListener(View.OnClickListener {
            updateDialog.dismiss()
        })
        updateDialog.show()
    }
    fun deleteTODO(todoClass: TODOClass){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete TODO")
        builder.setMessage("Are you sure want to delete ${todoClass.todo}")
        builder.setPositiveButton("Yes"){
            dialogInterface, which ->
            val databaseHandler: DatabaseHandler = DatabaseHandler(this)
            val status = databaseHandler.deleteTodo(TODOClass(todoClass.id,""))
            if(status > -1){
                Toast.makeText(applicationContext, "TODO Delete", Toast.LENGTH_LONG).show()
                setupListOfDataIntoRecycview()
            }
            dialogInterface.dismiss()
        }
        builder.setNegativeButton("No"){dialogInterface, which ->
            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog= builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }
}