package com.ved.SimpleEcommerceAPI.repository;

import com.ved.SimpleEcommerceAPI.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {
}
