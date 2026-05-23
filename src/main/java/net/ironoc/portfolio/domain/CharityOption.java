package net.ironoc.portfolio.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "charity_option")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CharityOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String donate;

    @Column(nullable = false)
    private String link;

    @Column(nullable = false)
    private String img;

    @Column(nullable = false)
    private String alt;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String overview;

    @Column(nullable = false)
    private Integer founded;

    @Column(nullable = false)
    private String phone;
}
