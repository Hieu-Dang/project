package vn.haguyen.realmexample;

import io.realm.RealmObject;

public class User extends RealmObject {
    public String login;
    public String avatar_url;
    public String type;

    public User() {
    }


    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
