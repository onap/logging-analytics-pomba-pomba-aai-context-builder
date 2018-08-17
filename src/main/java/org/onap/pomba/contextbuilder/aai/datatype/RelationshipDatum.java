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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class RelationshipDatum {

    @SerializedName("relationship-key")
    @Expose
    private String relationshipKey;
    @SerializedName("relationship-value")
    @Expose
    private String relationshipValue;

    /**
     * No args constructor for use in serialization
     *
     */
    public RelationshipDatum() {
    }

    /**
     *
     * @param relationshipValue
     * @param relationshipKey
     */
    public RelationshipDatum(String relationshipKey, String relationshipValue) {
        super();
        this.relationshipKey = relationshipKey;
        this.relationshipValue = relationshipValue;
    }

    public String getRelationshipKey() {
        return relationshipKey;
    }

    public void setRelationshipKey(String relationshipKey) {
        this.relationshipKey = relationshipKey;
    }

    public String getRelationshipValue() {
        return relationshipValue;
    }

    public void setRelationshipValue(String relationshipValue) {
        this.relationshipValue = relationshipValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("relationshipKey", relationshipKey).append("relationshipValue", relationshipValue).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(relationshipValue).append(relationshipKey).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RelationshipDatum)) {
            return false;
        }
        RelationshipDatum rhs = ((RelationshipDatum) other);
        return new EqualsBuilder().append(relationshipValue, rhs.relationshipValue).append(relationshipKey, rhs.relationshipKey).isEquals();
    }

}
