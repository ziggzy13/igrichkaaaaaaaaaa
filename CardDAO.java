package com.knowledgeheroes.dao;

import com.knowledgeheroes.config.DatabaseConfig;
import com.knowledgeheroes.model.Card;
import com.knowledgeheroes.model.Ability;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object за работа с карти в базата данни
 */
public class CardDAO {
    private Connection connection;
    
    /**
     * Конструктор, който инициализира връзката с базата данни
     */
    public CardDAO() {
        this.connection = DatabaseConfig.getConnection();
    }
    
    /**
     * Създава нова карта в базата данни
     * 
     * @param card обект на картата, която трябва да бъде създадена
     * @return true при успех, false при грешка
     */
    public boolean createCard(Card card) {
        String query = "INSERT INTO cards (name, description, category_id, rarity, image_path) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, card.getName());
            statement.setString(2, card.getDescription());
            
            if (card.getCategoryId() != 0) {
                statement.setInt(3, card.getCategoryId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            
            statement.setString(4, card.getRarity());
            statement.setString(5, card.getImagePath());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int cardId = generatedKeys.getInt(1);
                        card.setCardId(cardId);
                        
                        // Съхранение на уменията на картата, ако има такива
                        if (card.getAbilities() != null && !card.getAbilities().isEmpty()) {
                            for (Ability ability : card.getAbilities()) {
                                ability.setCardId(cardId);
                                saveAbility(ability);
                            }
                        }
                        
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на карта: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима карта по ID
     * 
     * @param cardId ID на картата
     * @return Card обект или null ако не е намерен
     */
    public Card getCardById(int cardId) {
        String query = "SELECT * FROM cards WHERE card_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cardId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Card card = mapResultSetToCard(resultSet);
                    
                    // Зареждане на уменията на картата
                    card.setAbilities(getAbilitiesForCard(cardId));
                    
                    return card;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на карта по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Взима списък с всички карти
     * 
     * @return списък с Card обекти
     */
    public List<Card> getAllCards() {
        List<Card> cards = new ArrayList<>();
        String query = "SELECT * FROM cards";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                Card card = mapResultSetToCard(resultSet);
                
                // Зареждане на уменията на картата
                card.setAbilities(getAbilitiesForCard(card.getCardId()));
                
                cards.add(card);
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на всички карти: " + e.getMessage());
        }
        
        return cards;
    }
    
    /**
     * Взима списък с карти по категория
     * 
     * @param categoryId ID на категорията
     * @return списък с Card обекти
     */
    public List<Card> getCardsByCategory(int categoryId) {
        List<Card> cards = new ArrayList<>();
        String query = "SELECT * FROM cards WHERE category_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Card card = mapResultSetToCard(resultSet);
                    
                    // Зареждане на уменията на картата
                    card.setAbilities(getAbilitiesForCard(card.getCardId()));
                    
                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на карти по категория: " + e.getMessage());
        }
        
        return cards;
    }
    
    /**
     * Взима списък с карти по рядкост
     * 
     * @param rarity рядкост на картите
     * @return списък с Card обекти
     */
    public List<Card> getCardsByRarity(String rarity) {
        List<Card> cards = new ArrayList<>();
        String query = "SELECT * FROM cards WHERE rarity = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, rarity);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Card card = mapResultSetToCard(resultSet);
                    
                    // Зареждане на уменията на картата
                    card.setAbilities(getAbilitiesForCard(card.getCardId()));
                    
                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на карти по рядкост: " + e.getMessage());
        }
        
        return cards;
    }
    
    /**
     * Актуализира информацията за карта
     * 
     * @param card обект на картата с актуализирани данни
     * @return true при успех, false при грешка
     */
    public boolean updateCard(Card card) {
        String query = "UPDATE cards SET name = ?, description = ?, category_id = ?, rarity = ?, image_path = ? WHERE card_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, card.getName());
            statement.setString(2, card.getDescription());
            
            if (card.getCategoryId() != 0) {
                statement.setInt(3, card.getCategoryId());
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            
            statement.setString(4, card.getRarity());
            statement.setString(5, card.getImagePath());
            statement.setInt(6, card.getCardId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                // Актуализиране на уменията на картата
                if (card.getAbilities() != null && !card.getAbilities().isEmpty()) {
                    // Изтриване на съществуващите умения
                    deleteAbilitiesForCard(card.getCardId());
                    
                    // Добавяне на новите умения
                    for (Ability ability : card.getAbilities()) {
                        ability.setCardId(card.getCardId());
                        saveAbility(ability);
                    }
                }
                
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при актуализиране на карта: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтрива карта от базата данни
     * 
     * @param cardId ID на картата
     * @return true при успех, false при грешка
     */
    public boolean deleteCard(int cardId) {
        // Изтриване на уменията на картата
        deleteAbilitiesForCard(cardId);
        
        // Изтриване на картата
        String query = "DELETE FROM cards WHERE card_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cardId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на карта: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Добавя карта към колекцията на играч
     * 
     * @param playerId ID на играча
     * @param cardId ID на картата
     * @return true при успех, false при грешка
     */
    public boolean addCardToPlayerCollection(int playerId, int cardId) {
        // Проверка дали картата вече съществува в колекцията на играча
        if (playerHasCard(playerId, cardId)) {
            // Увеличаване на количеството
            return incrementCardQuantity(playerId, cardId);
        } else {
            // Добавяне на нова карта в колекцията
            String query = "INSERT INTO player_cards (player_id, card_id, quantity, acquisition_date) VALUES (?, ?, 1, ?)";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, playerId);
                statement.setInt(2, cardId);
                statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
                
                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.err.println("Грешка при добавяне на карта към колекцията на играч: " + e.getMessage());
                return false;
            }
        }
    }
    
    /**
     * Премахва карта от колекцията на играч
     * 
     * @param playerId ID на играча
     * @param cardId ID на картата
     * @return true при успех, false при грешка
     */
    public boolean removeCardFromPlayerCollection(int playerId, int cardId) {
        // Проверка на текущото количество
        int quantity = getCardQuantity(playerId, cardId);
        
        if (quantity > 1) {
            // Намаляване на количеството
            return decrementCardQuantity(playerId, cardId);
        } else if (quantity == 1) {
            // Изтриване на записа
            String query = "DELETE FROM player_cards WHERE player_id = ? AND card_id = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, playerId);
                statement.setInt(2, cardId);
                
                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.err.println("Грешка при премахване на карта от колекцията на играч: " + e.getMessage());
                return false;
            }
        }
        
        return false;
    }
    
    /**
     * Взима списък с карти от колекцията на играч
     * 
     * @param playerId ID на играча
     * @return списък с Card обекти
     */
    public List<Card> getPlayerCards(int playerId) {
        List<Card> playerCards = new ArrayList<>();
        String query = "SELECT c.*, pc.quantity, pc.acquisition_date FROM cards c " +
                      "JOIN player_cards pc ON c.card_id = pc.card_id " +
                      "WHERE pc.player_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Card card = mapResultSetToCard(resultSet);
                    
                    // Добавяне на информация от player_cards
                    card.setQuantity(resultSet.getInt("quantity"));
                    
                    Timestamp acquisitionTimestamp = resultSet.getTimestamp("acquisition_date");
                    if (acquisitionTimestamp != null) {
                        card.setAcquisitionDate(acquisitionTimestamp.toLocalDateTime());
                    }
                    
                    // Зареждане на уменията на картата
                    card.setAbilities(getAbilitiesForCard(card.getCardId()));
                    
                    playerCards.add(card);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на карти от колекцията на играч: " + e.getMessage());
        }
        
        return playerCards;
    }
    
    /**
     * Проверява дали играч има конкретна карта
     * 
     * @param playerId ID на играча
     * @param cardId ID на картата
     * @return true ако играчът има картата, false ако не
     */
    public boolean playerHasCard(int playerId, int cardId) {
        String query = "SELECT COUNT(*) FROM player_cards WHERE player_id = ? AND card_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            statement.setInt(2, cardId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при проверка дали играч има карта: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Получава количеството на карта в колекцията на играч
     * 
     * @param playerId ID на играча
     * @param cardId ID на картата
     * @return количество на картата
     */
    public int getCardQuantity(int playerId, int cardId) {
        String query = "SELECT quantity FROM player_cards WHERE player_id = ? AND card_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            statement.setInt(2, cardId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("quantity");
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на количество на карта: " + e.getMessage());
        }
        
        return 0;
    }
    
    /**
     * Увеличава количеството на карта в колекцията на играч
     * 
     * @param playerId ID на играча
     * @param cardId ID на картата
     * @return true при успех, false при грешка
     */
    private boolean incrementCardQuantity(int playerId, int cardId) {
        String query = "UPDATE player_cards SET quantity = quantity + 1 WHERE player_id = ? AND card_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            statement.setInt(2, cardId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при увеличаване на количество на карта: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Намалява количеството на карта в колекцията на играч
     * 
     * @param playerId ID на играча
     * @param cardId ID на картата
     * @return true при успех, false при грешка
     */
    private boolean decrementCardQuantity(int playerId, int cardId) {
        String query = "UPDATE player_cards SET quantity = quantity - 1 WHERE player_id = ? AND card_id = ? AND quantity > 0";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            statement.setInt(2, cardId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при намаляване на количество на карта: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Съхранява умение за карта
     * 
     * @param ability обект на умението
     * @return true при успех, false при грешка
     */
    private boolean saveAbility(Ability ability) {
        String query = "INSERT INTO abilities (card_id, name, description, effect_type, effect_value) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, ability.getCardId());
            statement.setString(2, ability.getName());
            statement.setString(3, ability.getDescription());
            statement.setString(4, ability.getEffectType());
            statement.setInt(5, ability.getEffectValue());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        ability.setAbilityId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при съхранение на умение: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима списък с умения за конкретна карта
     * 
     * @param cardId ID на картата
     * @return списък с Ability обекти
     */
    private List<Ability> getAbilitiesForCard(int cardId) {
        List<Ability> abilities = new ArrayList<>();
        String query = "SELECT * FROM abilities WHERE card_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cardId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Ability ability = new Ability();
                    ability.setAbilityId(resultSet.getInt("ability_id"));
                    ability.setCardId(resultSet.getInt("card_id"));
                    ability.setName(resultSet.getString("name"));
                    ability.setDescription(resultSet.getString("description"));
                    ability.setEffectType(resultSet.getString("effect_type"));
                    ability.setEffectValue(resultSet.getInt("effect_value"));
                    
                    abilities.add(ability);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на умения за карта: " + e.getMessage());
        }
        
        return abilities;
    }
    
    /**
     * Изтрива всички умения за конкретна карта
     * 
     * @param cardId ID на картата
     * @return true при успех, false при грешка
     */
    private boolean deleteAbilitiesForCard(int cardId) {
        String query = "DELETE FROM abilities WHERE card_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, cardId);
            
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на умения за карта: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Създава Card обект от ResultSet
     * 
     * @param resultSet резултат от заявка
     * @return Card обект
     */
    private Card mapResultSetToCard(ResultSet resultSet) throws SQLException {
        Card card = new Card();
        card.setCardId(resultSet.getInt("card_id"));
        card.setName(resultSet.getString("name"));
        card.setDescription(resultSet.getString("description"));
        card.setCategoryId(resultSet.getInt("category_id"));
        card.setRarity(resultSet.getString("rarity"));
        card.setImagePath(resultSet.getString("image_path"));
        
        return card;
    }
}