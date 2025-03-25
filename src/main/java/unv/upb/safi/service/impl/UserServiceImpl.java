package unv.upb.safi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import unv.upb.safi.domain.entity.Role;
import unv.upb.safi.domain.entity.User;
import unv.upb.safi.repository.RoleRepository;
import unv.upb.safi.repository.UserRepository;
import unv.upb.safi.service.UserService;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user with username: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRoles().stream()
                        .map(Role::getName)
                        .toArray(String[]::new))
                .build();
    }

    @Override
    public void activateDeactiveUser(Long userId) {
            User user = userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("No user with id: " + userId));
            user.setEnabled(!user.getEnabled());
            userRepository.save(user);
    }
}
