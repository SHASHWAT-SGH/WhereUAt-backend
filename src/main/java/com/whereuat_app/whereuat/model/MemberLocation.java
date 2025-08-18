package com.whereuat_app.whereuat.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.Instant;

@Document
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@CompoundIndexes({
        @CompoundIndex(name = "event_member_unique", def = "{eventId: 1, memberId: 1}", unique = true)
})
public class MemberLocation {
    @Id
    private String id;

    @Indexed
    private String eventId;

    @Indexed
    private String memberId;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location; // [lng, lat]

    private Double accuracy;
    private Double heading;
    private Double speed;

    @LastModifiedDate
    private Instant updatedAt;
}
