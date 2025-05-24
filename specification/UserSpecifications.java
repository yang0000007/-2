package com.seniorlearn.onlinelearning.specification;

import com.seniorlearn.onlinelearning.model.Role;
import com.seniorlearn.onlinelearning.model.User;
import org.springframework.data.jpa.domain.Specification;

public class UserSpecifications {
    public static Specification<User> withUsernameLike(String username) {
        return (root, query, cb) ->
                username == null ? null : cb.like(root.get("username"), "%" + username + "%");
    }

    public static Specification<User> withEmailLike(String email) {
        return (root, query, cb) ->
                email == null ? null : cb.like(root.get("email"), "%" + email + "%");
    }

    public static Specification<User> withRole(String role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("role"), Role.valueOf(role));
    }
}