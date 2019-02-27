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

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.onap.pomba.contextbuilder.aai.exception.AuditError;
import org.onap.pomba.contextbuilder.aai.exception.AuditException;

public class LInterfaceInstance {

    @SerializedName("interface-id")
    @Expose
    private String interfaceId;

    @SerializedName("interface-name")
    @Expose
    private String interfaceName;
    @SerializedName("interface-role")
    @Expose
    private String interfaceRole;
    @SerializedName("is-port-mirrored")
    @Expose
    private String isPortMirrored;
    @SerializedName("admin-status")
    @Expose
    private String adminStatus;
    @SerializedName("network-name")
    @Expose
    private String networkName;
    @SerializedName("macaddr")
    @Expose
    private String macAddr;
    @SerializedName("in-maint")
    @Expose
    private String inMaint;
    @SerializedName("relationship-list")
    @Expose
    private RelationshipList relationshipList;

    private List<LogicalLinkInstance> logicalLinkInstanceList;
    public List<LogicalLinkInstance> getLogicalLinkInstanceList() {
        return logicalLinkInstanceList;
    }

    public void setLogicalLinkInstanceList(List<LogicalLinkInstance> logicalLinkInstanceList) {
        this.logicalLinkInstanceList = logicalLinkInstanceList;
    }

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public String toJson() {
        return gson.toJson(this);
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getInterfaceRole() {
        return interfaceRole;
    }

    public void setInterfaceRole(String interfaceRole) {
        this.interfaceRole = interfaceRole;
    }

    public String getIsPortMirrored() {
        return isPortMirrored;
    }

    public void setIsPortMirrored(String isPortMirrored) {
        this.isPortMirrored = isPortMirrored;
    }

    public String getAdminStatus() {
        return adminStatus;
    }

    public void setAdminStatus(String adminStatus) {
        this.adminStatus = adminStatus;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public String getMacAddr() {
        return macAddr;
    }

    public void setMacAddr(String macAddr) {
        this.macAddr = macAddr;
    }

    public String getInMaint() {
        return inMaint;
    }

    public void setInMaint(String inMaint) {
        this.inMaint = inMaint;
    }

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }

    public static LInterfaceInstance fromJson(String payload) throws AuditException {
        try {
            if (payload == null || payload.isEmpty()) {
                throw new AuditException("Empty Json response");
            }
            return gson.fromJson(payload, LInterfaceInstance.class);
        } catch (Exception ex) {
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR, ex);
        }
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public LInterfaceInstance() {
    }

    /**
    *
    * @param interfaceId
    * @param interfaceName
    * @param interfaceRole
    * @param isPortMirrored
    * @param adminStatus
    * @param networkName
    * @param macAddr
    * @param inMaint
    *
    */
   public LInterfaceInstance(String interfaceId, String interfaceName, String interfaceRole,
           String isPortMirrored, String adminStatus, String networkName,
           String macAddr,String inMaint, RelationshipList relationshipList
           ) {
       super();
       this.interfaceId = interfaceId;
       this.interfaceName = interfaceName;
       this.interfaceRole = interfaceRole;
       this.isPortMirrored = isPortMirrored;
       this.adminStatus = adminStatus;
       this.networkName = networkName;
       this.macAddr = macAddr;
       this.inMaint = inMaint;
       this.relationshipList = relationshipList;
   }



    /////////// common functions //////////////////////
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("interfaceId", interfaceId)
                .append("interfaceName", interfaceName)
                .append("interfaceRole", interfaceRole)
                .append("isPortMirrored", isPortMirrored)
                .append("adminStatus", adminStatus)
                .append("networkName", networkName)
                .append("macAddr", macAddr)
                .append("inMaint", inMaint)
                .append("relationshipList", relationshipList)
               .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(interfaceId)
                .append(interfaceName)
                .append(interfaceRole)
                .append(isPortMirrored)
                .append(adminStatus)
                .append(networkName)
                .append(macAddr)
                .append(inMaint)
                .append(relationshipList)
                .toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LInterfaceInstance)) {
            return false;
        }
        LInterfaceInstance rhs = ((LInterfaceInstance) other);
        return new EqualsBuilder()
                .append(interfaceId, rhs.interfaceId)
                .append(interfaceName, rhs.interfaceName)
                .append(interfaceRole, rhs.interfaceRole)
                .append(isPortMirrored, rhs.isPortMirrored)
                .append(adminStatus, rhs.adminStatus)
                .append(networkName, rhs.networkName)
                .append(macAddr, rhs.macAddr)
                .append(inMaint, rhs.inMaint)
                .append(relationshipList, rhs.relationshipList)
               .isEquals();
    }
}
