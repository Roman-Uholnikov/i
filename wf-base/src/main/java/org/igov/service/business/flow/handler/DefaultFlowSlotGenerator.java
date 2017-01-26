package org.igov.service.business.flow.handler;

import org.joda.time.DateTime;
import org.quartz.CronExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.igov.model.flow.FlowSlot;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * User: goodg_000
 * Date: 29.06.2015
 * Time: 18:57
 */
public class DefaultFlowSlotGenerator {

    protected static final Logger LOG = LoggerFactory.getLogger(DefaultFlowSlotGenerator.class);

    public List<FlowSlot> generateObjects(Map<String, String> configuration, DateTime startDate, DateTime endDate,
            int maxGeneratedSlotsCount, String defaultFlowSlotName, List<ExcludeDateRange> aDateRange_Exclude) {
        LOG.info("generateObjects slots is started");
        TreeMap<DateTime, FlowSlot> res = new TreeMap<>();

        for (Map.Entry<String, String> entry : configuration.entrySet()) {
            DateTime currDateTime = startDate;
            String cronExpressionString = entry.getKey();
            String slotDuration = entry.getValue();

            CronExpression cronExpression;
            try {
                cronExpression = new CronExpression(cronExpressionString);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            int generatedSlotsCount = 0;
            while (currDateTime.isBefore(endDate)) {
                currDateTime = new DateTime(cronExpression.getNextValidTimeAfter(currDateTime.toDate()));
                if (endDate.compareTo(currDateTime) <= 0) {
                    break;
                }
                
                boolean continueFlaf = false;
                
                if(aDateRange_Exclude != null && !aDateRange_Exclude.isEmpty()){
                    
                    for(ExcludeDateRange oExcludeRange : aDateRange_Exclude){
                        
                        if ((currDateTime.isAfter(oExcludeRange.getsDateTimeAt()) &&
                            currDateTime.isBefore(oExcludeRange.getsDateTimeTo()))||
                            currDateTime.isEqual(oExcludeRange.getsDateTimeAt())||
                            currDateTime.isEqual(oExcludeRange.getsDateTimeTo()))
                        {
                            continueFlaf = true;
                            break;
                        }
                    }
                }
                
                if(continueFlaf){
                    continue;
                }
                
                if (res.containsKey(currDateTime)) {
                    if (LOG.isDebugEnabled()) {
                        LOG.debug(String.format(
                                "Date %s generated by expression '%s' already generated by previous expressions. " +
                                        "Keeping first generated slot.", currDateTime, cronExpression));
                    }
                    continue;
                }

                if (generatedSlotsCount >= maxGeneratedSlotsCount) {
                    throw new IllegalStateException("Can't generate more then " + maxGeneratedSlotsCount + " slots!");
                }

                FlowSlot slot = new FlowSlot();
                slot.setName(defaultFlowSlotName);
                slot.setsDate(currDateTime);
                slot.setsDuration(slotDuration);
                res.put(currDateTime, slot);
                generatedSlotsCount++;
            }
        }

        return new ArrayList<>(res.values());
    }

}
