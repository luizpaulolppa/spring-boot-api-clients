package br.com.clients.microservices.domain.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.clients.microservices.controller.model.City;
import br.com.clients.microservices.controller.model.Client;
import br.com.clients.microservices.controller.model.State;
import br.com.clients.microservices.domain.entity.CityEntity;
import br.com.clients.microservices.domain.entity.ClientEntity;
import br.com.clients.microservices.domain.entity.StateEntity;
import br.com.clients.microservices.domain.repository.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	public List<Client> list() {
		List<ClientEntity> clients = clientRepository.findAll();
		return clients.stream().map((client) -> mapClient(client)).collect(Collectors.toList());
	}

	private Client mapClient(ClientEntity clientEntity) {
		Client client = new Client();
		client.setId(clientEntity.getId());
		client.setName(clientEntity.getName());
		client.setAge(clientEntity.getAge());
		client.setBirthDate(clientEntity.getBirthDate());

		CityEntity cityEntity = clientEntity.getCity();
		if (cityEntity != null) {
			City city = new City();
			city.setId(cityEntity.getId());
			city.setName(cityEntity.getName());
			
			StateEntity stateEntity = cityEntity.getState();
			State state = new State();
			state.setId(stateEntity.getId());
			state.setName(stateEntity.getName());
			city.setState(state);
			
			client.setCity(city);
		}

		return client;
	}

//	public City findById(Long id) {
//		Optional<CityEntity> optionalCity = cityRepository.findById(id);
//
//		if (optionalCity.isEmpty()) {
//			throw new EntityNotFoundException();
//		}
//
//		CityEntity city = optionalCity.get();
//		return mapCity(city);
//	}
//
//
//	public City save(City city) {
//		String name = city.getName();
//
//		if (name == null || name.isEmpty()) {
//			throw new EntityBusinessException("Name is required");
//		}
//
//		if (city.getState() == null || city.getState().getId() == null) {
//			throw new EntityBusinessException("State id is required");
//		}
//
//		Long stateId = city.getState().getId();
//
//		Optional<StateEntity> optionalState = stateRepository.findById(stateId);
//
//		if (optionalState.isEmpty()) {
//			throw new EntityBusinessException("State id doesn't exist");
//		}
//
//		StateEntity state = optionalState.get();
//
//		CityEntity cityEntity = new CityEntity();
//		cityEntity.setName(name);
//		cityEntity.setState(state);
//
//		cityEntity = cityRepository.save(cityEntity);
//
//		return mapCity(cityEntity);
//	}
//
//	public City update(Long id, City city) {
//		String name = city.getName();
//
//		if (name == null || name.isEmpty()) {
//			throw new EntityBusinessException("Name is required");
//		}
//
//		if (city.getState() == null || city.getState().getId() == null) {
//			throw new EntityBusinessException("State id is required");
//		}
//
//		Optional<CityEntity> optionalCity = cityRepository.findById(id);
//
//		if (optionalCity.isEmpty()) {
//			throw new EntityNotFoundException("City id doesn't exist");
//		}
//
//		Long stateId = city.getState().getId();
//
//		Optional<StateEntity> optionalState = stateRepository.findById(stateId);
//
//		if (optionalState.isEmpty()) {
//			throw new EntityBusinessException("State id doesn't exist");
//		}
//
//		StateEntity stateEntity = optionalState.get();
//		CityEntity cityEntity = optionalCity.get();
//		cityEntity.setName(name);
//		cityEntity.setState(stateEntity);
//
//		cityEntity = cityRepository.save(cityEntity);
//
//		return mapCity(cityEntity);
//	}
//
//	public void delete(Long id) {
//		try {
//			Optional<CityEntity> optionalCity = cityRepository.findById(id);
//			
//			if (optionalCity.isEmpty()) {
//				throw new EntityNotFoundException();
//			}
//			
//			cityRepository.delete(optionalCity.get());
//		} catch (DataIntegrityViolationException e) {
//			throw new EntityConflictException("Entity in use");
//		}
//	}

}
