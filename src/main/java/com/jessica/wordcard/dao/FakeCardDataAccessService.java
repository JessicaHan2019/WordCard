package com.jessica.wordcard.dao;

import com.jessica.wordcard.model.Card;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("fakeDao")
public class FakeCardDataAccessService implements CardDao {

    private static List<Card> DB = new ArrayList<>();

    static {
        DB.add(new Card(UUID.fromString("14a73b83-4f02-46b7-950d-e09a2ded3842"), "apple", null));
        DB.add(new Card(UUID.fromString("52e4b5cf-c619-4040-9c67-568a03b86cf1"), "banana", null));
    }



    @Override
    public int insertCard(UUID id, Card card) {
        DB.add(new Card(id, card.getSpelling(), null));
        return 1;
    }

    @Override
    public List<Card> selectAllCard() {
        return DB;
    }

    @Override
    public Optional<Card> selectCardById(UUID id) {

        return DB.stream()
                .filter(card -> card.getId().equals(id))
                .findFirst();
    }

    @Override
    public int deleteCardById(UUID id) {
        Optional<Card> cardMaybe = selectCardById(id);
        if(cardMaybe.isEmpty()){
            return 0;
        }

        DB.remove(cardMaybe.get());
        return 1;
    }

    @Override
    public int updateCardById(UUID id, Card updatedCard) {

        return selectCardById(id)
                .map(card -> {
                    int indexOfCardToUpdate = DB.indexOf(card);
                    if(indexOfCardToUpdate >= 0){
                        if (updatedCard.getCardImageLink().isPresent()){
                            DB.set(indexOfCardToUpdate, new Card(id, updatedCard.getSpelling(), updatedCard.getCardImageLink().toString()));
                        }else {
                            DB.set(indexOfCardToUpdate, new Card(id, updatedCard.getSpelling(), null));
                        }

                        return 1;
                    }
                    return 0;
                })
                .orElse(0);
    }
}
