package antifraud.Transactions;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionObject, Long> {


    TransactionObject save(TransactionObject object);
    @Transactional
    @Query(value = "select count( TRANSACTION_OBJECT.REGION) from TRANSACTION_OBJECT " +
            "where TRANSACTION_OBJECT.NUMBER =?1 and (TRANSACTION_OBJECT.date between ?2 and ?3) " +
            "and TRANSACTION_OBJECT.REGION<>?4", nativeQuery = true)
    long countByDistinctRegionAndDateBetween(String number, String date1, String date2, String ip);
    @Transactional
    @Query(value = "select count( TRANSACTION_OBJECT.IP) from TRANSACTION_OBJECT " +
            "where TRANSACTION_OBJECT.NUMBER =?1 and (TRANSACTION_OBJECT.date between ?2 and ?3) " +
            "and TRANSACTION_OBJECT.IP<>?4", nativeQuery = true)
    long countByDistinctIpAndDateBetween(String number, String date1, String date2, String ip);
}
