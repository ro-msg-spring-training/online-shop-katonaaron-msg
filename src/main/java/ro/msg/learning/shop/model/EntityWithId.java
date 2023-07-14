package ro.msg.learning.shop.model;

import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.util.UUID;

@MappedSuperclass
@Data
public abstract class EntityWithId {
    @Id
    private UUID id;

    protected EntityWithId() {
        this.id = UUID.randomUUID();
    }
}
