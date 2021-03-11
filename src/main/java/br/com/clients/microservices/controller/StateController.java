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
import br.com.clients.microservices.controller.model.State;
import br.com.clients.microservices.domain.exception.EntityBusinessException;
import br.com.clients.microservices.domain.exception.EntityConflictException;
import br.com.clients.microservices.domain.exception.EntityNotFoundException;
import br.com.clients.microservices.domain.service.CityService;
import br.com.clients.microservices.domain.service.StateService;

@RestController
@RequestMapping("/states")
public class StateController {

	@Autowired
	private StateService stateService;
	
	@Autowired
	private CityService cityService;

	@GetMapping
	public ResponseEntity<List<State>> list() {
		return ResponseEntity.ok(stateService.list());
	}

	@GetMapping("/{id}")
	public ResponseEntity<State> find(@PathVariable Long id) {
		try {
			State state = stateService.findById(id);

			return ResponseEntity.ok(state);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.noContent().build();
		}
	}
	
	@GetMapping("/{id}/cities")
	public ResponseEntity<List<City>> findCities(@PathVariable Long id) {
		try {
			List<City> cities = cityService.findCitiesByStateId(id);

			return ResponseEntity.ok(cities);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PostMapping
	public ResponseEntity<?> create(@RequestBody State state) {
		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(stateService.save(state));
		} catch (EntityBusinessException e) {
			return ResponseEntity.badRequest().body(new ErrorMessage(Arrays.asList(e.getMessage())));
		}
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody State state) {
		try {
			return ResponseEntity.ok(stateService.update(id, state));
		} catch (EntityBusinessException e) {
			return ResponseEntity.badRequest().body(new ErrorMessage(Arrays.asList(e.getMessage())));
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deletar(@PathVariable Long id) {
		try {
			stateService.delete(id);
			return ResponseEntity.noContent().build();
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}  catch (EntityConflictException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorMessage(Arrays.asList(e.getMessage())));
		}
	}

}
