package com.knowledgeheroes.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Клас модел, представящ карта в играта
 */
public class Card {
    private int cardId;
    private String name;
    private String description;
    private int categoryId;
    private String rarity;
    private String imagePath;
    
    // Допълнителни полета, използвани при карти в колекцията на играч
    private int quantity;
    private LocalDateTime acquisitionDate;
    
    // Умения на картата
    private List<Ability> abilities;
    
    /**
     * Конструктор по подразбиране
     */
    public Card() {
        this.abilities = new ArrayList<>();
    }
    
    /**
     * Конструктор с основни параметри
     * 
     * @param name име на картата
     * @param description описание на картата
     * @param rarity рядкост на картата
     */
    public Card(String name, String description, String rarity) {
        this();
        this.name = name;
        this.description = description;
        this.rarity = rarity;
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param cardId ID на картата
     * @param name име на картата
     * @param description описание на картата
     * @param categoryId ID на категорията
     * @param rarity рядкост на картата
     * @param imagePath път към изображението на картата
     */
    public Card(int cardId, String name, String description, int categoryId, 
               String rarity, String imagePath) {
        this();
        this.cardId = cardId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.rarity = rarity;
        this.imagePath = imagePath;
    }
    
    /**
     * Връща ID на картата
     * 
     * @return ID на картата
     */
    public int getCardId() {
        return cardId;
    }
    
    /**
     * Задава ID на картата
     * 
     * @param cardId ID на картата
     */
    public void setCardId(int cardId) {
        this.cardId = cardId;
    }
    
    /**
     * Връща име на картата
     * 
     * @return име на картата
     */
    public String getName() {
        return name;
    }
    
    /**
     * Задава име на картата
     * 
     * @param name име на картата
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Връща описание на картата
     * 
     * @return описание на картата
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Задава описание на картата
     * 
     * @param description описание на картата
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Връща ID на категорията
     * 
     * @return ID на категорията
     */
    public int getCategoryId() {
        return categoryId;
    }
    
    /**
     * Задава ID на категорията
     * 
     * @param categoryId ID на категорията
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    /**
     * Връща рядкост на картата
     * 
     * @return рядкост на картата
     */
    public String getRarity() {
        return rarity;
    }
    
    /**
     * Задава рядкост на картата
     * 
     * @param rarity рядкост на картата
     */
    public void setRarity(String rarity) {
        this.rarity = rarity;
    }
    
    /**
     * Връща път към изображението на картата
     * 
     * @return път към изображението на картата
     */
    public String getImagePath() {
        return imagePath;
    }
    
    /**
     * Задава път към изображението на картата
     * 
     * @param imagePath път към изображението на картата
     */
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    /**
     * Връща списък с умения на картата
     * 
     * @return списък с умения
     */
    public List<Ability> getAbilities() {
        return abilities;
    }
    
    /**
     * Задава списък с умения на картата
     * 
     * @param abilities списък с умения
     */
    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }
    
    /**
     * Добавя умение към картата
     * 
     * @param ability умение за добавяне
     */
    public void addAbility(Ability ability) {
        if (this.abilities == null) {
            this.abilities = new ArrayList<>();
        }
        
        ability.setCardId(this.cardId);
        this.abilities.add(ability);
    }
    
    /**
     * Премахва умение от картата
     * 
     * @param abilityId ID на умението за премахване
     * @return true ако умението е премахнато успешно, false ако не е намерено
     */
    public boolean removeAbility(int abilityId) {
        if (this.abilities == null) {
            return false;
        }
        
        return this.abilities.removeIf(ability -> ability.getAbilityId() == abilityId);
    }
    
    /**
     * Връща количество на картата (за колекции на играчи)
     * 
     * @return количество на картата
     */
    public int getQuantity() {
        return quantity;
    }
    
    /**
     * Задава количество на картата
     * 
     * @param quantity количество на картата
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    /**
     * Увеличава количеството на картата с 1
     */
    public void incrementQuantity() {
        this.quantity++;
    }
    
    /**
     * Намалява количеството на картата с 1
     * 
     * @return true ако количеството е положително след намаляването, false ако е 0 или по-малко
     */
    public boolean decrementQuantity() {
        this.quantity--;
        return this.quantity > 0;
    }
    
    /**
     * Връща дата на придобиване на картата
     * 
     * @return дата на придобиване
     */
    public LocalDateTime getAcquisitionDate() {
        return acquisitionDate;
    }
    
    /**
     * Задава дата на придобиване на картата
     * 
     * @param acquisitionDate дата на придобиване
     */
    public void setAcquisitionDate(LocalDateTime acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }
    
    /**
     * Проверява дали картата е рядка (rare, epic или legendary)
     * 
     * @return true ако картата е рядка, false ако не е
     */
    public boolean isRare() {
        return "rare".equalsIgnoreCase(rarity) || 
               "epic".equalsIgnoreCase(rarity) || 
               "legendary".equalsIgnoreCase(rarity);
    }
    
    /**
     * Връща числова стойност на рядкостта за сортиране
     * 
     * @return числова стойност на рядкостта (1-5)
     */
    public int getRarityValue() {
        switch (rarity.toLowerCase()) {
            case "common":
                return 1;
            case "uncommon":
                return 2;
            case "rare":
                return 3;
            case "epic":
                return 4;
            case "legendary":
                return 5;
            default:
                return 0;
        }
    }
    
    /**
     * Връща цвят, съответстващ на рядкостта на картата
     * 
     * @return RGB цвят като String
     */
    public String getRarityColor() {
        switch (rarity.toLowerCase()) {
            case "common":
                return "#FFFFFF"; // бяло
            case "uncommon":
                return "#00FF00"; // зелено
            case "rare":
                return "#0000FF"; // синьо
            case "epic":
                return "#800080"; // лилаво
            case "legendary":
                return "#FFA500"; // оранжево
            default:
                return "#C0C0C0"; // сребърно (по подразбиране)
        }
    }
    
    @Override
    public String toString() {
        return "Card{" +
               "cardId=" + cardId +
               ", name='" + name + '\'' +
               ", rarity='" + rarity + '\'' +
               ", abilities=" + abilities.size() +
               '}';
    }
}