package com.codekoi.domain.favorite.repository;

import com.codekoi.domain.favorite.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteCoreRepository extends JpaRepository<Favorite, Long> {


}
