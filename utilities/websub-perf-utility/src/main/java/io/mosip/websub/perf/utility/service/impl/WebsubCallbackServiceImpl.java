package io.mosip.websub.perf.utility.service.impl;


import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.mosip.websub.perf.utility.dto.PerformanceData;
import io.mosip.websub.perf.utility.dto.RequestDTO;
import io.mosip.websub.perf.utility.dto.ResultMetadata;
import io.mosip.websub.perf.utility.service.WebsubCallbackService;


@Service
public class WebsubCallbackServiceImpl implements WebsubCallbackService {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebsubCallbackServiceImpl.class);
	private static final String UTC_DATETIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
	private Map<String, PerformanceData> cache = new ConcurrentHashMap<>();
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern(UTC_DATETIME_PATTERN);
	
	@Override
	public void compute(RequestDTO requestDTO,String subID) {	 
		LOGGER.info("requestDTO timestamp - {}",requestDTO.getTimestamp());
		
		LocalDateTime timeStamp= LocalDateTime.parse(requestDTO.getTimestamp(), formatter);
		LocalDateTime timeNow= LocalDateTime.now(ZoneOffset.UTC);
		long millis=ChronoUnit.MILLIS.between(timeStamp,timeNow);
		// added sync block for thread safe alter of hashmap and perf data;
		if(!cache.containsKey(subID)) {
			synchronized (cache) {
				if(!cache.containsKey(subID)) {
					PerformanceData performenceData = new PerformanceData();
					cache.put(subID, performenceData);
				}
			}
		} 
		PerformanceData performenceData = cache.get(subID);
		performenceData.getTurnAroundTime().add(millis);
	}

	@Override
	public ResultMetadata getResult(String subID) {
		PerformanceData performenceData= cache.get(subID);
		if(performenceData==null) {
			throw new RuntimeException("No data for subID");
		}
		ResultMetadata metadata= new ResultMetadata();
		List<Long> tat= performenceData.getTurnAroundTime();
		Collections.sort(tat);
	    long sum=tat.parallelStream().collect(Collectors.summingLong(Long::longValue));
		int noOfRequest = tat.size();
		metadata.setMessageCount(noOfRequest);
		metadata.setAvgTurnAroundTime(sum/noOfRequest);
		metadata.setSubID(subID);
		int index=(int) (0.90*noOfRequest);
		metadata.setNinetiethPercentile(tat.get(index));
		return metadata;	
	}

	@Override
	public void reset() {
		cache = new HashMap<>();
	}

}
