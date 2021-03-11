package br.com.clients.microservices.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.clients.microservices.controller.model.Client;
import br.com.clients.microservices.controller.model.ErrorMessage;
import br.com.clients.microservices.domain.exception.EntityBusinessException;
import br.com.clients.microservices.domain.exception.EntityNotFoundException;
import br.com.clients.microservices.domain.service.ClientService;

@RestController
@RequestMapping("/clients")
public class ClientController {
	
	@Autowired
	private ClientService clientService;
	
	@GetMapping
	public ResponseEntity<List<Client>> list(@RequestParam(required = false) String name) {
		System.out.println(name);
		return ResponseEntity.ok(clientService.list(name));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Client> find(@PathVariable Long id) {
		try {
			Client client = clientService.findById(id);

			return ResponseEntity.ok(client);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.noContent().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody Client client) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(clientService.save(client));
		} catch (EntityBusinessException e) {
			return ResponseEntity.badRequest().body(new ErrorMessage(Arrays.asList(e.getMessage())));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Client client) {
		try {
			return ResponseEntity.ok(clientService.update(id, client));
		} catch (EntityBusinessException e) {
			return ResponseEntity.badRequest().body(new ErrorMessage(Arrays.asList(e.getMessage())));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			clientService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

}
