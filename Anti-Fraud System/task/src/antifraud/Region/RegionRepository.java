package antifraud.Region;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    Region save(Region region);
    Region findByRegion(String region);

    Region getRegionByRegion(String region);

    Boolean existsRegionByRegion(String region);

}
