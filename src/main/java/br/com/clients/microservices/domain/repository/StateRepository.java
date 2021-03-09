package br.com.clients.microservices.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.clients.microservices.domain.entity.StateEntity;

@Repository
public interface StateRepository extends JpaRepository<StateEntity, Long> {

}
