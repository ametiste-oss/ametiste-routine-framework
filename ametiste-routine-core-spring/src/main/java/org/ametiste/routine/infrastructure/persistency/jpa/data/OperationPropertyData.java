package org.ametiste.routine.infrastructure.persistency.jpa.data;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Embeddable
public class OperationPropertyData {

    private String name;

    private String value;

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(final String value) {
        this.value = value;
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if ( !(other instanceof OperationPropertyData) ) return false;

        final OperationPropertyData prop = (OperationPropertyData) other;

        if ( !prop.getValue().equals( getValue() ) ) return false;
        if ( !prop.getName().equals( getName() ) ) return false;

        return true;
    }

    public int hashCode() {
        int result;
        result = 29 * getValue().hashCode() + getName().hashCode();
        return result;
    }

}