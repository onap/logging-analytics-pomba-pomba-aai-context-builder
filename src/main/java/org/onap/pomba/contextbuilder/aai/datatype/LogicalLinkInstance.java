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

public class LogicalLinkInstance {
    @SerializedName("link-name")
    @Expose
    private String linkName;
    @SerializedName("link-id")
    @Expose
    private String linkId;
    @SerializedName("model-version-id")
    @Expose
    private String modelVersionId;
    @SerializedName("model-invariant-id")
    @Expose
    private String modelInvariantId;
    @SerializedName("in-maint")
    @Expose
    private String inMaint;
    @SerializedName("link-type")
    @Expose
    private String linkType;
    @SerializedName("routing-protocol")
    @Expose
    private String routingProtocol;
    @SerializedName("speed-value")
    @Expose
    private String speedValue;
    @SerializedName("speed-units")
    @Expose
    private String speedUnits;
    @SerializedName("ip-version")
    @Expose
    private String ipVersion;
    @SerializedName("prov-status")
    @Expose
    private String provStatus;
    @SerializedName("link-role")
    @Expose
    private String linkRole;
    @SerializedName("link-name2")
    @Expose
    private String linkName2;
    @SerializedName("circuit-id")
    @Expose
    @Valid
    private String circuitId;
    @SerializedName("purpose")
    @Expose
    @Valid
    private String purpose;


    public String getLinkName() {
        return linkName;
    }


    public void setLinkName(String linkName) {
        this.linkName = linkName;
    }


    public String getLinkId() {
        return linkId;
    }


    public void setLinkId(String linkId) {
        this.linkId = linkId;
    }


    public String getModelVersionId() {
        return modelVersionId;
    }


    public void setModelVersionId(String modelVersionId) {
        this.modelVersionId = modelVersionId;
    }


    public String getModelInvariantId() {
        return modelInvariantId;
    }


    public void setModelInvariantId(String modelInvariantId) {
        this.modelInvariantId = modelInvariantId;
    }


    public String getInMaint() {
        return inMaint;
    }


    public void setInMaint(String inMaint) {
        this.inMaint = inMaint;
    }


    public String getLinkType() {
        return linkType;
    }


    public void setLinkType(String linkType) {
        this.linkType = linkType;
    }


    public String getRoutingProtocol() {
        return routingProtocol;
    }


    public void setRoutingProtocol(String routingProtocol) {
        this.routingProtocol = routingProtocol;
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


    public String getIpVersion() {
        return ipVersion;
    }


    public void setIpVersion(String ipVersion) {
        this.ipVersion = ipVersion;
    }


    public String getProvStatus() {
        return provStatus;
    }


    public void setProvStatus(String provStatus) {
        this.provStatus = provStatus;
    }


    public String getLinkRole() {
        return linkRole;
    }


    public void setLinkRole(String linkRole) {
        this.linkRole = linkRole;
    }


    public String getLinkName2() {
        return linkName2;
    }


    public void setLinkName2(String linkName2) {
        this.linkName2 = linkName2;
    }


    public String getCircuitId() {
        return circuitId;
    }


    public void setCircuitId(String circuitId) {
        this.circuitId = circuitId;
    }


    public String getPurpose() {
        return purpose;
    }


    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public String toJson() {
        return gson.toJson(this);
    }


    public static LogicalLinkInstance fromJson(String payload) throws AuditException {
        try {
            if (payload == null || payload.isEmpty()) {
                throw new AuditException("Empty Json response");
            }
            return gson.fromJson(payload, LogicalLinkInstance.class);
        } catch (Exception ex) {
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR, ex);
        }
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public LogicalLinkInstance() {
    }

    /**
    *
    * @param linkName
    * @param linkId
    * @param modelVersionId
    * @param modelInvariantId
    * @param linkType
    * @param routingProtocol
    * @param speedValue
    * @param speedUnits
    * @param provStatus
    * @param inMaint
    * @param linkRole
    * @param ipVersion
    * @param linkName2
    *
    */
   public LogicalLinkInstance(String linkName, String linkId, String modelVersionId,
           String modelInvariantId, String linkType,String routingProtocol,
           String speedValue,String speedUnits,String provStatus,
           String inMaint, String linkRole, String ipVersion,
           String linkName2, String circuitId, String purpose) {
       super();
       this.linkName = linkName;
       this.linkId = linkId;
       this.modelVersionId = modelVersionId;
       this.modelInvariantId = modelInvariantId;
       this.linkType = linkType;
       this.routingProtocol = routingProtocol;
       this.speedValue = speedValue;
       this.speedUnits = speedUnits;
       this.provStatus = provStatus;
       this.inMaint = inMaint;
       this.linkRole = linkRole;
       this.ipVersion = ipVersion;
       this.linkName2 = linkName2;
       this.circuitId = circuitId;
       this.purpose = purpose;
   }



    /////////// common functions //////////////////////
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("linkName", linkName)
                .append("linkId", linkId)
                .append("modelInvariantId", modelInvariantId)
                .append("modelVersionId", modelVersionId)
                .append("linkType", linkType)
                .append("routingProtocol", routingProtocol)
                .append("speedValue", speedValue)
                .append("speedUnits", speedUnits)
                .append("ipVersion", ipVersion)
                .append("provStatus", provStatus)
                .append("in-maint", inMaint)
                .append("linkRole", linkRole)
                .append("linkName2", linkName2)
                .append("circuitId", circuitId)
                .append("purpose", purpose)
               .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(linkName).append(ipVersion).append(speedUnits).append(speedValue).append(circuitId).append(purpose).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LogicalLinkInstance)) {
            return false;
        }
        LogicalLinkInstance rhs = ((LogicalLinkInstance) other);
        return new EqualsBuilder()
                .append(linkName, rhs.linkName)
                .append(linkId, rhs.linkId)
                .append(speedUnits, rhs.speedUnits)
                .append(ipVersion, rhs.ipVersion)
                .append(linkType, rhs.linkType)
                .append(speedValue, rhs.speedValue)
                .append(modelInvariantId, rhs.modelInvariantId)
                .append(circuitId, rhs.circuitId)
                .append(linkRole, rhs.linkRole)
                .append(linkName2, rhs.linkName2)
                .append(purpose, rhs.purpose)
                .isEquals();
    }
}
