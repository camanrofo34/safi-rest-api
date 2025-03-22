package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.FacultyRequest;
import unv.upb.safi.domain.dto.response.FacultyResponse;
import unv.upb.safi.domain.entity.College;
import unv.upb.safi.domain.entity.Faculty;
import unv.upb.safi.exception.entityNotFoundException.CollegeNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.FacultyNotFoundException;
import unv.upb.safi.repository.CollegeRepository;
import unv.upb.safi.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    private final CollegeRepository collegeRepository;

    private final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    @Autowired
    public FacultyService(FacultyRepository facultyRepository, CollegeRepository collegeRepository) {
        this.facultyRepository = facultyRepository;
        this.collegeRepository = collegeRepository;
    }

    @Transactional
    public FacultyResponse addFaculty(FacultyRequest facultyRequest) {
        logger.info("Transaction ID: {}, Adding faculty {}",
                MDC.get("transactionId"), facultyRequest.getFacultyName());

            Faculty faculty = new Faculty();
            faculty.setFacultyName(facultyRequest.getFacultyName());
            faculty.setCollege(getCollegeByIdOrThrow(facultyRequest.getCollegeId()));

            faculty = facultyRepository.save(faculty);

            logger.info("Transaction ID: {}, Faculty added successfully", MDC.get("transactionId"));
            return mapToResponse(faculty);
    }

    public FacultyResponse updateFaculty (Long facultyId ,FacultyRequest facultyRequest){
        logger.info("Transaction ID: {}, Updating faculty with id {}",
                MDC.get("transactionId"), facultyId);

            Faculty faculty = getFacultyByIdOrThrow(facultyId);
            faculty.setFacultyName(facultyRequest.getFacultyName());
            faculty.setCollege(getCollegeByIdOrThrow(facultyRequest.getCollegeId()));
            faculty = facultyRepository.save(faculty);

            logger.info("Transaction ID: {}, Faculty updated successfully", MDC.get("transactionId"));
            return mapToResponse(faculty);
    }

    @Transactional
    public void deleteFaculty(Long id) {
        logger.info("Transaction ID: {}, Deleting faculty with id {}",
                MDC.get("transactionId"), id);
        Faculty faculty = getFacultyByIdOrThrow(id);

        facultyRepository.delete(faculty);
        logger.info("Transaction ID: {}, Faculty deleted successfully", MDC.get("transactionId"));
    }

    public FacultyResponse getFaculty(Long id) {
        logger.info("Transaction ID: {}, Getting faculty with id {}",
                MDC.get("transactionId"), id);
            Faculty faculty = getFacultyByIdOrThrow(id);

            return mapToResponse(faculty);
    }


    public Page<FacultyResponse> getFacultyByCollegeId(Long collegeId, int page, int size, String sortBy, String direction) {
        logger.info("Transaction ID: {}, Getting all faculties by college id {}",
                MDC.get("transactionId"), collegeId);
        College college = getCollegeByIdOrThrow(collegeId);

        return facultyRepository.findAllByCollege(college, PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy)))
                .map(this::mapToResponse);
    }

    private FacultyResponse mapToResponse(Faculty faculty) {
        return new FacultyResponse(
                faculty.getFacultyId(),
                faculty.getFacultyName(),
                faculty.getCollege().getName()
        );
    }

    private Faculty getFacultyByIdOrThrow(Long id) {
        return facultyRepository.findById(id)
                .orElseThrow(() -> new FacultyNotFoundException(id.toString()));
    }

    private College getCollegeByIdOrThrow(Long id) {
        return collegeRepository.findById(id)
                .orElseThrow(() -> new CollegeNotFoundException(id.toString()));
    }
}
