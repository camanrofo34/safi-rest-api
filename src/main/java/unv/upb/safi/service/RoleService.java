package unv.upb.safi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import unv.upb.safi.domain.dto.response.RoleResponse;

public interface RoleService {
    Page<RoleResponse> getAllRoles(int page, int size, String sortBy, Sort.Direction direction);
}
