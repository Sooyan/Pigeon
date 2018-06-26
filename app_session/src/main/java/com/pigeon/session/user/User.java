package com.pigeon.session.user;

import com.stone.base.ArgsUtils;

/**
 * Created by Joseph.Yan.
 */
public abstract class User {

    private final Property userProperty;

    public User(Property property) {
        ArgsUtils.notNull(property, "Property must not be null");
        this.userProperty = property;
    }
}
