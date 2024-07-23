package mate.academy.store.repository.role;

import mate.academy.store.model.Role;
import mate.academy.store.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
}
