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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.onap.pomba.contextbuilder.aai.exception.AuditError;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;

public class PInterfaceInstance {

    @SerializedName("interface-name")
    @Expose
    private String interfaceName;
    @SerializedName("speed-value")
    @Expose
    private String speedValue;
    @SerializedName("speed-units")
    @Expose
    private String speedUnits;
    @SerializedName("port-description")
    @Expose
    private String portDescription;
    @SerializedName("equipment-identifier")
    @Expose
    private String equipmentIdentifier;
    @SerializedName("interface-role")
    @Expose
    private String interfaceRole;
    @SerializedName("interface-type")
    @Expose
    private String interfaceType;
    @SerializedName("prov-status")
    @Expose
    private String provStatus;
    @SerializedName("resource-version")
    @Expose
    private String resourceVersion;
    @SerializedName("in-maint")
    @Expose
    private String inMaint;
    @SerializedName("inv-status")
    @Expose
    private String invStatus;

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getSpeedValue() {
        return speedValue;
    }

    public void setSpeedValue(String speedValue) {
        this.speedValue = speedValue;
    }

    public String getSpeedUnits() {
        return speedUnits;
    }

    public void setSpeedUnits(String speedUnits) {
        this.speedUnits = speedUnits;
    }

    public String getPortDescription() {
        return portDescription;
    }

    public void setPortDescription(String portDescription) {
        this.portDescription = portDescription;
    }

    public String getEquipmentIdentifier() {
        return equipmentIdentifier;
    }

    public void setEquipmentIdentifier(String equipmentIdentifier) {
        this.equipmentIdentifier = equipmentIdentifier;
    }

    public String getInterfaceRole() {
        return interfaceRole;
    }

    public void setInterfaceRole(String interfaceRole) {
        this.interfaceRole = interfaceRole;
    }

    public String getInterfaceType() {
        return interfaceType;
    }

    public void setInterfaceType(String interfaceType) {
        this.interfaceType = interfaceType;
    }

    public String getProvStatus() {
        return provStatus;
    }

    public void setProvStatus(String provStatus) {
        this.provStatus = provStatus;
    }

    public String getResourceVersion() {
        return resourceVersion;
    }

    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    public String getInMaint() {
        return inMaint;
    }

    public void setInMaint(String inMaint) {
        this.inMaint = inMaint;
    }

    public String getInvStatus() {
        return invStatus;
    }

    public void setInvStatus(String invStatus) {
        this.invStatus = invStatus;
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public PInterfaceInstance() {
    }

    /**
     *
     * @param interfaceName
     * @param speedValue
     * @param speedUnits
     * @param portDescription
     * @param equipmentIdentifier
     * @param interfaceRole
     * @param interfaceType
     * @param provStatus
     * @param resourceVersion
     * @param inMaint
     * @param invStatus
     */
    public PInterfaceInstance(String interfaceName,String speedValue,String speedUnits,
            String portDescription,String equipmentIdentifier,String interfaceRole,String interfaceType,
            String provStatus,String resourceVersion,String  inMaint, String invStatus ) {
        super();
        this.interfaceName   = interfaceName;
        this.speedValue      = speedValue;
        this.speedUnits      = speedUnits;
        this.portDescription = portDescription;
        this.equipmentIdentifier = equipmentIdentifier;
        this.provStatus      = provStatus;
        this.resourceVersion = resourceVersion;
        this.inMaint = inMaint;
        this.invStatus = invStatus;
    }

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public String toJson() {
        return gson.toJson(this);
    }

    public static PInterfaceInstance fromJson(String payload) throws AuditException {
        try {
            if (payload == null || payload.isEmpty()) {
                throw new AuditException("Empty Json response");
            }
            return gson.fromJson(payload, PInterfaceInstance.class);
        } catch (Exception ex) {
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR, ex);
        }
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("interfaceName", interfaceName).append("speedValue", speedValue).append("speedUnits", speedUnits)
                .append("portDescription", portDescription).append("equipmentIdentifier", equipmentIdentifier).append("interfaceRole", interfaceRole)
                .append("interfaceType", interfaceType).append("provStatus", provStatus).append("resourceVersion", resourceVersion)
                .append("inMaint", inMaint).append("invStatus", invStatus)
                .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(speedValue).append(equipmentIdentifier).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PInterfaceInstance)) {
            return false;
        }
        PInterfaceInstance rhs = ((PInterfaceInstance) other);
        return new EqualsBuilder().append(interfaceName, rhs.interfaceName).append(speedValue, rhs.speedValue)
                .append(speedUnits, rhs.speedUnits).append(equipmentIdentifier, rhs.equipmentIdentifier)
                .append(interfaceRole, rhs.interfaceRole).append(interfaceType, rhs.interfaceType)
                .append(inMaint, rhs.inMaint)
                .isEquals();
    }

}
