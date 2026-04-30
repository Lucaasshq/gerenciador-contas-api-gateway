package com.lucas.ms_gateway.controller;

import com.lucas.ms_gateway.dto.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class AppController {

    private final WebClient webClient;

    public AppController(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }

    @GetMapping("/home")
    public Mono<String> home(Model model) {
        Mono<List<ClienteDTO>> clientesMono = webClient.get()
                .uri("http://localhost:8081/cliente")
                .retrieve()
                .bodyToMono(ClienteResponse.class)
                .map(response -> response._embedded().clienteList())
                .onErrorReturn(List.of());

        Mono<List<ContaDTO>> contasMono = webClient.get()
                .uri("http://localhost:8082/conta")
                .retrieve()
                .bodyToMono(ContaResponse.class)
                .map(response -> response._embedded().contaList())
                .onErrorReturn(List.of());

        return Mono.zip(clientesMono, contasMono)
                .map(tuple -> {
                    List<ClienteDTO> clienteDTOList = tuple.getT1();
                    List<ContaDTO> contaDTOList = tuple.getT2();

                    List<ClienteContaDTO> listaUnificada = clienteDTOList.stream()
                            .map(clienteDTO -> {
                                ContaDTO conta = contaDTOList.stream()
                                        .filter(c -> c.id().equals(clienteDTO.id()))
                                        .findFirst()
                                        .orElse(null);
                                return new ClienteContaDTO(
                                        clienteDTO.id(),
                                        clienteDTO.nome(),
                                        conta != null ? conta.id() : null,
                                        conta != null ? conta.numero() : "Sem Conta",
                                        conta != null ? conta.saldo() : 0.0
                                );
                            }).toList();

                    model.addAttribute("listaClientes", listaUnificada);
                    return "home";
                });
    }
}
