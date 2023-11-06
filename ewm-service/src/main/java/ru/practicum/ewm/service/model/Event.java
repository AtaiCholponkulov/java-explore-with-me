package ru.practicum.ewm.service.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Event {

    /*
    * createdOn - дата и время создания события
    * description - полное описание
    * location - широта и долгота места
    * participantLimit - ограничение на кол-во участников
    * publishedOn - дата и время публикации события
    * requestModeration - пре-модерация заявок на участие
    * state - состояние жизненного цикла
    * annotation - краткое описание
    * category - id категории события
    * confirmedRequests - одобренные заявки на участие
    * eventDate - дата события
    * id - идент-ор события
    * initiator - автор события
    * paid - (бес)платный вход
    * title - заголовок
    * views - кол-во просмотров
    */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "annotation")
    private String annotation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "confirmed_requests")
    private int confirmedRequests;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "description")
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id")
    private User initiator;

    @Column(name = "longitude")
    private Float longitude;

    @Column(name = "latitude")
    private Float latitude;

    @Column(name = "paid")
    private Boolean paid;

    @Column(name = "participant_limit")
    private Integer participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn;

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(value = EnumType.STRING)
    private State state;

    @Column(name = "title")
    private String title;
}
