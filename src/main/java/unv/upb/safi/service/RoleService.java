package unv.upb.safi.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import unv.upb.safi.domain.dto.response.RoleResponse;

public interface RoleService {
    PagedModel<EntityModel<RoleResponse>> getAllRoles(Pageable pageable);
}
