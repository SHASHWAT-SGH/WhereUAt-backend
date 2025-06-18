package com.whereuat_app.whereuat.repository;

import com.whereuat_app.whereuat.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> getByEventOrganizerId(String eventOrganizerId);
    List<Event> findByEventMembersIdContaining(String memberId);
}
