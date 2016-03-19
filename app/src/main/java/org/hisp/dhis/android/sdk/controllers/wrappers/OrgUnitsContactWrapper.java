package org.hisp.dhis.android.sdk.controllers.wrappers;

import android.util.Log;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.hisp.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.hisp.dhis.android.sdk.controllers.DhisController;
import org.hisp.dhis.android.sdk.persistence.models.OrganisationUnitContactInfo;
import org.hisp.dhis.android.sdk.utils.StringConverter;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

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
        JsonNode organisationUnitsNode = rootNode.get(ApiEndpointContainer.ORGANISATIONUNITS);

        Iterator <Map.Entry<String, JsonNode>> entryIterator= rootNode.fields();

        while(entryIterator.hasNext()){
            Map.Entry<String, JsonNode> ind = entryIterator.next();
            if(ind.getKey() == "attributeValues" || ind.getKey() == "id"){
                contactInfo = DhisController.getInstance().getObjectMapper().
                    readValue("{\"" +ind.getKey() + "\":" + ind.getValue() + "}", OrganisationUnitContactInfo.class);
            }
        }

        return contactInfo;
    }

    // TODO : Fix the "hacky stuff" that is done to pass the json string
    public OrganisationUnitContactInfo deserialize(Response response) throws ConversionException, IOException {
        OrganisationUnitContactInfo contactInfo = null;
        String responseBodyString = new StringConverter().fromBody(response.getBody(), String.class);
        JsonNode rootNode = DhisController.getInstance().getObjectMapper().
                    readTree(responseBodyString);

        Iterator <Map.Entry<String, JsonNode>> entryIterator= rootNode.fields();

        while(entryIterator.hasNext()){
            Map.Entry<String, JsonNode> ind = entryIterator.next();
            if(ind.getKey() == "attributeValues" || ind.getKey() == "id"){
                contactInfo = DhisController.getInstance().getObjectMapper().
                    readValue("{\"" +ind.getKey() + "\":" + ind.getValue() + "}", OrganisationUnitContactInfo.class);
            }
        }

        Log.d(CLASS_TAG, "ID: " + contactInfo.getId());
        Log.d(CLASS_TAG, "Attrs: " + contactInfo.getAttributeValues().toString());

        return contactInfo;
    }
}
