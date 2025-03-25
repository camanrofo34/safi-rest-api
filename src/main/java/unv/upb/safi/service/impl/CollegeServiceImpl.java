package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.CollegeRequest;
import unv.upb.safi.domain.dto.response.CollegeResponse;
import unv.upb.safi.domain.entity.College;
import unv.upb.safi.domain.entity.Faculty;
import unv.upb.safi.exception.entityNotFoundException.CollegeNotFoundException;
import unv.upb.safi.repository.CollegeRepository;
import unv.upb.safi.service.CollegeService;
import unv.upb.safi.util.SearchNormalizerUtil;

import java.util.stream.Collectors;

@Service
public class CollegeServiceImpl implements CollegeService {

    private final CollegeRepository collegeRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

    @Autowired
    public CollegeServiceImpl(CollegeRepository collegeRepository,
                              SearchNormalizerUtil searchNormalizerUtil) {
        this.collegeRepository = collegeRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }


    @Transactional
    @Override
    public CollegeResponse addCollege(CollegeRequest collegeRequest) {
        College college = new College();
        college.setName(collegeRequest.getName());
        college = collegeRepository.save(college);

        return mapToResponse(college);
    }

    @Transactional
    @Override
    public void deleteCollege(Long id) {

        College college = getCollegeByIdOrThrow(id);

        if (!college.getFaculties().isEmpty()) {
            throw new DataIntegrityViolationException("Cannot delete college with associated faculties.");
        }

        collegeRepository.deleteById(id);
    }

    @Override
    public CollegeResponse getCollege(Long id) {
        College college = getCollegeByIdOrThrow(id);

        return mapToResponse(college);
    }

    @Override
    public Page<CollegeResponse> getColleges(Pageable pageable) {
        return collegeRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public Page<CollegeResponse> getCollegesByName(String name, Pageable pageable) {
        return collegeRepository.findAllByNameContainingIgnoreCase(searchNormalizerUtil.normalize(name), pageable)
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

