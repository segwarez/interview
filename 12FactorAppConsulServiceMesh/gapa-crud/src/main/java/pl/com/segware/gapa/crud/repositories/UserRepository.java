package pl.com.segware.gapa.crud.repositories;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "users", path = "users")
@SecurityRequirement(name = "OAuth2")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    List<User> findByLastName(@Param("lastName") String lastName);
}