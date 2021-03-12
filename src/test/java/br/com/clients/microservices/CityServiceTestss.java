package br.com.clients.microservices;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

import br.com.clients.microservices.controller.model.City;
import br.com.clients.microservices.controller.model.State;
import br.com.clients.microservices.domain.exception.EntityBusinessException;
import br.com.clients.microservices.domain.service.CityService;
import br.com.clients.microservices.domain.service.ClientService;
import br.com.clients.microservices.domain.service.StateService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootApiClientsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@PropertySource("application-test.properties")
class CityServiceTestss {

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
	void shouldCreateCityWithSuccess() {
		State state = stateService.save(new State(null, "PR"));
		City newCity = new City();
		newCity.setName("Curitiba");
		newCity.setState(state);

		newCity = cityService.save(newCity);

		assertThat(newCity).isNotNull();
		assertThat(newCity.getId()).isNotNull();
		assertThat(newCity.getName()).isEqualTo("Curitiba");
		assertThat(newCity.getState().getId()).isEqualTo(state.getId());
		assertThat(newCity.getState().getName()).isEqualTo(state.getName());
	}

	@Test
	void shouldNotCreateCityWithoutName() {
		State state = stateService.save(new State(null, "PR"));
		City newCity = new City();
		newCity.setName("");
		newCity.setState(state);

		assertThrows(EntityBusinessException.class, () -> cityService.save(newCity));
	}

	@Test
	void shouldVerifyFindByCityId() {
		State state = stateService.save(new State(null, "PR"));
		City newCity = new City();
		newCity.setName("Curitiba");
		newCity.setState(state);
		newCity = cityService.save(newCity);
		
		newCity = cityService.findById(newCity.getId());

		assertThat(newCity).isNotNull();
		assertThat(newCity.getId()).isEqualTo(newCity.getId());
		assertThat(newCity.getName()).isEqualTo("Curitiba");
	}

	@Test
	void shouldVerifyFindAllCities() {
		State state = stateService.save(new State(null, "PR"));
		cityService.save(new City(null, "Curitiba", state));
		cityService.save(new City(null, "Auto Piquiri", state));

		List<City> cities = cityService.list(null);

		assertThat(cities.size()).isEqualTo(2);
	}
	
	@Test
	void shouldVerifyIfWasDeletedCity() {
		assertThat(cityService.list(null).size()).isEqualTo(0);
		
		State state = stateService.save(new State(null, "PR"));
		City city = cityService.save(new City(null, "Curitiba", state));
		
		assertThat(cityService.list(null).size()).isEqualTo(1);

		cityService.delete(city.getId());

		assertThat(cityService.list(null).size()).isEqualTo(0);
	}

}
