package praktikum;

import org.apache.commons.lang3.RandomStringUtils;
import praktikum.User.Userrequests.User;

public class UserGenerator {
    public static User randomUser() {
        return new User(RandomStringUtils.randomAlphanumeric(10) + "@yandex.ru", RandomStringUtils.randomAlphanumeric(10), RandomStringUtils.randomAlphanumeric(10));
    }
}
