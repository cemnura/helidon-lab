package com.cemnura.entity;

import javax.persistence.*;

@Entity
@Table(name = "quote")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quote;

    @ManyToOne
    @JoinColumn(name = "hero_id")
    private MovieCharacter hero;

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public void setHero(MovieCharacter hero) {
        this.hero = hero;
    }

    public String getQuote() {
        return quote;
    }

    public MovieCharacter getHero() {
        return hero;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "quote='" + quote + '\'' +
                '}';
    }
}
