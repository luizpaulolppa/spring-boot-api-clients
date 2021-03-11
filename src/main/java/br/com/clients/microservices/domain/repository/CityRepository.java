package br.com.clients.microservices.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.clients.microservices.domain.entity.CityEntity;
import br.com.clients.microservices.domain.entity.StateEntity;

@Repository
public interface CityRepository extends JpaRepository<CityEntity, Long> {

	@Query("SELECT ce FROM CityEntity ce WHERE ce.name LIKE :name")
	List<CityEntity> findByName(String name);
	
	List<CityEntity> findByState(StateEntity state);

}
