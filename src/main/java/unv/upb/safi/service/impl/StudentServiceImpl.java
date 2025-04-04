package unv.upb.safi.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Service;
import unv.upb.safi.controller.StudentController;
import unv.upb.safi.domain.dto.request.StudentRequest;
import unv.upb.safi.domain.dto.response.StudentResponse;
import unv.upb.safi.domain.entity.Faculty;
import unv.upb.safi.domain.entity.Role;
import unv.upb.safi.domain.entity.Student;
import unv.upb.safi.domain.entity.User;
import unv.upb.safi.exception.entityNotFoundException.StudentNotFoundException;
import unv.upb.safi.repository.FacultyRepository;
import unv.upb.safi.repository.RoleRepository;
import unv.upb.safi.repository.StudentRepository;
import unv.upb.safi.repository.UserRepository;
import unv.upb.safi.service.StudentService;
import unv.upb.safi.util.MailUtil;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final FacultyRepository facultyRepository;

    private final RoleRepository roleRepository;

    private final MailUtil mailUtil;

    @Autowired
    public StudentServiceImpl(StudentRepository studentRepository,
                              UserRepository userRepository,
                              FacultyRepository facultyRepository,
                              RoleRepository roleRepository,
                              MailUtil mailUtil) {
        this.studentRepository = studentRepository;
        this.userRepository = userRepository;
        this.facultyRepository = facultyRepository;
        this.roleRepository = roleRepository;
        this.mailUtil = mailUtil;
    }

    @Transactional
    @Override
    public EntityModel<StudentResponse> registerStudent(StudentRequest studentRequest, String verificationCode) {

        if (!mailUtil.verifyCode(studentRequest.getEmail(), verificationCode)) {
            throw new IllegalArgumentException("Invalid verification code");
        }

        User user = mapToUser(studentRequest);
        userRepository.save(user);

        Set<Faculty> faculties = studentRequest.getFacultyId().stream()
                .map(facultyId -> facultyRepository.findById(facultyId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Student student = new Student();
        student.setUser(user);
        student.setFaculty(faculties);
        student = studentRepository.save(student);

        return mapToResponse(student);
    }

    @Override
    public EntityModel<StudentResponse> updateStudent(Long studentId, StudentRequest studentRequest) {
        Student student = getStudentByIdOrThrow(studentId);

        User user = mapToUser(studentRequest);
        userRepository.save(user);

        Set<Faculty> faculties = studentRequest.getFacultyId().stream()
                .map(facultyId -> facultyRepository.findById(facultyId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        student.setUser(user);
        student.setFaculty(faculties);
        student = studentRepository.save(student);

        return mapToResponse(student);
    }

    @Transactional
    @Override
    public void deleteStudent(Long studentId) {
        Student student = getStudentByIdOrThrow(studentId);

        studentRepository.delete(student);
    }

    @Override
    public EntityModel<StudentResponse> getStudent(Long studentId) {
        Student student = getStudentByIdOrThrow(studentId);

        return mapToResponse(student);
    }

    private User mapToUser(StudentRequest studentRequest) {
        User user = new User();
        user.setUsername(studentRequest.getUsername());
        user.setPassword(studentRequest.getPassword());
        user.setFirstName(studentRequest.getFirstName());
        user.setLastName(studentRequest.getLastName());
        user.setEmail(studentRequest.getEmail());
        user.setEnabled(true);
        Set<Role> roles = studentRequest.getRolesId().stream()
                .map(roleId -> roleRepository.findById(roleId).orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        user.setRoles(
                roles
        );
        return user;
    }

    private EntityModel<StudentResponse> mapToResponse(Student student) {
        StudentResponse studentResponse = new StudentResponse(
            student.getStudentId(),
            student.getUser().getFirstName(),
            student.getUser().getLastName(),
            student.getUser().getEmail(),
            student.getUser().getUsername(),
            student.getUser().getRoles().stream().map(Role::getName).toList(),
            student.getFaculty().stream().map(Faculty::getFacultyName).toList()
        );

        return EntityModel.of(studentResponse,
                linkTo(methodOn(StudentController.class).getStudent(student.getStudentId())).withSelfRel(),
                linkTo(methodOn(StudentController.class).deleteStudent(student.getStudentId())).withRel("delete-student")
                );
    }

    private Student getStudentByIdOrThrow(Long studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new StudentNotFoundException(studentId.toString()));
    }
}
