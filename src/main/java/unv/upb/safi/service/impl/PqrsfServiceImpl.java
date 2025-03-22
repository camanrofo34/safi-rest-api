package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.dto.request.PqrsfRequest;
import unv.upb.safi.domain.dto.response.PqrsfResponse;
import unv.upb.safi.domain.entity.Pqrsf;
import unv.upb.safi.domain.entity.Student;
import unv.upb.safi.exception.entityNotFoundException.PqrsfNotFoundException;
import unv.upb.safi.exception.entityNotFoundException.StudentNotFoundException;
import unv.upb.safi.repository.PqrsfRepository;
import unv.upb.safi.repository.StudentRepository;
import unv.upb.safi.service.PqrsfService;


@Service
public class PqrsfServiceImpl implements PqrsfService {

    private final PqrsfRepository pqrsfRepository;

    private final StudentRepository studentRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public PqrsfServiceImpl(PqrsfRepository pqrsfRepository, StudentRepository studentRepository) {
        this.pqrsfRepository = pqrsfRepository;
        this.studentRepository = studentRepository;
    }

    @Transactional
    @Override
    public PqrsfResponse createPqrsf(Long studentId, PqrsfRequest pqrsfRequest) {
        logger.info("Transaction ID: {}, Student {} is creating a PQRSF",
                MDC.get("transactionId"), studentId);

        Student student = getStudentByIdOrThrow(studentId);

        Pqrsf pqrsf = new Pqrsf();
        pqrsf.setStudent(student);
        pqrsf.setRequestType(pqrsfRequest.getRequestType());
        pqrsf.setDescription(pqrsfRequest.getDescription());
        pqrsf.setSubmissionDate(pqrsfRequest.getSubmissionDate());
        pqrsf = pqrsfRepository.save(pqrsf);

        logger.info("Transaction ID: {}, PQRSF created for student {}",
                MDC.get("transactionId"), studentId);
        return mapToResponse(pqrsf);
    }

    @Transactional
    @Override
    public void deletePqrsf(Long requestId) {
        logger.info("Transaction ID: {}, Deleting PQRSF {}",
                MDC.get("transactionId"), requestId);

        Pqrsf pqrsf = getPqrsfByIdOrThrow(requestId);


        pqrsfRepository.delete(pqrsf);
        logger.info("Transaction ID: {}, PQRSF {} deleted",
                    MDC.get("transactionId"), requestId);
    }

    @Override
    public PqrsfResponse getPqrsf(Long requestId) {
        logger.info("Transaction ID: {}, Getting PQRSF {}",
                MDC.get("transactionId"), requestId);
        Pqrsf pqrsf = getPqrsfByIdOrThrow(requestId);

        logger.info("Transaction ID: {}, PQRSF {} retrieved",
                MDC.get("transactionId"), requestId);
        return mapToResponse(pqrsf);
    }

    @Override
    public Page<PqrsfResponse> getAllPqrsf(int page, int size, String sortBy, Sort.Direction direction) {
        logger.info("Transaction ID: {}, Getting all PQRSFs",
                MDC.get("transactionId"));

        return pqrsfRepository.findAll(
                PageRequest.of(page, size, Sort.by(direction, sortBy))
        ).map(this::mapToResponse);
    }

    private PqrsfResponse mapToResponse(Pqrsf pqrsf) {
        return new PqrsfResponse(
                pqrsf.getPqrsfId(),
                pqrsf.getStudent().getStudentId(),
                pqrsf.getRequestType(),
                pqrsf.getDescription(),
                pqrsf.getSubmissionDate()
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
