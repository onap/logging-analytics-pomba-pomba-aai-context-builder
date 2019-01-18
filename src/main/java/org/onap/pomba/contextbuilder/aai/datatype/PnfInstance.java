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

public class PnfInstance {

    @SerializedName("pnf-id")
    @Expose
    private String pnfId;
    @SerializedName("pnf-name")
    @Expose
    private String pnfName;
    @SerializedName("nf-function")
    @Expose
    private String nfFunction;
    @SerializedName("nf-role")
    @Expose
    private String nfRole;
    @SerializedName("resource-version")
    @Expose
    private String resourceVersion;
    @SerializedName("pnf-name2")
    @Expose
    private String pnfName2;
    @SerializedName("pnf-name2-source")
    @Expose
    private String pnfName2Source;
    @SerializedName("equip-type")
    @Expose
    private String equipmentType;
    @SerializedName("equip-vendor")
    @Expose
    private String equipmentVendor;
    @SerializedName("equip-model")
    @Expose
    private String equipmentModel;
    @SerializedName("management-option")
    @Expose
    private String managementOptions;
    @SerializedName("sw-version")
    @Expose
    private String swVersion;
    @SerializedName("frame-id")
    @Expose
    private String frameId;
    @SerializedName("serial-number")
    @Expose
    private String serialNumber;
    @SerializedName("model-invariant-id")
    @Expose
    private String modelInvariantId;
    @SerializedName("model-version-id")
    @Expose
    private String modelVersionId;

    @SerializedName("p-interfaces")
    @Expose
    @Valid
    private PInterfaceInstanceList pInterfaceInstanceList;

    public String getModelInvariantId() {
        return modelInvariantId;
    }
    public String getPnfId() {
        return pnfId;
    }

    public void setPnfId(String pnfId) {
        this.pnfId = pnfId;
    }

    public String getPnfName() {
        return pnfName;
    }

    public void setPnfName(String name) {
        this.pnfName = name;
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

    public void setNfRole(String networkRole) {
        this.nfRole = networkRole;
    }
    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getPnfName2() {
        return pnfName2;
    }

    public void setPnfName2(String name2) {
        this.pnfName2 = name2;
    }

    public String getPnfName2Source() {
        return pnfName2Source;
    }

    public void setPnfName2Source(String name2Source) {
        this.pnfName2Source = name2Source;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentVendor() {
        return equipmentVendor;
    }

    public void setEquipmentVendor(String equipmentVendor) {
        this.equipmentVendor = equipmentVendor;
    }

    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    public String getManagementOptions() {
        return managementOptions;
    }

    public void setManagementOptions(String managementOptions) {
        this.managementOptions = managementOptions;
    }

    public String getSwVersion() {
        return swVersion;
    }

    public void setSwVersion(String swVersion) {
        this.swVersion = swVersion;
    }

    public String getFrameId() {
        return frameId;
    }

    public void setFrameId(String frameId) {
        this.frameId = frameId;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
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

    public PInterfaceInstanceList getPInterfaceInstanceList() {
        return pInterfaceInstanceList;
    }

    public void setPInterfaceInstanceList(PInterfaceInstanceList pInterfaceInstanceList) {
        this.pInterfaceInstanceList = pInterfaceInstanceList;
    }

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public String toJson() {
        return gson.toJson(this);
    }


    public static PnfInstance fromJson(String payload) throws AuditException {
        try {
            if (payload == null || payload.isEmpty()) {
                throw new AuditException("Empty Json response");
            }
            return gson.fromJson(payload, PnfInstance.class);
        } catch (Exception ex) {
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR, ex);
        }
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public PnfInstance() {
    }

    /**
    *
    * @param uuid
    * @param name
    * @param networkRole
    * @param name2
    * @param name2Source
    * @param equipmentType
    * @param equipmentVendor
    * @param equipmentModel
    * @param managementOptions
    * @param swVersion
    * @param frameId
    * @param serialNumber
    * @param modelInvariantId
    * @param modelVersionId
    * @param pInterfaceInstanceList
    *
    */
   public PnfInstance(String uuid, String name, String networkRole, String name2, String name2Source,String equipmentType,String equipmentVendor,String equipmentModel,String managementOptions,String swVersion, String frameId, String serialNumber, String modelInvariantId, String modelVersionId, PInterfaceInstanceList pInterfaceInstanceList) {
       super();
       this.pnfId = uuid;
       this.pnfName = name;
       this.nfRole = networkRole;
       this.pnfName2 = name2;
       this.pnfName2Source = name2Source;
       this.equipmentType = equipmentType;
       this.equipmentVendor = equipmentVendor;
       this.equipmentModel = equipmentModel;
       this.managementOptions = managementOptions;
       this.swVersion = swVersion;
       this.frameId = frameId;
       this.serialNumber = serialNumber;
       this.modelInvariantId = modelInvariantId;
       this.modelVersionId = modelVersionId;
       this.pInterfaceInstanceList = pInterfaceInstanceList;
   }



    /////////// common functions //////////////////////
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uuid", pnfId)
                .append("name", pnfName)
                .append("networkRole", nfRole)
                .append("name2", pnfName2)
                .append("name2Source", pnfName2Source)
                .append("equipmentType", equipmentType)
                .append("equipmentVendor", equipmentVendor)
                .append("equipmentModel", equipmentModel)
                .append("managementOptions", managementOptions)
                .append("swVersion", swVersion)
                .append("frameId", frameId)
                .append("serialNumber", serialNumber)
                .append("modelInvariantId", modelInvariantId)
                .append("modelVersionId", modelVersionId)
                .append("pInterfaceInstanceList", pInterfaceInstanceList)
               .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pnfId).append(frameId).append(serialNumber).append(modelInvariantId).append(modelVersionId).append(pInterfaceInstanceList).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PnfInstance)) {
            return false;
        }
        PnfInstance rhs = ((PnfInstance) other);
        return new EqualsBuilder()
                .append(pnfId, rhs.pnfId)
                .append(pnfName, rhs.pnfName)
                .append(pnfName2, rhs.pnfName2)
                .append(equipmentType, rhs.equipmentType)
                .append(equipmentModel, rhs.equipmentModel)
                .append(frameId, rhs.frameId)
                .append(serialNumber, rhs.serialNumber)
                .isEquals();
    }
}
