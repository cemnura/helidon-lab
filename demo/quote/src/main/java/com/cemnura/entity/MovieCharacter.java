package com.cemnura.entity;

import org.hibernate.annotations.NaturalId;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "movie_character")
public class MovieCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String name;

    @OneToMany(mappedBy = "hero",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    private List<Quote> quotes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public List<Quote> getQuotes() {
        return quotes;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuotes(List<Quote> quotes) {
        this.quotes = quotes;
    }

    public void addQuote(Quote quote) {
        quotes.add(quote);
        quote.setHero(this);
    }
}
