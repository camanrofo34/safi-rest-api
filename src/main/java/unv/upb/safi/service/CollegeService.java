package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.CollegeRequest;
import unv.upb.safi.domain.dto.response.CollegeResponse;
import unv.upb.safi.domain.entity.College;
import unv.upb.safi.domain.entity.Faculty;
import unv.upb.safi.exception.entityNotFoundException.CollegeNotFoundException;
import unv.upb.safi.repository.CollegeRepository;

import java.util.stream.Collectors;

@Service
public class CollegeService {

    private final CollegeRepository collegeRepository;
    private final Logger logger = LoggerFactory.getLogger(CollegeService.class);

    @Autowired
    public CollegeService(CollegeRepository collegeRepository) {
        this.collegeRepository = collegeRepository;
    }

    @Transactional
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

        College college = getCollegeByIdOrThrow(id);

        if (!college.getFaculties().isEmpty()) {
            throw new DataIntegrityViolationException("Cannot delete college with associated faculties.");
        }

        collegeRepository.deleteById(id);
        logger.info("Transaction ID: {}, Deleted college {}", MDC.get("transactionId"), id);
    }

    public CollegeResponse getCollege(Long id) {
        logger.info("Transaction ID: {}, Getting college with id {}", MDC.get("transactionId"), id);

        College college = getCollegeByIdOrThrow(id);

        logger.info("Transaction ID: {}, College found", MDC.get("transactionId"));
        return mapToResponse(college);
    }

    public Page<CollegeResponse> getColleges(int page, int size, String sortBy, String direction) {
        logger.info("Transaction ID: {}, Getting all colleges", MDC.get("transactionId"));
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return collegeRepository.findAll(PageRequest.of(page, size, sort))
                .map(this::mapToResponse);
    }

    public Page<CollegeResponse> getCollegesByName(String name, int page, int size, String sortBy, String direction) {
        logger.info("Transaction ID: {}, Searching colleges by name '{}'", MDC.get("transactionId"), name);
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        return collegeRepository.findAllByNameContainingIgnoreCase(name, PageRequest.of(page, size, sort))
                .map(this::mapToResponse);
    }

    private CollegeResponse mapToResponse(College college) {
        return new CollegeResponse(
                college.getCollegeId(),
                college.getName(),
                college.getFaculties().stream().map(Faculty::getFacultyId).collect(Collectors.toSet())
        );
    }

    private College getCollegeByIdOrThrow(Long id) {
        return collegeRepository.findById(id)
                .orElseThrow(() -> new CollegeNotFoundException(id.toString()));
    }
}

