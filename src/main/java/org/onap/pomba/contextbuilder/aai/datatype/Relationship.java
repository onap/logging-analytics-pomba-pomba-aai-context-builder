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
import java.util.List;
import javax.validation.Valid;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class Relationship {

    @SerializedName("related-to")
    @Expose
    private String relatedTo;
    @SerializedName("related-link")
    @Expose
    private String relatedLink;
    @SerializedName("relationship-data")
    @Expose
    @Valid
    private List<RelationshipDatum> relationshipData = null;
    @SerializedName("related-to-property")
    @Expose
    @Valid
    private List<RelatedToProperty> relatedToProperty = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Relationship() {
    }

    /**
     *
     * @param relatedToProperty
     * @param relatedLink
     * @param relationshipData
     * @param relatedTo
     */
    public Relationship(String relatedTo, String relatedLink, List<RelationshipDatum> relationshipData, List<RelatedToProperty> relatedToProperty) {
        super();
        this.relatedTo = relatedTo;
        this.relatedLink = relatedLink;
        this.relationshipData = relationshipData;
        this.relatedToProperty = relatedToProperty;
    }

    public String getRelatedTo() {
        return relatedTo;
    }

    public void setRelatedTo(String relatedTo) {
        this.relatedTo = relatedTo;
    }

    public String getRelatedLink() {
        return relatedLink;
    }

    public void setRelatedLink(String relatedLink) {
        this.relatedLink = relatedLink;
    }

    public List<RelationshipDatum> getRelationshipData() {
        return relationshipData;
    }

    public void setRelationshipData(List<RelationshipDatum> relationshipData) {
        this.relationshipData = relationshipData;
    }

    public List<RelatedToProperty> getRelatedToProperty() {
        return relatedToProperty;
    }

    public void setRelatedToProperty(List<RelatedToProperty> relatedToProperty) {
        this.relatedToProperty = relatedToProperty;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("relatedTo", relatedTo).append("relatedLink", relatedLink).append("relationshipData", relationshipData).append("relatedToProperty", relatedToProperty).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(relatedToProperty).append(relatedLink).append(relationshipData).append(relatedTo).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Relationship)) {
            return false;
        }
        Relationship rhs = ((Relationship) other);
        return new EqualsBuilder().append(relatedToProperty, rhs.relatedToProperty).append(relatedLink, rhs.relatedLink).append(relationshipData, rhs.relationshipData).append(relatedTo, rhs.relatedTo).isEquals();
    }

}
