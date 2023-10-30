package boot.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class User {

    private Integer id;

    private String username;

    private String password;
}
