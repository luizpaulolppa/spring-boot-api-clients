package br.com.clients.microservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.clients.microservices.SpringBootApiClientsApplication;
import br.com.clients.microservices.controller.model.City;
import br.com.clients.microservices.controller.model.Client;
import br.com.clients.microservices.controller.model.State;
import br.com.clients.microservices.domain.exception.EntityBusinessException;
import br.com.clients.microservices.domain.service.CityService;
import br.com.clients.microservices.domain.service.ClientService;
import br.com.clients.microservices.domain.service.StateService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootApiClientsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@PropertySource("application-test.properties")
class ClientServiceTestss {

	@Autowired
	private StateService stateService;
	
	@Autowired
	private ClientService clientService;
	
	@Autowired
	private CityService cityService;

	@BeforeEach
	public void beforeEach() {
		clientService.list(null).forEach(client -> clientService.delete(client.getId()));
		cityService.list(null).forEach(city -> cityService.delete(city.getId()));
		stateService.list().forEach(state -> stateService.delete(state.getId()));
	}

	@Test
	void shouldCreateClientWithSuccess() {
		State state = stateService.save(new State(null, "PR"));
		City city = cityService.save(new City(null, "Curitiba", state));
		Client newClient = new Client();
		newClient.setName("Ana Carla Pilegi");
		newClient.setAge(20);
		newClient.setSex("MALE");
		newClient.setBirthDate(LocalDate.of(1995, 12, 3));
		newClient.setCity(city);

		newClient = clientService.save(newClient);

		assertThat(newClient).isNotNull();
		assertThat(newClient.getId()).isNotNull();
		assertThat(newClient.getName()).isEqualTo("Ana Carla Pilegi");
		assertThat(newClient.getCity().getId()).isEqualTo(city.getId());
		assertThat(newClient.getCity().getName()).isEqualTo(city.getName());
	}

	@Test
	void shouldNotCreateClientWithoutName() {
		State state = stateService.save(new State(null, "PR"));
		City city = cityService.save(new City(null, "Curitiba", state));
		Client newClient = new Client();
		newClient.setName("");
		newClient.setAge(20);
		newClient.setSex("MALE");
		newClient.setBirthDate(LocalDate.of(1995, 12, 3));
		newClient.setCity(city);

		assertThrows(EntityBusinessException.class, () -> clientService.save(newClient));
	}
	
	@Test
	void shouldNotCreateClientWithoutAge() {
		State state = stateService.save(new State(null, "PR"));
		City city = cityService.save(new City(null, "Curitiba", state));
		Client newClient = new Client();
		newClient.setName("Ana Carla Pilegi");
		newClient.setAge(null);
		newClient.setSex("MALE");
		newClient.setBirthDate(LocalDate.of(1995, 12, 3));
		newClient.setCity(city);

		assertThrows(EntityBusinessException.class, () -> clientService.save(newClient));
	}
	
	@Test
	void shouldNotCreateClientWithoutSex() {
		State state = stateService.save(new State(null, "PR"));
		City city = cityService.save(new City(null, "Curitiba", state));
		Client newClient = new Client();
		newClient.setName("Ana Carla Pilegi");
		newClient.setAge(22);
		newClient.setSex("");
		newClient.setBirthDate(LocalDate.of(1995, 12, 3));
		newClient.setCity(city);

		assertThrows(EntityBusinessException.class, () -> clientService.save(newClient));
	}
	
	@Test
	void shouldNotCreateClientWithoutBirthDate() {
		State state = stateService.save(new State(null, "PR"));
		City city = cityService.save(new City(null, "Curitiba", state));
		Client newClient = new Client();
		newClient.setName("Ana Carla Pilegi");
		newClient.setAge(22);
		newClient.setSex("MALE");
		newClient.setBirthDate(null);
		newClient.setCity(city);

		assertThrows(EntityBusinessException.class, () -> clientService.save(newClient));
	}
	
	@Test
	void shouldNotCreateClientWithoutCity() {
		City city = null;
		Client newClient = new Client();
		newClient.setName("Ana Carla Pilegi");
		newClient.setAge(22);
		newClient.setSex("MALE");
		newClient.setBirthDate(null);
		newClient.setCity(city);

		assertThrows(EntityBusinessException.class, () -> clientService.save(newClient));
	}

	@Test
	void shouldVerifyFindByClientId() {
		State state = stateService.save(new State(null, "PR"));
		City city = cityService.save(new City(null, "Curitiba", state));
		Client newClient = new Client();
		newClient.setName("Ana Carla Pilegi");
		newClient.setAge(20);
		newClient.setSex("MALE");
		newClient.setBirthDate(LocalDate.of(1995, 12, 3));
		newClient.setCity(city);
		newClient = clientService.save(newClient);
		
		newClient = clientService.findById(newClient.getId());

		assertThat(newClient).isNotNull();
		assertThat(newClient.getName()).isEqualTo("Ana Carla Pilegi");
	}

	@Test
	void shouldVerifyFindAllClients() {
		assertThat(cityService.list(null).size()).isEqualTo(0);
		
		State state = stateService.save(new State(null, "PR"));
		City city = cityService.save(new City(null, "Curitiba", state));
		Client newClient = new Client();
		newClient.setName("Ana Carla Pilegi");
		newClient.setAge(20);
		newClient.setSex("MALE");
		newClient.setBirthDate(LocalDate.of(1995, 12, 3));
		newClient.setCity(city);
		newClient = clientService.save(newClient);

		List<Client> clients = clientService.list(null);

		assertThat(clients.size()).isEqualTo(1);
	}
	
	@Test
	void shouldVerifyIfWasDeletedClient() {
		assertThat(clientService.list(null).size()).isEqualTo(0);
		
		State state = stateService.save(new State(null, "PR"));
		City city = cityService.save(new City(null, "Curitiba", state));
		Client newClient = new Client();
		newClient.setName("Ana Carla Pilegi");
		newClient.setAge(20);
		newClient.setSex("MALE");
		newClient.setBirthDate(LocalDate.of(1995, 12, 3));
		newClient.setCity(city);
		newClient = clientService.save(newClient);
		
		assertThat(clientService.list(null).size()).isEqualTo(1);

		clientService.delete(newClient.getId());

		assertThat(clientService.list(null).size()).isEqualTo(0);
	}

}
