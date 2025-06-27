package org.leisureup.global.auth.token.repository;

import org.springframework.data.repository.*;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

}
