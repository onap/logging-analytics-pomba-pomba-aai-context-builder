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

public class VnfInstance {

    @SerializedName("vnf-id")
    @Expose
    private String vnfId;
    @SerializedName("vnf-name")
    @Expose
    private String vnfName;
    @SerializedName("vnf-name2")
    @Expose
    private String vnfName2;
    @SerializedName("vnf-type")
    @Expose
    private String vnfType;
    @SerializedName("service-id")
    @Expose
    private String serviceId;
    @SerializedName("prov-status")
    @Expose
    private String provisionStatus;
    @SerializedName("license-key")
    @Expose
    private String licenseKey;
    @SerializedName("equipment-role")
    @Expose
    private String equipmentRole;
    @SerializedName("orchestration-status")
    @Expose
    private String orchestrationStatus;
    @SerializedName("heat-stack-id")
    @Expose
    private String heatStackId;
    @SerializedName("mso-catalog-key")
    @Expose
    private String msoCatalogKey;
    @SerializedName("ipv4-oam-address")
    @Expose
    private String ipv4OamAddress;
    @SerializedName("ipv4-loopback0-address")
    @Expose
    private String ipv4Loopback0Address;
    @SerializedName("nm-lan-v6-address")
    @Expose
    private String nmLanV6Address;
    @SerializedName("management-v6-address")
    @Expose
    private String managementV6Address;
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
    @SerializedName("model-customization-id")
    @Expose
    private String modelCustomizationId;
    @SerializedName("nf-type")
    @Expose
    private String nfType;
    @SerializedName("nf-function")
    @Expose
    private String nfFunction;
    @SerializedName("nf-role")
    @Expose
    private String nfRole;
    @SerializedName("nf-naming-code")
    @Expose
    private String nfNamingCode;
    @SerializedName("relationship-list")
    @Expose
    @Valid
    private RelationshipList relationshipList;
    @SerializedName("vf-modules")
    @Expose
    @Valid
    private VfModules vfModules;

   private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

       public String toJson() {
           return gson.toJson(this);
       }

       public static VnfInstance fromJson(String payload) throws AuditException {
           try {
               if (payload == null || payload.isEmpty()) {
                   throw new AuditException("Empty Json response");
               }
               return gson.fromJson(payload, VnfInstance.class);
           } catch (Exception ex) {
               throw new AuditException(AuditError.JSON_READER_PARSE_ERROR, ex);
           }
       }

    /**
     * No args constructor for use in serialization
     *
     */
    public VnfInstance() {
    }

    /**
     *
     * @param serviceId
     * @param modelCustomizationId
     * @param vnfType
     * @param ipv4Loopback0Address
     * @param nfFunction
     * @param modelInvariantId
     * @param resourceVersion
     * @param vnfName2
     * @param relationshipList
     * @param nmLanV6Address
     * @param nfRole
     * @param nfType
     * @param modelVersionId
     * @param ipv4OamAddress
     * @param vnfName
     * @param inMaintenance
     * @param msoCatalogKey
     * @param provisionStatus
     * @param vfModules
     * @param equipmentRole
     * @param vnfId
     * @param orchestrationStatus
     * @param nfNamingCode
     * @param heatStackId
     * @param isClosedLoopDisabled
     * @param licenseKey
     * @param managementV6Address
     */
    public VnfInstance(String vnfId, String vnfName, String vnfName2, String vnfType, String serviceId, String provisionStatus, String licenseKey, String equipmentRole, String orchestrationStatus, String heatStackId, String msoCatalogKey, String ipv4OamAddress, String ipv4Loopback0Address, String nmLanV6Address, String managementV6Address, Boolean inMaintenance, Boolean isClosedLoopDisabled, String resourceVersion, String modelInvariantId, String modelVersionId, String modelCustomizationId, String nfType, String nfFunction, String nfRole, String nfNamingCode, RelationshipList relationshipList, VfModules vfModules) {
        super();
        this.vnfId = vnfId;
        this.vnfName = vnfName;
        this.vnfName2 = vnfName2;
        this.vnfType = vnfType;
        this.serviceId = serviceId;
        this.provisionStatus = provisionStatus;
        this.licenseKey = licenseKey;
        this.equipmentRole = equipmentRole;
        this.orchestrationStatus = orchestrationStatus;
        this.heatStackId = heatStackId;
        this.msoCatalogKey = msoCatalogKey;
        this.ipv4OamAddress = ipv4OamAddress;
        this.ipv4Loopback0Address = ipv4Loopback0Address;
        this.nmLanV6Address = nmLanV6Address;
        this.managementV6Address = managementV6Address;
        this.inMaintenance = inMaintenance;
        this.isClosedLoopDisabled = isClosedLoopDisabled;
        this.resourceVersion = resourceVersion;
        this.modelInvariantId = modelInvariantId;
        this.modelVersionId = modelVersionId;
        this.modelCustomizationId = modelCustomizationId;
        this.nfType = nfType;
        this.nfFunction = nfFunction;
        this.nfRole = nfRole;
        this.nfNamingCode = nfNamingCode;
        this.relationshipList = relationshipList;
        this.vfModules = vfModules;
    }

    public String getVnfId() {
        return vnfId;
    }

    public void setVnfId(String vnfId) {
        this.vnfId = vnfId;
    }

    public String getVnfName() {
        return vnfName;
    }

    public void setVnfName(String vnfName) {
        this.vnfName = vnfName;
    }

    public String getVnfName2() {
        return vnfName2;
    }

    public void setVnfName2(String vnfName2) {
        this.vnfName2 = vnfName2;
    }

    public String getVnfType() {
        return vnfType;
    }

    public void setVnfType(String vnfType) {
        this.vnfType = vnfType;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getProvisionStatus() {
        return provisionStatus;
    }

    public void setProvisionStatus(String provisionStatus) {
        this.provisionStatus = provisionStatus;
    }

    public String getLicenseKey() {
        return licenseKey;
    }

    public void setLicenseKey(String licenseKey) {
        this.licenseKey = licenseKey;
    }

    public String getEquipmentRole() {
        return equipmentRole;
    }

    public void setEquipmentRole(String equipmentRole) {
        this.equipmentRole = equipmentRole;
    }

    public String getOrchestrationStatus() {
        return orchestrationStatus;
    }

    public void setOrchestrationStatus(String orchestrationStatus) {
        this.orchestrationStatus = orchestrationStatus;
    }

    public String getHeatStackId() {
        return heatStackId;
    }

    public void setHeatStackId(String heatStackId) {
        this.heatStackId = heatStackId;
    }

    public String getMsoCatalogKey() {
        return msoCatalogKey;
    }

    public void setMsoCatalogKey(String msoCatalogKey) {
        this.msoCatalogKey = msoCatalogKey;
    }

    public String getIpv4OamAddress() {
        return ipv4OamAddress;
    }

    public void setIpv4OamAddress(String ipv4OamAddress) {
        this.ipv4OamAddress = ipv4OamAddress;
    }

    public String getIpv4Loopback0Address() {
        return ipv4Loopback0Address;
    }

    public void setIpv4Loopback0Address(String ipv4Loopback0Address) {
        this.ipv4Loopback0Address = ipv4Loopback0Address;
    }

    public String getNmLanV6Address() {
        return nmLanV6Address;
    }

    public void setNmLanV6Address(String nmLanV6Address) {
        this.nmLanV6Address = nmLanV6Address;
    }

    public String getManagementV6Address() {
        return managementV6Address;
    }

    public void setManagementV6Address(String managementV6Address) {
        this.managementV6Address = managementV6Address;
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

    public String getModelCustomizationId() {
        return modelCustomizationId;
    }

    public void setModelCustomizationId(String modelCustomizationId) {
        this.modelCustomizationId = modelCustomizationId;
    }

    public String getNfType() {
        return nfType;
    }

    public void setNfType(String nfType) {
        this.nfType = nfType;
    }

    public String getNfFunction() {
        return nfFunction;
    }

    public void setNfFunction(String nfFunction) {
        this.nfFunction = nfFunction;
    }

    public String getNfRole() {
        return nfRole;
    }

    public void setNfRole(String nfRole) {
        this.nfRole = nfRole;
    }

    public String getNfNamingCode() {
        return nfNamingCode;
    }

    public void setNfNamingCode(String nfNamingCode) {
        this.nfNamingCode = nfNamingCode;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }

    public VfModules getVfModules() {
        return vfModules;
    }

    public void setVfModules(VfModules vfModules) {
        this.vfModules = vfModules;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("vnfId", vnfId).append("vnfName", vnfName).append("vnfName2", vnfName2).append("vnfType", vnfType).append("serviceId", serviceId).append("provisionStatus", provisionStatus).append("licenseKey", licenseKey).append("equipmentRole", equipmentRole).append("orchestrationStatus", orchestrationStatus).append("heatStackId", heatStackId).append("msoCatalogKey", msoCatalogKey).append("ipv4OamAddress", ipv4OamAddress).append("ipv4Loopback0Address", ipv4Loopback0Address).append("nmLanV6Address", nmLanV6Address).append("managementV6Address", managementV6Address).append("inMaintenance", inMaintenance).append("isClosedLoopDisabled", isClosedLoopDisabled).append("resourceVersion", resourceVersion).append("modelInvariantId", modelInvariantId).append("modelVersionId", modelVersionId).append("modelCustomizationId", modelCustomizationId).append("nfType", nfType).append("nfFunction", nfFunction).append("nfRole", nfRole).append("nfNamingCode", nfNamingCode).append("relationshipList", relationshipList).append("vfModules", vfModules).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(modelCustomizationId).append(serviceId).append(ipv4Loopback0Address).append(vnfType).append(nfFunction).append(modelInvariantId).append(resourceVersion).append(vnfName2).append(relationshipList).append(nmLanV6Address).append(nfRole).append(nfType).append(modelVersionId).append(ipv4OamAddress).append(vnfName).append(inMaintenance).append(msoCatalogKey).append(provisionStatus).append(vfModules).append(equipmentRole).append(vnfId).append(orchestrationStatus).append(nfNamingCode).append(isClosedLoopDisabled).append(heatStackId).append(licenseKey).append(managementV6Address).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof VnfInstance)) {
            return false;
        }
        VnfInstance rhs = ((VnfInstance) other);
        return new EqualsBuilder().append(modelCustomizationId, rhs.modelCustomizationId).append(serviceId, rhs.serviceId).append(ipv4Loopback0Address, rhs.ipv4Loopback0Address).append(vnfType, rhs.vnfType).append(nfFunction, rhs.nfFunction).append(modelInvariantId, rhs.modelInvariantId).append(resourceVersion, rhs.resourceVersion).append(vnfName2, rhs.vnfName2).append(relationshipList, rhs.relationshipList).append(nmLanV6Address, rhs.nmLanV6Address).append(nfRole, rhs.nfRole).append(nfType, rhs.nfType).append(modelVersionId, rhs.modelVersionId).append(ipv4OamAddress, rhs.ipv4OamAddress).append(vnfName, rhs.vnfName).append(inMaintenance, rhs.inMaintenance).append(msoCatalogKey, rhs.msoCatalogKey).append(provisionStatus, rhs.provisionStatus).append(vfModules, rhs.vfModules).append(equipmentRole, rhs.equipmentRole).append(vnfId, rhs.vnfId).append(orchestrationStatus, rhs.orchestrationStatus).append(nfNamingCode, rhs.nfNamingCode).append(isClosedLoopDisabled, rhs.isClosedLoopDisabled).append(heatStackId, rhs.heatStackId).append(licenseKey, rhs.licenseKey).append(managementV6Address, rhs.managementV6Address).isEquals();
    }

}
