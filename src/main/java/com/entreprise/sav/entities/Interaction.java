package com.entreprise.sav.entities;

import com.entreprise.sav.enumeration.InteractionType;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

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
}
