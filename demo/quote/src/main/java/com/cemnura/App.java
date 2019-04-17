package com.cemnura;

import com.cemnura.dal.QuoteDBAccess;
import com.cemnura.entity.MovieCharacter;
import com.cemnura.entity.Quote;

import java.util.*;

public class App {

    public static void main(String[] args) {

        MovieCharacter character = new MovieCharacter();

        character.setName("Cem");

        List<Quote> my_qoutes = new ArrayList<>();

        Quote my_qoute = new Quote();

        my_qoute.setQuote("Helidon is awesome");

        my_qoutes.add(my_qoute);


        character.addQuote(my_qoute);
        Quote my_qoute2 = new Quote();

        my_qoute2.setQuote("Helidon is awesome 2");
        character.addQuote(my_qoute2);




        MovieCharacter character2 = new MovieCharacter();

        character2.setName("Nuras");

        Quote my_qoute3 = new Quote();

        my_qoute3.setQuote("Helidon is awesome 3");

        my_qoutes.add(my_qoute);

        character2.addQuote(my_qoute3);
        Quote my_qoute4 = new Quote();

        my_qoute4.setQuote("Helidon is awesome 4");
        character2.addQuote(my_qoute4);

        QuoteDBAccess dbAccess = new QuoteDBAccess();

        dbAccess.insertCharacter(character);
        dbAccess.insertCharacter(character2);


        Optional<MovieCharacter> hero = dbAccess.getCharacterByName("Cem");

        try {
            Thread.sleep(10000);
        }catch (Exception ex){

        }



        if (!hero.isPresent())
            System.out.println("Empty");
        else {
            System.out.println("Not empty");
            MovieCharacter h = hero.get();

            System.out.println(h.getName());

            List<Quote> retrieved_quotes = h.getQuotes();

            for (Quote quote:
                 retrieved_quotes) {

                System.out.println(quote.getQuote());
            }
        }


    }
}
