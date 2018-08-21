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
package org.onap.logging_analytics.pomba.pomba_aai_context_builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipList;
import org.onap.pomba.contextbuilder.aai.datatype.VfModules;
import org.onap.pomba.contextbuilder.aai.datatype.VnfInstance;

public class VnfInstanceTest {
	@Test
	public void testVnfInstance() {
		VnfInstance vnfInstance = new VnfInstance();

		vnfInstance.setServiceId("serviceId");
		vnfInstance.setModelCustomizationId("modelCustomizationId");
		vnfInstance.setVnfType("vnfType");
		vnfInstance.setIpv4Loopback0Address("ipv4Loopback0Address");
		vnfInstance.setNfFunction("nfFunction");
		vnfInstance.setModelInvariantId("modelInvariantId");
		vnfInstance.setResourceVersion("resourceVersion");
		vnfInstance.setVnfName2("vnfName2");
		vnfInstance.setRelationshipList(new RelationshipList());
		vnfInstance.setNmLanV6Address("nmLanV6Address");
		vnfInstance.setNfRole("nfRole");
		vnfInstance.setNfType("nfType");
		vnfInstance.setModelVersionId("modelVersionId");
		vnfInstance.setIpv4OamAddress("ipv4OamAddress");
		vnfInstance.setVnfName("vnfName");
		vnfInstance.setInMaintenance(true);
		vnfInstance.setMsoCatalogKey("msoCatalogKey");
		vnfInstance.setProvisionStatus("provisionStatus");
		vnfInstance.setVfModules(new VfModules());
		vnfInstance.setEquipmentRole("equipmentRole");
		vnfInstance.setVnfId("vnfId");
		vnfInstance.setOrchestrationStatus("orchestrationStatus");
		vnfInstance.setNfNamingCode("nfNamingCode");
		vnfInstance.setHeatStackId("heatStackId");
		vnfInstance.setIsClosedLoopDisabled(true);
		vnfInstance.setLicenseKey("licenseKey");
		vnfInstance.setManagementV6Address("managementV6Address");

		assertEquals("serviceId", vnfInstance.getServiceId());
		assertEquals("modelCustomizationId", vnfInstance.getModelCustomizationId());
		assertEquals("vnfType", vnfInstance.getVnfType());
		assertEquals("ipv4Loopback0Address", vnfInstance.getIpv4Loopback0Address());
		assertEquals("nfFunction", vnfInstance.getNfFunction());
		assertEquals("modelInvariantId", vnfInstance.getModelInvariantId());
		assertEquals("resourceVersion", vnfInstance.getResourceVersion());
		assertEquals("vnfName2", vnfInstance.getVnfName2());
		assertTrue(vnfInstance.getRelationshipList() instanceof RelationshipList);
		assertEquals("nmLanV6Address", vnfInstance.getNmLanV6Address());
		assertEquals("nfRole", vnfInstance.getNfRole());
		assertEquals("nfType", vnfInstance.getNfType());
		assertEquals("modelVersionId", vnfInstance.getModelVersionId());
		assertEquals("ipv4OamAddress", vnfInstance.getIpv4OamAddress());
		assertEquals("vnfName", vnfInstance.getVnfName());
		assertTrue(vnfInstance.getInMaintenance());
		assertEquals("msoCatalogKey", vnfInstance.getMsoCatalogKey());
		assertEquals("provisionStatus", vnfInstance.getProvisionStatus());
		assertTrue(vnfInstance.getVfModules() instanceof VfModules);
		assertEquals("equipmentRole", vnfInstance.getEquipmentRole());
		assertEquals("vnfId", vnfInstance.getVnfId());
		assertEquals("orchestrationStatus", vnfInstance.getOrchestrationStatus());
		assertEquals("nfNamingCode", vnfInstance.getNfNamingCode());
		assertEquals("heatStackId", vnfInstance.getHeatStackId());
		assertTrue(vnfInstance.getIsClosedLoopDisabled());
		assertEquals("licenseKey", vnfInstance.getLicenseKey());
		assertEquals("managementV6Address", vnfInstance.getManagementV6Address());

	}

	@Test
	public void testVnfInstanceWithParameters() {
		VnfInstance vnfInstance = new VnfInstance("vnfId", "vnfName", "vnfName2", "vnfType", "serviceId", "provisionStatus", "licenseKey", "equipmentRole", "orchestrationStatus", "heatStackId", "msoCatalogKey", "ipv4OamAddress", "ipv4Loopback0Address", "nmLanV6Address", "managementV6Address", true, true, "resourceVersion", "modelInvariantId", "modelVersionId", "modelCustomizationId", "nfType", "nfFunction", "nfRole", "nfNamingCode", new RelationshipList(), new VfModules());

		assertEquals("serviceId", vnfInstance.getServiceId());
		assertEquals("modelCustomizationId", vnfInstance.getModelCustomizationId());
		assertEquals("vnfType", vnfInstance.getVnfType());
		assertEquals("ipv4Loopback0Address", vnfInstance.getIpv4Loopback0Address());
		assertEquals("nfFunction", vnfInstance.getNfFunction());
		assertEquals("modelInvariantId", vnfInstance.getModelInvariantId());
		assertEquals("resourceVersion", vnfInstance.getResourceVersion());
		assertEquals("vnfName2", vnfInstance.getVnfName2());
		assertTrue(vnfInstance.getRelationshipList() instanceof RelationshipList);
		assertEquals("nmLanV6Address", vnfInstance.getNmLanV6Address());
		assertEquals("nfRole", vnfInstance.getNfRole());
		assertEquals("nfType", vnfInstance.getNfType());
		assertEquals("modelVersionId", vnfInstance.getModelVersionId());
		assertEquals("ipv4OamAddress", vnfInstance.getIpv4OamAddress());
		assertEquals("vnfName", vnfInstance.getVnfName());
		assertTrue(vnfInstance.getInMaintenance());
		assertEquals("msoCatalogKey", vnfInstance.getMsoCatalogKey());
		assertEquals("provisionStatus", vnfInstance.getProvisionStatus());
		assertTrue(vnfInstance.getVfModules() instanceof VfModules);
		assertEquals("equipmentRole", vnfInstance.getEquipmentRole());
		assertEquals("vnfId", vnfInstance.getVnfId());
		assertEquals("orchestrationStatus", vnfInstance.getOrchestrationStatus());
		assertEquals("nfNamingCode", vnfInstance.getNfNamingCode());
		assertEquals("heatStackId", vnfInstance.getHeatStackId());
		assertTrue(vnfInstance.getIsClosedLoopDisabled());
		assertEquals("licenseKey", vnfInstance.getLicenseKey());
		assertEquals("managementV6Address", vnfInstance.getManagementV6Address());

		String vnfInstanceString = vnfInstance.toString();
		assertTrue(vnfInstanceString.contains("[vnfId=vnfId,vnfName=vnfName,vnfName2=vnfName2,vnfType=vnfType,serviceId=serviceId,provisionStatus=provisionStatus,licenseKey=licenseKey,equipmentRole=equipmentRole,orchestrationStatus=orchestrationStatus,heatStackId=heatStackId,msoCatalogKey=msoCatalogKey,ipv4OamAddress=ipv4OamAddress,ipv4Loopback0Address=ipv4Loopback0Address,nmLanV6Address=nmLanV6Address,managementV6Address=managementV6Address,inMaintenance=true,isClosedLoopDisabled=true,resourceVersion=resourceVersion,modelInvariantId=modelInvariantId,modelVersionId=modelVersionId,modelCustomizationId=modelCustomizationId,nfType=nfType,nfFunction=nfFunction,nfRole=nfRole,nfNamingCode=nfNamingCode,"));
	}

	@Test
	public void testVnfInstanceEquals() {
		VnfInstance vnfInstance1 = new VnfInstance("vnfId1", "vnfName1", "vnfName2-1", "vnfType1", "serviceId1", "provisionStatus1", "licenseKey1", "equipmentRole1", "orchestrationStatus1", "heatStackId1", "msoCatalogKey1", "ipv4OamAddress1", "ipv4Loopback0Address1", "nmLanV6Address1", "managementV6Address1", true, true, "resourceVersion1", "modelInvariantId1", "modelVersionId1", "modelCustomizationId1", "nfType1", "nfFunction1", "nfRole1", "nfNamingCode1", new RelationshipList(), new VfModules());
		VnfInstance vnfInstance2 = new VnfInstance("vnfId2", "vnfName2", "vnfName2-2", "vnfType2", "serviceId2", "provisionStatus2", "licenseKey2", "equipmentRole2", "orchestrationStatus2", "heatStackId2", "msoCatalogKey2", "ipv4OamAddress2", "ipv4Loopback0Address2", "nmLanV6Address2", "managementV6Address2", true, true, "resourceVersion2", "modelInvariantId2", "modelVersionId2", "modelCustomizationId2", "nfType2", "nfFunction2", "nfRole2", "nfNamingCode2", new RelationshipList(), new VfModules());
		VnfInstance vnfInstance3 = new VnfInstance("vnfId1", "vnfName1", "vnfName2-1", "vnfType1", "serviceId1", "provisionStatus1", "licenseKey1", "equipmentRole1", "orchestrationStatus1", "heatStackId1", "msoCatalogKey1", "ipv4OamAddress1", "ipv4Loopback0Address1", "nmLanV6Address1", "managementV6Address1", true, true, "resourceVersion1", "modelInvariantId1", "modelVersionId1", "modelCustomizationId1", "nfType1", "nfFunction1", "nfRole1", "nfNamingCode1", new RelationshipList(), new VfModules());

		assertTrue(vnfInstance1.equals(vnfInstance1));
		assertTrue(!vnfInstance1.equals(vnfInstance2));
		assertTrue(vnfInstance1.equals(vnfInstance3));

	}
}
