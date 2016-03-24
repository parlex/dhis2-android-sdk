package org.hisp.dhis.android.sdk.persistence.models;

import android.util.Log;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.hisp.dhis.android.sdk.controllers.metadata.MetaDataController;
import org.hisp.dhis.android.sdk.persistence.Dhis2Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by premer on 15.03.16.
 */
@Table(databaseName = Dhis2Database.NAME)
public class OrganisationUnitContactInfo extends BaseModel{
    private final String CLASS_TAG = getClass().getName();

    @JsonProperty("id")
    @Column(name = "id")
    @PrimaryKey
    String id;

    @JsonProperty("shortName")
    @Column(name = "shortName")
    String shortName;

    @Column(name = "contactName")
    String contactName;

    @Column(name = "contactNo")
    String contactNo;

    public OrganisationUnitContactInfo(){}

    // TODO : Fix the hacky stuff with the attribute id, should/(has to) be a better way to get the ID
    @JsonProperty("attributeValues")
    public void setAttributeValues(List<Map<String, Object>> attributeValues) {

        for (Map<String, Object> attributeValue : attributeValues) {
            //This thing is stupid as .... Needs fixing
            String id = attributeValue.get("attribute").toString().split("=")[1];
            id = id.substring(0, id.length()-1);

            CreatedAttributes createdAttributes = MetaDataController.getCreatedAttribute(id);
            if(createdAttributes != null){
                if(createdAttributes.getName().equalsIgnoreCase("MES Contact")){
                    setContactName((String) attributeValue.get("value"));
                }else if(createdAttributes.getName().equalsIgnoreCase("MES Phone Number")){
                    setContactNo((String) attributeValue.get("value"));
                }
            }
        }
    }

    public void setContactName(String contactName){
        this.contactName = contactName;
    }

    public String getContactName(){
        return this.contactName;
    }

    public void setContactNo(String contactNo){
        this.contactNo = contactNo;
    }

    public String getContactNo(){
        return this.contactNo;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return this.id;
    }

    public void setShortName(String shortName){
        this.shortName = shortName;
    }

    public String getShortName(){
        return this.shortName;
    }

}
