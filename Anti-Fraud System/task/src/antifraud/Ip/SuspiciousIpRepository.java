package antifraud.Ip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SuspiciousIpRepository extends JpaRepository<SuspiciousIp, Long> {

    SuspiciousIp save(SuspiciousIp ip);

    SuspiciousIp findSuspiciousIpByIp(String ip);

    @Transactional
    void deleteSuspiciousIpByIp(String ip);

    List<SuspiciousIp> findAll();

}
