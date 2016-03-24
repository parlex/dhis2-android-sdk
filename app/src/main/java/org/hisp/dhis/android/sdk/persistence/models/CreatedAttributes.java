package org.hisp.dhis.android.sdk.persistence.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.hisp.dhis.android.sdk.persistence.Dhis2Database;

/**
 * Created by premer on 21.03.16.
 */
@Table(databaseName = Dhis2Database.NAME)
public class CreatedAttributes extends BaseModel{
    private final String CLASS_TAG = getClass().getName();

    @JsonProperty("id")
    @Column(name = "id")
    @PrimaryKey
    String id;

    @JsonProperty("name")
    @Column(name = "name")
    String name;

    @JsonProperty("organisationUnitAttribute")
    @Column(name = "organisationUnitAttribute")
    boolean orgUnitAttribute;

    public CreatedAttributes(){}


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOrgUnitAttribute() {
        return orgUnitAttribute;
    }

    public void setOrgUnitAttribute(boolean orgUnitAttribute) {
        this.orgUnitAttribute = orgUnitAttribute;
    }

}
