package com.flowid.refd.ws.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flowid.refd.v1.AddLogEntryRequest;
import com.flowid.refd.v1.AddLogEntryResponse;
import com.flowid.refd.v1.LogService;

@javax.jws.WebService(
        serviceName = "LogServiceService",
        portName = "LogServiceSoap11",
        targetNamespace = "urn:flowid.com:refd:v1",
        endpointInterface = "com.flowid.refd.v1")
public class LogServiceImpl implements LogService {
    private static final Logger logger = LoggerFactory.getLogger(LogServiceImpl.class);

    /*
     * @see LogService#addLogEntry(AddLogEntryRequest addLogEntryRequest )*
     */
    public AddLogEntryResponse addLogEntry(AddLogEntryRequest addLogEntryRequest) {
        logger.info("Executing operation addLogEntry");
        System.out.println(addLogEntryRequest);
        try {
            AddLogEntryResponse result = null;
            return result;
        } catch (java.lang.Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException(ex);
        }
    }
}
