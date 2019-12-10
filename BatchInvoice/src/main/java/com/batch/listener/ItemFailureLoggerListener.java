package com.batch.listener;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.batch.core.listener.ItemListenerSupport;

public class ItemFailureLoggerListener extends ItemListenerSupport {

    private static Log logger = LogFactory.getLog("item.error");

    public void onReadError(Exception ex) {
        logger.error("Encountered error on read", ex);
    }

    @Override
    public void onWriteError(Exception ex, List item) {
    	// TODO Auto-generated method stub
        logger.error("Encountered error on write", ex);
    }


}