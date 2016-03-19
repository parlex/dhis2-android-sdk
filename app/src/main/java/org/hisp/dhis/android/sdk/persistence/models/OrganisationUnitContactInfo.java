package org.hisp.dhis.android.sdk.persistence.models;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.hisp.dhis.android.sdk.persistence.Dhis2Database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by premer on 15.03.16.
 */
@Table(databaseName = Dhis2Database.NAME)
public class OrganisationUnitContactInfo extends BaseModel{
    private final String CLASS_TAG = "OrganisationUnitContactInfo";

    @JsonProperty("id")
    @Column(name = "id")
    @PrimaryKey
    String id;

    List<String> attributeValues;

    public OrganisationUnitContactInfo(){}

    @JsonProperty("attributeValues")
    public void setAttributeValues(List<Map<String, Object>> attributeValues) {
        List<String> tempAttrValues = new ArrayList<>();
        for (Map<String, Object> attributeValue : attributeValues) {
            tempAttrValues.add((String) attributeValue.get("value"));
        }
        Log.d(CLASS_TAG, "Setting attributeValues to: " + tempAttrValues.toString());
        this.attributeValues = tempAttrValues;
    }

    public void setId(String id){
        Log.d(CLASS_TAG, "Setting id to: " + id);
        this.id = id;
        Log.d(CLASS_TAG, "Id is now: " + this.getId());
    }

    public String getId(){
        return this.id;
    }

    public List<String> getAttributeValues() {
        return attributeValues;
    }

}
