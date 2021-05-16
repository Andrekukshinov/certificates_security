package com.epam.esm.persistence.entity;

import com.epam.esm.persistence.model.enums.Action;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "audit_history")
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "activity_time")
    private LocalDateTime activityTime;

    private String object;

    @Enumerated(EnumType.STRING)
    private Action action;

    public History() {
    }

    public History(LocalDateTime activityTime, String object, Action action) {
        this.activityTime = activityTime;
        this.object = object;
        this.action = action;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getActivityTime() {
        return activityTime;
    }

    public void setActivityTime(LocalDateTime activityTime) {
        this.activityTime = activityTime;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        History history = (History) o;
        return Objects.equals(getId(), history.getId()) && Objects.equals(getActivityTime(), history.getActivityTime()) && Objects.equals(getObject(), history.getObject()) && getAction() == history.getAction();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getActivityTime(), getObject(), getAction());
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", activityTime=" + activityTime +
                ", object='" + object + '\'' +
                ", action=" + action +
                '}';
    }
}
