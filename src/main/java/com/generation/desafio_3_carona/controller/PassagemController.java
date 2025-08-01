package com.generation.desafio_3_carona.controller;

import com.generation.desafio_3_carona.dto.*;
import com.generation.desafio_3_carona.model.Carona;
import com.generation.desafio_3_carona.model.Usuario;
import com.generation.desafio_3_carona.repository.UsuarioRepository;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.generation.desafio_3_carona.service.PagamentoService;
import com.generation.desafio_3_carona.service.PassagemService;
import com.generation.desafio_3_carona.service.RecursoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.generation.desafio_3_carona.model.Passagem;
import com.generation.desafio_3_carona.repository.CaronaRepository;
import com.generation.desafio_3_carona.repository.PassagemRepository;

@RestController
@RequestMapping("/passagens")

public class PassagemController {
	
	@Autowired
    private PagamentoService pagamento;

    private final UsuarioRepository usuarioRepository;
    private final PassagemRepository passagemRepository;
    private final PassagemService passagemService;

    PassagemController(UsuarioRepository usuarioRepository, CaronaRepository caronaRepository, PassagemRepository passagemRepository, RecursoService recursoService, PassagemService passagemService) {
        this.usuarioRepository = usuarioRepository;
        this.passagemRepository = passagemRepository;
        this.passagemService = passagemService;
    }

    @GetMapping
    public ResponseEntity<List<PassagemResponseDTO>> getMinhasPassagens() {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

        Usuario usuario = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado."));

        List<Passagem> passagensDoUsuario = passagemRepository.findAllByPassageiro_Id(usuario.getId());

        
        List<PassagemResponseDTO> passagensDTO = passagensDoUsuario.stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
        

        if (passagensDoUsuario.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(passagensDTO);
    }

    private PassagemResponseDTO converterParaDTO(Passagem passagem) {
        PassagemResponseDTO passagemDTO = new PassagemResponseDTO();
        passagemDTO.setId(passagem.getId());

        Carona carona = passagem.getCarona();
        if (carona != null) {
            CaronaResponseDTO caronaDTO = new CaronaResponseDTO();
            caronaDTO.setId(carona.getId());
            caronaDTO.setOrigem(carona.getOrigem());
            caronaDTO.setDestino(carona.getDestino());
            caronaDTO.setDataHoraPartida(carona.getDataHoraPartida());
            caronaDTO.setDataHoraChegada(carona.getDataHoraChegada());
            caronaDTO.setValorPorPassageiro(carona.getValorPorPassageiro());
            caronaDTO.setStatusCarona(carona.getStatusCarona());
            caronaDTO.setVagas(carona.getVagas());
            caronaDTO.setDistanciaKm(carona.getDistanciaKm());
            caronaDTO.setTempoViagem(carona.getTempoViagem());


            Usuario motorista = carona.getMotorista();
            if (motorista != null) {
                caronaDTO.setMotorista(new UsuarioDTO(motorista.getId(), motorista.getNome(), motorista.getFoto()));
            }
            passagemDTO.setCarona(caronaDTO);
        }

        Usuario passageiro = passagem.getPassageiro();
        if (passageiro != null) {
            passagemDTO.setPassageiro(new UsuarioDTO(passageiro.getId(), passageiro.getNome(), passageiro.getFoto()));
        }

        return passagemDTO;
    }

    @GetMapping("/{id}")
    public ResponseEntity<PassagemResponseDTO> getById(@PathVariable Long id) {
        return passagemRepository.findById(id)
                .map(this::converterParaDTO)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Passagem não encontrada"));
    }

    @PostMapping("/criar")
    public ResponseEntity<PassagemResponseDTO> criarPassagem(@RequestBody PassagemDTO passagemDTO) {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        Passagem novaPassagem = passagemService.criarPassagem(passagemDTO.getCaronaId(), emailUsuarioLogado);
        return ResponseEntity.status(HttpStatus.CREATED).body(converterParaDTO(novaPassagem));
    }
   
    

    @PutMapping
    public ResponseEntity<Passagem> updatePassagem(@RequestBody Passagem passagem) {
        if (usuarioRepository.existsById(passagem.getPassageiro().getId()) && passagemRepository.existsById(passagem.getId()))
            return ResponseEntity.status(HttpStatus.CREATED).body(passagemRepository.save(passagem));
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Passagem ou usuário não exite!", null);
    }

    @DeleteMapping({"/{id}"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePassagem(@PathVariable Long id) {
        String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();
        passagemService.deletarPassagem(id, emailUsuarioLogado);
    }
    
    
    
    @PostMapping("/pagamento/abacate")
    public ResponseEntity<String> criarPagamento(@RequestBody PagamentoRequestDTO dto) {
        try {
            String emailUsuarioLogado = SecurityContextHolder.getContext().getAuthentication().getName();

            if (emailUsuarioLogado == null || emailUsuarioLogado.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
            }

            

            String linkPagamento = pagamento.criarCobranca(dto);
            return ResponseEntity.ok(linkPagamento);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar pagamento: " + e.getMessage());
        }
    }

}
