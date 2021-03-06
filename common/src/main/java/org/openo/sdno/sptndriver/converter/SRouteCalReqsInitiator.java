/*
 * Copyright 2016 ZTE Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openo.sdno.sptndriver.converter;

import org.openo.sdno.sptndriver.exception.ParamErrorException;
import org.openo.sdno.sptndriver.models.north.NL2Vpn;
import org.openo.sdno.sptndriver.models.north.NMplsTePolicy;
import org.openo.sdno.sptndriver.models.north.NParticularConstraint;
import org.openo.sdno.sptndriver.models.north.NTunnelService;
import org.openo.sdno.sptndriver.models.south.SCalculateConstraint;
import org.openo.sdno.sptndriver.models.south.SNeId;
import org.openo.sdno.sptndriver.models.south.SRouteCalReq;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqContainer;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqElement;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqElementLeftneids;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqElementRightneids;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqs;
import org.openo.sdno.sptndriver.models.south.SRouteCalReqsInput;

/**
 * The class to initialize route calculate request.
 */
public class SRouteCalReqsInitiator {

    // Sequence number of work lsp is 1, protection lsp is 2
    private static final String WORK_SEQUENCE_NO = "1";

    private SRouteCalReqsInitiator(){}

    /**
     * Initialize LSP route calculate request of L2.
     *
     * @param l2vpn L2vpn create parameters.
     * @return LSP route calculate request.
     */
    public static SRouteCalReqsInput initElineLspCalRoute(NL2Vpn l2vpn)
        throws ParamErrorException {
        if (l2vpn == null) {
            throw new ParamErrorException("Input l2vpn is null.");
        }

        NMplsTePolicy mplsTePolicy = null;
        String ingressNe = null;
        String egressNe = null;
        // Try to get mplsTePolicy from particular constraints.
        NTunnelService tunnelService = l2vpn.getTunnelService();
        if (!isParticularConstraintEmpty(tunnelService)) {
            NParticularConstraint
                particularConstraint =
                tunnelService.getParticularConstraints().getParticularConstraint().get(0);
            mplsTePolicy = particularConstraint.getMplsTe();
            ingressNe = particularConstraint.getIngressNe();
            egressNe = particularConstraint.getEgressNe();
        }
        // If get mplsTePolicy from particular constraints failed, try to get it from tunnel service.
        if (mplsTePolicy == null && !isMplsTeNull(tunnelService)) {
            mplsTePolicy = tunnelService.getMplsTe();
        }
        // if initialization of ingress NE or egress NE failed, try to get the information from ACs.
        if (isAnyNeNullorEmpty(ingressNe, egressNe) && hasTwoAcs(l2vpn)) {
                ingressNe = l2vpn.getAcs().getAc().get(0).getNeId();
                egressNe = l2vpn.getAcs().getAc().get(1).getNeId();
        }
        // if initialization of ingress NE or egress NE failed, try to get the information from PWs.
        if (isAnyNeNullorEmpty(ingressNe, egressNe) && hasTwoPws(l2vpn)) {
                ingressNe = l2vpn.getPws().getPws().get(0).getNeId();
                egressNe = l2vpn.getPws().getPws().get(1).getNeId();
        }

        return initElineLspCalRoute(mplsTePolicy, ingressNe, egressNe);
    }

    private static SRouteCalReqsInput initElineLspCalRoute(NMplsTePolicy mplsTePolicy,
                                                           String ingressNe,
                                                           String egressNe)
        throws ParamErrorException {
        if (isAnyNeNullorEmpty(ingressNe,egressNe)) {
            throw new ParamErrorException("Ingress ne or egress ne is null or empty.");
        }

        SRouteCalReqs routeCalReqs = new SRouteCalReqs();
        boolean hasBackupLsp = hasProtection(mplsTePolicy);
        SRouteCalReqContainer routeCalReqContainer
            = initElineLspCalRoute(mplsTePolicy, ingressNe, egressNe, hasBackupLsp);
        routeCalReqs.setRouteCalReqs(new SRouteCalReq());
        routeCalReqs.getRouteCalReqs().getRouteCalReq().add(routeCalReqContainer);

        SRouteCalReqsInput routeCalReqsInput = new SRouteCalReqsInput();
        routeCalReqsInput.setInput(routeCalReqs);
        return routeCalReqsInput;
    }

    private static SRouteCalReqContainer initElineLspCalRoute(NMplsTePolicy mplsTePolicy,
                                                              String ingressNe,
                                                              String egressNe,
                                                              boolean hasProtect) {
        SRouteCalReqElement routeCalReq = new SRouteCalReqElement();
        routeCalReq.setSequenceNo(WORK_SEQUENCE_NO);

        if (!hasProtect) {
            routeCalReq.setCalculatePolicy(SRouteCalReqElement.CalculatePolicyEnum.MASTER);
        } else {
            routeCalReq.setCalculatePolicy(SRouteCalReqElement.CalculatePolicyEnum.LOCAL_PROTECTION);
        }

        routeCalReq.setCalculateMode(SRouteCalReqElement.CalculateModeEnum.SIMPLE);
        if (isBestEffort(mplsTePolicy)) {
            routeCalReq.setCalculateType(SRouteCalReqElement.CalculateTypeEnum.BESTEFFORT_SEPARATE);
        } else {
            routeCalReq.setCalculateType(SRouteCalReqElement.CalculateTypeEnum.STRICT_SEPARATE);
        }

        routeCalReq.setCalculateInterconnectionMode(SRouteCalReqElement
            .CalculateInterconnectionModeEnum.NNI_NNI);
        routeCalReq.setLayerRate(SRouteCalReqElement.LayerRateEnum.LSP);
        SNeId leftNe = new SNeId();
        leftNe.setNeId(ingressNe);
        SNeId rightNe = new SNeId();
        rightNe.setNeId(egressNe);
        routeCalReq.setLeftNeIds(new SRouteCalReqElementLeftneids());
        routeCalReq.getLeftNeIds().getLeftNeId().add(leftNe);
        routeCalReq.setRightNeIds(new SRouteCalReqElementRightneids());
        routeCalReq.getRightNeIds().getRightNeId().add(rightNe);
        SCalculateConstraint calculateConstraint = new SCalculateConstraint();
        if (mplsTePolicy.getBandwidth() == null) {
            calculateConstraint.setBandwidth("0");
        } else {
            calculateConstraint.setBandwidth(mplsTePolicy.getBandwidth().toString());
        }

        calculateConstraint.setCalPolicy(SCalculateConstraint.CalPolicyEnum.BANDWIDTH_BALANCING);
        routeCalReq.setWorkCalculateConstraint(calculateConstraint);
        if (hasProtect) {
            routeCalReq.setProtectCalculateConstraint(calculateConstraint);
        }
        SRouteCalReqContainer routeCalReqContainer = new SRouteCalReqContainer();
        routeCalReqContainer.setRouteCalReqContainer(routeCalReq);
        return routeCalReqContainer;
    }

    private static boolean hasProtection(NMplsTePolicy mplsTePolicy) {
        return mplsTePolicy != null && mplsTePolicy.getPathProtectPolicy() != null;
    }

    private static boolean isBestEffort(NMplsTePolicy mplsTePolicy) {
        // If user doesn't specify besteffort field, the default value is true
        if (mplsTePolicy== null || mplsTePolicy.getBesteffort() == null) {
            return true;
        } else {
            return mplsTePolicy.getBesteffort();
        }
    }

    private static boolean hasTwoAcs(NL2Vpn l2vpn) {
        return l2vpn.getAcs() != null
            && l2vpn.getAcs().getAc() != null
            && l2vpn.getAcs().getAc().size() == 2;
    }

    private static boolean hasTwoPws(NL2Vpn l2vpn) {
        return l2vpn.getPws() != null
            && l2vpn.getPws().getPws() != null
            && l2vpn.getPws().getPws().size() == 2;
    }

    private static boolean isParticularConstraintEmpty(NTunnelService tunnelService) {
        return tunnelService == null
            || tunnelService.getParticularConstraints() == null
            || tunnelService.getParticularConstraints().getParticularConstraint() == null
            || tunnelService.getParticularConstraints().getParticularConstraint().isEmpty();
    }

    private static boolean isMplsTeNull(NTunnelService tunnelService) {
        return tunnelService == null
            || tunnelService.getMplsTe() == null;
    }

    private static boolean isAnyNeNullorEmpty(String ingressNe, String egressNe) {
        return ingressNe == null
            || ingressNe.isEmpty()
            || egressNe == null
            || egressNe.isEmpty();
    }
}
