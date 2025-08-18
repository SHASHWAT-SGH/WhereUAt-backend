package com.whereuat_app.whereuat.repository;

import com.whereuat_app.whereuat.model.MemberLocation;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface MemberLocationRepository extends MongoRepository<MemberLocation, String> {
    List<MemberLocation> findByEventId(String eventId);
    Optional<MemberLocation> findByEventIdAndMemberId(String eventId, String memberId);
}
