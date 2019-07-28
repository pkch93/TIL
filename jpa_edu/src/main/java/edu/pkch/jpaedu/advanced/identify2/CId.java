package edu.pkch.jpaedu.advanced.identify2;

import java.io.Serializable;
import java.util.Objects;

public class CId implements Serializable {
    private Long parent;
    private Long childId;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CId cId = (CId) o;
        return Objects.equals(parent, cId.parent) &&
                Objects.equals(childId, cId.childId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, childId);
    }
}
