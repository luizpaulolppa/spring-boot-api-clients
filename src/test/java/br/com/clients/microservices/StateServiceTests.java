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

import br.com.clients.microservices.SpringBootApiClientsApplication;
import br.com.clients.microservices.controller.model.State;
import br.com.clients.microservices.domain.exception.EntityBusinessException;
import br.com.clients.microservices.domain.service.CityService;
import br.com.clients.microservices.domain.service.ClientService;
import br.com.clients.microservices.domain.service.StateService;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringBootApiClientsApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "test")
@PropertySource("application-test.properties")
class StateServiceTests {

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
	void shouldCreateStateWithSuccess() {
		State newState = new State();
		newState.setName("PR");

		newState = stateService.save(newState);

		assertThat(newState).isNotNull();
		assertThat(newState.getId()).isNotNull();
		assertThat(newState.getName()).isEqualTo("PR");
	}

	@Test
	void shouldNotCreateStateWithoutName() {
		State newState = new State();
		newState.setName("");

		assertThrows(EntityBusinessException.class, () -> stateService.save(newState));
	}

	@Test
	void shouldVerifyFindByStateId() {
		State state = stateService.save(new State(null, "PR"));
		assertThat(state).isNotNull();
		assertThat(state.getId()).isNotNull();
		assertThat(state.getName()).isEqualTo("PR");

		state = stateService.findById(state.getId());

		assertThat(state).isNotNull();
		assertThat(state.getId()).isEqualTo(state.getId());
		assertThat(state.getName()).isEqualTo("PR");
	}

	@Test
	void shouldVerifyFindAllStates() {
		stateService.save(new State(null, "PR"));
		stateService.save(new State(null, "SP"));
		stateService.save(new State(null, "MT"));

		List<State> states = stateService.list();

		assertThat(states.size()).isEqualTo(3);
	}
	
	@Test
	void shouldVerifyIfWasDeletedState() {
		assertThat(stateService.list().size()).isEqualTo(0);
		
		State state = stateService.save(new State(null, "PR"));
		
		assertThat(stateService.list().size()).isEqualTo(1);

		stateService.delete(state.getId());

		assertThat(stateService.list().size()).isEqualTo(0);
	}

}
