package net.ironoc.portfolio.repository;

import net.ironoc.portfolio.domain.CharityOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CharityOptionRepository extends JpaRepository<CharityOption, Long> {

    Optional<CharityOption> findByName(String name);

    List<CharityOption> findByFounded(Integer founded);

    Optional<CharityOption> findByDonate(String donate);

    boolean existsByName(String name);
}
