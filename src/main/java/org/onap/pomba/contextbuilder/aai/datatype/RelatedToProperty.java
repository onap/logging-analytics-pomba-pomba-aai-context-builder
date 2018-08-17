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

public class RelatedToProperty {

    @SerializedName("property-key")
    @Expose
    private String propertyKey;
    @SerializedName("property-value")
    @Expose
    private String propertyValue;

    /**
     * No args constructor for use in serialization
     *
     */
    public RelatedToProperty() {
    }

    /**
     *
     * @param propertyKey
     * @param propertyValue
     */
    public RelatedToProperty(String propertyKey, String propertyValue) {
        super();
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;
    }

    public String getPropertyKey() {
        return propertyKey;
    }

    public void setPropertyKey(String propertyKey) {
        this.propertyKey = propertyKey;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("propertyKey", propertyKey).append("propertyValue", propertyValue).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(propertyKey).append(propertyValue).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RelatedToProperty)) {
            return false;
        }
        RelatedToProperty rhs = ((RelatedToProperty) other);
        return new EqualsBuilder().append(propertyKey, rhs.propertyKey).append(propertyValue, rhs.propertyValue).isEquals();
    }

}
