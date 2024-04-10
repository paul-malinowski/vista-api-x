package gov.va.octo.vista.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author william.mccarty@va.gov
 * 
 * 
 */

@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
@Setter
public abstract class BaseModel implements Serializable {

    private static final long serialVersionUID = -4092437485601153517L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private Long oplock;
    private boolean active = true;
    private Date createdAt = new Date();
    private Date modifiedAt = new Date();

}
