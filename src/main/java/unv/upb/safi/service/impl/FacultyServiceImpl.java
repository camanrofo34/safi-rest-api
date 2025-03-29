package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.FacultyController;
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

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final FacultyRepository facultyRepository;

    private final CollegeRepository collegeRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

    private PagedResourcesAssembler<FacultyResponse> pagedResourcesAssembler;

    @Autowired
    public FacultyServiceImpl(FacultyRepository facultyRepository,
                              CollegeRepository collegeRepository,
                              SearchNormalizerUtil searchNormalizerUtil) {
        this.facultyRepository = facultyRepository;
        this.collegeRepository = collegeRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<FacultyResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    @Override
    public EntityModel<FacultyResponse> addFaculty(FacultyRequest facultyRequest) {
        Faculty faculty = new Faculty();
        faculty.setFacultyName(facultyRequest.getFacultyName());
        faculty.setCollege(getCollegeByIdOrThrow(facultyRequest.getCollegeId()));

        faculty = facultyRepository.save(faculty);

        return mapToResponse(faculty);
    }

    @Override
    public EntityModel<FacultyResponse> updateFaculty(Long facultyId, FacultyRequest facultyRequest){

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
    public EntityModel<FacultyResponse> getFaculty(Long id) {
        Faculty faculty = getFacultyByIdOrThrow(id);

        return mapToResponse(faculty);
    }

    @Override
    public PagedModel<EntityModel<FacultyResponse>> getFacultiesByCollegeId(Long collegeId, Pageable pageable) {
        College college = getCollegeByIdOrThrow(collegeId);

        Page<FacultyResponse> facultyResponses = facultyRepository.findAllByCollege(college, pageable)
                .map(faculty ->
                        new FacultyResponse(
                                faculty.getFacultyId(),
                                faculty.getFacultyName(),
                                faculty.getCollege().getName()
                        ));

        return pagedResourcesAssembler.toModel(facultyResponses, this::mapToEntityModelToResourceModel);
    }

    @Override
    public PagedModel<EntityModel<FacultyResponse>> getFacultiesByName(String name, Pageable pageable) {
        Page<FacultyResponse> facultyResponses = facultyRepository.findAllByFacultyNameContainingIgnoreCase(
                searchNormalizerUtil.normalize(name), pageable)
                .map(faculty ->
                        new FacultyResponse(
                                faculty.getFacultyId(),
                                faculty.getFacultyName(),
                                faculty.getCollege().getName()
                        ));

        return pagedResourcesAssembler.toModel(facultyResponses, this::mapToEntityModelToResourceModel);
    }

    private EntityModel<FacultyResponse> mapToResponse(Faculty faculty) {
        FacultyResponse facultyResponse = new FacultyResponse(
                faculty.getFacultyId(),
                faculty.getFacultyName(),
                faculty.getCollege().getName()
        );

        return mapToEntityModel(facultyResponse);
    }

    private EntityModel<FacultyResponse> mapToEntityModel(FacultyResponse facultyResponse) {
        return EntityModel.of(facultyResponse,
                linkTo(methodOn(FacultyController.class).getFaculty(facultyResponse.getFacultyId())).withSelfRel(),
                linkTo(methodOn(FacultyController.class).deleteFaculty(facultyResponse.getFacultyId())).withRel("delete-faculty")
                );
    }

    private EntityModel<FacultyResponse> mapToEntityModelToResourceModel(FacultyResponse facultyResponse) {
        return EntityModel.of(facultyResponse,
                linkTo(methodOn(FacultyController.class).getFaculty(facultyResponse.getFacultyId())).withSelfRel()
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
