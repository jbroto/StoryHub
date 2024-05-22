package es.ucm.fdi.iw.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable
public class MediaUserRelationId implements Serializable {
    private long mediaId;
    private long userId;
}
