package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.request.TeacherRequest;
import unv.upb.safi.domain.dto.response.TeacherResponse;

public interface TeacherService {
    @Transactional
    TeacherResponse registerTeacher(TeacherRequest teacherRequest);

    TeacherResponse getTeacher(Long teacherId);

    TeacherResponse updateTeacher(Long teacherId, TeacherRequest teacherRequest);

    @Transactional
    void deleteTeacher(Long teacherId);

    Page<TeacherResponse> getAllTeachers(Pageable pageable);

    Page<TeacherResponse> getTeachersByName(String name, Pageable pageable);
}
