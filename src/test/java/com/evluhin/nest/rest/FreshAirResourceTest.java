package com.evluhin.nest.rest;

import com.evluhin.nest.Application;
import com.evluhin.nest.FreshAirService;
import com.evluhin.nest.dao.model.Alarm;
import com.evluhin.nest.dao.model.Structure;
import com.evluhin.nest.dao.model.Thermostat;
import com.evluhin.nest.dto.NestDataDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import org.apache.commons.collections4.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class FreshAirResourceTest {
	protected MockMvc mockMvc;


	@InjectMocks
	FreshAirResource resource;

	@Mock
	FreshAirService service;

	@Captor
	ArgumentCaptor<NestDataDto> captor;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.standaloneSetup(resource).build();
	}

	@Test
	public void testSettings() throws Exception {
		doReturn(null).when(service).update(anyObject());

		ObjectMapper mapper = new ObjectMapper();
		//prepare data
		NestDataDto dto = new NestDataDto();
		dto.setThermostats(Lists.newArrayList(new Thermostat("t1")));
		dto.setAlarms(Lists.newArrayList(new Alarm("a1")));
		Structure structure = new Structure("s1");
		//set to structure same alarms list
		structure.setAlarms(dto.getAlarms());
		//set to structure same thermostats list
		structure.setThermostats(dto.getThermostats());
		dto.setStructures(Lists.newArrayList(structure));

		String jsonContent = mapper.writeValueAsString(dto);
		String response = mockMvc.perform(post("/api/data").contentType(MediaType.APPLICATION_JSON).content(jsonContent))
			   .andExpect(status().isOk())
			   .andReturn().getResponse().getContentAsString();

		ArgumentCaptor<NestDataDto> captor = ArgumentCaptor.forClass(NestDataDto.class);
		verify(service).update(captor.capture());

		NestDataDto value = captor.getValue();
		assertTrue("structure list", CollectionUtils.isNotEmpty(value.getStructures()));
		Structure structure0 = value.getStructures().get(0);
		assertEquals("structure", "s1", structure0.getId());


		assertTrue("structure thermostat list", CollectionUtils.isNotEmpty(structure0.getThermostats()));
		assertEquals("structure thermostat", "t1", structure0.getThermostats().get(0).getId());


		assertTrue("structure alarm list", CollectionUtils.isNotEmpty(structure0.getAlarms()));
		assertEquals("structure alarm", "a1", structure0.getAlarms().get(0).getId());


		assertTrue("thermostat list", CollectionUtils.isNotEmpty(value.getThermostats()));
		assertEquals("thermostat in list", "t1", value.getThermostats().get(0).getId());

		assertTrue("alarm list", CollectionUtils.isNotEmpty(value.getAlarms()));
		assertEquals("alarm in list", "a1", value.getAlarms().get(0).getId());

	}
}