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

public class RelationshipList {

    @SerializedName("relationship")
    @Expose
    @Valid
    private List<Relationship> relationship = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public RelationshipList() {
    }

    /**
     *
     * @param relationship
     */
    public RelationshipList(List<Relationship> relationship) {
        super();
        this.relationship = relationship;
    }

    public List<Relationship> getRelationship() {
        return relationship;
    }

    public void setRelationship(List<Relationship> relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("relationship", relationship).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(relationship).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof RelationshipList)) {
            return false;
        }
        RelationshipList rhs = ((RelationshipList) other);
        return new EqualsBuilder().append(relationship, rhs.relationship).isEquals();
    }

}
