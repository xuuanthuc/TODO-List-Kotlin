package com.example.todolist

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHandler(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        private const val DATABASE_NAME = "TodoDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_TODO = "todoTable"
        private const val KEY_ID = "_id"
        private const val KEy_TODO = "todo"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TODO_TABLE =
            ("CREATE TABLE " + TABLE_TODO + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEy_TODO + " TEXT " + ")")
        db?.execSQL(CREATE_TODO_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(" DROP TABLE IF EXISTS " + TABLE_TODO)
        onCreate(db)
    }

    fun addTodo(todo: TODOClass): Long {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEy_TODO, todo.todo)
        val success = db.insert(TABLE_TODO, null, contentValues)
        db.close()
        return success
    }

    fun viewTodo(): ArrayList<TODOClass> {
        val todoList: ArrayList<TODOClass> = ArrayList<TODOClass>()
        val selectQuery = "SELECT * FROM $TABLE_TODO"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLException){
            db.execSQL(selectQuery)
            return  ArrayList()
        }
        var id: Int
        var todo: String
        if(cursor.moveToFirst()){
            do {
                id = cursor.getInt(cursor.getColumnIndex(KEY_ID))
                todo = cursor.getString(cursor.getColumnIndex(KEy_TODO))
                val itemTodo = TODOClass(id = id, todo = todo)
                todoList.add(itemTodo)
            } while (
                cursor.moveToNext()
            )
        }
        return todoList
    }
    fun updateTodo(todo: TODOClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEy_TODO, todo.todo)
        val success = db.update(TABLE_TODO, contentValues, KEY_ID + " = " + todo.id, null)
        db.close()
        return success
    }
    fun deleteTodo(todo: TODOClass): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_ID, todo.id)
        val success = db.delete(TABLE_TODO, KEY_ID + " = " + todo.id, null)
        db.close()
        return success
    }

}