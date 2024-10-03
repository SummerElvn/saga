package com.summerelvn.saga.controller;

import com.summerelvn.saga.config.ConfigProps;
import com.summerelvn.saga.model.order.OrderRequest;
import com.summerelvn.saga.service.SagaOrchestratorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/saga-orchestrator")
public class SagaController {

    private final SagaOrchestratorService sagaOrchestratorService;
    private final ConfigProps configProps;
    public SagaController(SagaOrchestratorService sagaOrchestratorService,ConfigProps configProps){
        this.sagaOrchestratorService = sagaOrchestratorService;
        this.configProps = configProps;
    }

    @PostMapping("/create-order")
    public void createOrder(@RequestBody OrderRequest request){
        sagaOrchestratorService.startSaga(request);
    }

    @GetMapping("/get-all-properties")
    public String getAllProperties(){
        return configProps.toString();
    }
}
