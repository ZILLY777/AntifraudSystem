package antifraud.User;

import antifraud.Jsons.JsonListOutputObject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.transaction.annotation.Isolation.READ_UNCOMMITTED;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    @Transactional(propagation = REQUIRED ,isolation = READ_UNCOMMITTED)
    User findByUsername(String username);

    @Transactional(propagation = REQUIRED ,isolation = READ_UNCOMMITTED)
    User getUserByUsername(String username);

    User save(User newUser);

    @Transactional(isolation = READ_UNCOMMITTED)
    @Query(value = "SELECT  users.id as id,  users.name, users.username as username, users.role FROM USERS ORDER BY id",
            nativeQuery = true)
    List<JsonListOutputObject> findall();

    @Transactional(isolation = READ_UNCOMMITTED)
    void deleteUserByUsername(String username);

    @Modifying
    @Transactional(propagation = REQUIRED )
    @Query(value = "UPDATE users SET users.role=?1 where users.username = ?2",
            nativeQuery = true)
    void changeUserRole(String role, String username);

    @Modifying
    @Transactional(isolation = READ_UNCOMMITTED)
    @Query(value = "UPDATE users SET users.enabled = ?1 where users.username = ?2",
            nativeQuery = true)
    void changeUserAccountStatus(int enable, String username);
}
