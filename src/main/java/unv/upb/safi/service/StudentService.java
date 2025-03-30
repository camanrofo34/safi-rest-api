package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.hateoas.EntityModel;
import unv.upb.safi.domain.dto.request.StudentRequest;
import unv.upb.safi.domain.dto.response.StudentResponse;

public interface StudentService {
    @Transactional
    EntityModel<StudentResponse> registerStudent(StudentRequest studentRequest, String verificationCode);

    EntityModel<StudentResponse> updateStudent(Long studentId, StudentRequest studentRequest);

    @Transactional
    void deleteStudent(Long studentId);

    EntityModel<StudentResponse> getStudent(Long studentId);
}
