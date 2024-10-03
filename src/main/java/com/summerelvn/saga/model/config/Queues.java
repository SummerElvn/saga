package com.summerelvn.saga.model.config;

import lombok.Data;

@Data
public class Queues {
   private String processQueueName;
   private String processQueueCreateRoute;
   private String processQueueCompensateRoute;
   private String statusQueueName;
   private String statusQueueSuccessRoute;
   private String statusQueueFailureRoute;
}
