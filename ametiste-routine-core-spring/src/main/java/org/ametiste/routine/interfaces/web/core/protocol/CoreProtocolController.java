package org.ametiste.routine.interfaces.web.core.protocol;

import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.ametiste.routine.infrastructure.laplatform.LaPlatformStats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @since
 */
@RestController
@RequestMapping("/core/protocols")
public class CoreProtocolController {

    @Autowired
    private ProtocolGatewayService protocolGatewayService;

    @Autowired
    private LaPlatformStats laPlatformStats;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public CoreProtocolsData listProtocols() {

        final HashMap<String, List<ProtocolData>> protocolGroups = new HashMap<>();

        protocolGatewayService.listRegisteredProtocols().forEach(
            p -> protocolGroups.computeIfAbsent(p.group, k -> new ArrayList<ProtocolData>())
                    .add(new ProtocolData(
                            p.name,
                            p.factory.protocolType(),
                            p.factory,
                            p.operationsMapping,
                            p.isProduceEvents,
                            laPlatformStats.loadProtocolStats(p.type)
                    ))
        );

        return new CoreProtocolsData(protocolGroups);
    }

}
