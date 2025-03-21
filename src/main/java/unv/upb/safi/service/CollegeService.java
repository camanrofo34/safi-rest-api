package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.CollegeRequest;
import unv.upb.safi.domain.dto.response.CollegeResponse;
import unv.upb.safi.domain.entity.College;
import unv.upb.safi.domain.entity.Faculty;
import unv.upb.safi.exception.entityNotFoundException.CollegeNotFoundException;
import unv.upb.safi.repository.CollegeRepository;

import java.util.List;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final Logger logger = LoggerFactory.getLogger(CollegeService.class);

    @Autowired
    public CollegeService(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    public CollegeResponse addCollege(CollegeRequest collegeRequest) {
        logger.info("Transaction ID: {}, Adding college {}", MDC.get("transactionId"), collegeRequest.getName());

        College college = new College();
        college.setName(collegeRequest.getName());
        college = collegeRepository.save(college);

        logger.info("Transaction ID: {}, College added successfully", MDC.get("transactionId"));
        return mapToResponse(college);
    }

    @Transactional
    public void deleteCollege(Long id) {
        logger.info("Transaction ID: {}, Deleting college with id {}", MDC.get("transactionId"), id);

        if (!collegeRepository.existsById(id)) {
            throw new CollegeNotFoundException(id.toString());
        }

        collegeRepository.deleteById(id);
        logger.info("Transaction ID: {}, Deleted college {}", MDC.get("transactionId"), id);
    }

    public CollegeResponse getCollege(Long id) {
        logger.info("Transaction ID: {}, Getting college with id {}", MDC.get("transactionId"), id);

        College college = collegeRepository.findById(id)
                .orElseThrow(() -> new CollegeNotFoundException(id.toString()));
        logger.info("Transaction ID: {}, College found", MDC.get("transactionId"));
        return mapToResponse(college);
    }

    public List<CollegeResponse> getColleges() {
        logger.info("Transaction ID: {}, Getting all colleges", MDC.get("transactionId"));
        return collegeRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<CollegeResponse> getCollegesByName(String name) {
        logger.info("Transaction ID: {}, Searching colleges by name '{}'", MDC.get("transactionId"), name);
        return collegeRepository.findByNameIgnoreCase(name).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CollegeResponse mapToResponse(College college) {
        return new CollegeResponse(
                college.getCollegeId(),
                college.getName(),
                college.getFaculties().stream().map(Faculty::getFacultyId).toList()
        );
    }
}

