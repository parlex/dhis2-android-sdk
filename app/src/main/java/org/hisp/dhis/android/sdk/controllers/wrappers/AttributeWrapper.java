package org.hisp.dhis.android.sdk.controllers.wrappers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import org.hisp.dhis.android.sdk.controllers.ApiEndpointContainer;
import org.hisp.dhis.android.sdk.controllers.DhisController;
import org.hisp.dhis.android.sdk.persistence.models.CreatedAttributes;
import org.hisp.dhis.android.sdk.persistence.models.meta.DbOperation;
import org.hisp.dhis.android.sdk.utils.StringConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import retrofit.client.Response;
import retrofit.converter.ConversionException;

/**
 * Created by premer on 21.03.16.
 */
public class AttributeWrapper extends JsonDeserializer<CreatedAttributes> {
    private final String CLASS_TAG = "OrgUnitContactWrapper";
    @Override
    public CreatedAttributes deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        CreatedAttributes contactInfo = null;
        JsonNode rootNode = p.getCodec().readTree(p);

        contactInfo = DhisController.getInstance().getObjectMapper().
                    readValue(rootNode.toString(), CreatedAttributes.class);

        return contactInfo;
    }

    public CreatedAttributes deserialize(Response response) throws ConversionException, IOException {
        CreatedAttributes attribute = null;
        String responseBodyString = new StringConverter().fromBody(response.getBody(), String.class);
        JsonNode rootNode = DhisController.getInstance().getObjectMapper().
                    readTree(responseBodyString);

        attribute = DhisController.getInstance().getObjectMapper().
                    readValue(rootNode.toString(), CreatedAttributes.class);

        return attribute;
    }

    public List<String> deserializeAttributeIDs(Response response) throws ConversionException, IOException {
        List<String> attributeIDs = new ArrayList<>();

        String responseBodyString = new StringConverter().fromBody(response.getBody(), String.class);
        JsonNode rootNode = DhisController.getInstance().getObjectMapper().
                    readTree(responseBodyString);

        JsonNode attributesNode = rootNode.get(ApiEndpointContainer.ATTRIBUTES);

        if (attributesNode == null) { /* in case there are no items */
            return attributeIDs;
        } else {
            Iterator<JsonNode> nodes = attributesNode.elements();
            while(nodes.hasNext()) {
                JsonNode indexNode = nodes.next();
                attributeIDs.add(indexNode.get("id").textValue());
            }
        }

        return attributeIDs;
    }



    public static List<DbOperation> getOperations(List<CreatedAttributes> createdAttributes) {
        List<DbOperation> operations = new ArrayList<>();

        for (CreatedAttributes createdAttribute: createdAttributes) {
            operations.add(DbOperation.save(createdAttribute));
        }
        return operations;
    }
}
