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

public class VnfcInstance {

    @SerializedName("vnfc-name")
    @Expose
    private String vnfcName;
    @SerializedName("nfc-naming-code")
    @Expose
    private String nfcNamingCode;
    @SerializedName("nfc-function")
    @Expose
    private String nfcFunction;
    @SerializedName("prov-status")
    @Expose
    private String provisionStatus;
    @SerializedName("orchestration-status")
    @Expose
    private String orchestrationStatus;
    @SerializedName("in-maint")
    @Expose
    private Boolean inMaintenance;
    @SerializedName("is-closed-loop-disabled")
    @Expose
    private Boolean isClosedLoopDisabled;
    @SerializedName("resource-version")
    @Expose
    private String resourceVersion;
    @SerializedName("model-invariant-id")
    @Expose
    private String modelInvariantId;
    @SerializedName("model-version-id")
    @Expose
    private String modelVersionId;
    @SerializedName("relationship-list")
    @Expose
    @Valid
    private RelationshipList relationshipList;

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public String toJson() {
        return gson.toJson(this);
    }

    public static VnfcInstance fromJson(String payload) throws AuditException {
        try {
            if (payload == null || payload.isEmpty()) {
                throw new AuditException("Empty Json response");
            }
            return gson.fromJson(payload, VnfcInstance.class);
        } catch (Exception ex) {
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR, ex);
        }
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public VnfcInstance() {
    }

    /**
     *
     * @param nfcNamingCode
     * @param inMaintenance
     * @param nfcFunction
     * @param orchestrationStatus
     * @param modelInvariantId
     * @param isClosedLoopDisabled
     * @param resourceVersion
     * @param provisionStatus
     * @param relationshipList
     * @param modelVersionId
     * @param vnfcName
     */
    public VnfcInstance(String vnfcName, String nfcNamingCode, String nfcFunction, String provisionStatus, String orchestrationStatus, Boolean inMaintenance, Boolean isClosedLoopDisabled, String resourceVersion, String modelInvariantId, String modelVersionId, RelationshipList relationshipList) {
        super();
        this.vnfcName = vnfcName;
        this.nfcNamingCode = nfcNamingCode;
        this.nfcFunction = nfcFunction;
        this.provisionStatus = provisionStatus;
        this.orchestrationStatus = orchestrationStatus;
        this.inMaintenance = inMaintenance;
        this.isClosedLoopDisabled = isClosedLoopDisabled;
        this.resourceVersion = resourceVersion;
        this.modelInvariantId = modelInvariantId;
        this.modelVersionId = modelVersionId;
        this.relationshipList = relationshipList;
    }

    public String getVnfcName() {
        return vnfcName;
    }

    public void setVnfcName(String vnfcName) {
        this.vnfcName = vnfcName;
    }

    public String getNfcNamingCode() {
        return nfcNamingCode;
    }

    public void setNfcNamingCode(String nfcNamingCode) {
        this.nfcNamingCode = nfcNamingCode;
    }

    public String getNfcFunction() {
        return nfcFunction;
    }

    public void setNfcFunction(String nfcFunction) {
        this.nfcFunction = nfcFunction;
    }

    public String getProvisionStatus() {
        return provisionStatus;
    }

    public void setProvisionStatus(String provisionStatus) {
        this.provisionStatus = provisionStatus;
    }

    public String getOrchestrationStatus() {
        return orchestrationStatus;
    }

    public void setOrchestrationStatus(String orchestrationStatus) {
        this.orchestrationStatus = orchestrationStatus;
    }

    public Boolean getInMaintenance() {
        return inMaintenance;
    }

    public void setInMaintenance(Boolean inMaintenance) {
        this.inMaintenance = inMaintenance;
    }

    public Boolean getIsClosedLoopDisabled() {
        return isClosedLoopDisabled;
    }

    public void setIsClosedLoopDisabled(Boolean isClosedLoopDisabled) {
        this.isClosedLoopDisabled = isClosedLoopDisabled;
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

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("vnfcName", vnfcName).append("nfcNamingCode", nfcNamingCode).append("nfcFunction", nfcFunction).append("provisionStatus", provisionStatus).append("orchestration_status", orchestrationStatus).append("inMaintenance", inMaintenance).append("isClosedLoopDisabled", isClosedLoopDisabled).append("resourceVersion", resourceVersion).append("modelInvariantId", modelInvariantId).append("modelVersionId", modelVersionId).append("relationshipList", relationshipList).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(nfcNamingCode).append(inMaintenance).append(nfcFunction).append(orchestrationStatus).append(modelInvariantId).append(isClosedLoopDisabled).append(resourceVersion).append(provisionStatus).append(relationshipList).append(modelVersionId).append(vnfcName).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof VnfcInstance)) {
            return false;
        }
        VnfcInstance rhs = ((VnfcInstance) other);
        return new EqualsBuilder().append(nfcNamingCode, rhs.nfcNamingCode).append(inMaintenance, rhs.inMaintenance).append(nfcFunction, rhs.nfcFunction).append(orchestrationStatus, rhs.orchestrationStatus).append(modelInvariantId, rhs.modelInvariantId).append(isClosedLoopDisabled, rhs.isClosedLoopDisabled).append(resourceVersion, rhs.resourceVersion).append(provisionStatus, rhs.provisionStatus).append(relationshipList, rhs.relationshipList).append(modelVersionId, rhs.modelVersionId).append(vnfcName, rhs.vnfcName).isEquals();
    }

}
