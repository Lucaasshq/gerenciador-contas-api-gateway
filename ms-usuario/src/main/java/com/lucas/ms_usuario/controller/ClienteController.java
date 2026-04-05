package com.lucas.ms_usuario.controller;

import com.lucas.ms_usuario.entity.Cliente;
import com.lucas.ms_usuario.repository.ClienteRepository;
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
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    ClienteRepository clienteRepository;

    @GetMapping("/{id}")
    public EntityModel<Cliente> buscarCliente(@PathVariable Long id){
        Cliente cliente = clienteRepository.findById(id).orElseThrow(() -> new RuntimeException("Cliente não encontrado!"));
        EntityModel<Cliente> model = EntityModel.of(cliente);
        model.add(linkTo(methodOn(ClienteController.class).buscarCliente(id)).withSelfRel());
        return model;
    }

    @GetMapping()
    public CollectionModel<EntityModel<Cliente>> listarTodos(){
        List<Cliente> list = clienteRepository.findAll();
        List<EntityModel<Cliente>> clientesComLinks = list.stream()
                .map(cliente -> EntityModel.of(cliente,linkTo(methodOn(ClienteController.class).buscarCliente(cliente.getId())).withSelfRel()))
                .toList();

        return CollectionModel.of(clientesComLinks, linkTo(methodOn(ClienteController.class).listarTodos()).withSelfRel());
    }
}
