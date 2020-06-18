package com.jessica.wordcard.service;

import com.jessica.wordcard.bucket.BucketName;
import com.jessica.wordcard.dao.CardDao;
import com.jessica.wordcard.fileStore.FileStore;
import com.jessica.wordcard.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class CardService {
    private final CardDao cardDao;
    private final FileStore fileStore;

    @Autowired
    public CardService(@Qualifier("fakeDao") CardDao cardDao, FileStore fileStore) {
        this.cardDao = cardDao;
        this.fileStore = fileStore;
    }

    public int addCard(String spelling){ return cardDao.insertCard(spelling); }

    public List<Card> getAllCards() { return cardDao.selectAllCard(); }

    public Optional<Card> getCardById(UUID id) { return cardDao.selectCardById(id); }

    public int deleteCard(UUID id) { return cardDao.deleteCardById(id); }

    public int updateCard(UUID id, Card newCard) {return cardDao.updateCardById(id, newCard); }

    public void uploadCardImage(UUID cardId, MultipartFile file) {
        // 1. Check if image is empty
        isFileEmpty(file);

        // 2. If file is an image
        isImage(file);

        // 3. Whether the user exists in our database
        Card card = getCarOrThrow(cardId);

        // 4. Grab some metadata from file if any
        Map<String, String> metadata = ExtractMetadata(file);

        // 5. Store the image in s3 and update database (userProfileImageLink) with s3 image link
        String path = String.format("%s/%s", BucketName.CARD_IMAGE.getBucketName(), card.getId());
        String fileName = String.format("%s-%s", file.getOriginalFilename(),UUID.randomUUID());

        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            card.setCardImageLink(fileName);
        }catch (IOException e){
            throw new IllegalStateException(e);
        }
    }

    public byte[] downloadCardImage(UUID cardId){
        Card card = getCarOrThrow(cardId);
        String path =  String.format("%s/%s",
                BucketName.CARD_IMAGE.getBucketName(),
                card.getId());

        return card.getCardImageLink()
                .map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);
    }

    private Map<String, String> ExtractMetadata(MultipartFile file) {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        return metadata;
    }

    private Card getCarOrThrow(UUID cardId) {

        return cardDao.selectAllCard()
                .stream()
                .filter(card -> card.getId().equals(cardId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("Card image %s not found", cardId)));
    }

    private void isImage(MultipartFile file) {
        if(!Arrays.asList(IMAGE_JPEG.getMimeType(), IMAGE_PNG.getMimeType(), IMAGE_GIF.getMimeType()).contains(file.getContentType())){
            throw new IllegalStateException(("File must be an image"));
        }
    }

    private void isFileEmpty(MultipartFile file) {
        if(file.isEmpty()){
            throw new IllegalStateException(("Cannot uplaod empty file [ " + file.getSize() + " ]"));
        }
    }
}
