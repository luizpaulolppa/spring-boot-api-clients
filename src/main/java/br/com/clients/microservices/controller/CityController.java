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
import org.springframework.web.bind.annotation.RestController;

import br.com.clients.microservices.controller.model.City;
import br.com.clients.microservices.controller.model.ErrorMessage;
import br.com.clients.microservices.domain.exception.EntityBusinessException;
import br.com.clients.microservices.domain.exception.EntityConflictException;
import br.com.clients.microservices.domain.exception.EntityNotFoundException;
import br.com.clients.microservices.domain.service.CityService;

@RestController
@RequestMapping("/cities")
public class CityController {

	@Autowired
	private CityService cityService;

	@GetMapping
	public ResponseEntity<List<City>> list() {
		return ResponseEntity.ok(cityService.list());
	}

	@GetMapping("/{id}")
	public ResponseEntity<City> find(@PathVariable Long id) {
		try {
			City city = cityService.findById(id);

			return ResponseEntity.ok(city);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.noContent().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody City city) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(cityService.save(city));
		} catch (EntityBusinessException e) {
			return ResponseEntity.badRequest().body(new ErrorMessage(Arrays.asList(e.getMessage())));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody City city) {
		try {
			return ResponseEntity.ok(cityService.update(id, city));
		} catch (EntityBusinessException e) {
			return ResponseEntity.badRequest().body(new ErrorMessage(Arrays.asList(e.getMessage())));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
		try {
			cityService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}  catch (EntityConflictException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(Arrays.asList(e.getMessage())));
		}
	}

}
