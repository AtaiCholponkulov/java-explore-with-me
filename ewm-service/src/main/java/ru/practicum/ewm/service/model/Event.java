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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;/* идент-ор события */

    @Column(name = "annotation")
    private String annotation;/* краткое описание */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;/* категория события */

    @Column(name = "confirmed_requests")
    private int confirmedRequests;/* одобренные заявки на участие */

    @Column(name = "created_on")
    private LocalDateTime createdOn;/* дата и время создания события */

    @Column(name = "description")
    private String description;/* полное описание */

    @Column(name = "event_date")
    private LocalDateTime eventDate;/* дата события */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "initiator_id")
    private User initiator;/* автор события */

    @Column(name = "longitude")
    private Float longitude;/* широта места */

    @Column(name = "latitude")
    private Float latitude;/* долгота места */

    @Column(name = "paid")
    private Boolean paid;/* (бес)платный вход */

    @Column(name = "participant_limit")
    private Integer participantLimit;/* ограничение на кол-во участников */

    @Column(name = "published_on")
    private LocalDateTime publishedOn;/* дата и время публикации события */

    @Column(name = "request_moderation")
    private Boolean requestModeration;/* пре-модерация заявок на участие */

    @Enumerated(value = EnumType.STRING)
    private State state;/* состояние жизненного цикла */

    @Column(name = "title")
    private String title;/* заголовок */
}
