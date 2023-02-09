package antifraud.Validators;

import antifraud.Region.RegionRepository;
import org.apache.commons.validator.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegionValidator {

    private static  RegionRepository regionRepository ;

    @Autowired
    public RegionValidator(RegionRepository regionRepository) {
        RegionValidator.regionRepository = regionRepository;
    }

    public static void isValid(String region) throws ValidatorException {
        try{
            if (regionRepository.existsRegionByRegion(region).equals(false)){
                throw new NullPointerException();
            };
        } catch (NullPointerException e) {
            throw new ValidatorException();
        }
    }
}
