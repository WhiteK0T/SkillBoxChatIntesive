package org.techlab.whitek0t.SkillBoxChats.repos;

import org.springframework.data.repository.CrudRepository;
import org.techlab.whitek0t.SkillBoxChats.model.Message;

public interface MessageRepository extends CrudRepository<Message, Integer> {
    MessageRepository getBySessionId(String sessionId);
}
