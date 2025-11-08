package com.amcom.desafio_tecnico_amcom.support.constant;

public class BrokerConstant {
    private BrokerConstant() {}

    public static final class Topics {
        private Topics(){}

        public static final String ORDER_EVENTS_TOPIC = "order-events";
        public static final String ORDER_PROCESSED_TOPIC = "order-processed";
    }

    public static final class Consumer {
        private Consumer(){}

        public static final String GROUP_ID = "order-group";
    }
}