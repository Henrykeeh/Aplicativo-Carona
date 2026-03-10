package com.generation.desafio_3_carona.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;

import com.generation.desafio_3_carona.dto.PagamentoRequestDTO;
import com.generation.desafio_3_carona.model.Carona;
import com.generation.desafio_3_carona.model.Pagamento;
import com.generation.desafio_3_carona.repository.CaronaRepository;
import com.generation.desafio_3_carona.dto.CaronaDTO;

@Service
public class PagamentoService {
	
	@Value("${ABACATE_API_KEY}")
    private String apiKey;
	
	private final CaronaRepository caronaRepository;
	
	@Autowired
    public PagamentoService(CaronaRepository caronaRepository) { 
        this.caronaRepository = caronaRepository;
    }

	public String criarCobranca(PagamentoRequestDTO dto) {
	    RestTemplate restTemplate = new RestTemplate();
	    
	    Carona carona = caronaRepository.findById(dto.getCaronaId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Carona não encontrada para criar pagamento!"));


	    Pagamento request = new Pagamento();
	    request.setFrequency("ONE_TIME");
	    request.setMethods(Collections.singletonList("PIX"));

	    Pagamento.Product product = new Pagamento.Product();
	    product.setExternalId(dto.getCaronaId().toString());
	    product.setName(carona.getOrigem() + " -> " + carona.getDestino());
	    product.setQuantity(1);
	    product.setPrice(dto.getValorEmCentavos());
	    product.setDescription("Carona ID: " + dto.getCaronaId());
	    request.setProducts(Collections.singletonList(product));

	    Pagamento.Customer customer = new Pagamento.Customer();
	    customer.setEmail(dto.getEmailCliente());
	    customer.setName(dto.getNomeCliente());
	    customer.setCellphone("1199999999");
	    customer.setTaxId("11144477735");
	    request.setCustomer(customer);

	    request.setReturnUrl("http://localhost:5173/caronas");
	    request.setCompletionUrl("http://localhost:5173/passagens?success=true");

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(apiKey);

	    HttpEntity<Pagamento> entity = new HttpEntity<>(request, headers);

	    ResponseEntity<String> response = restTemplate.exchange(
	        "https://api.abacatepay.com/v1/billing/create",
	        HttpMethod.POST,
	        entity,
	        String.class
	    );

	    if (!response.getStatusCode().is2xxSuccessful()) {
	        throw new RuntimeException("Erro ao criar cobrança: " + response.getBody());
	    }

	    return response.getBody(); 
	}

}

