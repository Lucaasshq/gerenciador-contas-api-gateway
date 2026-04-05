package com.lucas.ms_gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class teste {

    @GetMapping("teste/")
    public String teste(){
        return "teste";
    }
}
