package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.FacultyRequest;
import unv.upb.safi.domain.dto.response.FacultyResponse;
import unv.upb.safi.domain.entity.College;
import unv.upb.safi.domain.entity.Faculty;
import unv.upb.safi.exception.entityNotFoundException.CollegeNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.FacultyNotFoundException;
import unv.upb.safi.repository.CollegeRepository;
import unv.upb.safi.repository.FacultyRepository;
import unv.upb.safi.service.FacultyService;
import unv.upb.safi.util.SearchNormalizerUtil;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    private final CollegeRepository collegeRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository,
                              CollegeRepository collegeRepository,
                              SearchNormalizerUtil searchNormalizerUtil) {
        this.facultyRepository = facultyRepository;
        this.collegeRepository = collegeRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Transactional
    @Override
    public FacultyResponse addFaculty(FacultyRequest facultyRequest) {
        Faculty faculty = new Faculty();
        faculty.setFacultyName(facultyRequest.getFacultyName());
        faculty.setCollege(getCollegeByIdOrThrow(facultyRequest.getCollegeId()));

        faculty = facultyRepository.save(faculty);

        return mapToResponse(faculty);
    }

    @Override
    public FacultyResponse updateFaculty(Long facultyId, FacultyRequest facultyRequest){

        Faculty faculty = getFacultyByIdOrThrow(facultyId);
        faculty.setFacultyName(facultyRequest.getFacultyName());
        faculty.setCollege(getCollegeByIdOrThrow(facultyRequest.getCollegeId()));
        faculty = facultyRepository.save(faculty);

        return mapToResponse(faculty);
    }

    @Transactional
    @Override
    public void deleteFaculty(Long id) {
        Faculty faculty = getFacultyByIdOrThrow(id);

        facultyRepository.delete(faculty);
    }

    @Override
    public FacultyResponse getFaculty(Long id) {
        Faculty faculty = getFacultyByIdOrThrow(id);

        return mapToResponse(faculty);
    }

    @Override
    public Page<FacultyResponse> getFacultiesByCollegeId(Long collegeId, Pageable pageable) {
        College college = getCollegeByIdOrThrow(collegeId);

        return facultyRepository.findAllByCollege(college, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<FacultyResponse> getFacultiesByName(String name, Pageable pageable) {
        return facultyRepository.findByFacultyNameContainingIgnoreCase(searchNormalizerUtil.normalize(name), pageable)
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
