package com.example.todo.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.example.todo.model.Todo;
import com.example.todo.model.TodoRowMapper;

import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;


@Service
public class TodoH2Service{

    @Autowired
    private JdbcTemplate db;

    public ArrayList<Todo> getTodos() {
       return (ArrayList<Todo>) db.query("SELECT * FROM todolist",new TodoRowMapper());
    }
    public Todo getTodoById(int id){
        try{
      return db.queryForObject("SELECT * FROM todolist where id=?",new TodoRowMapper(),id);
    }
        catch(Exception e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }
    public Todo addTodo(Todo todo){
        db.update("INSERT INTO todolist(todo,priority,status) values(?,?,?)",todo.getTodo(),todo.getPriority(),todo.getStatus());
        return db.queryForObject("select * from todolist where todo=? and priority=?",new TodoRowMapper(),todo.getTodo(),todo.getPriority());
    }
    
    public Todo updateTodo(int id,Todo todo){
        if(todo.getTodo()!=null){
            db.update("Update todolist set todo=? where id=?",todo.getTodo(),id);
        }
         if(todo.getPriority()!=null){
            db.update("Update todolist set priority=? where id=?",todo.getPriority(),id);
        }
         if(todo.getStatus()!=null){
            db.update("Update todolist set status=? where id=?",todo.getStatus(),id);
        }
       return getTodoById(id);
    }

    public void deleteTodo(int id){
      db.update("delete from todolist where id=?",id);
    }
}