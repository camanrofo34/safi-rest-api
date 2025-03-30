package unv.upb.safi.service;

public interface UserService {

    void activateDeactiveUser(Long userId);

    void sendVerificationCode(String email);

    boolean changePassword(String email, String newPassword, String code);
}
