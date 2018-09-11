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

public class Vserver {

    @SerializedName("vserver-id")
    @Expose
    private String vserverId;
    @SerializedName("vserver-name")
    @Expose
    private String vserverName;
    @SerializedName("vserver-name2")
    @Expose
    private String vserverName2;
    @SerializedName("prov-status")
    @Expose
    private String provStatus;
    @SerializedName("vserver-selflink")
    @Expose
    private String vserverSelflink;
    @SerializedName("in-maint")
    @Expose
    private Boolean inMaint;
    @SerializedName("is-closed-loop-disabled")
    @Expose
    private Boolean isClosedLoopDisabled;
    @SerializedName("resource-version")
    @Expose
    private String resourceVersion;
    @SerializedName("relationship-list")
    @Expose
    private RelationshipList relationshipList;

    private static final Gson gson = new GsonBuilder().disableHtmlEscaping().create();

    public String toJson() {
        return gson.toJson(this);
    }

    public static Vserver fromJson(String payload) throws AuditException {
        try {
            if (payload == null || payload.isEmpty()) {
                throw new AuditException("Empty Json response");
            }
            return gson.fromJson(payload, Vserver.class);
        } catch (Exception ex) {
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR, ex);
        }
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public Vserver() {
    }

    /**
     *
     * @param relationshipList
     * @param provStatus
     * @param inMaint
     * @param vserverName2
     * @param resourceVersion
     * @param vserverSelflink
     * @param vserverName
     * @param vserverId
     * @param isClosedLoopDisabled
     */
    public Vserver(String vserverId, String vserverName, String vserverName2, String provStatus, String vserverSelflink, Boolean inMaint, Boolean isClosedLoopDisabled, String resourceVersion, RelationshipList relationshipList) {
        super();
        this.vserverId = vserverId;
        this.vserverName = vserverName;
        this.vserverName2 = vserverName2;
        this.provStatus = provStatus;
        this.vserverSelflink = vserverSelflink;
        this.inMaint = inMaint;
        this.isClosedLoopDisabled = isClosedLoopDisabled;
        this.resourceVersion = resourceVersion;
        this.relationshipList = relationshipList;
    }

    public String getVserverId() {
        return vserverId;
    }

    public void setVserverId(String vserverId) {
        this.vserverId = vserverId;
    }

    public String getVserverName() {
        return vserverName;
    }

    public void setVserverName(String vserverName) {
        this.vserverName = vserverName;
    }

    public String getVserverName2() {
        return vserverName2;
    }

    public void setVserverName2(String vserverName2) {
        this.vserverName2 = vserverName2;
    }

    public String getProvStatus() {
        return provStatus;
    }

    public void setProvStatus(String provStatus) {
        this.provStatus = provStatus;
    }

    public String getVserverSelflink() {
        return vserverSelflink;
    }

    public void setVserverSelflink(String vserverSelflink) {
        this.vserverSelflink = vserverSelflink;
    }

    public Boolean getInMaint() {
        return inMaint;
    }

    public void setInMaint(Boolean inMaint) {
        this.inMaint = inMaint;
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

    public RelationshipList getRelationshipList() {
        return relationshipList;
    }

    public void setRelationshipList(RelationshipList relationshipList) {
        this.relationshipList = relationshipList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("vserverId", vserverId).append("vserverName", vserverName).append("vserverName2", vserverName2).append("provStatus", provStatus).append("vserverSelflink", vserverSelflink).append("inMaint", inMaint).append("isClosedLoopDisabled", isClosedLoopDisabled).append("resourceVersion", resourceVersion).append("relationshipList", relationshipList).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(relationshipList).append(provStatus).append(inMaint).append(vserverName2).append(resourceVersion).append(vserverSelflink).append(vserverName).append(vserverId).append(isClosedLoopDisabled).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Vserver) == false) {
            return false;
        }
        Vserver rhs = ((Vserver) other);
        return new EqualsBuilder().append(relationshipList, rhs.relationshipList).append(provStatus, rhs.provStatus).append(inMaint, rhs.inMaint).append(vserverName2, rhs.vserverName2).append(resourceVersion, rhs.resourceVersion).append(vserverSelflink, rhs.vserverSelflink).append(vserverName, rhs.vserverName).append(vserverId, rhs.vserverId).append(isClosedLoopDisabled, rhs.isClosedLoopDisabled).isEquals();
    }

}
