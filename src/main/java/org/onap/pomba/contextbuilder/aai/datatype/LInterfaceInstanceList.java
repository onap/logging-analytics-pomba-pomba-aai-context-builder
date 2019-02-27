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

public class LInterfaceInstanceList {

    @SerializedName("l-interface")
    @Expose
    @Valid
    private List<LInterfaceInstance> lInterfaceList = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public LInterfaceInstanceList() {
    }

    /**
     *
     * @param lInterfaceList
     */
    public LInterfaceInstanceList(List<LInterfaceInstance> lInterfaceList) {
        super();
        this.lInterfaceList = lInterfaceList;
    }

    public List<LInterfaceInstance> getLInterfaceList() {
        return lInterfaceList;
    }

    public void setLInterfaceList(List<LInterfaceInstance> lInterfaceList) {
        this.lInterfaceList = lInterfaceList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("lInterfaceList", lInterfaceList).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(lInterfaceList).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof LInterfaceInstanceList)) {
            return false;
        }
        LInterfaceInstanceList rhs = ((LInterfaceInstanceList) other);
        return new EqualsBuilder().append(lInterfaceList, rhs.lInterfaceList).isEquals();
    }

}
