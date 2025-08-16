package com.whereuat_app.whereuat.repository;

import com.whereuat_app.whereuat.enums.JoinStatus;
import com.whereuat_app.whereuat.model.EventMember;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface EventMemberRepository extends MongoRepository<EventMember, String> {
    List<EventMember> findByEventId(String id);
    List<EventMember> findByUserId(String id);

    List<EventMember> findByUserIdAndStatus(String memberId, JoinStatus joinStatus);

    Optional<EventMember> findByEventIdAndUserId(String eventId, String userId);
}
