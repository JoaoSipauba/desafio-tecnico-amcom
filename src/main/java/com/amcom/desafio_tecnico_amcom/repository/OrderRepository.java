package com.amcom.desafio_tecnico_amcom.repository;

import com.amcom.desafio_tecnico_amcom.model.entity.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {

    Optional<Order> findByExternalId(String externalId);
}
