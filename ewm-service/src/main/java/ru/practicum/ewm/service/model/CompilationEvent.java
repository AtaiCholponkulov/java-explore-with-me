package ru.practicum.ewm.service.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "compilation_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompilationEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "compilation_id")
    private Compilation compilation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
}
