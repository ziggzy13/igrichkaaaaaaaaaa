package com.knowledgeheroes.dao;

import com.knowledgeheroes.config.DatabaseConfig;
import com.knowledgeheroes.model.Level;
import com.knowledgeheroes.model.Puzzle;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object за работа с нива в базата данни
 */
public class LevelDAO {
    private Connection connection;
    
    /**
     * Конструктор, който инициализира връзката с базата данни
     */
    public LevelDAO() {
        this.connection = DatabaseConfig.getConnection();
    }
    
    /**
     * Създава ново ниво в базата данни
     * 
     * @param level обект на нивото, което трябва да бъде създадено
     * @return true при успех, false при грешка
     */
    public boolean createLevel(Level level) {
        String query = "INSERT INTO levels (name, description, difficulty, unlock_requirement, background_path) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, level.getName());
            statement.setString(2, level.getDescription());
            statement.setString(3, level.getDifficulty());
            statement.setString(4, level.getUnlockRequirement());
            statement.setString(5, level.getBackgroundPath());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int levelId = generatedKeys.getInt(1);
                        level.setLevelId(levelId);
                        
                        // Съхранение на пъзелите на нивото, ако има такива
                        if (level.getPuzzles() != null && !level.getPuzzles().isEmpty()) {
                            PuzzleDAO puzzleDAO = new PuzzleDAO();
                            for (Puzzle puzzle : level.getPuzzles()) {
                                puzzle.setLevelId(levelId);
                                puzzleDAO.createPuzzle(puzzle);
                            }
                        }
                        
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на ниво: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима ниво по ID
     * 
     * @param levelId ID на нивото
     * @return Level обект или null ако не е намерен
     */
    public Level getLevelById(int levelId) {
        String query = "SELECT * FROM levels WHERE level_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, levelId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Level level = mapResultSetToLevel(resultSet);
                    
                    // Зареждане на пъзелите на нивото
                    PuzzleDAO puzzleDAO = new PuzzleDAO();
                    level.setPuzzles(puzzleDAO.getPuzzlesByLevelId(levelId));
                    
                    return level;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на ниво по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Взима списък с всички нива
     * 
     * @return списък с Level обекти
     */
    public List<Level> getAllLevels() {
        List<Level> levels = new ArrayList<>();
        String query = "SELECT * FROM levels ORDER BY level_id";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                Level level = mapResultSetToLevel(resultSet);
                levels.add(level);
            }
            
            // Зареждане на пъзелите за всички нива
            if (!levels.isEmpty()) {
                PuzzleDAO puzzleDAO = new PuzzleDAO();
                for (Level level : levels) {
                    level.setPuzzles(puzzleDAO.getPuzzlesByLevelId(level.getLevelId()));
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на всички нива: " + e.getMessage());
        }
        
        return levels;
    }
    
    /**
     * Актуализира информацията за ниво
     * 
     * @param level обект на нивото с актуализирани данни
     * @return true при успех, false при грешка
     */
    public boolean updateLevel(Level level) {
        String query = "UPDATE levels SET name = ?, description = ?, difficulty = ?, unlock_requirement = ?, background_path = ? WHERE level_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, level.getName());
            statement.setString(2, level.getDescription());
            statement.setString(3, level.getDifficulty());
            statement.setString(4, level.getUnlockRequirement());
            statement.setString(5, level.getBackgroundPath());
            statement.setInt(6, level.getLevelId());
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при актуализиране на ниво: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтрива ниво от базата данни
     * 
     * @param levelId ID на нивото
     * @return true при успех, false при грешка
     */
    public boolean deleteLevel(int levelId) {
        // Изтриване на пъзелите за нивото
        PuzzleDAO puzzleDAO = new PuzzleDAO();
        puzzleDAO.deletePuzzlesByLevelId(levelId);
        
        // Изтриване на нивото
        String query = "DELETE FROM levels WHERE level_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, levelId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на ниво: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима списък с нива по трудност
     * 
     * @param difficulty трудност на нивата
     * @return списък с Level обекти
     */
    public List<Level> getLevelsByDifficulty(String difficulty) {
        List<Level> levels = new ArrayList<>();
        String query = "SELECT * FROM levels WHERE difficulty = ? ORDER BY level_id";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, difficulty);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Level level = mapResultSetToLevel(resultSet);
                    levels.add(level);
                }
                
                // Зареждане на пъзелите за всички нива
                if (!levels.isEmpty()) {
                    PuzzleDAO puzzleDAO = new PuzzleDAO();
                    for (Level level : levels) {
                        level.setPuzzles(puzzleDAO.getPuzzlesByLevelId(level.getLevelId()));
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на нива по трудност: " + e.getMessage());
        }
        
        return levels;
    }
    
    /**
     * Записва прогрес на играч за ниво
     * 
     * @param playerId ID на играча
     * @param levelId ID на нивото
     * @param completed дали нивото е завършено
     * @param stars брой звезди (0-3)
     * @param score резултат
     * @return true при успех, false при грешка
     */
    public boolean savePlayerProgress(int playerId, int levelId, boolean completed, int stars, int score) {
        // Проверка дали вече има запис за този играч и ниво
        if (hasPlayerProgress(playerId, levelId)) {
            // Актуализиране на съществуващия запис
            return updatePlayerProgress(playerId, levelId, completed, stars, score);
        } else {
            // Създаване на нов запис
            String query = "INSERT INTO player_progress (player_id, level_id, completed, completion_date, stars, score) VALUES (?, ?, ?, ?, ?, ?)";
            
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, playerId);
                statement.setInt(2, levelId);
                statement.setBoolean(3, completed);
                
                if (completed) {
                    statement.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
                } else {
                    statement.setNull(4, Types.TIMESTAMP);
                }
                
                statement.setInt(5, stars);
                statement.setInt(6, score);
                
                int affectedRows = statement.executeUpdate();
                return affectedRows > 0;
            } catch (SQLException e) {
                System.err.println("Грешка при записване на прогрес на играч: " + e.getMessage());
                return false;
            }
        }
    }
    
    /**
     * Актуализира прогрес на играч за ниво
     * 
     * @param playerId ID на играча
     * @param levelId ID на нивото
     * @param completed дали нивото е завършено
     * @param stars брой звезди (0-3)
     * @param score резултат
     * @return true при успех, false при грешка
     */
    private boolean updatePlayerProgress(int playerId, int levelId, boolean completed, int stars, int score) {
        String query;
        
        if (completed) {
            query = "UPDATE player_progress SET completed = ?, completion_date = ?, stars = ?, score = ? WHERE player_id = ? AND level_id = ?";
        } else {
            query = "UPDATE player_progress SET completed = ?, stars = ?, score = ? WHERE player_id = ? AND level_id = ?";
        }
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, completed);
            
            if (completed) {
                statement.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
                statement.setInt(3, stars);
                statement.setInt(4, score);
                statement.setInt(5, playerId);
                statement.setInt(6, levelId);
            } else {
                statement.setInt(2, stars);
                statement.setInt(3, score);
                statement.setInt(4, playerId);
                statement.setInt(5, levelId);
            }
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при актуализиране на прогрес на играч: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Проверява дали има запис за прогрес на играч за ниво
     * 
     * @param playerId ID на играча
     * @param levelId ID на нивото
     * @return true ако има запис, false ако няма
     */
    public boolean hasPlayerProgress(int playerId, int levelId) {
        String query = "SELECT COUNT(*) FROM player_progress WHERE player_id = ? AND level_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            statement.setInt(2, levelId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при проверка за прогрес на играч: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Взима прогрес на играч за ниво
     * 
     * @param playerId ID на играча
     * @param levelId ID на нивото
     * @return обект с информация за прогреса или null ако няма запис
     */
    public LevelProgress getPlayerProgress(int playerId, int levelId) {
        String query = "SELECT * FROM player_progress WHERE player_id = ? AND level_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            statement.setInt(2, levelId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    LevelProgress progress = new LevelProgress();
                    progress.setPlayerId(resultSet.getInt("player_id"));
                    progress.setLevelId(resultSet.getInt("level_id"));
                    progress.setCompleted(resultSet.getBoolean("completed"));
                    
                    Timestamp completionTimestamp = resultSet.getTimestamp("completion_date");
                    if (completionTimestamp != null) {
                        progress.setCompletionDate(completionTimestamp.toLocalDateTime());
                    }
                    
                    progress.setStars(resultSet.getInt("stars"));
                    progress.setScore(resultSet.getInt("score"));
                    
                    return progress;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на прогрес на играч: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Взима списък с прогрес на играч за всички нива
     * 
     * @param playerId ID на играча
     * @return списък с обекти с информация за прогреса
     */
    public List<LevelProgress> getAllPlayerProgress(int playerId) {
        List<LevelProgress> progressList = new ArrayList<>();
        String query = "SELECT * FROM player_progress WHERE player_id = ? ORDER BY level_id";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LevelProgress progress = new LevelProgress();
                    progress.setPlayerId(resultSet.getInt("player_id"));
                    progress.setLevelId(resultSet.getInt("level_id"));
                    progress.setCompleted(resultSet.getBoolean("completed"));
                    
                    Timestamp completionTimestamp = resultSet.getTimestamp("completion_date");
                    if (completionTimestamp != null) {
                        progress.setCompletionDate(completionTimestamp.toLocalDateTime());
                    }
                    
                    progress.setStars(resultSet.getInt("stars"));
                    progress.setScore(resultSet.getInt("score"));
                    
                    progressList.add(progress);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на прогрес на играч за всички нива: " + e.getMessage());
        }
        
        return progressList;
    }
    
    /**
     * Изтрива прогрес на играч за ниво
     * 
     * @param playerId ID на играча
     * @param levelId ID на нивото
     * @return true при успех, false при грешка
     */
    public boolean deletePlayerProgress(int playerId, int levelId) {
        String query = "DELETE FROM player_progress WHERE player_id = ? AND level_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, playerId);
            statement.setInt(2, levelId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на прогрес на играч: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Създава Level обект от ResultSet
     * 
     * @param resultSet резултат от заявка
     * @return Level обект
     */
    private Level mapResultSetToLevel(ResultSet resultSet) throws SQLException {
        Level level = new Level();
        level.setLevelId(resultSet.getInt("level_id"));
        level.setName(resultSet.getString("name"));
        level.setDescription(resultSet.getString("description"));
        level.setDifficulty(resultSet.getString("difficulty"));
        level.setUnlockRequirement(resultSet.getString("unlock_requirement"));
        level.setBackgroundPath(resultSet.getString("background_path"));
        
        return level;
    }
    
    /**
     * Вътрешен клас за съхранение на прогрес на играч за ниво
     */
    public static class LevelProgress {
        private int playerId;
        private int levelId;
        private boolean completed;
        private LocalDateTime completionDate;
        private int stars;
        private int score;

        public int getPlayerId() {
            return playerId;
        }

        public void setPlayerId(int playerId) {
            this.playerId = playerId;
        }

        public int getLevelId() {
            return levelId;
        }

        public void setLevelId(int levelId) {
            this.levelId = levelId;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void setCompleted(boolean completed) {
            this.completed = completed;
        }

        public LocalDateTime getCompletionDate() {
            return completionDate;
        }

        public void setCompletionDate(LocalDateTime completionDate) {
            this.completionDate = completionDate;
        }

        public int getStars() {
            return stars;
        }

        public void setStars(int stars) {
            this.stars = stars;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }
    }
}