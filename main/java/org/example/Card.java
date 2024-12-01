package org.example;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Card {

    private SimpleStringProperty cardId;
    private SimpleStringProperty userId;
    private SimpleStringProperty fullName;
    private SimpleStringProperty typeCard;
    private SimpleStringProperty status;
    private ObjectProperty<LocalDate> cardRegistrationDate;
    private ObjectProperty<LocalDate> expiryDate;
    private BooleanProperty selected;
    public Card() {this.cardId = new SimpleStringProperty();
        this.userId = new SimpleStringProperty();
        this.fullName = new SimpleStringProperty();
        this.status = new SimpleStringProperty();
        this.typeCard = new SimpleStringProperty();
        this.cardRegistrationDate = new SimpleObjectProperty<>();;
        this.expiryDate = new SimpleObjectProperty<>();
        this.selected = new SimpleBooleanProperty();}

    public Card(String cardId, String userId, String fullName, String status, String typeCard,
                LocalDate cardRegistrationDate,LocalDate expiryDate, boolean selected) {
        this.cardId = new SimpleStringProperty(cardId);
        this.userId = new SimpleStringProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.status = new SimpleStringProperty(status);
        this.typeCard = new SimpleStringProperty(typeCard);
        this.cardRegistrationDate = new SimpleObjectProperty<>(cardRegistrationDate);;
        this.expiryDate = new SimpleObjectProperty<>(expiryDate);
        this.selected = new SimpleBooleanProperty(selected);
    }

    public Card(String userId, String fullName, String typeCard,
                LocalDate cardRegistrationDate,LocalDate expiryDate) {
        this.userId = new SimpleStringProperty(userId);
        this.fullName = new SimpleStringProperty(fullName);
        this.typeCard = new SimpleStringProperty(typeCard);
        this.cardRegistrationDate = new SimpleObjectProperty<>(cardRegistrationDate);;
        this.expiryDate = new SimpleObjectProperty<>(expiryDate);
    }

    public String getCardId() {
        return cardId.get();
    }

    public SimpleStringProperty cardIdProperty() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId.set(cardId);
    }

    public String getUserId() {
        return userId.get();
    }

    public SimpleStringProperty userIdProperty() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId.set(userId);
    }

    public LocalDate getCardRegistrationDate() {
        return cardRegistrationDate.get();
    }

    public ObjectProperty<LocalDate> cardRegistrationDateProperty() {
        return cardRegistrationDate;
    }

    public void setCardRegistrationDate(LocalDate cardRegistrationDate) {
        this.cardRegistrationDate.set(cardRegistrationDate);
    }

    public LocalDate getExpiryDate() {
        return expiryDate.get();
    }

    public ObjectProperty<LocalDate> expiryDateProperty() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate.set(expiryDate);
    }

    public String getFullName() {
        return fullName.get();
    }

    public SimpleStringProperty fullNameProperty() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName.set(fullName);
    }

    public boolean isSelected() {
        return selected.get();
    }

    public BooleanProperty selectedProperty() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected.set(selected);
    }

    public String getStatus() {
        return status.get();
    }

    public SimpleStringProperty statusProperty() {
        return status;
    }

    public void setStatus(String status) {
        this.status.set(status);
    }

    public String getTypeCard() {
        return typeCard.get();
    }

    public SimpleStringProperty typeCardProperty() {
        return typeCard;
    }

    public void setTypeCard(String typeCard) {
        this.typeCard.set(typeCard);
    }
}
