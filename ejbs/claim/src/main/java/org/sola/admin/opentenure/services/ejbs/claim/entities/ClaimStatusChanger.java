package org.sola.admin.opentenure.services.ejbs.claim.entities;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import org.sola.services.common.repository.entities.AbstractEntity;

@Table(schema = "opentenure", name = "claim")
public class ClaimStatusChanger extends AbstractEntity {

    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "status_code")
    private String statusCode;

    public ClaimStatusChanger() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
