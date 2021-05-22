package org.techlab.whitek0t.SkillBoxChats.repos;

import org.springframework.data.repository.CrudRepository;
import org.techlab.whitek0t.SkillBoxChats.model.User;

public interface UserRepository extends CrudRepository<User, Integer> {
    User getBySessionId(String sessionId);
}