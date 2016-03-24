package org.hisp.dhis.android.sdk.controllers.wrappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.hisp.dhis.android.sdk.controllers.DhisController;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnitContactInfo;
import org.hisp.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.hisp.dhis.android.sdk.utils.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit.client.Response;
import retrofit.converter.ConversionException;

/**
 * Created by premer on 08.03.16.
 */
public class OrgUnitsContactWrapper extends JsonDeserializer<OrganisationUnitContactInfo> {
    private final String CLASS_TAG = "OrgUnitContactWrapper";
    @Override
    public OrganisationUnitContactInfo deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        OrganisationUnitContactInfo contactInfo = null;
        JsonNode rootNode = p.getCodec().readTree(p);

        contactInfo = DhisController.getInstance().getObjectMapper().
                    readValue(rootNode.toString(), OrganisationUnitContactInfo.class);

        return contactInfo;
    }

    public OrganisationUnitContactInfo deserialize(Response response) throws ConversionException, IOException {
        OrganisationUnitContactInfo contactInfo = null;
        String responseBodyString = new StringConverter().fromBody(response.getBody(), String.class);
        JsonNode rootNode = DhisController.getInstance().getObjectMapper().
                    readTree(responseBodyString);

        contactInfo = DhisController.getInstance().getObjectMapper().
                    readValue(rootNode.toString(), OrganisationUnitContactInfo.class);

        return contactInfo;
    }


    public static List<DbOperation> getOperations(List<OrganisationUnitContactInfo> orgUnitContactInfo) {
        List<DbOperation> operations = new ArrayList<>();

        for (OrganisationUnitContactInfo contactInfo: orgUnitContactInfo) {
            operations.add(DbOperation.save(contactInfo));
        }
        return operations;
    }
}
