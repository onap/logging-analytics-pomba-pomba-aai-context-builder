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

public class PserverInstance {

    @SerializedName("pserver-id")
    @Expose
    private String pserverId;
    @SerializedName("hostname")
    @Expose
    private String hostname;
    @SerializedName("pserver-name2")
    @Expose
    private String pserverName2;
    @SerializedName("ptnii-equip-name")
    @Expose
    private String ptniiEquipName;
    @SerializedName("equip-type")
    @Expose
    private String equipType;
    @SerializedName("equip-vendor")
    @Expose
    private String equipVendor;
    @SerializedName("equip-model")
    @Expose
    private String equipModel;
    @SerializedName("fqdn")
    @Expose
    private String fqdn;
    @SerializedName("serial-number")
    @Expose
    private String serialNumber;
    @SerializedName("internet-topology")
    @Expose
    private String internetTopology;
    @SerializedName("in-maint")
    @Expose
    private String inMaint;
    @SerializedName("resource-version")
    @Expose
    private String resourceVersion;
    @SerializedName("purpose")
    @Expose
    private String purpose;

    public String getPserverId() {
        return pserverId;
    }

    public void setPserverId(String pserverId) {
        this.pserverId = pserverId;
    }


    public String getHostname() {
        return hostname;
    }


    public void setHostname(String hostname) {
        this.hostname = hostname;
    }


    public String getPserverName2() {
        return pserverName2;
    }


    public void setPserverName2(String pserverName2) {
        this.pserverName2 = pserverName2;
    }


    public String getPtniiEquipName() {
        return ptniiEquipName;
    }


    public void setPtniiEquipName(String ptniiEquipName) {
        this.ptniiEquipName = ptniiEquipName;
    }


    public String getEquipType() {
        return equipType;
    }


    public void setEquipType(String equipType) {
        this.equipType = equipType;
    }


    public String getEquipVendor() {
        return equipVendor;
    }


    public void setEquipVendor(String equipVendor) {
        this.equipVendor = equipVendor;
    }


    public String getEquipModel() {
        return equipModel;
    }


    public void setEquipModel(String equipModel) {
        this.equipModel = equipModel;
    }


    public String getFqdn() {
        return fqdn;
    }


    public void setFqdn(String fqdn) {
        this.fqdn = fqdn;
    }


    public String getSerialNumber() {
        return serialNumber;
    }


    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }


    public String getInternetTopology() {
        return internetTopology;
    }


    public void setInternetTopology(String internetTopology) {
        this.internetTopology = internetTopology;
    }


    public String getInMaint() {
        return inMaint;
    }


    public void setInMaint(String inMaint) {
        this.inMaint = inMaint;
    }


    public String getResourceVersion() {
        return resourceVersion;
    }


    public void setResourceVersion(String resourceVersion) {
        this.resourceVersion = resourceVersion;
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


    public static PserverInstance fromJson(String payload) throws AuditException {
        try {
            if (payload == null || payload.isEmpty()) {
                throw new AuditException("Empty Json response");
            }
            return gson.fromJson(payload, PserverInstance.class);
        } catch (Exception ex) {
            throw new AuditException(AuditError.JSON_READER_PARSE_ERROR, ex);
        }
    }

    /**
     * No args constructor for use in serialization
     *
     */
    public PserverInstance() {
    }

    /**
    *
    * @param pserverId
    * @param hostname
    * @param pserverName2
    * @param ptniiEquipName
    * @param equipType
    * @param equipVendor
    * @param equipModel
    * @param fqdn
    * @param internetTopology
    * @param inMaint
    * @param resourceVersion
    * @param serialNumber
    * @param purpose
    *
    */
   public PserverInstance(String pserverId, String hostname, String pserverName2,
           String ptniiEquipName, String equipType,String equipVendor,
           String equipModel,String fqdn,String internetTopology,
           String inMaint, String resourceVersion, String serialNumber,
           String purpose, String relationshipList) {
       super();
       this.pserverId = pserverId;
       this.hostname = hostname;
       this.pserverName2 = pserverName2;
       this.ptniiEquipName = ptniiEquipName;
       this.equipType = equipType;
       this.equipVendor = equipVendor;
       this.equipModel = equipModel;
       this.fqdn = fqdn;
       this.internetTopology = internetTopology;
       this.inMaint = inMaint;
       this.resourceVersion = resourceVersion;
       this.serialNumber = serialNumber;
       this.purpose = purpose;
   }



    /////////// common functions //////////////////////
    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("pserver-id", pserverId)
                .append("hostname", hostname)
                .append("ptnii-equip-name", ptniiEquipName)
                .append("pserver-name2", pserverName2)
                .append("equip-type", equipType)
                .append("equip-vendor", equipVendor)
                .append("equip-model", equipModel)
                .append("fqdn", fqdn)
                .append("serial-number", serialNumber)
                .append("internet-topology", internetTopology)
                .append("in-maint", inMaint)
                .append("resource-version", resourceVersion)
                .append("purpose", purpose)
               .toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(pserverId).append(serialNumber).append(fqdn).append(equipModel).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof PserverInstance)) {
            return false;
        }
        PserverInstance rhs = ((PserverInstance) other);
        return new EqualsBuilder()
                .append(pserverId, rhs.pserverId)
                .append(hostname, rhs.hostname)
                .append(fqdn, rhs.fqdn)
                .append(serialNumber, rhs.serialNumber)
                .append(equipType, rhs.equipType)
                .append(equipModel, rhs.equipModel)
                .append(ptniiEquipName, rhs.ptniiEquipName)
                .isEquals();
    }
}
