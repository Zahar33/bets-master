package com.example.dao.entity;

import com.example.dao.Dao;
import com.example.dao.DaoException;
import com.example.model.Bet;
import com.example.model.Condition;
import com.example.model.Match;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class ConditionDao extends Dao implements EntityDao<Condition> {

    private static final Logger LOG = LoggerFactory.getLogger(ConditionDao.class);

    private static final String INSERT_CONDITION = "INSERT INTO conditions VALUES (id,?,?,NULL)";
    private static final String DELETE_CONDITION = "DELETE FROM conditions WHERE id = ?";
    private static final String UPDATE_CONDITION = "UPDATE conditions set result = ? WHERE id=?";
    private static final String SET_CONDITION_TO_MATCH = "INSERT INTO matches_conditions VALUES (?,?)";
    private static final String FIND_BY_ID = "SELECT * FROM conditions WHERE id=?";
    private static final String GET_MATCHS_CONDITIONS = "SELECT conditions.id , conditions.conditionsName, conditions.coefficient , conditions.result FROM conditions JOIN matches_conditions ON conditions.id = matches_conditions.condition_id WHERE matches_conditions.match_id=?";
    private static final String GET_BETS_CONDITIONS = "SELECT conditions.id , conditions.conditionsName, conditions.coefficient, conditions.result FROM conditions JOIN bets_conditions ON conditions.id = bets_conditions.conditions_id WHERE bets_conditions.bets_id=?";

    @Override
    public Condition create(Condition condition) throws DaoException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_CONDITION, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, condition.getConditionsName());
            statement.setDouble(2, condition.getCoefficient());
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            int id = resultSet.getInt(1);
            LOG.debug("Set id = {} to condition - {}", id, condition);
            condition.setId(id);
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for insert condition", e);
        }
        return condition;
    }

    @Override
    public Condition findById(int id) throws DaoException {
        Condition condition = null;
        try (PreparedStatement statement = getConnection().prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                condition = pickConditionFromResultSet(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException("Cannot create dao for finding by id", e);
        }
        return condition;
    }

    @Override
    public void update(Condition condition) throws DaoException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_CONDITION)) {
            if (condition.getResult() != null) {
                statement.setBoolean(1, condition.getResult());
            } else {
                statement.setNull(1, Types.BOOLEAN);
            }
            statement.setInt(2, condition.getId());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for updating condition", e);
        }
    }

    @Override
    public void delete(Condition condition) throws DaoException {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_CONDITION)) {
            statement.setInt(1, condition.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for deleting condition", e);
        }
    }

    public void addConditionToMatch(Condition condition, Match match) throws DaoException {
        try (PreparedStatement statement = getConnection().prepareStatement(SET_CONDITION_TO_MATCH)) {
            statement.setInt(1, match.getId());
            statement.setInt(2, condition.getId());
            statement.execute();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for set condition to match", e);
        }

    }

    public List<Condition> getMatchsConditions(Match match) throws DaoException {
        List<Condition> conditions = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(GET_MATCHS_CONDITIONS)) {
            statement.setInt(1, match.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                conditions.add(pickConditionFromResultSet(resultSet));
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for get conditions", e);
        }
        return conditions;
    }


    public List<Condition> getBetsConditions(Bet bet) throws DaoException {
        List<Condition> conditions = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement(GET_BETS_CONDITIONS)) {
            statement.setInt(1, bet.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Condition condition = pickConditionFromResultSet(resultSet);
                LOG.debug("Get condition - {} for bet - {}", condition, bet);
                conditions.add(condition);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for get conditions", e);
        }
        return conditions;
    }

    private Condition pickConditionFromResultSet(ResultSet resultSet) throws DaoException {
        Condition condition = new Condition();
        try {
            condition.setId(resultSet.getInt(1));
            condition.setConditionsName(resultSet.getString(2));
            condition.setCoefficient(resultSet.getDouble(3));
            condition.setResult(resultSet.getBoolean(4));
            if (resultSet.wasNull()) {
                condition.setResult(null);
            }
        } catch (SQLException e) {
            throw new DaoException("Cannot pick condition from result set", e);
        }
        return condition;
    }
}

