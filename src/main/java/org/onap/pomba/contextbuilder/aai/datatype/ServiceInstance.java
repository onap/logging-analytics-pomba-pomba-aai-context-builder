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

public class ServiceInstance {

    @SerializedName("service-instance-id")
    @Expose
    private String serviceInstanceId;
    @SerializedName("service-instance-name")
    @Expose
    private String serviceInstanceName;
    @SerializedName("service-type")
    @Expose
    private String serviceType;
    @SerializedName("service-role")
    @Expose
    private String serviceRole;
    @SerializedName("environment-context")
    @Expose
    private String environmentContext;
    @SerializedName("workload-context")
    @Expose
    private String workloadContext;
    @SerializedName("model-invariant-id")
    @Expose
    private String modelInvariantId;
    @SerializedName("model-version-id")
    @Expose
    private String modelVersionId;
    @SerializedName("resource-version")
    @Expose
    private String resourceVersion;
    @SerializedName("orchestration-status")
    @Expose
    private String orchestrationStatus;
    @SerializedName("relationship-list")
    @Expose
    @Valid
    private RelationshipList relationshipList;

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

       public String toJson() {
           return gson.toJson(this);
       }

       public static ServiceInstance fromJson(String payload) throws AuditException {
           try {
               if (payload == null || payload.isEmpty()) {
                   throw new AuditException("Empty Json response");
               }
               return gson.fromJson(payload, ServiceInstance.class);
           } catch (Exception ex) {
               throw new AuditException(AuditError.JSON_READER_PARSE_ERROR, ex);
           }
       }

    /**
     * No args constructor for use in serialization
     *
     */
    public ServiceInstance() {
    }

    /**
     *
     * @param serviceRole
     * @param serviceInstanceName
     * @param orchestrationStatus
     * @param serviceType
     * @param modelInvariantId
     * @param workloadContext
     * @param environmentContext
     * @param resourceVersion
     * @param relationshipList
     * @param modelVersionId
     * @param serviceInstanceId
     */
    public ServiceInstance(String serviceInstanceId, String serviceInstanceName, String serviceType, String serviceRole, String environmentContext, String workloadContext, String modelInvariantId, String modelVersionId, String resourceVersion, String orchestrationStatus, RelationshipList relationshipList) {
        super();
        this.serviceInstanceId = serviceInstanceId;
        this.serviceInstanceName = serviceInstanceName;
        this.serviceType = serviceType;
        this.serviceRole = serviceRole;
        this.environmentContext = environmentContext;
        this.workloadContext = workloadContext;
        this.modelInvariantId = modelInvariantId;
        this.modelVersionId = modelVersionId;
        this.resourceVersion = resourceVersion;
        this.orchestrationStatus = orchestrationStatus;
        this.relationshipList = relationshipList;
    }

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public String getServiceInstanceName() {
        return serviceInstanceName;
    }

    public void setServiceInstanceName(String serviceInstanceName) {
        this.serviceInstanceName = serviceInstanceName;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getServiceRole() {
        return serviceRole;
    }

    public void setServiceRole(String serviceRole) {
        this.serviceRole = serviceRole;
    }

    public String getEnvironmentContext() {
        return environmentContext;
    }

    public void setEnvironmentContext(String environmentContext) {
        this.environmentContext = environmentContext;
    }

    public String getWorkloadContext() {
        return workloadContext;
    }

    public void setWorkloadContext(String workloadContext) {
        this.workloadContext = workloadContext;
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

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getOrchestrationStatus() {
        return orchestrationStatus;
    }

    public void setOrchestrationStatus(String orchestrationStatus) {
        this.orchestrationStatus = orchestrationStatus;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("serviceInstanceId", serviceInstanceId).append("serviceInstanceName", serviceInstanceName).append("serviceType", serviceType).append("serviceRole", serviceRole).append("environmentContext", environmentContext).append("workloadContext", workloadContext).append("modelInvariantId", modelInvariantId).append("modelVersionId", modelVersionId).append("resourceVersion", resourceVersion).append("orchestrationStatus", orchestrationStatus).append("relationshipList", relationshipList).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(serviceRole).append(serviceInstanceName).append(orchestrationStatus).append(serviceType).append(modelInvariantId).append(workloadContext).append(environmentContext).append(resourceVersion).append(relationshipList).append(modelVersionId).append(serviceInstanceId).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof ServiceInstance)) {
            return false;
        }
        ServiceInstance rhs = ((ServiceInstance) other);
        return new EqualsBuilder().append(serviceRole, rhs.serviceRole).append(serviceInstanceName, rhs.serviceInstanceName).append(orchestrationStatus, rhs.orchestrationStatus).append(serviceType, rhs.serviceType).append(modelInvariantId, rhs.modelInvariantId).append(workloadContext, rhs.workloadContext).append(environmentContext, rhs.environmentContext).append(resourceVersion, rhs.resourceVersion).append(relationshipList, rhs.relationshipList).append(modelVersionId, rhs.modelVersionId).append(serviceInstanceId, rhs.serviceInstanceId).isEquals();
    }

}
