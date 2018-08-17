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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import javax.validation.Valid;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class VfModule {

    @SerializedName("vf-module-id")
    @Expose
    private String vfModuleId;
    @SerializedName("vf-module-name")
    @Expose
    private String vfModuleName;
    @SerializedName("heat-stack-id")
    @Expose
    private String heatStackId;
    @SerializedName("orchestration-status")
    @Expose
    private String orchestrationStatus;
    @SerializedName("is-base-vf-module")
    @Expose
    private Boolean isBaseVfModule;
    @SerializedName("resource-version")
    @Expose
    private String resourceVersion;
    @SerializedName("model-invariant-id")
    @Expose
    private String modelInvariantId;
    @SerializedName("model-version-id")
    @Expose
    private String modelVersionId;
    @SerializedName("model-customization-id")
    @Expose
    private String modelCustomizationId;
    @SerializedName("module-index")
    @Expose
    private Integer moduleIndex;
    @SerializedName("relationship-list")
    @Expose
    @Valid
    private RelationshipList relationshipList;

    /**
     * No args constructor for use in serialization
     *
     */
    public VfModule() {
    }

    /**
     *
     * @param modelCustomizationId
     * @param moduleIndex
     * @param vfModuleName
     * @param orchestrationStatus
     * @param vfModuleId
     * @param modelInvariantId
     * @param heatStackId
     * @param isBaseVfModule
     * @param resourceVersion
     * @param relationshipList
     * @param modelVersionId
     */
    public VfModule(String vfModuleId, String vfModuleName, String heatStackId, String orchestrationStatus, Boolean isBaseVfModule, String resourceVersion, String modelInvariantId, String modelVersionId, String modelCustomizationId, Integer moduleIndex, RelationshipList relationshipList) {
        super();
        this.vfModuleId = vfModuleId;
        this.vfModuleName = vfModuleName;
        this.heatStackId = heatStackId;
        this.orchestrationStatus = orchestrationStatus;
        this.isBaseVfModule = isBaseVfModule;
        this.resourceVersion = resourceVersion;
        this.modelInvariantId = modelInvariantId;
        this.modelVersionId = modelVersionId;
        this.modelCustomizationId = modelCustomizationId;
        this.moduleIndex = moduleIndex;
        this.relationshipList = relationshipList;
    }

    public String getVfModuleId() {
        return vfModuleId;
    }

    public void setVfModuleId(String vfModuleId) {
        this.vfModuleId = vfModuleId;
    }

    public String getVfMduleName() {
        return vfModuleName;
    }

    public void setVfModuleName(String vfModuleName) {
        this.vfModuleName = vfModuleName;
    }

    public String getHeatStackId() {
        return heatStackId;
    }

    public void setHeatStackId(String heatStackId) {
        this.heatStackId = heatStackId;
    }

    public String getOrchestrationStatus() {
        return orchestrationStatus;
    }

    public void setOrchestrationStatus(String orchestrationStatus) {
        this.orchestrationStatus = orchestrationStatus;
    }

    public Boolean getIsBaseVfModule() {
        return isBaseVfModule;
    }

    public void setIsBaseVfModule(Boolean isBaseVfModule) {
        this.isBaseVfModule = isBaseVfModule;
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

    public String getModelCustomizationId() {
        return modelCustomizationId;
    }

    public void setModelCustomizationId(String modelCustomizationId) {
        this.modelCustomizationId = modelCustomizationId;
    }

    public Integer getModuleIndex() {
        return moduleIndex;
    }

    public void setModuleIndex(Integer moduleIndex) {
        this.moduleIndex = moduleIndex;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("vfModuleId", vfModuleId).append("vfModuleName", vfModuleName).append("heatStackId", heatStackId).append("orchestrationStatus", orchestrationStatus).append("isBaseVfModule", isBaseVfModule).append("resourceVersion", resourceVersion).append("modelInvariantId", modelInvariantId).append("modelVersionId", modelVersionId).append("modelCustomizationId", modelCustomizationId).append("moduleIndex", moduleIndex).append("relationshipList", relationshipList).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(modelCustomizationId).append(moduleIndex).append(vfModuleName).append(orchestrationStatus).append(vfModuleId).append(modelInvariantId).append(heatStackId).append(isBaseVfModule).append(resourceVersion).append(relationshipList).append(modelVersionId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof VfModule)) {
            return false;
        }
        VfModule rhs = ((VfModule) other);
        return new EqualsBuilder().append(modelCustomizationId, rhs.modelCustomizationId).append(moduleIndex, rhs.moduleIndex).append(vfModuleName, rhs.vfModuleName).append(orchestrationStatus, rhs.orchestrationStatus).append(vfModuleId, rhs.vfModuleId).append(modelInvariantId, rhs.modelInvariantId).append(heatStackId, rhs.heatStackId).append(isBaseVfModule, rhs.isBaseVfModule).append(resourceVersion, rhs.resourceVersion).append(relationshipList, rhs.relationshipList).append(modelVersionId, rhs.modelVersionId).isEquals();
    }

}
