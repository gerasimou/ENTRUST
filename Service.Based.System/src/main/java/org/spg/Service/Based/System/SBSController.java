package org.spg.Service.Based.System;

import java.util.List;

import org.spg.Service.Based.System.auxiliary.Utility;
import org.spg.Service.Based.System.prism.PrismAPI;
import org.spg.Service.Based.System.service.AbstractService;

public class SBSController {
	
	private PrismAPI api;
	private String propertiesFileName;
	private String modelFileName;
	private List<List<AbstractService>> srvList;

	public SBSController(List<List<AbstractService>> srvList) {		
		modelFileName 		= Utility.getProperty("MODEL_FILE");
		propertiesFileName	= Utility.getProperty("PROPERTIES_FILE");
		
		this.srvList 		= srvList;
		
		api  				= new PrismAPI();
		api.setPropertiesFile(propertiesFileName);

	}

}
