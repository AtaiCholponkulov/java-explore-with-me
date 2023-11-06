package ru.practicum.ewm.service.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "compilation")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Compilation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pinned")
    private Boolean pinned;

    @Column(name = "title")
    private String title;
}
