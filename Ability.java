package com.knowledgeheroes.model;

/**
 * Клас модел, представящ умение на карта
 */
public class Ability {
    private int abilityId;
    private int cardId;
    private String name;
    private String description;
    private String effectType;
    private int effectValue;
    
    /**
     * Конструктор по подразбиране
     */
    public Ability() {
    }
    
    /**
     * Конструктор с основни параметри
     * 
     * @param name име на умението
     * @param description описание на умението
     * @param effectType тип на ефекта
     * @param effectValue стойност на ефекта
     */
    public Ability(String name, String description, String effectType, int effectValue) {
        this.name = name;
        this.description = description;
        this.effectType = effectType;
        this.effectValue = effectValue;
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param abilityId ID на умението
     * @param cardId ID на картата
     * @param name име на умението
     * @param description описание на умението
     * @param effectType тип на ефекта
     * @param effectValue стойност на ефекта
     */
    public Ability(int abilityId, int cardId, String name, String description, 
                  String effectType, int effectValue) {
        this.abilityId = abilityId;
        this.cardId = cardId;
        this.name = name;
        this.description = description;
        this.effectType = effectType;
        this.effectValue = effectValue;
    }
    
    /**
     * Връща ID на умението
     * 
     * @return ID на умението
     */
    public int getAbilityId() {
        return abilityId;
    }
    
    /**
     * Задава ID на умението
     * 
     * @param abilityId ID на умението
     */
    public void setAbilityId(int abilityId) {
        this.abilityId = abilityId;
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
     * Връща име на умението
     * 
     * @return име на умението
     */
    public String getName() {
        return name;
    }
    
    /**
     * Задава име на умението
     * 
     * @param name име на умението
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Връща описание на умението
     * 
     * @return описание на умението
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Задава описание на умението
     * 
     * @param description описание на умението
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Връща тип на ефекта
     * 
     * @return тип на ефекта
     */
    public String getEffectType() {
        return effectType;
    }
    
    /**
     * Задава тип на ефекта
     * 
     * @param effectType тип на ефекта
     */
    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }
    
    /**
     * Връща стойност на ефекта
     * 
     * @return стойност на ефекта
     */
    public int getEffectValue() {
        return effectValue;
    }
    
    /**
     * Задава стойност на ефекта
     * 
     * @param effectValue стойност на ефекта
     */
    public void setEffectValue(int effectValue) {
        this.effectValue = effectValue;
    }
    
    /**
     * Проверява дали умението е от тип атака
     * 
     * @return true ако умението е от тип атака, false ако не е
     */
    public boolean isAttack() {
        return "damage".equalsIgnoreCase(effectType) || 
               "attack".equalsIgnoreCase(effectType);
    }
    
    /**
     * Проверява дали умението е от тип лечение
     * 
     * @return true ако умението е от тип лечение, false ако не е
     */
    public boolean isHealing() {
        return "heal".equalsIgnoreCase(effectType) || 
               "healing".equalsIgnoreCase(effectType);
    }
    
    /**
     * Проверява дали умението е от тип бъф
     * 
     * @return true ако умението е от тип бъф, false ако не е
     */
    public boolean isBuff() {
        return "buff".equalsIgnoreCase(effectType);
    }
    
    /**
     * Проверява дали умението е от тип дебъф
     * 
     * @return true ако умението е от тип дебъф, false ако не е
     */
    public boolean isDebuff() {
        return "debuff".equalsIgnoreCase(effectType);
    }
    
    /**
     * Изчислява ефективната стойност на умението, като взема предвид бонуси от героя
     * 
     * @param character герой, който използва умението
     * @return ефективна стойност на умението
     */
    public int calculateEffectiveValue(Character character) {
        // Тук може да се добави логика за изчисляване на ефективната стойност,
        // като се вземат предвид характеристиките на героя
        
        int bonus = 0;
        
        if (isAttack()) {
            // Бонус от сила за атаки
            bonus = character.getStrengthBonus();
        } else if (isHealing()) {
            // Бонус от мъдрост за лечение
            bonus = character.getWisdomBonus();
        } else if (isBuff() || isDebuff()) {
            // Бонус от интелигентност за бъфове и дебъфове
            bonus = character.getIntelligenceBonus();
        }
        
        return effectValue + bonus;
    }
    
    /**
     * Връща форматирано описание на ефекта
     * 
     * @return форматирано описание на ефекта
     */
    public String getFormattedEffect() {
        String effectName;
        
        switch (effectType.toLowerCase()) {
            case "damage":
            case "attack":
                effectName = "Щета";
                break;
            case "heal":
            case "healing":
                effectName = "Лечение";
                break;
            case "buff":
                effectName = "Бъф";
                break;
            case "debuff":
                effectName = "Дебъф";
                break;
            default:
                effectName = effectType;
        }
        
        return effectName + ": " + effectValue;
    }
    
    @Override
    public String toString() {
        return "Ability{" +
               "abilityId=" + abilityId +
               ", name='" + name + '\'' +
               ", effectType='" + effectType + '\'' +
               ", effectValue=" + effectValue +
               '}';
    }
}