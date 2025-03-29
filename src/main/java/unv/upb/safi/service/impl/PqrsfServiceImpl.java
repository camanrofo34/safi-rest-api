package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.PqrsfController;
import unv.upb.safi.domain.dto.request.PqrsfRequest;
import unv.upb.safi.domain.dto.response.PqrsfResponse;
import unv.upb.safi.domain.entity.Pqrsf;
import unv.upb.safi.domain.entity.Student;
import unv.upb.safi.exception.entityNotFoundException.PqrsfNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.StudentNotFoundException;
import unv.upb.safi.repository.PqrsfRepository;
import unv.upb.safi.repository.StudentRepository;
import unv.upb.safi.service.PqrsfService;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@Service
public class PqrsfServiceImpl implements PqrsfService {

    private final PqrsfRepository pqrsfRepository;

    private final StudentRepository studentRepository;

    private PagedResourcesAssembler<PqrsfResponse> pagedResourcesAssembler;

    @Autowired
    public PqrsfServiceImpl(PqrsfRepository pqrsfRepository, StudentRepository studentRepository) {
        this.pqrsfRepository = pqrsfRepository;
        this.studentRepository = studentRepository;
    }

    @Autowired
    public void setPagedResourcesAssembler(PagedResourcesAssembler<PqrsfResponse> pagedResourcesAssembler) {
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @Transactional
    @Override
    public EntityModel<PqrsfResponse> createPqrsf(Long studentId, PqrsfRequest pqrsfRequest) {
        Student student = getStudentByIdOrThrow(studentId);

        Pqrsf pqrsf = new Pqrsf();
        pqrsf.setStudent(student);
        pqrsf.setRequestType(pqrsfRequest.getRequestType());
        pqrsf.setDescription(pqrsfRequest.getDescription());
        pqrsf.setSubmissionDate(pqrsfRequest.getSubmissionDate());
        pqrsf = pqrsfRepository.save(pqrsf);

        return mapToResponse(pqrsf);
    }

    @Transactional
    @Override
    public void deletePqrsf(Long requestId) {
        Pqrsf pqrsf = getPqrsfByIdOrThrow(requestId);


        pqrsfRepository.delete(pqrsf);
    }

    @Override
    public EntityModel<PqrsfResponse> getPqrsf(Long requestId) {

        Pqrsf pqrsf = getPqrsfByIdOrThrow(requestId);

        return mapToResponse(pqrsf);
    }

    @Override
    public PagedModel<EntityModel<PqrsfResponse>> getAllPqrsf(Pageable pageable) {
        Page<PqrsfResponse> pqrsfResponses = pqrsfRepository.findAll(pageable).
                map(pqrsf -> new PqrsfResponse(
                        pqrsf.getPqrsfId(),
                        pqrsf.getStudent().getStudentId(),
                        pqrsf.getRequestType(),
                        pqrsf.getDescription(),
                        pqrsf.getSubmissionDate()
                ));

        return pagedResourcesAssembler.toModel(pqrsfResponses, this::mapToEntityModelToResourceModel);
    }

    private EntityModel<PqrsfResponse> mapToResponse(Pqrsf pqrsf) {
        PqrsfResponse pqrsfResponse = new PqrsfResponse(
                pqrsf.getPqrsfId(),
                pqrsf.getStudent().getStudentId(),
                pqrsf.getRequestType(),
                pqrsf.getDescription(),
                pqrsf.getSubmissionDate()
        );

        return mapToEntityModel(pqrsfResponse);
    }

    private EntityModel<PqrsfResponse> mapToEntityModel(PqrsfResponse pqrsfResponse) {
        return EntityModel.of(pqrsfResponse,
                linkTo(methodOn(PqrsfController.class).getPqrsf(pqrsfResponse.getPqrsfId())).withSelfRel(),
                linkTo(methodOn(PqrsfController.class).deletePqrsf(pqrsfResponse.getPqrsfId())).withRel("delete-pqrsf")
        );
    }

    private EntityModel<PqrsfResponse> mapToEntityModelToResourceModel(PqrsfResponse pqrsfResponse) {
        return EntityModel.of(pqrsfResponse,
                linkTo(methodOn(PqrsfController.class).getPqrsf(pqrsfResponse.getPqrsfId())).withSelfRel()
        );
    }

    private Pqrsf getPqrsfByIdOrThrow(Long requestId) {
        return pqrsfRepository.findById(requestId)
                .orElseThrow(() -> new PqrsfNotFoundException(requestId.toString()));
    }

    private Student getStudentByIdOrThrow(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId.toString()));
    }
}
