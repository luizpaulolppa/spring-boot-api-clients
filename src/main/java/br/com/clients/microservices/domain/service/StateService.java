package br.com.clients.microservices.domain.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.clients.microservices.controller.model.State;
import br.com.clients.microservices.domain.entity.StateEntity;
import br.com.clients.microservices.domain.exception.EntityBusinessException;
import br.com.clients.microservices.domain.exception.EntityConflictException;
import br.com.clients.microservices.domain.exception.EntityNotFoundException;
import br.com.clients.microservices.domain.repository.StateRepository;

@Service
public class StateService {

	@Autowired
	private StateRepository stateRepository;
	
	public List<State> list() {
		List<StateEntity> states = stateRepository.findAll();
		return states.stream().map((state) -> new State(state.getId(), state.getName())).collect(Collectors.toList());
	}

	public State findById(Long id) {
		Optional<StateEntity> optionalState = stateRepository.findById(id);

		if (optionalState.isEmpty()) {
			throw new EntityNotFoundException();
		}

		StateEntity state = optionalState.get();
		return new State(state.getId(), state.getName());
	}

	public State save(State state) {
		String name = state.getName();
		
		if (name == null || name.isEmpty()) {
			throw new EntityBusinessException("Name is required");
		}
		
		StateEntity stateEntity = new StateEntity();
		stateEntity.setName(state.getName());
		
		stateEntity = stateRepository.save(stateEntity);
		
		return new State(stateEntity.getId(), stateEntity.getName());
	}

	public State update(Long id, State state) {
		String name = state.getName();
		
		if (name == null || name.isEmpty()) {
			throw new EntityBusinessException("Name is required");
		}
		
		Optional<StateEntity> optionalState = stateRepository.findById(id);
		
		if (optionalState.isEmpty()) {
			throw new EntityNotFoundException();
		}
		
		StateEntity stateEntity = optionalState.get();
		stateEntity.setName(name);
		
		stateEntity = stateRepository.save(stateEntity);
		
		return new State(stateEntity.getId(), stateEntity.getName());
	}

	public void delete(Long id) {
		try {
			Optional<StateEntity> optionalState = stateRepository.findById(id);
			
			if (optionalState.isEmpty()) {
				throw new EntityNotFoundException();
			}
			
			stateRepository.delete(optionalState.get());
		} catch (DataIntegrityViolationException e) {
			throw new EntityConflictException("Entity in use");
		}
	}

}
