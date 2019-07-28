package edu.pkch.jpaedu.advanced.identify;

import java.io.Serializable;
import java.util.Objects;

public class ParentId implements Serializable {
    private String id1;
    private String id2;

    public ParentId() {
    }

    public ParentId(final String id1, final String id2) {
        this.id1 = id1;
        this.id2 = id2;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ParentId parentId = (ParentId) o;
        return Objects.equals(id1, parentId.id1) &&
                Objects.equals(id2, parentId.id2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id1, id2);
    }
}
