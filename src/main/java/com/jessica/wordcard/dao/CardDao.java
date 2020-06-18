package com.jessica.wordcard.dao;


import com.jessica.wordcard.model.Card;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardDao {
    int insertCard(UUID id, Card card);

    default int insertCard(String spelling){
        UUID id = UUID.randomUUID();

        return insertCard(id, new Card(id, spelling, null));
    }

    List<Card> selectAllCard();

    Optional<Card> selectCardById(UUID id);

    int deleteCardById(UUID id);

    int updateCardById(UUID id, Card card);

}
