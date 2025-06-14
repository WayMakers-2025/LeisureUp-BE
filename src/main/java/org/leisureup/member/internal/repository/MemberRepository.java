package org.leisureup.member.internal.repository;

import org.leisureup.member.internal.domain.*;
import org.springframework.data.jpa.repository.*;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
