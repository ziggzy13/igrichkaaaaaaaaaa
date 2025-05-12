package com.knowledgeheroes.dao;

import com.knowledgeheroes.config.DatabaseConfig;
import com.knowledgeheroes.model.Answer;
import com.knowledgeheroes.model.Question;
import com.knowledgeheroes.model.Quiz;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object за работа с куизове, въпроси и отговори в базата данни
 */
public class QuizDAO {
    private Connection connection;
    
    /**
     * Конструктор, който инициализира връзката с базата данни
     */
    public QuizDAO() {
        this.connection = DatabaseConfig.getConnection();
    }
    
    /**
     * Създава нов куиз в базата данни
     * 
     * @param quiz обект на куиза, който трябва да бъде създаден
     * @return true при успех, false при грешка
     */
    public boolean createQuiz(Quiz quiz) {
        String query = "INSERT INTO quizzes (level_id, name, description, time_limit) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (quiz.getLevelId() != 0) {
                statement.setInt(1, quiz.getLevelId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            
            statement.setString(2, quiz.getName());
            statement.setString(3, quiz.getDescription());
            
            if (quiz.getTimeLimit() != 0) {
                statement.setInt(4, quiz.getTimeLimit());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int quizId = generatedKeys.getInt(1);
                        quiz.setQuizId(quizId);
                        
                        // Свързване на въпроси с куиза, ако има такива
                        if (quiz.getQuestions() != null && !quiz.getQuestions().isEmpty()) {
                            int order = 1;
                            for (Question question : quiz.getQuestions()) {
                                linkQuestionToQuiz(quizId, question.getQuestionId(), order++);
                            }
                        }
                        
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на куиз: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима куиз по ID
     * 
     * @param quizId ID на куиза
     * @return Quiz обект или null ако не е намерен
     */
    public Quiz getQuizById(int quizId) {
        String query = "SELECT * FROM quizzes WHERE quiz_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quizId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Quiz quiz = mapResultSetToQuiz(resultSet);
                    
                    // Зареждане на въпросите за куиза
                    quiz.setQuestions(getQuestionsForQuiz(quizId));
                    
                    return quiz;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на куиз по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Взима списък с всички куизове
     * 
     * @return списък с Quiz обекти
     */
    public List<Quiz> getAllQuizzes() {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM quizzes";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                Quiz quiz = mapResultSetToQuiz(resultSet);
                quizzes.add(quiz);
            }
            
            // Зареждане на въпросите за всички куизове
            for (Quiz quiz : quizzes) {
                quiz.setQuestions(getQuestionsForQuiz(quiz.getQuizId()));
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на всички куизове: " + e.getMessage());
        }
        
        return quizzes;
    }
    
    /**
     * Взима списък с куизове за конкретно ниво
     * 
     * @param levelId ID на нивото
     * @return списък с Quiz обекти
     */
    public List<Quiz> getQuizzesByLevelId(int levelId) {
        List<Quiz> quizzes = new ArrayList<>();
        String query = "SELECT * FROM quizzes WHERE level_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, levelId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Quiz quiz = mapResultSetToQuiz(resultSet);
                    quizzes.add(quiz);
                }
                
                // Зареждане на въпросите за всички куизове
                for (Quiz quiz : quizzes) {
                    quiz.setQuestions(getQuestionsForQuiz(quiz.getQuizId()));
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на куизове за ниво: " + e.getMessage());
        }
        
        return quizzes;
    }
    
    /**
     * Актуализира информацията за куиз
     * 
     * @param quiz обект на куиза с актуализирани данни
     * @return true при успех, false при грешка
     */
    public boolean updateQuiz(Quiz quiz) {
        String query = "UPDATE quizzes SET level_id = ?, name = ?, description = ?, time_limit = ? WHERE quiz_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (quiz.getLevelId() != 0) {
                statement.setInt(1, quiz.getLevelId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            
            statement.setString(2, quiz.getName());
            statement.setString(3, quiz.getDescription());
            
            if (quiz.getTimeLimit() != 0) {
                statement.setInt(4, quiz.getTimeLimit());
            } else {
                statement.setNull(4, Types.INTEGER);
            }
            
            statement.setInt(5, quiz.getQuizId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                // Актуализиране на въпросите в куиза
                if (quiz.getQuestions() != null) {
                    // Изтриване на съществуващите връзки
                    removeQuestionsFromQuiz(quiz.getQuizId());
                    
                    // Добавяне на нови връзки
                    int order = 1;
                    for (Question question : quiz.getQuestions()) {
                        linkQuestionToQuiz(quiz.getQuizId(), question.getQuestionId(), order++);
                    }
                }
                
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при актуализиране на куиз: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтрива куиз от базата данни
     * 
     * @param quizId ID на куиза
     * @return true при успех, false при грешка
     */
    public boolean deleteQuiz(int quizId) {
        // Изтриване на връзките между куиза и въпросите
        removeQuestionsFromQuiz(quizId);
        
        // Изтриване на куиза
        String query = "DELETE FROM quizzes WHERE quiz_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quizId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на куиз: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Създава нов въпрос в базата данни
     * 
     * @param question обект на въпроса, който трябва да бъде създаден
     * @return true при успех, false при грешка
     */
    public boolean createQuestion(Question question) {
        String query = "INSERT INTO questions (category_id, text, difficulty) VALUES (?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            if (question.getCategoryId() != 0) {
                statement.setInt(1, question.getCategoryId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            
            statement.setString(2, question.getText());
            statement.setString(3, question.getDifficulty());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int questionId = generatedKeys.getInt(1);
                        question.setQuestionId(questionId);
                        
                        // Съхранение на отговорите на въпроса, ако има такива
                        if (question.getAnswers() != null && !question.getAnswers().isEmpty()) {
                            for (Answer answer : question.getAnswers()) {
                                answer.setQuestionId(questionId);
                                createAnswer(answer);
                            }
                        }
                        
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на въпрос: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима въпрос по ID
     * 
     * @param questionId ID на въпроса
     * @return Question обект или null ако не е намерен
     */
    public Question getQuestionById(int questionId) {
        String query = "SELECT * FROM questions WHERE question_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, questionId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Question question = mapResultSetToQuestion(resultSet);
                    
                    // Зареждане на отговорите на въпроса
                    question.setAnswers(getAnswersForQuestion(questionId));
                    
                    return question;
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на въпрос по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Взима списък с всички въпроси
     * 
     * @return списък с Question обекти
     */
    public List<Question> getAllQuestions() {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            while (resultSet.next()) {
                Question question = mapResultSetToQuestion(resultSet);
                questions.add(question);
            }
            
            // Зареждане на отговорите за всички въпроси
            for (Question question : questions) {
                question.setAnswers(getAnswersForQuestion(question.getQuestionId()));
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на всички въпроси: " + e.getMessage());
        }
        
        return questions;
    }
    
    /**
     * Взима списък с въпроси по категория
     * 
     * @param categoryId ID на категорията
     * @return списък с Question обекти
     */
    public List<Question> getQuestionsByCategory(int categoryId) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE category_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, categoryId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Question question = mapResultSetToQuestion(resultSet);
                    questions.add(question);
                }
                
                // Зареждане на отговорите за всички въпроси
                for (Question question : questions) {
                    question.setAnswers(getAnswersForQuestion(question.getQuestionId()));
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на въпроси по категория: " + e.getMessage());
        }
        
        return questions;
    }
    
    /**
     * Взима списък с въпроси по трудност
     * 
     * @param difficulty трудност на въпросите
     * @return списък с Question обекти
     */
    public List<Question> getQuestionsByDifficulty(String difficulty) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT * FROM questions WHERE difficulty = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, difficulty);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Question question = mapResultSetToQuestion(resultSet);
                    questions.add(question);
                }
                
                // Зареждане на отговорите за всички въпроси
                for (Question question : questions) {
                    question.setAnswers(getAnswersForQuestion(question.getQuestionId()));
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на въпроси по трудност: " + e.getMessage());
        }
        
        return questions;
    }
    
    /**
     * Актуализира информацията за въпрос
     * 
     * @param question обект на въпроса с актуализирани данни
     * @return true при успех, false при грешка
     */
    public boolean updateQuestion(Question question) {
        String query = "UPDATE questions SET category_id = ?, text = ?, difficulty = ? WHERE question_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            if (question.getCategoryId() != 0) {
                statement.setInt(1, question.getCategoryId());
            } else {
                statement.setNull(1, Types.INTEGER);
            }
            
            statement.setString(2, question.getText());
            statement.setString(3, question.getDifficulty());
            statement.setInt(4, question.getQuestionId());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                // Актуализиране на отговорите на въпроса
                if (question.getAnswers() != null) {
                    // Изтриване на съществуващите отговори
                    deleteAnswersForQuestion(question.getQuestionId());
                    
                    // Добавяне на нови отговори
                    for (Answer answer : question.getAnswers()) {
                        answer.setQuestionId(question.getQuestionId());
                        createAnswer(answer);
                    }
                }
                
                return true;
            }
            
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при актуализиране на въпрос: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтрива въпрос от базата данни
     * 
     * @param questionId ID на въпроса
     * @return true при успех, false при грешка
     */
    public boolean deleteQuestion(int questionId) {
        // Изтриване на отговорите на въпроса
        deleteAnswersForQuestion(questionId);
        
        // Изтриване на връзките между въпроса и куизовете
        removeQuestionFromAllQuizzes(questionId);
        
        // Изтриване на въпроса
        String query = "DELETE FROM questions WHERE question_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, questionId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на въпрос: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Създава нов отговор в базата данни
     * 
     * @param answer обект на отговора, който трябва да бъде създаден
     * @return true при успех, false при грешка
     */
    public boolean createAnswer(Answer answer) {
        String query = "INSERT INTO answers (question_id, text, is_correct) VALUES (?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, answer.getQuestionId());
            statement.setString(2, answer.getText());
            statement.setBoolean(3, answer.isCorrect());
            
            int affectedRows = statement.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        answer.setAnswerId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Грешка при създаване на отговор: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима отговор по ID
     * 
     * @param answerId ID на отговора
     * @return Answer обект или null ако не е намерен
     */
    public Answer getAnswerById(int answerId) {
        String query = "SELECT * FROM answers WHERE answer_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, answerId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToAnswer(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при търсене на отговор по ID: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Актуализира информацията за отговор
     * 
     * @param answer обект на отговора с актуализирани данни
     * @return true при успех, false при грешка
     */
    public boolean updateAnswer(Answer answer) {
        String query = "UPDATE answers SET question_id = ?, text = ?, is_correct = ? WHERE answer_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, answer.getQuestionId());
            statement.setString(2, answer.getText());
            statement.setBoolean(3, answer.isCorrect());
            statement.setInt(4, answer.getAnswerId());
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при актуализиране на отговор: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтрива отговор от базата данни
     * 
     * @param answerId ID на отговора
     * @return true при успех, false при грешка
     */
    public boolean deleteAnswer(int answerId) {
        String query = "DELETE FROM answers WHERE answer_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, answerId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на отговор: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Взима списък с въпроси за конкретен куиз
     * 
     * @param quizId ID на куиза
     * @return списък с Question обекти
     */
    public List<Question> getQuestionsForQuiz(int quizId) {
        List<Question> questions = new ArrayList<>();
        String query = "SELECT q.* FROM questions q " +
                      "JOIN quiz_questions qq ON q.question_id = qq.question_id " +
                      "WHERE qq.quiz_id = ? " +
                      "ORDER BY qq.question_order";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quizId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Question question = mapResultSetToQuestion(resultSet);
                    
                    // Зареждане на отговорите на въпроса
                    question.setAnswers(getAnswersForQuestion(question.getQuestionId()));
                    
                    questions.add(question);
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на въпроси за куиз: " + e.getMessage());
        }
        
        return questions;
    }
    
    /**
     * Взима списък с отговори за конкретен въпрос
     * 
     * @param questionId ID на въпроса
     * @return списък с Answer обекти
     */
    public List<Answer> getAnswersForQuestion(int questionId) {
        List<Answer> answers = new ArrayList<>();
        String query = "SELECT * FROM answers WHERE question_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, questionId);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    answers.add(mapResultSetToAnswer(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Грешка при извличане на отговори за въпрос: " + e.getMessage());
        }
        
        return answers;
    }
    
    /**
     * Свързва въпрос с куиз
     * 
     * @param quizId ID на куиза
     * @param questionId ID на въпроса
     * @param order ред на въпроса в куиза
     * @return true при успех, false при грешка
     */
    public boolean linkQuestionToQuiz(int quizId, int questionId, int order) {
        String query = "INSERT INTO quiz_questions (quiz_id, question_id, question_order) VALUES (?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quizId);
            statement.setInt(2, questionId);
            statement.setInt(3, order);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при свързване на въпрос с куиз: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Премахва връзка между въпрос и куиз
     * 
     * @param quizId ID на куиза
     * @param questionId ID на въпроса
     * @return true при успех, false при грешка
     */
    public boolean unlinkQuestionFromQuiz(int quizId, int questionId) {
        String query = "DELETE FROM quiz_questions WHERE quiz_id = ? AND question_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quizId);
            statement.setInt(2, questionId);
            
            int affectedRows = statement.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Грешка при премахване на връзка между въпрос и куиз: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Премахва всички въпроси от куиз
     * 
     * @param quizId ID на куиза
     * @return true при успех, false при грешка
     */
    public boolean removeQuestionsFromQuiz(int quizId) {
        String query = "DELETE FROM quiz_questions WHERE quiz_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, quizId);
            
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Грешка при премахване на всички въпроси от куиз: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Премахва въпрос от всички куизове
     * 
     * @param questionId ID на въпроса
     * @return true при успех, false при грешка
     */
    public boolean removeQuestionFromAllQuizzes(int questionId) {
        String query = "DELETE FROM quiz_questions WHERE question_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, questionId);
            
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Грешка при премахване на въпрос от всички куизове: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Изтрива всички отговори за конкретен въпрос
     * 
     * @param questionId ID на въпроса
     * @return true при успех, false при грешка
     */
    public boolean deleteAnswersForQuestion(int questionId) {
        String query = "DELETE FROM answers WHERE question_id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, questionId);
            
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Грешка при изтриване на отговори за въпрос: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Създава Quiz обект от ResultSet
     * 
     * @param resultSet резултат от заявка
     * @return Quiz обект
     */
    private Quiz mapResultSetToQuiz(ResultSet resultSet) throws SQLException {
        Quiz quiz = new Quiz();
        quiz.setQuizId(resultSet.getInt("quiz_id"));
        
        int levelId = resultSet.getInt("level_id");
        if (!resultSet.wasNull()) {
            quiz.setLevelId(levelId);
        }
        
        quiz.setName(resultSet.getString("name"));
        quiz.setDescription(resultSet.getString("description"));
        
        int timeLimit = resultSet.getInt("time_limit");
        if (!resultSet.wasNull()) {
            quiz.setTimeLimit(timeLimit);
        }
        
        return quiz;
    }
    
    /**
     * Създава Question обект от ResultSet
     * 
     * @param resultSet резултат от заявка
     * @return Question обект
     */
    private Question mapResultSetToQuestion(ResultSet resultSet) throws SQLException {
        Question question = new Question();
        question.setQuestionId(resultSet.getInt("question_id"));
        
        int categoryId = resultSet.getInt("category_id");
        if (!resultSet.wasNull()) {
            question.setCategoryId(categoryId);
        }
        
        question.setText(resultSet.getString("text"));
        question.setDifficulty(resultSet.getString("difficulty"));
        
        return question;
    }
    
    /**
     * Създава Answer обект от ResultSet
     * 
     * @param resultSet резултат от заявка
     * @return Answer обект
     */
    private Answer mapResultSetToAnswer(ResultSet resultSet) throws SQLException {
        Answer answer = new Answer();
        answer.setAnswerId(resultSet.getInt("answer_id"));
        answer.setQuestionId(resultSet.getInt("question_id"));
        answer.setText(resultSet.getString("text"));
        answer.setCorrect(resultSet.getBoolean("is_correct"));
        
        return answer;
    }
}