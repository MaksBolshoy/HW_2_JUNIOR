package org.example.zad_2;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.UUID;

public class QueryBuilder {
    /**
     * Формирование SQL запроса на помещение объекта в БД.
     *
     * @param obj объект для помещения в БД.
     * @return SQL запрос в строковом представлении или null
     * при отсутствии возможности сформировать запрос.
     * @throws IllegalAccessException исключение при обращении к полям объекта.
     */
    public String buildInsertQuery(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        StringBuilder query = new StringBuilder("INSERT INTO ");

        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            query
                    .append(tableAnnotation.name())
                    .append(" (");

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    query
                            .append(columnAnnotation.name()).append(", ");
                }
            }

            if (query.charAt(query.length() - 2) == ',') {
                query.delete(query.length() - 2, query.length());
            }

            query.append(") VALUES (");

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);

                    query
                            .append("'")
                            .append(field.get(obj))
                            .append("', ");
                }
            }

            if (query.charAt(query.length() - 2) == ',') {
                query.delete(query.length() - 2, query.length());
            }

            query.append(")");

            return query.toString();

        } else {
            return null;
        }
    }

    /**
     * Формирование запроса на получение объекта.
     *
     * @param clazz      тип объекта.
     * @param primaryKey уникальный идентификатор объекта.
     * @return SQL запрос в строковом представлении
     * или null при отсутствии возможности сформировать запрос.
     */
    public String buildSelectQuery(Class<?> clazz, UUID primaryKey) {
        StringBuilder query = new StringBuilder("SELECT * FROM ");
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            query.append(tableAnnotation.name()).append(" WHERE ");

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    if (columnAnnotation.primaryKey()) {
                        query
                                .append(columnAnnotation.name())
                                .append(" = '")
                                .append(primaryKey)
                                .append("'");
                        break;
                    }
                }
            }
            return query.toString();
        } else {
            return null;
        }
    }

    /**
     * Формирование SQL запроса на обновление объекта.
     *
     * @param obj объект для обновления
     * @return SQL запрос в строковом представлении
     * или null при отсутствии возможности сформировать запрос.
     * @throws IllegalAccessException исключение при обращении к полям объекта.
     */
    public String buildUpdateQuery(Object obj) throws IllegalAccessException {
        Class<?> clazz = obj.getClass();
        StringBuilder query = new StringBuilder("UPDATE ");
        if (clazz.isAnnotationPresent(Table.class)) {
            Table tableAnnotation = clazz.getAnnotation(Table.class);
            query.append(tableAnnotation.name()).append(" SET ");

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    field.setAccessible(true);

                    Column columnAnnotation = field.getAnnotation(Column.class);
                    if (columnAnnotation.primaryKey()) {
                        continue;
                    }
                    query.append(columnAnnotation.name()).append(" = '")
                            .append(field.get(obj)).append("', ");
                }
            }

            if (query.charAt(query.length() - 2) == ',') {
                query.delete(query.length() - 2, query.length());
            }

            query.append(" WHERE ");

            for (Field field : fields) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column columnAnnotation = field.getAnnotation(Column.class);
                    if (columnAnnotation.primaryKey()) {
                        query
                                .append(columnAnnotation.name())
                                .append(" = '")
                                .append(field.get(obj))
                                .append("'");
                        break;
                    }
                }
            }

            return query.toString();
        } else {
            return null;
        }
    }

    /**
     * Формирование SQL запроса на удаление объекта.
     *
     * @param obj объект для удаления.
     * @return SQL запрос в строковом представлении
     * или null при отсутствии возможности сформировать запрос.
     */
    public String buildDeleteQuery(Object obj) {
        Class<?> clazz = obj.getClass();
        StringBuilder query = new StringBuilder("DELETE FROM ");
        if (clazz.isAnnotationPresent(Table.class)) {
            query.append(clazz.getAnnotation(Table.class).name());
            query.append(" WHERE ");
            Field[] fields = clazz.getDeclaredFields();
            Arrays.stream(fields).forEach(field -> {
                Column columnAnnotation = field.getAnnotation(Column.class);
                if (columnAnnotation.primaryKey()) {
                    try {
                        field.setAccessible(true);
                        query.append(field.getName())
                                .append(" = '")
                                .append(field.get(obj))
                                .append("'");
                        field.setAccessible(false);
                    } catch (IllegalAccessException e) {
                        System.out.printf(
                                "При получении данных из поля: %s, ошибка: %s\n",
                                field.getName(), e.getMessage());
                    }
                }
            });
            return query.toString();
        }
        return null;
    }


}
