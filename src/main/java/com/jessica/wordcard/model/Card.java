package com.jessica.wordcard.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class Card {

    private final UUID id;

    @NotBlank
    private final String spelling;
    private String cardImageLink;

    public Card(@JsonProperty("id") UUID id,
                @JsonProperty("spelling") String spelling,
                @JsonProperty("cardImageLink") String cardImageLink) {
        this.id = id;
        this.spelling = spelling;
        this.cardImageLink = cardImageLink;
    }

    public UUID getId() {
        return id;
    }

    public String getSpelling() {
        return spelling;
    }

    public Optional<String> getCardImageLink() {
        return Optional.ofNullable(cardImageLink);
    }

    public void setCardImageLink(String cardImageLink) {
        this.cardImageLink = cardImageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return Objects.equals(id,card.id) &&
                Objects.equals(spelling,card.spelling) &&
                Objects.equals(cardImageLink,card.cardImageLink);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, spelling, cardImageLink);
    }
}

