package com.entreprise.sav.entities;

import com.entreprise.sav.enumeration.InteractionType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.panache.common.Parameters;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "interactions")
public class Interaction extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    public Client client;

    @Column(name = "commercial")
    public String commercial;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    public InteractionType type;

    @Column(nullable = false, columnDefinition = "TEXT")
    public String summary;

    @Column(name = "occurred_at", nullable = false)
    public LocalDateTime occurredAt;

    public Integer duration;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    public LocalDateTime createdAt;

    public static PanacheQuery<Interaction> findFiltered(Long client_id, InteractionType type, String commercial, LocalDateTime from, LocalDateTime to) {
        StringBuilder query = new StringBuilder("1=1");
        Parameters params = new Parameters();

        if (client_id != null) {
            query.append(" AND client.id = :clientId");
            params.and("clientId", client_id);
        }
        if (type != null) {
            query.append(" AND type = :type");
            params.and("type", type);
        }
        if (commercial != null && !commercial.isBlank()) {
            query.append(" AND lower(commercial) LIKE lower(:commercial)");
            params.and("commercial", "%" + commercial + "%");
        }
        if (from != null) {
            query.append(" AND occurredAt >= :from");
            params.and("from", from);
        }
        if (to != null) {
            query.append(" AND occurredAt <= :to");
            params.and("to", to);
        }

        return find(query.toString(), params);
    }
}
