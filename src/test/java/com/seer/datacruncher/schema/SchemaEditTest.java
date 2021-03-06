/*
 * Copyright (c) 2015  www.see-r.com
 * All rights reserved
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.seer.datacruncher.schema;


import static org.junit.Assert.assertTrue;
import com.seer.datacruncher.jpa.Update;
import com.seer.datacruncher.jpa.dao.DaoSet;
import com.seer.datacruncher.jpa.dao.SchemasDao;
import com.seer.datacruncher.jpa.entity.SchemaEntity;

import java.io.InputStream;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * 
 * @author KGandhi
 * This junit test class updates the schema. 
 * It will update startdate, enddate and description fields of the schema.
 */
@ContextConfiguration("classpath:test-config.xml")
public class SchemaEditTest  extends AbstractJUnit4SpringContextTests implements DaoSet {

	private Properties properties;

    @Autowired
    ApplicationContext ctx;

    private SchemasDao schemasDao;

    @Before
	public void setUp() throws Exception {

        schemasDao = (SchemasDao) ctx.getBean("SchemasDao");
		properties = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream("test.properties");
		
		try {
			properties.load(in);
		} catch (Exception e) {
			assertTrue("Failed in loading test.properties file",false);			
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testEditSchema() {
				
		String schemaName =   properties.getProperty("schemaname");

		List<SchemaEntity> listSchemaEntity = schemasDao.findByName(schemaName);
		
		if(listSchemaEntity == null || listSchemaEntity.size() == 0) {
			assertTrue("Schema record not found", false);
			return;
		}
		
		SchemaEntity schemaEntity = listSchemaEntity.get(0);
		Calendar now = Calendar.getInstance();
				
		schemaEntity.setStartDate(now.getTime());
		now.add(Calendar.MONTH, 1);
		schemaEntity.setEndDate(now.getTime());
		
		schemaEntity.setDescription(properties.getProperty("schemadesc"));
		
		
		Update update = schemasDao.update(schemaEntity);		
		assertTrue(update.getMessage(),update.isSuccess());
	}
}
