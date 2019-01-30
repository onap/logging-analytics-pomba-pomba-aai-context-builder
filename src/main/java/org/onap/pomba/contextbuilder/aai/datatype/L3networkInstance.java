/*
 * ============LICENSE_START===================================================
 * Copyright (c) 2018 Amdocs
 * ============================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=====================================================
 */

package org.onap.pomba.contextbuilder.aai.datatype;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import javax.validation.Valid;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.onap.pomba.contextbuilder.aai.exception.AuditError;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;

public class L3networkInstance {
    @SerializedName("network-id")
    @Expose
    private String networkId;

    @SerializedName("network-name")
    @Expose
    private String networkName;
    @SerializedName("network-type")
    @Expose
    private String networkType;
    @SerializedName("network-role")
    @Expose
    private String networkRole;
    @SerializedName("network-technology")
    @Expose
    private String networkTechnology;
    @SerializedName("resource-version")
    @Expose
    private String resourceVersion;
    @SerializedName("model-invariant-id")
    @Expose
    private String modelInvariantId;
    @SerializedName("model-version-id")
    @Expose
    private String modelVersionId;
    @SerializedName("physical-network-name")
    @Expose
    private String physicalNetworkName;
    @SerializedName("is-shared-network")
    @Expose
    private String sharedNetworkBoolean;

    @SerializedName("relationship-list")
    @Expose
    @Valid
    private RelationshipList relationshipList;

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getNetworkRole() {
        return networkRole;
    }

    public void setNetworkRole(String networkRole) {
        this.networkRole = networkRole;
    }

    public String getNetworkTechnology() {
        return networkTechnology;
    }

    public void setNetworkTechnology(String networkTechnology) {
        this.networkTechnology = networkTechnology;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getModelInvariantId() {
        return modelInvariantId;
    }

    public void setModelInvariantId(String modelInvariantId) {
        this.modelInvariantId = modelInvariantId;
    }

    public String getModelVersionId() {
        return modelVersionId;
    }

    public void setModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }

    public String getPhysicalNetworkName() {
        return physicalNetworkName;
    }

    public void setPhysicalNetworkName(String physicalNetworkName) {
        this.physicalNetworkName = physicalNetworkName;
    }

    public String getSharedNetworkBoolean() {
        return sharedNetworkBoolean;
    }

    public void setSharedNetworkBoolean(String sharedNetworkBoolean) {
        this.sharedNetworkBoolean = sharedNetworkBoolean;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public String toJson() {
        return gson.toJson(this);
    }

    public static L3networkInstance fromJson(String payload) throws AuditException {
        try {
            if (payload == null || payload.isEmpty()) {
                throw new AuditException("Empty Json response");
            }
            return gson.fromJson(payload, L3networkInstance.class);
        } catch (Exception ex) {
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR, ex);
        }
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public L3networkInstance() {
    }

    /**
    *
    * @param networkId
    * @param networkName
    * @param networkType
    * @param networkRole
    * @param networkTechnology
    * @param resourceVersion
    * @param modelInvariantId
    * @param modelVersionId
    * @param physicalNetworkName
    * @param sharedNetworkBoolean
    *
    */
   public L3networkInstance(String networkId, String networkName, String networkType,
           String networkRole, String networkTechnology,String resourceVersion,
           String modelInvariantId, String modelVersionId, String physicalNetworkName,String sharedNetworkBoolean,
           RelationshipList relationshipList) {
       super();
       this.networkId = networkId;
       this.networkName = networkName;
       this.networkType = networkType;
       this.networkRole = networkRole;
       this.networkTechnology = networkTechnology;
       this.resourceVersion = resourceVersion;
       this.modelInvariantId = modelInvariantId;
       this.modelVersionId = modelVersionId;
       this.physicalNetworkName = physicalNetworkName;
       this.sharedNetworkBoolean = sharedNetworkBoolean;
       this.relationshipList = relationshipList;
   }



    /////////// common functions //////////////////////
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("networkId", networkId)
                .append("networkName", networkName)
                .append("networkType", networkType)
                .append("networkRole", networkRole)
                .append("networkTechnology", networkTechnology)
                .append("resourceVersion", resourceVersion)
                .append("modelInvariantId", modelInvariantId)
                .append("modelVersionId", modelVersionId)
                .append("physicalNetworkName", physicalNetworkName)
                .append("sharedNetworkBoolean", sharedNetworkBoolean)
                .append("relationshipList", relationshipList)
               .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(networkId)
                .append(networkName)
                .append(networkType)
                .append(networkRole)
                .append(networkTechnology)
                .append(resourceVersion)
                .append(modelInvariantId)
                .append(modelVersionId)
                .append(physicalNetworkName)
                .append(sharedNetworkBoolean)
                .append(relationshipList)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof L3networkInstance)) {
            return false;
        }
        L3networkInstance rhs = ((L3networkInstance) other);
        return new EqualsBuilder()
                .append(networkId, rhs.networkId)
                .append(networkName, rhs.networkName)
                .append(networkType, rhs.networkType)
                .append(networkRole, rhs.networkRole)
                .append(networkTechnology, rhs.networkTechnology)
                .append(resourceVersion, rhs.resourceVersion)
                .append(modelInvariantId, rhs.modelInvariantId)
                .append(modelVersionId, rhs.modelVersionId)
                .append(physicalNetworkName, rhs.physicalNetworkName)
                .append(sharedNetworkBoolean, rhs.sharedNetworkBoolean)
               .isEquals();
    }
}
