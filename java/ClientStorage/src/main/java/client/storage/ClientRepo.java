package client.storage;

import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class ClientRepo {
    Map<String, Credentials> DB = Map.of(
            "Jonathan",
            new Credentials(
                    "Jonathan",
                    "2oW8bFOrmj4JHjdv77BVVDErQ8Lo+9OVN6ivCiajTeA="
            )
    );

    public Credentials getCredentialsByName(String name) {
        var entity = DB.get(name);
        if (entity == null) {
            throw new RuntimeException("No such user");
        }
        return entity;
    }
}
