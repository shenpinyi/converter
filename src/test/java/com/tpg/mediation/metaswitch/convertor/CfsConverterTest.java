package com.tpg.mediation.metaswitch.convertor;

import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tpg.mediation.metaswitch.Application;
import com.tpg.mediation.metaswitch.conf.CfsConverterSetting;
import com.tpg.mediation.metaswitch.conf.CfsInputSetting;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class CfsConverterTest {

	@Autowired
	private CfsConverter cfsConverter;
	
	@Test
	public void testDoGetFilesWhenDirectoryIsNotExist() {
		CfsConverterSetting setting = cfsConverter.getSetting();
		List <CfsInputSetting> inputs = setting.getInput();
		inputs.clear();
		CfsInputSetting input = new CfsInputSetting();
		input.setPath("D:\\notexisting");
		input.setFilter("^.*$");
		inputs.add(input);
		
		List <File> files = cfsConverter.doGetFiles();
		assertNotEquals(files, null);
	}
	
	@Test
	public void testGetFilesByPathAndFilterWhenDirectoryIsNotExist() {
		List <File> files = cfsConverter.getFilesByPathAndFilter("\\notexisting", "^.*$");
		assertNotEquals(files, null);
	}

}
