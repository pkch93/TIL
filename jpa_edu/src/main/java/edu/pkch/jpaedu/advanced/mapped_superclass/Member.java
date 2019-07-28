package edu.pkch.jpaedu.advanced.mapped_superclass;

import javax.persistence.*;

@Entity
@AttributeOverrides({
        @AttributeOverride(name = "id", column = @Column(name = "MEMBER_ID")),
        @AttributeOverride(name = "name", column = @Column(name = "MEMBER_NAME", unique = true))
})
@AssociationOverride(name = "products", joinColumns = {
        @JoinColumn(name = "MEMBER_ID", foreignKey = @ForeignKey(name = "MEMBER_TO_PRODUCT_FK"))
})
public class Member extends BaseEntity {
    private String email;
}
