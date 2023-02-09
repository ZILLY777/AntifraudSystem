package antifraud.Card;

import antifraud.Ip.SuspiciousIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Repository
public interface StolenCardRepository extends JpaRepository<StolenCard, Long> {

    StolenCard save(StolenCard card);

    StolenCard findStolenCardByNumber(String number);

    @Transactional
    void deleteStolenCardByNumber(String number);

    List<StolenCard> findAll();
}
