
import java.util.logging.Level;
import java.util.logging.Logger;

import org.json.JSONObject;

import com.catalyst.Context;
import com.catalyst.event.EVENT_STATUS;
import com.catalyst.event.EventRequest;
import com.catalyst.event.CatalystEventHandler;

import com.zc.common.ZCProject;
import com.zc.component.cache.ZCCache;

public class ActionEvent implements CatalystEventHandler{
	
	private static final Logger LOGGER = Logger.getLogger(ActionEvent.class.getName());

	@Override
	public EVENT_STATUS handleEvent(EventRequest paramEventRequest, Context paramContext) throws Exception {
		
		try
		{
			ZCProject.initProject();
			int test = 180;
			System.out.println(test);
			Object eventData = paramEventRequest.getData();
			LOGGER.log(Level.SEVERE,"Data is "+eventData.toString());
			ZCCache.getInstance().putCacheValue("ActionEvent", "Working", 1l);
			LOGGER.log(Level.SEVERE,"Project Details "+paramEventRequest.getProjectDetails().toString());
		}
		catch (Exception e) {
			LOGGER.log(Level.SEVERE,"Exception in ActionEvent Function",e);
		}
		
		return EVENT_STATUS.SUCCESS;
	}

}
