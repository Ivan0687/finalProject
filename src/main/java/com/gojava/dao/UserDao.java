package com.gojava.dao;

import com.gojava.model.User;

public interface UserDao<T extends User> extends DAO<T> {
    //Зарегистрировать пользователя
    //Редактировать данные пользователя
    //Удалить пользователя
}
