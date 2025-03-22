package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import unv.upb.safi.domain.dto.request.StudentRequest;
import unv.upb.safi.domain.dto.response.StudentResponse;

public interface StudentService {
    @Transactional
    StudentResponse registerStudent(StudentRequest studentRequest);

    StudentResponse updateStudent(Long studentId, StudentRequest studentRequest);

    @Transactional
    void deleteStudent(Long studentId);

    StudentResponse getStudent(Long studentId);
}
