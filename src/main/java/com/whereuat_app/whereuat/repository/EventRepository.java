package com.whereuat_app.whereuat.repository;

import com.whereuat_app.whereuat.model.Event;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> getByEventOrganizerId(String eventOrganizerId);

    // Find events where a user has joined
    @Query("{ 'eventMembers': { $elemMatch: { userId: ?0, status: 'JOINED' } } }")
    List<Event> findJoinedEventsByUserId(String userId);

    @Query("{ 'eventMembers.userId': ?0 }")
    List<Event> findByEventMembersUserId(String userId);

}
