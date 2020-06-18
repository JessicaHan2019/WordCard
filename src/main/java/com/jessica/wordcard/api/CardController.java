package com.jessica.wordcard.api;

import com.jessica.wordcard.model.Card;
import com.jessica.wordcard.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequestMapping("/card")
@RestController
public class CardController {
    private final CardService cardService;

    @Autowired
    public CardController(CardService cardService) {
        this.cardService = cardService;
    }

    @GetMapping
    public List<Card> getAllCard() { return cardService.getAllCards(); }

    @GetMapping(path = "{id}")
    public Card getCardById(@PathVariable("id") UUID id) {
        return cardService.getCardById(id)
                .orElse(null);
    }

    @PostMapping(
            path = "{id}/image/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public void uploadCardImage(@PathVariable("id") UUID cardId,
                                @RequestParam("file") MultipartFile file){
        cardService.uploadCardImage(cardId, file);
    }

    @GetMapping(path = "{id}/image/download")
    public byte[] downloadCardImage(@PathVariable("id") UUID cardId){
        return cardService.downloadCardImage(cardId);
    }

    @PostMapping(path = "{spelling}/add")
    public void addCard(@PathVariable("spelling") String spelling) { cardService.addCard(spelling); }

    @PutMapping(path = "{id}")
    public void updateCard(@PathVariable("id") UUID id, Card cardToUpdate){ cardService.updateCard(id, cardToUpdate); }

    @DeleteMapping(path = "{id}")
    public void deleteCard(@PathVariable("id") UUID id){ cardService.deleteCard(id); }

}
