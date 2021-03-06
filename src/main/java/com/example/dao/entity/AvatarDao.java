package com.example.dao.entity;

import com.example.dao.Dao;
import com.example.dao.DaoException;
import com.example.model.Avatar;
import com.example.model.Customer;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;


public class AvatarDao extends Dao implements EntityDao<Avatar> {

    private static final Logger LOG = LoggerFactory.getLogger(AvatarDao.class);

    private static final String INSERT_AVATAR = "INSERT INTO avatars VALUES (id,?,?)";
    private static final String FIND_BY_ID = "SELECT id , picture , avatars.date FROM avatars WHERE id=?";
    private static final String UPDATE_AVATAR = "UPDATE avatars SET picture=? , avatars.date = ? WHERE id=?";
    private static final String DELETE_AVATAR = "DELETE FROM avatars WHERE id=?";
    private static final String FIND_BY_CUSTOMER = "SELECT avatars.id , avatars.picture , avatars.date FROM avatars JOIN customers ON customers.avatar_id=avatars.id WHERE customers.id=?";
    private static final String FIND_BY_CUSTOMER_AND_DATE = "SELECT avatars.id , avatars.picture , avatars.date FROM avatars JOIN customers ON customers.avatar_id=avatars.id WHERE date > STR_TO_DATE(?, '%Y-%m-%d %H:%i:%s') AND customers.id=?";

    @Override
    public Avatar create(Avatar avatar) throws DaoException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT_AVATAR, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setBlob(1, avatar.getPicture());
            statement.setTimestamp(2, new Timestamp(avatar.getCreationDate().getMillis()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            resultSet.next();
            avatar.setId(resultSet.getInt(1));
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for insert avatar", e);
        }
        return avatar;
    }

    @Override
    public Avatar findById(int id) throws DaoException {
        Avatar avatar = null;
        try (PreparedStatement statement = getConnection().prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                avatar = pickAvatarFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for finding by id", e);
        }
        return avatar;
    }

    @Override
    public void update(Avatar avatar) throws DaoException {
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE_AVATAR)) {
            statement.setBlob(1, avatar.getPicture());
            statement.setTimestamp(2, new Timestamp(avatar.getCreationDate().getMillis()));
            statement.setInt(3, avatar.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for updating avatar", e);
        }
    }

    @Override
    public void delete(Avatar avatar) throws DaoException {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE_AVATAR)) {
            statement.setInt(1, avatar.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for deleting avatar", e);
        }
    }

    public Avatar findByPerson(Customer customer) throws DaoException {
        Avatar avatar = null;
        try (PreparedStatement statement = getConnection().prepareStatement(FIND_BY_CUSTOMER)) {
            LOG.debug("Find avatar by customer's id - {}", customer.getId());
            statement.setInt(1, customer.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LOG.debug("Pick avatar from result set");
                avatar = pickAvatarFromResultSet(resultSet);
            }
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for finding avatar by customer", e);
        }
        return avatar;
    }

    public Avatar findByPersonAndDate(Customer loggedCustomer, String formattedDate) throws DaoException {
        Avatar avatar = null;
        try (PreparedStatement statement = getConnection().prepareStatement(FIND_BY_CUSTOMER_AND_DATE)) {
            statement.setString(1, formattedDate);
            statement.setInt(2, loggedCustomer.getId());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                avatar = pickAvatarFromResultSet(resultSet);
            }
            LOG.debug("Pick avatar from result set", avatar);
            resultSet.close();
        } catch (SQLException e) {
            throw new DaoException("Cannot create statement for find by person and date", e);
        }
        return avatar;
    }

    private Avatar pickAvatarFromResultSet(ResultSet resultSet) throws DaoException {
        Avatar avatar = null;
        try {
            if (!resultSet.wasNull()) {
                avatar = new Avatar();
                avatar.setId(resultSet.getInt(1));
                avatar.setPicture(resultSet.getBinaryStream(2));
                avatar.setCreationDate(new DateTime(resultSet.getTimestamp(3)));
            }
        } catch (SQLException e) {
            throw new DaoException("Cannot pick avatar from result set", e);
        }
        return avatar;
    }
}
