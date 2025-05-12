package com.knowledgeheroes.dao;

import com.knowledgeheroes.model.Player;
import com.knowledgeheroes.config.DatabaseConfig;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object за работа с играчи в базата данни
 */
public class PlayerDAO {
    private Connection connection;
    
    /**
     * Конструктор, който инициализира връзката с базата данни
     */
    public PlayerDAO() {
        this.connection = DatabaseConfig.getConnection();
    }
    
    /**
     * Създава нов играч в базата данни
     * 
     * @param player обект на играча, който трябва да бъде създаден
     * @return true при успех, false при грешка
     */
    public boolean createPlayer(Player player) {
        String query = "INSERT INTO players (username, password, email, registration_date) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, player.getUsername());
            statement.setString(2, player.getPassword());
            statement.setString(3, player.getEmail());
            statement.setTimestamp(4, Timestamp.valueOf(player.getRegistrationDate()));
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        player.setPlayerId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на играч: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима играч по ID
     * 
     * @param playerId ID на играча
     * @return Player обект или null ако не е намерен
     */
    public Player getPlayerById(int playerId) {
        String query = "SELECT * FROM players WHERE player_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPlayer(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на играч по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Взима играч по потребителско име
     * 
     * @param username потребителско име
     * @return Player обект или null ако не е намерен
     */
    public Player getPlayerByUsername(String username) {
        String query = "SELECT * FROM players WHERE username = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPlayer(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на играч по потребителско име: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Взима играч по email
     * 
     * @param email email адрес
     * @return Player обект или null ако не е намерен
     */
    public Player getPlayerByEmail(String email) {
        String query = "SELECT * FROM players WHERE email = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToPlayer(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на играч по email: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Актуализира информацията за играч
     * 
     * @param player обект на играча с актуализирани данни
     * @return true при успех, false при грешка
     */
    public boolean updatePlayer(Player player) {
        String query = "UPDATE players SET username = ?, password = ?, email = ? WHERE player_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, player.getUsername());
            statement.setString(2, player.getPassword());
            statement.setString(3, player.getEmail());
            statement.setInt(4, player.getPlayerId());
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при актуализиране на играч: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтрива играч от базата данни
     * 
     * @param playerId ID на играча
     * @return true при успех, false при грешка
     */
    public boolean deletePlayer(int playerId) {
        String query = "DELETE FROM players WHERE player_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на играч: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Връща списък с всички играчи
     * 
     * @return списък с Player обекти
     */
    public List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        String query = "SELECT * FROM players";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                players.add(mapResultSetToPlayer(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на всички играчи: " + e.getMessage());
        }
        
        return players;
    }
    
    /**
     * Актуализира датата на последно влизане на играч
     * 
     * @param playerId ID на играча
     * @return true при успех, false при грешка
     */
    public boolean updateLastLogin(int playerId) {
        String query = "UPDATE players SET last_login = ? WHERE player_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(2, playerId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при актуализиране на последно влизане: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Проверява дали потребителското име съществува в базата данни
     * 
     * @param username потребителско име
     * @return true ако съществува, false ако не съществува
     */
    public boolean usernameExists(String username) {
        String query = "SELECT COUNT(*) FROM players WHERE username = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при проверка за съществуване на потребителско име: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Проверява дали email адресът съществува в базата данни
     * 
     * @param email email адрес
     * @return true ако съществува, false ако не съществува
     */
    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM players WHERE email = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при проверка за съществуване на email: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Проверява дали потребителското име и паролата съвпадат
     * 
     * @param username потребителско име
     * @param password парола (хеширана)
     * @return true ако съвпадат, false ако не съвпадат
     */
    public boolean authenticate(String username, String password) {
        String query = "SELECT * FROM players WHERE username = ? AND password = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            System.err.println("Грешка при автентикация: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Създава Player обект от ResultSet
     * 
     * @param resultSet резултат от заявка
     * @return Player обект
     */
    private Player mapResultSetToPlayer(ResultSet resultSet) throws SQLException {
        Player player = new Player();
        player.setPlayerId(resultSet.getInt("player_id"));
        player.setUsername(resultSet.getString("username"));
        player.setPassword(resultSet.getString("password"));
        player.setEmail(resultSet.getString("email"));
        
        Timestamp registrationTimestamp = resultSet.getTimestamp("registration_date");
        if (registrationTimestamp != null) {
            player.setRegistrationDate(registrationTimestamp.toLocalDateTime());
        }
        
        Timestamp lastLoginTimestamp = resultSet.getTimestamp("last_login");
        if (lastLoginTimestamp != null) {
            player.setLastLogin(lastLoginTimestamp.toLocalDateTime());
        }
        
        return player;
    }
}