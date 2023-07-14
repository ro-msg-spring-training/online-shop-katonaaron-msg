package ro.msg.learning.shop.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Customer extends EntityWithId {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String emailAddress;
}
