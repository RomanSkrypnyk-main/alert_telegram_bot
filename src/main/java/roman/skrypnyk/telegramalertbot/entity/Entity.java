package roman.skrypnyk.telegramalertbot.entity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@javax.persistence.Entity(name = "users")
public class Entity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Long userId;

    public Entity(Long userId) {
        this.userId = userId;
    }

    public Entity(Integer id, Long userId) {
        this.id = id;
        this.userId = userId;
    }

    public Entity() {

    }


}
