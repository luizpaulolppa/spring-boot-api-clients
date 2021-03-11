package br.com.clients.microservices.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import br.com.clients.microservices.controller.model.City;
import br.com.clients.microservices.controller.model.State;
import br.com.clients.microservices.domain.entity.CityEntity;
import br.com.clients.microservices.domain.entity.StateEntity;
import br.com.clients.microservices.domain.exception.EntityBusinessException;
import br.com.clients.microservices.domain.exception.EntityConflictException;
import br.com.clients.microservices.domain.exception.EntityNotFoundException;
import br.com.clients.microservices.domain.repository.CityRepository;
import br.com.clients.microservices.domain.repository.StateRepository;

@Service
public class CityService {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private StateRepository stateRepository;

	public List<City> list(String name) {
		List<CityEntity> cities = new ArrayList<>();
		;

		if (name != null && !name.isEmpty()) {
			cities = cityRepository.findByName("%" + name + "%");
		} else {
			cities = cityRepository.findAll();
		}

		return cities.stream().map((city) -> mapCity(city)).collect(Collectors.toList());
	}

	public City findById(Long id) {
		Optional<CityEntity> optionalCity = cityRepository.findById(id);

		if (optionalCity.isEmpty()) {
			throw new EntityNotFoundException();
		}

		CityEntity city = optionalCity.get();
		return mapCity(city);
	}

	private City mapCity(CityEntity cityEntity) {
		City city = new City();
		city.setId(cityEntity.getId());
		city.setName(cityEntity.getName());

		if (cityEntity.getState() != null) {
			State state = new State();
			state.setId(cityEntity.getState().getId());
			state.setName(cityEntity.getState().getName());
			city.setState(state);
		}

		return city;
	}

	public City save(City city) {
		String name = city.getName();

		if (name == null || name.isEmpty()) {
			throw new EntityBusinessException("Name is required");
		}

		if (city.getState() == null || city.getState().getId() == null) {
			throw new EntityBusinessException("State id is required");
		}

		Long stateId = city.getState().getId();

		Optional<StateEntity> optionalState = stateRepository.findById(stateId);

		if (optionalState.isEmpty()) {
			throw new EntityBusinessException("State id doesn't exist");
		}

		StateEntity state = optionalState.get();

		CityEntity cityEntity = new CityEntity();
		cityEntity.setName(name);
		cityEntity.setState(state);

		cityEntity = cityRepository.save(cityEntity);

		return mapCity(cityEntity);
	}

	public City update(Long id, City city) {
		String name = city.getName();

		if (name == null || name.isEmpty()) {
			throw new EntityBusinessException("Name is required");
		}

		if (city.getState() == null || city.getState().getId() == null) {
			throw new EntityBusinessException("State id is required");
		}

		Optional<CityEntity> optionalCity = cityRepository.findById(id);

		if (optionalCity.isEmpty()) {
			throw new EntityNotFoundException("City id doesn't exist");
		}

		Long stateId = city.getState().getId();

		Optional<StateEntity> optionalState = stateRepository.findById(stateId);

		if (optionalState.isEmpty()) {
			throw new EntityBusinessException("State id doesn't exist");
		}

		StateEntity stateEntity = optionalState.get();
		CityEntity cityEntity = optionalCity.get();
		cityEntity.setName(name);
		cityEntity.setState(stateEntity);

		cityEntity = cityRepository.save(cityEntity);

		return mapCity(cityEntity);
	}

	public void delete(Long id) {
		try {
			Optional<CityEntity> optionalCity = cityRepository.findById(id);

			if (optionalCity.isEmpty()) {
				throw new EntityNotFoundException();
			}

			cityRepository.delete(optionalCity.get());
		} catch (DataIntegrityViolationException e) {
			throw new EntityConflictException("Entity in use");
		}
	}

	public List<City> findCitiesByStateId(Long id) {
		Optional<StateEntity> optionalState = stateRepository.findById(id);

		if (optionalState.isEmpty()) {
			throw new EntityNotFoundException();
		}

		List<CityEntity> cities = cityRepository.findByState(optionalState.get());

		return cities.stream().map((city) -> mapCity(city)).collect(Collectors.toList());
	}

}
