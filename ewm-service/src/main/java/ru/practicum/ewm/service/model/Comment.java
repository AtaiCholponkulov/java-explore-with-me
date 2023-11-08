package ru.practicum.ewm.service.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;/* идент-ор события */

    @Column(name = "text")
    private String text;/* комментарий */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_id")
    private Event event;/* комментируемое событие */

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private User author;/* автор */

    @Column(name = "created_on")
    private LocalDateTime createdOn;/* дата и время создания */
}
