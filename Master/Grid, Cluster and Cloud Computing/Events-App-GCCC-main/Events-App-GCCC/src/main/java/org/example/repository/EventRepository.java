package org.example.repository;

import org.example.domain.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;


@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    Optional<Event> findByNameAndLocationAndDateTime(String name, String location, LocalDateTime dateTime);
}
