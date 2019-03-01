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

package org.onap.pomba.contextbuilder.aai.test.datatype;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.onap.pomba.contextbuilder.aai.datatype.VfModule;
import org.onap.pomba.contextbuilder.aai.datatype.VfModules;

public class VfModulesTest {
    @Test
    public void testVfModules() {
        VfModules vfModules = new VfModules();

        List<VfModule> vfModuleList = new ArrayList<VfModule>();
        vfModuleList.add(new VfModule());
        vfModules.setVfModule(vfModuleList);

        assertTrue(vfModules.getVfModule().get(0) instanceof VfModule);
    }

    @Test
    public void testVfModulesWithParameters() {
        VfModules vfModules = new VfModules(new ArrayList<VfModule>());

        String vfModulesString = vfModules.toString();
        assertTrue(vfModulesString.contains("[vfModule="));
    }

    @Test
    public void testVfModulesEquals() {
        VfModules vfModules1 = new VfModules(new ArrayList<VfModule>());
        VfModules vfModules2 = new VfModules(new ArrayList<VfModule>());

        assertTrue(vfModules1.equals(vfModules1));
        assertTrue(vfModules1.equals(vfModules2));
    }
}
