package com.example.demo.controller;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.demo.entity.Studentity;
import org.example.demo.entity.Studservice;

@RestController
@RequestMapping("/student")
public class Studctl{
    @Autowired
    private Studservice ser;
    //POST
    @PostMapping("/add")
    public Studentity addStudent(@RequestBody )
}