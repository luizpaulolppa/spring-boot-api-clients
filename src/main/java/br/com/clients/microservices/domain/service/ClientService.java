package br.com.clients.microservices.domain.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.clients.microservices.controller.model.City;
import br.com.clients.microservices.controller.model.Client;
import br.com.clients.microservices.controller.model.State;
import br.com.clients.microservices.domain.entity.CityEntity;
import br.com.clients.microservices.domain.entity.ClientEntity;
import br.com.clients.microservices.domain.entity.StateEntity;
import br.com.clients.microservices.domain.enun.SexType;
import br.com.clients.microservices.domain.exception.EntityBusinessException;
import br.com.clients.microservices.domain.exception.EntityNotFoundException;
import br.com.clients.microservices.domain.repository.CityRepository;
import br.com.clients.microservices.domain.repository.ClientRepository;

@Service
public class ClientService {

	@Autowired
	private ClientRepository clientRepository;

	@Autowired
	private CityRepository cityRepository;

	public List<Client> list(String name) {
		List<ClientEntity> clients = new ArrayList<>();
		
		if (name != null && !name.isEmpty()) {
			clients = clientRepository.findByName("%" + name + "%");
		} else {
			clients = clientRepository.findAll();
		}
		
		return clients.stream().map((client) -> mapClient(client)).collect(Collectors.toList());
	}

	public Client findById(Long id) {
		Optional<ClientEntity> optionalClient = clientRepository.findById(id);

		if (optionalClient.isEmpty()) {
			throw new EntityNotFoundException();
		}

		ClientEntity client = optionalClient.get();
		return mapClient(client);
	}

	public Client save(Client client) {
		String name = client.getName();
		Integer age = client.getAge();
		LocalDate birthDate = client.getBirthDate();
		String sex = client.getSex();

		if (name == null || name.isEmpty()) {
			throw new EntityBusinessException("Name is required");
		}

		if (age == null) {
			throw new EntityBusinessException("Age is required");
		}

		if (birthDate == null) {
			throw new EntityBusinessException("Birth Date is required, you can complete using yyyy-mm-dd");
		}
		
		if (sex == null || sex.isEmpty() || !SexType.getEnumNames().contains(sex)) {
			throw new EntityBusinessException("Sex is required or invalid, you can complete using MALE, FEMALE or OTHER");
		}

		if (client.getCity() == null || client.getCity().getId() == null) {
			throw new EntityBusinessException("City id is required");
		}

		Long cityId = client.getCity().getId();

		Optional<CityEntity> optionalCity = cityRepository.findById(cityId);

		if (optionalCity.isEmpty()) {
			throw new EntityBusinessException("City id doesn't exist");
		}

		CityEntity city = optionalCity.get();

		ClientEntity clientEntity = new ClientEntity();
		clientEntity.setName(name);
		clientEntity.setAge(age);
		clientEntity.setBirthDate(birthDate);
		clientEntity.setSex(SexType.getSexEnum(sex));
		clientEntity.setCity(city);

		clientEntity = clientRepository.save(clientEntity);

		return mapClient(clientEntity);
	}

	public Client update(Long id, Client client) {
		String name = client.getName();
		Integer age = client.getAge();
		LocalDate birthDate = client.getBirthDate();
		String sex = client.getSex();

		Optional<ClientEntity> optionalClient = clientRepository.findById(id);

		if (optionalClient.isEmpty()) {
			throw new EntityNotFoundException("Id doesn't exist");
		}

		if (name == null || name.isEmpty()) {
			throw new EntityBusinessException("Name is required");
		}

		if (age == null) {
			throw new EntityBusinessException("Age is required");
		}

		if (birthDate == null) {
			throw new EntityBusinessException("Birth Date is required, you can complete using yyyy-mm-dd");
		}
		
		if (sex == null || sex.isEmpty() || !SexType.getEnumNames().contains(sex)) {
			throw new EntityBusinessException("Sex is required, you can complete using MALE, FEMALE or OTHER");
		}

		if (client.getCity() == null || client.getCity().getId() == null) {
			throw new EntityBusinessException("City id is required");
		}

		Long cityId = client.getCity().getId();

		Optional<CityEntity> optionalCity = cityRepository.findById(cityId);

		if (optionalCity.isEmpty()) {
			throw new EntityBusinessException("City id doesn't exist");
		}

		CityEntity cityEntity = optionalCity.get();
		ClientEntity clientEntity = optionalClient.get();
		clientEntity.setName(name);
		clientEntity.setAge(age);
		clientEntity.setBirthDate(birthDate);
		clientEntity.setSex(SexType.getSexEnum(sex));
		clientEntity.setCity(cityEntity);

		return mapClient(clientRepository.save(clientEntity));
	}

	public void delete(Long id) {
		Optional<ClientEntity> optionalClient = clientRepository.findById(id);

		if (optionalClient.isEmpty()) {
			throw new EntityNotFoundException();
		}

		clientRepository.delete(optionalClient.get());
	}

	private Client mapClient(ClientEntity clientEntity) {
		Client client = new Client();
		client.setId(clientEntity.getId());
		client.setName(clientEntity.getName());
		client.setAge(clientEntity.getAge());
		client.setBirthDate(clientEntity.getBirthDate());
		client.setSex(clientEntity.getSex().name());

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

}
