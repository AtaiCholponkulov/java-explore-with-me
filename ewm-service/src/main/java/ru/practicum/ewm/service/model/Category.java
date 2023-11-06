package ru.practicum.ewm.service.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "category")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;
}
