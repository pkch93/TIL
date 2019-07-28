package edu.pkch.jpaedu.advanced.identify2;

import java.io.Serializable;
import java.util.Objects;

public class GCId implements Serializable {
    private CId cId;
    private Long gcId;

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GCId gcId1 = (GCId) o;
        return Objects.equals(cId, gcId1.cId) &&
                Objects.equals(gcId, gcId1.gcId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cId, gcId);
    }
}
