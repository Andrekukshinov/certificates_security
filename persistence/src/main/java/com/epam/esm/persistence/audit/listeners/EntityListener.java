package com.epam.esm.persistence.audit.listeners;

import com.epam.esm.persistence.audit.util.BeanUtil;
import com.epam.esm.persistence.entity.History;
import com.epam.esm.persistence.model.enums.Action;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import java.time.LocalDateTime;

import static com.epam.esm.persistence.model.enums.Action.DELETING;
import static com.epam.esm.persistence.model.enums.Action.INSERTING;
import static com.epam.esm.persistence.model.enums.Action.UPDATING;

public class EntityListener {

    @PostPersist
    public void prePersist(Object target) {
        trackAction(target, INSERTING);
    }

    @PostUpdate
    public void preUpdate(Object target) {
        trackAction(target, UPDATING);
    }

    @PostRemove
    public void preRemove(Object target) {
        trackAction(target, DELETING);
    }

    @Transactional()
    protected void trackAction(Object target, Action action) {
        EntityManager bean = BeanUtil.getBean(EntityManager.class);
        History str = new History(LocalDateTime.now(), target.toString(), action);
        bean.persist(str);
    }
}
