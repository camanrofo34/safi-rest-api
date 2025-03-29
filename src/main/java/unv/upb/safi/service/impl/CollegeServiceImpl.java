package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.CollegeController;
import unv.upb.safi.domain.dto.request.CollegeRequest;
import unv.upb.safi.domain.dto.response.CollegeResponse;
import unv.upb.safi.domain.entity.College;
import unv.upb.safi.domain.entity.Faculty;
import unv.upb.safi.exception.entityNotFoundException.CollegeNotFoundException;
import unv.upb.safi.repository.CollegeRepository;
import unv.upb.safi.service.CollegeService;
import unv.upb.safi.util.SearchNormalizerUtil;

import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class CollegeServiceImpl implements CollegeService {

    private final CollegeRepository collegeRepository;

    private final SearchNormalizerUtil searchNormalizerUtil;

    private PagedResourcesAssembler<CollegeResponse> pagedResourcesAssembler;

    @Autowired
    public CollegeServiceImpl(CollegeRepository collegeRepository,
                              SearchNormalizerUtil searchNormalizerUtil) {
        this.collegeRepository = collegeRepository;
        this.searchNormalizerUtil = searchNormalizerUtil;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<CollegeResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }


    @Transactional
    @Override
    public EntityModel<CollegeResponse> addCollege(CollegeRequest collegeRequest) {
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
    public EntityModel<CollegeResponse> getCollege(Long id) {
        College college = getCollegeByIdOrThrow(id);

        return mapToResponse(college);
    }

    @Override
    public PagedModel<EntityModel<CollegeResponse>> getColleges(Pageable pageable) {
        Page<CollegeResponse> collegeResponses = collegeRepository.findAll(pageable)
                .map(college -> new CollegeResponse(
                        college.getCollegeId(),
                        college.getName(),
                        college.getFaculties().stream().map(Faculty::getFacultyId).collect(Collectors.toSet())
                ));

        return pagedResourcesAssembler.toModel(collegeResponses, this::mapToEntityModelToPagedResource);
    }

    @Override
    public PagedModel<EntityModel<CollegeResponse>> getCollegesByName(String name, Pageable pageable) {
        Page<CollegeResponse> collegeResponses = collegeRepository.findAllByNameContainingIgnoreCase(
                searchNormalizerUtil.normalize(name), pageable)
                .map(college -> new CollegeResponse(
                        college.getCollegeId(),
                        college.getName(),
                        college.getFaculties().stream().map(Faculty::getFacultyId).collect(Collectors.toSet())
                ));

        return pagedResourcesAssembler.toModel(collegeResponses, this::mapToEntityModelToPagedResource);
    }

    private EntityModel<CollegeResponse> mapToResponse(College college) {
        CollegeResponse collegeResponse = new CollegeResponse(
                college.getCollegeId(),
                college.getName(),
                college.getFaculties().stream().map(Faculty::getFacultyId).collect(Collectors.toSet())
        );

        return mapToEntityModel(collegeResponse);
    }

    private EntityModel<CollegeResponse> mapToEntityModel(CollegeResponse collegeResponse) {
        return EntityModel.of(
                collegeResponse,
                linkTo(methodOn(CollegeController.class).getCollege(collegeResponse.getCollegeId())).withSelfRel(),
                linkTo(methodOn(CollegeController.class).deleteCollege(collegeResponse.getCollegeId())).withRel("delete-college")
        );
    }

    private EntityModel<CollegeResponse> mapToEntityModelToPagedResource(CollegeResponse collegeResponse) {
        return EntityModel.of(
                collegeResponse,
                linkTo(methodOn(CollegeController.class).getCollege(collegeResponse.getCollegeId())).withSelfRel()
        );
    }

    private College getCollegeByIdOrThrow(Long id) {
        return collegeRepository.findById(id)
                .orElseThrow(() -> new CollegeNotFoundException(id.toString()));
    }
}

