package br.com.clients.microservices.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.clients.microservices.domain.entity.ClientEntity;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
	
	@Query("SELECT ce FROM ClientEntity ce WHERE ce.name LIKE :name")
	public List<ClientEntity> findByName(String name);

}
