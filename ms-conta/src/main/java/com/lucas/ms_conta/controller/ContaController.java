package com.lucas.ms_conta.controller;

import com.lucas.ms_conta.entity.Conta;
import com.lucas.ms_conta.repository.ContaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    private ContaRepository contaRepository;

    @GetMapping()
    public CollectionModel<EntityModel<Conta>> listarTodas(){
        List<EntityModel<Conta>> listContas = contaRepository.findAll().stream()
                .map(conta -> EntityModel.of(conta,
                        linkTo(methodOn(ContaController.class).buscarPorId(conta.getId())).withSelfRel()))
                .toList();
        return CollectionModel.of(listContas
                ,linkTo(methodOn(ContaController.class).listarTodas()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Conta> buscarPorId(@PathVariable Long id){
        Conta conta = contaRepository.findById(id).orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        return EntityModel.of(conta,
                linkTo(methodOn(ContaController.class).buscarPorId(id)).withSelfRel(),
                linkTo(methodOn(ContaController.class).listarTodas()).withRel("lista-contas"));
    }
}
