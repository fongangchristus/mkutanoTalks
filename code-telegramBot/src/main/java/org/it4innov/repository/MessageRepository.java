package org.it4innov.repository;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.it4innov.entity.MessageReceived;

public interface MessageRepository extends PanacheRepository<MessageReceived> {

}
