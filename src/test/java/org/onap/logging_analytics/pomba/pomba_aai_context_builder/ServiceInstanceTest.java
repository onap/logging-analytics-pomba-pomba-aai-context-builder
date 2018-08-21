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

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.onap.pomba.contextbuilder.aai.datatype.RelatedToProperty;
import org.onap.pomba.contextbuilder.aai.datatype.Relationship;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipDatum;
import org.onap.pomba.contextbuilder.aai.datatype.RelationshipList;
import org.onap.pomba.contextbuilder.aai.datatype.ServiceInstance;

public class ServiceInstanceTest {
	@Test
	public void testServiceInstance() {
		ServiceInstance serviceInstance = new ServiceInstance();
		serviceInstance.setServiceInstanceId("serviceInstanceId");
		serviceInstance.setServiceInstanceName("serviceInstanceName");
		serviceInstance.setServiceType("serviceType");
		serviceInstance.setServiceRole("serviceRole");
		serviceInstance.setEnvironmentContext("environmentContext");
		serviceInstance.setWorkloadContext("workloadContext");
		serviceInstance.setModelInvariantId("modelInvariantId");
		serviceInstance.setModelVersionId("modelVersionId");
		serviceInstance.setResourceVersion("resourceVersion");
		serviceInstance.setOrchestrationStatus("orchestrationStatus");
		RelationshipList relationshipList = new RelationshipList();
		List<Relationship> listOfRelationship = new ArrayList<Relationship>();
		Relationship relationship = new Relationship("relatedTo", "relatedLink", new ArrayList<RelationshipDatum>(), new ArrayList<RelatedToProperty>());
		listOfRelationship.add(relationship);
		relationshipList.setRelationship(listOfRelationship);

		serviceInstance.setRelationshipList(relationshipList);

		assertEquals("serviceInstanceId", serviceInstance.getServiceInstanceId());
		assertEquals("serviceInstanceName", serviceInstance.getServiceInstanceName());
		assertEquals("serviceType", serviceInstance.getServiceType());
		assertEquals("serviceRole", serviceInstance.getServiceRole());
		assertEquals("environmentContext", serviceInstance.getEnvironmentContext());
		assertEquals("workloadContext", serviceInstance.getWorkloadContext());
		assertEquals("modelInvariantId", serviceInstance.getModelInvariantId());
		assertEquals("modelVersionId", serviceInstance.getModelVersionId());
		assertEquals("resourceVersion", serviceInstance.getResourceVersion());
		assertEquals("orchestrationStatus", serviceInstance.getOrchestrationStatus());
		assertEquals("relatedTo", serviceInstance.getRelationshipList().getRelationship().get(0).getRelatedTo());
	}

	@Test
	public void testServiceInstanceWithParameters() {
		ServiceInstance serviceInstance = new ServiceInstance("serviceInstanceId", "serviceInstanceName", "serviceType", "serviceRole", "environmentContext", "workloadContext", "modelInvariantId", "modelVersionId", "resourceVersion", "orchestrationStatus", new RelationshipList());

		assertEquals("serviceInstanceId", serviceInstance.getServiceInstanceId());
		assertEquals("serviceInstanceName", serviceInstance.getServiceInstanceName());
		assertEquals("serviceType", serviceInstance.getServiceType());
		assertEquals("serviceRole", serviceInstance.getServiceRole());
		assertEquals("environmentContext", serviceInstance.getEnvironmentContext());
		assertEquals("workloadContext", serviceInstance.getWorkloadContext());
		assertEquals("modelInvariantId", serviceInstance.getModelInvariantId());
		assertEquals("modelVersionId", serviceInstance.getModelVersionId());
		assertEquals("resourceVersion", serviceInstance.getResourceVersion());
		assertEquals("orchestrationStatus", serviceInstance.getOrchestrationStatus());

		String serviceInstanceString = serviceInstance.toString();
		assertTrue(serviceInstanceString.contains("[serviceInstanceId=serviceInstanceId,serviceInstanceName=serviceInstanceName,serviceType=serviceType,serviceRole=serviceRole,environmentContext=environmentContext,workloadContext=workloadContext,modelInvariantId=modelInvariantId,modelVersionId=modelVersionId,resourceVersion=resourceVersion,orchestrationStatus=orchestrationStatus,"));
	}

	@Test
	public void testServiceInstanceEquals() {
		ServiceInstance serviceInstance1 = new ServiceInstance("serviceInstanceId1", "serviceInstanceName1", "serviceType1", "serviceRole1", "environmentContext1", "workloadContext1", "modelInvariantId1", "modelVersionId1", "resourceVersion1", "orchestrationStatus1", new RelationshipList());
		ServiceInstance serviceInstance2 = new ServiceInstance("serviceInstanceId2", "serviceInstanceName2", "serviceType2", "serviceRole2", "environmentContext2", "workloadContext2", "modelInvariantId2", "modelVersionId2", "resourceVersion2", "orchestrationStatus2", new RelationshipList());
		ServiceInstance serviceInstance3 = new ServiceInstance("serviceInstanceId1", "serviceInstanceName1", "serviceType1", "serviceRole1", "environmentContext1", "workloadContext1", "modelInvariantId1", "modelVersionId1", "resourceVersion1", "orchestrationStatus1", new RelationshipList());

		assertTrue(serviceInstance1.equals(serviceInstance1));
		assertTrue(!serviceInstance1.equals(serviceInstance2));
		assertTrue(serviceInstance1.equals(serviceInstance3));
	}
}
