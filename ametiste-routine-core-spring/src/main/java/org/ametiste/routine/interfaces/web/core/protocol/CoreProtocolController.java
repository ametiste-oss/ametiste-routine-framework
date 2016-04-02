package org.ametiste.routine.interfaces.web.core.protocol;

import org.ametiste.laplatform.protocol.gateway.ProtocolGatewayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
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

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public CoreProtocolsData listProtocols() {

        final List<ProtocolData> protocols = new ArrayList<>();

        protocolGatewayService.listRegisteredProtocols().forEach(
            p -> protocols.add(new ProtocolData(p.getName()))
        );

        return new CoreProtocolsData(protocols);
    }

}
