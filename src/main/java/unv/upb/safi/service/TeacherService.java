package unv.upb.safi.service;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.request.TeacherRequest;
import unv.upb.safi.domain.dto.response.TeacherResponse;

public interface TeacherService {
    @Transactional
    EntityModel<TeacherResponse> registerTeacher(TeacherRequest teacherRequest);

    EntityModel<TeacherResponse> getTeacher(Long teacherId);

    EntityModel<TeacherResponse> updateTeacher(Long teacherId, TeacherRequest teacherRequest);

    @Transactional
    void deleteTeacher(Long teacherId);

    PagedModel<EntityModel<TeacherResponse>> getAllTeachers(Pageable pageable);

    PagedModel<EntityModel<TeacherResponse>> getTeachersByName(String name, Pageable pageable);
}
