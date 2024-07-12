package diplom;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserData {
    private String name;
    private String email;
    private String password;

    public UserData(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}
