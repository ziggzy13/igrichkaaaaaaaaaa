package com.knowledgeheroes.dao;

import com.knowledgeheroes.config.DatabaseConfig;
import com.knowledgeheroes.model.Leaderboard;
import com.knowledgeheroes.model.LeaderboardEntry;
import com.knowledgeheroes.model.Player;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object за работа с класации в базата данни
 */
public class LeaderboardDAO {
    private Connection connection;
    private PlayerDAO playerDAO;
    
    /**
     * Конструктор, който инициализира връзката с базата данни
     */
    public LeaderboardDAO() {
        this.connection = DatabaseConfig.getConnection();
        this.playerDAO = new PlayerDAO();
    }
    
    /**
     * Създава нова класация в базата данни
     * 
     * @param leaderboard обект на класацията, която трябва да бъде създадена
     * @return true при успех, false при грешка
     */
    public boolean createLeaderboard(Leaderboard leaderboard) {
        String query = "INSERT INTO leaderboards (level_id, category, name) VALUES (?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (leaderboard.getLevelId() != 0) {
                statement.setInt(1, leaderboard.getLevelId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            
            statement.setString(2, leaderboard.getCategory());
            statement.setString(3, leaderboard.getName());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        leaderboard.setLeaderboardId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на класация: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима класация по ID
     * 
     * @param leaderboardId ID на класацията
     * @return Leaderboard обект или null ако не е намерен
     */
    public Leaderboard getLeaderboardById(int leaderboardId) {
        String query = "SELECT * FROM leaderboards WHERE leaderboard_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, leaderboardId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Leaderboard leaderboard = mapResultSetToLeaderboard(resultSet);
                    
                    // Зареждане на записите в класацията
                    leaderboard.setEntries(getLeaderboardEntries(leaderboardId));
                    
                    return leaderboard;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на класация по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Взима списък с всички класации
     * 
     * @return списък с Leaderboard обекти
     */
    public List<Leaderboard> getAllLeaderboards() {
        List<Leaderboard> leaderboards = new ArrayList<>();
        String query = "SELECT * FROM leaderboards";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                Leaderboard leaderboard = mapResultSetToLeaderboard(resultSet);
                leaderboards.add(leaderboard);
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на всички класации: " + e.getMessage());
        }
        
        return leaderboards;
    }
    
    /**
     * Взима списък с глобални класации (без ниво)
     * 
     * @return списък с Leaderboard обекти
     */
    public List<Leaderboard> getGlobalLeaderboards() {
        List<Leaderboard> leaderboards = new ArrayList<>();
        String query = "SELECT * FROM leaderboards WHERE level_id IS NULL";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                Leaderboard leaderboard = mapResultSetToLeaderboard(resultSet);
                leaderboards.add(leaderboard);
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на глобални класации: " + e.getMessage());
        }
        
        return leaderboards;
    }
    
    /**
     * Взима списък с класации за конкретно ниво
     * 
     * @param levelId ID на нивото
     * @return списък с Leaderboard обекти
     */
    public List<Leaderboard> getLeaderboardsByLevelId(int levelId) {
        List<Leaderboard> leaderboards = new ArrayList<>();
        String query = "SELECT * FROM leaderboards WHERE level_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, levelId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Leaderboard leaderboard = mapResultSetToLeaderboard(resultSet);
                    leaderboards.add(leaderboard);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на класации за ниво: " + e.getMessage());
        }
        
        return leaderboards;
    }
    
    /**
     * Взима класация по категория
     * 
     * @param category категория на класацията
     * @param levelId ID на нивото (може да бъде 0 за глобална класация)
     * @return Leaderboard обект или null ако не е намерен
     */
    public Leaderboard getLeaderboardByCategory(String category, int levelId) {
        String query;
        PreparedStatement statement = null;
        
        try {
            if (levelId != 0) {
                // Класация за конкретно ниво
                query = "SELECT * FROM leaderboards WHERE category = ? AND level_id = ?";
                statement = connection.prepareStatement(query);
                statement.setString(1, category);
                statement.setInt(2, levelId);
            } else {
                // Глобална класация
                query = "SELECT * FROM leaderboards WHERE category = ? AND level_id IS NULL";
                statement = connection.prepareStatement(query);
                statement.setString(1, category);
            }
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Leaderboard leaderboard = mapResultSetToLeaderboard(resultSet);
                    
                    // Зареждане на записите в класацията
                    leaderboard.setEntries(getLeaderboardEntries(leaderboard.getLeaderboardId()));
                    
                    return leaderboard;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на класация по категория: " + e.getMessage());
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    System.err.println("Грешка при затваряне на statement: " + e.getMessage());
                }
            }
        }
        
        return null;
    }
    
    /**
     * Актуализира информацията за класация
     * 
     * @param leaderboard обект на класацията с актуализирани данни
     * @return true при успех, false при грешка
     */
    public boolean updateLeaderboard(Leaderboard leaderboard) {
        String query = "UPDATE leaderboards SET level_id = ?, category = ?, name = ? WHERE leaderboard_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (leaderboard.getLevelId() != 0) {
                statement.setInt(1, leaderboard.getLevelId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            
            statement.setString(2, leaderboard.getCategory());
            statement.setString(3, leaderboard.getName());
            statement.setInt(4, leaderboard.getLeaderboardId());
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при актуализиране на класация: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтрива класация от базата данни
     * 
     * @param leaderboardId ID на класацията
     * @return true при успех, false при грешка
     */
    public boolean deleteLeaderboard(int leaderboardId) {
        // Изтриване на записите в класацията
        deleteLeaderboardEntries(leaderboardId);
        
        // Изтриване на класацията
        String query = "DELETE FROM leaderboards WHERE leaderboard_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, leaderboardId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на класация: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Добавя запис в класация
     * 
     * @param leaderboardId ID на класацията
     * @param playerId ID на играча
     * @param score резултат
     * @return true при успех, false при грешка
     */
    public boolean addLeaderboardEntry(int leaderboardId, int playerId, int score) {
        String query = "INSERT INTO leaderboard_entries (leaderboard_id, player_id, score, date) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, leaderboardId);
            statement.setInt(2, playerId);
            statement.setInt(3, score);
            statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при добавяне на запис в класация: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Актуализира запис в класация
     * 
     * @param entryId ID на записа
     * @param score нов резултат
     * @return true при успех, false при грешка
     */
    public boolean updateLeaderboardEntry(int entryId, int score) {
        String query = "UPDATE leaderboard_entries SET score = ?, date = ? WHERE entry_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, score);
            statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            statement.setInt(3, entryId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при актуализиране на запис в класация: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтрива запис от класация
     * 
     * @param entryId ID на записа
     * @return true при успех, false при грешка
     */
    public boolean deleteLeaderboardEntry(int entryId) {
        String query = "DELETE FROM leaderboard_entries WHERE entry_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, entryId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на запис от класация: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтрива всички записи от класация
     * 
     * @param leaderboardId ID на класацията
     * @return true при успех, false при грешка
     */
    public boolean deleteLeaderboardEntries(int leaderboardId) {
        String query = "DELETE FROM leaderboard_entries WHERE leaderboard_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, leaderboardId);
            
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на записи от класация: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима списък с най-добрите записи от класация
     * 
     * @param leaderboardId ID на класацията
     * @param limit брой записи
     * @return списък с LeaderboardEntry обекти
     */
    public List<LeaderboardEntry> getTopEntries(int leaderboardId, int limit) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM leaderboard_entries WHERE leaderboard_id = ? ORDER BY score DESC LIMIT ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, leaderboardId);
            statement.setInt(2, limit);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LeaderboardEntry entry = mapResultSetToLeaderboardEntry(resultSet);
                    entries.add(entry);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на най-добрите записи от класация: " + e.getMessage());
        }
        
        return entries;
    }
    
    /**
     * Взима всички записи от класация
     * 
     * @param leaderboardId ID на класацията
     * @return списък с LeaderboardEntry обекти
     */
    public List<LeaderboardEntry> getLeaderboardEntries(int leaderboardId) {
        List<LeaderboardEntry> entries = new ArrayList<>();
        String query = "SELECT * FROM leaderboard_entries WHERE leaderboard_id = ? ORDER BY score DESC";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, leaderboardId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LeaderboardEntry entry = mapResultSetToLeaderboardEntry(resultSet);
                    entries.add(entry);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на записи от класация: " + e.getMessage());
        }
        
        return entries;
    }
    
    /**
     * Взима позицията на играч в класация
     * 
     * @param leaderboardId ID на класацията
     * @param playerId ID на играча
     * @return позиция (1-базирана) или -1 ако играчът не е в класацията
     */
    public int getPlayerRank(int leaderboardId, int playerId) {
        String query = "SELECT COUNT(*) + 1 AS rank FROM leaderboard_entries " +
                      "WHERE leaderboard_id = ? AND score > (SELECT score FROM leaderboard_entries " +
                      "WHERE leaderboard_id = ? AND player_id = ? ORDER BY score DESC LIMIT 1)";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, leaderboardId);
            statement.setInt(2, leaderboardId);
            statement.setInt(3, playerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("rank");
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на позиция на играч в класация: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Взима резултат на играч в класация
     * 
     * @param leaderboardId ID на класацията
     * @param playerId ID на играча
     * @return най-добрият резултат или -1 ако играчът не е в класацията
     */
    public int getPlayerScore(int leaderboardId, int playerId) {
        String query = "SELECT score FROM leaderboard_entries " +
                      "WHERE leaderboard_id = ? AND player_id = ? " +
                      "ORDER BY score DESC LIMIT 1";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, leaderboardId);
            statement.setInt(2, playerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("score");
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на резултат на играч в класация: " + e.getMessage());
        }
        
        return -1;
    }
    
    /**
     * Проверява дали играч има запис в класация
     * 
     * @param leaderboardId ID на класацията
     * @param playerId ID на играча
     * @return true ако има запис, false ако няма
     */
    public boolean playerHasEntry(int leaderboardId, int playerId) {
        String query = "SELECT COUNT(*) FROM leaderboard_entries WHERE leaderboard_id = ? AND player_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, leaderboardId);
            statement.setInt(2, playerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при проверка дали играч има запис в класация: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Добавя или актуализира запис в класация (запазва само най-добрия резултат)
     * 
     * @param leaderboardId ID на класацията
     * @param playerId ID на играча
     * @param score резултат
     * @return true при успех, false при грешка
     */
    public boolean updateOrAddLeaderboardEntry(int leaderboardId, int playerId, int score) {
        // Проверка дали играчът вече има запис
        if (playerHasEntry(leaderboardId, playerId)) {
            int currentScore = getPlayerScore(leaderboardId, playerId);
            
            if (score > currentScore) {
                // Актуализиране на най-добрия резултат
                String query = "UPDATE leaderboard_entries SET score = ?, date = ? " +
                              "WHERE leaderboard_id = ? AND player_id = ? AND score = ?";
                
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    statement.setInt(1, score);
                    statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                    statement.setInt(3, leaderboardId);
                    statement.setInt(4, playerId);
                    statement.setInt(5, currentScore);
                    
                    int affectedRows = statement.executeUpdate();
                    return affectedRows > 0;
                } catch (SQLException e) {
                    System.err.println("Грешка при актуализиране на запис в класация: " + e.getMessage());
                    return false;
                }
            } else {
                // Резултатът не е по-добър от текущия
                return true;
            }
        } else {
            // Добавяне на нов запис
            return addLeaderboardEntry(leaderboardId, playerId, score);
        }
    }
    
    /**
     * Създава Leaderboard обект от ResultSet
     * 
     * @param resultSet резултат от заявка
     * @return Leaderboard обект
     */
    private Leaderboard mapResultSetToLeaderboard(ResultSet resultSet) throws SQLException {
        Leaderboard leaderboard = new Leaderboard();
        leaderboard.setLeaderboardId(resultSet.getInt("leaderboard_id"));
        
        int levelId = resultSet.getInt("level_id");
        if (!resultSet.wasNull()) {
            leaderboard.setLevelId(levelId);
        }
        
        leaderboard.setCategory(resultSet.getString("category"));
        leaderboard.setName(resultSet.getString("name"));
        
        return leaderboard;
    }
    
    /**
     * Създава LeaderboardEntry обект от ResultSet
     * 
     * @param resultSet резултат от заявка
     * @return LeaderboardEntry обект
     */
    private LeaderboardEntry mapResultSetToLeaderboardEntry(ResultSet resultSet) throws SQLException {
        LeaderboardEntry entry = new LeaderboardEntry();
        entry.setEntryId(resultSet.getInt("entry_id"));
        entry.setLeaderboardId(resultSet.getInt("leaderboard_id"));
        entry.setPlayerId(resultSet.getInt("player_id"));
        entry.setScore(resultSet.getInt("score"));
        
        Timestamp dateTimestamp = resultSet.getTimestamp("date");
        if (dateTimestamp != null) {
            entry.setDate(dateTimestamp.toLocalDateTime());
        }
        
        // Зареждане на информация за играча
        Player player = playerDAO.getPlayerById(entry.getPlayerId());
        if (player != null) {
            entry.setPlayerName(player.getUsername());
        }
        
        return entry;
    }
}