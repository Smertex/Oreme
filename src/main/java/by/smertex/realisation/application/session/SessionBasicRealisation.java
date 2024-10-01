package by.smertex.realisation.application.session;

import by.smertex.annotation.entity.fields.columns.Column;
import by.smertex.annotation.entity.fields.columns.Id;
import by.smertex.exceptions.application.SessionException;
import by.smertex.interfaces.application.builders.SessionQueryBuilder;
import by.smertex.interfaces.application.session.*;
import by.smertex.interfaces.application.session.ProxyEntityFactory;
import by.smertex.interfaces.mapper.Mapper;
import by.smertex.realisation.elements.IsolationLevel;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class SessionBasicRealisation implements Session {

    private final ProxyEntityFactory proxyEntityFactory;

    private final SessionQueryBuilder sessionQueryBuilder;

    private final Mapper<Map<String, Object>, ResultSet> mapper;

    private final Connection connection;

    private Transaction transaction;

    @Override
    public void beginTransaction() {
        this.transaction = new TransactionBasicRealisation(connection, this);
        this.transaction.begin();
    }

    @Override
    public Transaction getTransaction() {
        return transaction;
    }

    @Override
    public void setIsolationLevel(IsolationLevel level) {
        try {
            connection.setTransactionIsolation(level.getLevel());
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

    @Override
    public Optional<Object> find(Class<?> entity, Long id) {
        return Optional.of(proxyEntityFactory.buildProxyEntity(entity, id));
    }

    @Override
    public Optional<Object> find(Class<?> entity, CompositeKey compositeKey) {
        return Optional.of(proxyEntityFactory.buildProxyEntity(entity, compositeKey));
    }

    @Override
    public List<Object> findAll(Class<?> entity) {
        String sql = sessionQueryBuilder.selectSql(entity);
        List<Object> entities = new ArrayList<>();

        try(PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()){

            while(resultSet.next()) entities.add(proxyEntityFactory
                    .buildProxyEntity(entity, queryResultToCompositeKey(entity, resultSet)));

        } catch (SQLException e) {
            throw new SessionException(e);
        }

        return entities;
    }

    @Override
    public boolean update(Object entity) {
        return false;
    }

    @Override
    public Object save(Object entity) {
        return null;
    }

    @Override
    public boolean delete(Object entity) {
        return false;
    }

    @Override
    public void close() {
        if(transaction != null) transaction.commit();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new SessionException(e);
        }
    }

    private CompositeKey queryResultToCompositeKey(Class<?> entity, ResultSet resultSet){
        CompositeKey compositeKey = new CompositeKeyBasicRealisation(entity);
        Map<String, Object> column = mapper.mapFrom(resultSet);

        idCollector(entity).forEach(columnName -> compositeKey.setValue(columnName, column.get(columnName)));
        return compositeKey;
    }

    private List<String> idCollector(Class<?> entity){
       return Arrays.stream(entity.getDeclaredFields())
                .filter(column -> column.getDeclaredAnnotation(Id.class) != null)
                .map(column -> column.getDeclaredAnnotation(Column.class).name())
                .collect(Collectors.toList());
    }

    protected SessionBasicRealisation(Connection connection,
                                      IsolationLevel level,
                                      ProxyEntityFactory proxyEntityFactory,
                                      SessionQueryBuilder sessionQueryBuilder,
                                      Mapper<Map<String, Object>, ResultSet> mapper){
        this.connection = connection;
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new SessionException(e);
        }
        setIsolationLevel(level);
        this.proxyEntityFactory = proxyEntityFactory;
        this.sessionQueryBuilder = sessionQueryBuilder;
        this.mapper = mapper;
    }
}
